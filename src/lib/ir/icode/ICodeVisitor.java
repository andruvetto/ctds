package lib.ir.icode;
import lib.ir.Visitor;
import lib.ir.ast.*;
import java.util.List;
import java.util.LinkedList;

/**
 * This class represents the intermediate code visitor for generate 3-way code
 * 
 */

public class ICodeVisitor extends Visitor<LinkedList<Instruction>> {
    private int numtemp;
    private Expression lastExpression;
    private LinkedList<Label> lastStartLabel;
    private LinkedList<Label> lastEndLabel;
    private int offset;
    private int bytes = 8; //Default decrement offset in this value
    
    private String getNextIdTemp(){
        numtemp++;
        return "" + numtemp;
    }
    
    public ICodeVisitor(){
        super();
        numtemp = 0;
        lastExpression = null;
        lastStartLabel = new LinkedList();
        lastEndLabel = new LinkedList();
        offset = 0;
    }

    /*Visit ForStmt*/
    @Override
    public LinkedList<Instruction> visit(ForStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        String id = getNextIdTemp();
        Label startLabel = new Label("startFor"+id+":");
        lastStartLabel.push(startLabel);
        Label endLabel = new Label("endFor"+id+":");
        lastEndLabel.push(endLabel);
        Label checkLabel = new Label("checkFor"+id+":");
        instructions.add(new Instruction(TypeInstruction.LABEL,startLabel));
        Location var = stmt.getAssign().getLocation();
        instructions.addAll(this.visit(var)); 
        instructions.addAll(this.visit(stmt.getAssign()));
        instructions.addAll(this.visit(stmt.getCondition()));
        Expression max = lastExpression;
        instructions.add(new Instruction(TypeInstruction.LABEL,checkLabel));
        VarLocation res = new VarLocation("temp"+getNextIdTemp());
        offset -= bytes;
        res.setOffset(offset);
        Instruction check = new Instruction (TypeInstruction.LESS,var,max,res);
        instructions.add(check);
        Instruction jumpfalse = new Instruction (TypeInstruction.JUMPFALSE,res,endLabel);
        instructions.add(jumpfalse);
        instructions.addAll(this.visit(stmt.getForStatement()));
        Instruction incrVar = new Instruction (TypeInstruction.INC,var);
        instructions.add(incrVar);
        Instruction jump = new Instruction (TypeInstruction.JUMP,checkLabel);
        instructions.add(jump);
        instructions.add(new Instruction(TypeInstruction.LABEL,endLabel));
        lastStartLabel.pop();
        lastEndLabel.pop();
        return instructions;
    }
    
    /*Visit WhileStmt*/
    @Override
    public LinkedList<Instruction> visit(WhileStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        String id = getNextIdTemp();
        Label startLabel = new Label("startWhile"+id+":");
        lastStartLabel.push(startLabel);
        Label endLabel = new Label("endWhile"+id+":");
        lastEndLabel.push(endLabel);
        instructions.add(new Instruction(TypeInstruction.LABEL,startLabel));
        instructions.addAll(this.visit(stmt.getCondition()));
        Expression condition = lastExpression;
        Instruction jumpfalse = new Instruction (TypeInstruction.JUMPFALSE,condition,endLabel);
        instructions.add(jumpfalse);
        instructions.addAll(this.visit(stmt.getWhileStatement()));
        instructions.add(new Instruction(TypeInstruction.JUMP,startLabel));
        instructions.add(new Instruction(TypeInstruction.LABEL,endLabel));
        lastStartLabel.pop();
        lastEndLabel.pop();
        return instructions;
    }
    
    /*Visit IFStmt*/
    @Override
    public LinkedList<Instruction> visit(IfStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getCondition()));
        Expression condition = lastExpression;
        Label elseLabel = new Label("else"+getNextIdTemp()+":");
        Label endLabel = new Label("endif"+getNextIdTemp()+":");
        Instruction jumpfalse;
        if (stmt.getElseStatement() != null){
            jumpfalse = new Instruction(TypeInstruction.JUMPFALSE,condition,elseLabel);
        }
        else{
            jumpfalse = new Instruction(TypeInstruction.JUMPFALSE,condition,endLabel);
        }
        instructions.add(jumpfalse);
        instructions.addAll(this.visit(stmt.getIfStatement()));
        instructions.add(new Instruction(TypeInstruction.JUMP,endLabel));
        if (stmt.getElseStatement() != null){
            instructions.add(new Instruction(TypeInstruction.LABEL,elseLabel));
            instructions.addAll(this.visit(stmt.getElseStatement()));
        }
        instructions.add(new Instruction(TypeInstruction.LABEL,endLabel));
        return instructions;      
    }
    
     /*Visit AssignStmt*/
    @Override
    public LinkedList<Instruction> visit(AssignStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getExpression()));
        Expression operand = lastExpression;
        Location location = stmt.getLocation();

        
        if (location.isArray()){
            ArrayLocation array;
            Expression pos;
            array = ((ArrayLocation)location);
            instructions.addAll(this.visit(array.getExpression()));
            pos = lastExpression;
            
            //Verify acces out of range
            ArrayLocation arrayDeclarated = (ArrayLocation)array.getDeclarated();
            IntLiteral size = new IntLiteral(arrayDeclarated.getSize());
            VarLocation resVerPos = new VarLocation("temp"+getNextIdTemp());
            offset -= bytes;
            resVerPos.setOffset(offset);
            Label endLabel = new Label("errorArray:");
            Instruction verifyPos = new Instruction(TypeInstruction.LESS, pos, size, resVerPos);
            instructions.add(verifyPos);
            Instruction jumpFalse = new Instruction(TypeInstruction.JUMPFALSE,resVerPos,endLabel);
            instructions.add(jumpFalse);
            IntLiteral zero = new IntLiteral("0");
            verifyPos = new Instruction(TypeInstruction.GTR_EQ, pos, zero, resVerPos);
            instructions.add(verifyPos);
            instructions.add(jumpFalse);
            ///////////////////////////////
            
            Instruction assmnt;
            Instruction access;
            VarLocation res;
            switch (stmt.getOperator()){
                case ASSMNT:
                    assmnt = new Instruction(TypeInstruction.ARRAYASSMNT,operand,pos,array.getDeclarated());
                    instructions.add(assmnt);
                    break;
                case ASSMNT_INC:
                    res = new VarLocation("temp"+getNextIdTemp());
                    offset -= bytes;
                    res.setOffset(offset);
                    res.setType(stmt.getLocation().getType());
                    access = new Instruction(TypeInstruction.ARRAYACCESS,array.getDeclarated(),pos,res);
                    instructions.add(access);
                    Instruction sum;
                    if (stmt.getLocation().getType().equals(Type.INT)){
                        sum = new Instruction(TypeInstruction.SUMINT,res,operand,res);
                    }
                    else{
                        sum = new Instruction(TypeInstruction.SUMFLOAT,res,operand,res);    
                    }
                    instructions.add(sum);
                    assmnt = new Instruction(TypeInstruction.ARRAYASSMNT,res,pos,array.getDeclarated());
                    instructions.add(assmnt);
                    break;
                case ASSMNT_DEC:
                    res = new VarLocation("temp"+getNextIdTemp());
                    offset -= bytes;
                    res.setOffset(offset);
                    res.setType(stmt.getLocation().getType());
                    access = new Instruction(TypeInstruction.ARRAYACCESS,array.getDeclarated(),pos,res);
                    instructions.add(access);
                    Instruction sub;
                    if (stmt.getLocation().getType().equals(Type.INT)){
                        sub = new Instruction(TypeInstruction.SUBINT,res,operand,res);
                    }
                    else{
                        sub = new Instruction(TypeInstruction.SUBFLOAT,res,operand,res);    
                    }
                    instructions.add(sub);
                    assmnt = new Instruction(TypeInstruction.ARRAYASSMNT,res,pos,array.getDeclarated());
                    instructions.add(assmnt);
                    break;
            }
                
        }
        else{
            Instruction instruction;
            switch (stmt.getOperator()){
                case ASSMNT:
                    instruction = new Instruction(TypeInstruction.ASSMNT,operand,location.getDeclarated());
                    instructions.add(instruction);
                    break;
                case ASSMNT_INC:
                    if (stmt.getLocation().getType().equals(Type.INT)){
                        instruction = new Instruction(TypeInstruction.SUMINT,location.getDeclarated(),operand,location.getDeclarated());
                        instructions.add(instruction); 
                        lastExpression = location.getDeclarated();
                    }
                    else{
                        instruction = new Instruction(TypeInstruction.SUMFLOAT,location.getDeclarated(),operand,location.getDeclarated());
                        instructions.add(instruction); 
                        lastExpression = location.getDeclarated();
                    }
                    break;
                case ASSMNT_DEC:
                    if (stmt.getLocation().getType().equals(Type.INT)){
                        instruction = new Instruction(TypeInstruction.SUBINT,location.getDeclarated(),operand,location.getDeclarated());
                        instructions.add(instruction); 
                        lastExpression = location.getDeclarated();
                    }
                    else{
                        instruction = new Instruction(TypeInstruction.SUBFLOAT,location.getDeclarated(),operand,location.getDeclarated());
                        instructions.add(instruction); 
                        lastExpression = location.getDeclarated();
                    }
                    break;
            }
        }
        return  instructions;
    }
    
    /*Visit Expression*/
    @Override
    public LinkedList<Instruction> visit(Expression exp) {
        String c = exp.getClass().getSimpleName();
        LinkedList<Instruction> instructions = new LinkedList();
        switch (c) {
            case "BinOpExpr" :
                return this.visit((BinOpExpr) exp);
            case "UnOpExpr" :
                return this.visit((UnOpExpr) exp);
            case "MethodCall" :
                return this.visit((MethodCall) exp);          
            case "Location" :
                return this.visit((Location) exp);
            case "Literal" :
                return this.visit((Literal) exp);
            case "IntLiteral" :
                lastExpression = exp;
                return instructions;
            case "FloatLiteral" :
                lastExpression = exp;
                return instructions;
            case "BooleanLiteral" :
                lastExpression = exp;
                return instructions;
            case "VarLocation" :
                lastExpression = ((VarLocation)exp).getDeclarated();
                return instructions;
            case "ArrayLocation" :
                ArrayLocation array = (ArrayLocation)exp;
                instructions.addAll(this.visit(array.getExpression()));
                Expression pos = lastExpression;
                
                
                //Verify acces out of range
                ArrayLocation arrayDeclarated = (ArrayLocation)array.getDeclarated();
                IntLiteral size = new IntLiteral(arrayDeclarated.getSize());
                VarLocation resVerPos = new VarLocation("temp"+getNextIdTemp());
                offset -= bytes;
                resVerPos.setOffset(offset);
                Label endLabel = new Label("errorArray:");
                Instruction verifyPos = new Instruction(TypeInstruction.LESS, pos, size, resVerPos);
                instructions.add(verifyPos);
                Instruction jumpFalse = new Instruction(TypeInstruction.JUMPFALSE,resVerPos,endLabel);
                instructions.add(jumpFalse);
                IntLiteral zero = new IntLiteral("0");
                verifyPos = new Instruction(TypeInstruction.GTR_EQ, pos, zero, resVerPos);
                instructions.add(verifyPos);
                instructions.add(jumpFalse);
                ///////////////////////////////
                
                
                
                Instruction access;
                VarLocation res = new VarLocation("temp"+getNextIdTemp());
                offset -= bytes;
                res.setOffset(offset);
                res.setType(array.getType());
                access = new Instruction(TypeInstruction.ARRAYACCESS,array.getDeclarated(),pos,res);
                instructions.add(access);
                lastExpression = res;
                return instructions;
            default:
                return null;
        }
    }
    
    /*Visit MethodCallStmt*/
    @Override
    public LinkedList<Instruction> visit(MethodCallStmt m) {
        LinkedList<Instruction> instructions = new LinkedList();
        LinkedList<Instruction> pushes = new LinkedList();
        List<Expression> expressions = m.getExpressions();
        int integers = 0;
        int floats = 0;
        for (Expression e : expressions){
            if (e.getType().equals(Type.FLOAT)){
                floats++;
            }
            else{
                integers++;
            }
        }
        for (Expression e : expressions){
            instructions.addAll(this.visit(e));
            Literal parameterNum;
            if (e.getType().equals(Type.FLOAT)){
                parameterNum = new IntLiteral(floats-1);
                floats--;
            }
            else{
                parameterNum = new IntLiteral(integers);
                integers--;
            }
            Expression expr = lastExpression;
            Instruction push = new Instruction(TypeInstruction.PUSH, parameterNum, expr);
            pushes.add(push);
            
        }
        instructions.addAll(pushes);
        VarLocation res = new VarLocation("temp"+getNextIdTemp());
        res.setType(m.getMethodDecl().getType());
        offset -= bytes;
        res.setOffset(offset);
        Instruction call = new Instruction(TypeInstruction.CALL, m.getLocation(),res);
        lastExpression = res;
        instructions.add(call);
        return instructions;
    }
    
    
    /*Visit MethodCall*/
    @Override
    public LinkedList<Instruction> visit(MethodCall m) {
        return this.visit(m.getMethod());
    }
    
    
    /*Visit ReturnStmt*/
    @Override
    public LinkedList<Instruction> visit(ReturnStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        Expression expression = stmt.getExpression();
        if (expression!=null){
            instructions.addAll(this.visit(expression));
            Instruction instruction = new Instruction(TypeInstruction.RETURN, lastExpression);
            instructions.add(instruction);
        }
        else{
            Instruction instruction = new Instruction(TypeInstruction.RETURN, null);
            instructions.add(instruction);
        }
        
        return instructions;
        
    }
    
    /*Visit BreakStmt*/
    @Override
    public LinkedList<Instruction> visit(BreakStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        Instruction jump = new Instruction(TypeInstruction.JUMP,lastEndLabel.getFirst());
        instructions.add(jump);
        return instructions;
    }

    /*Visit ContinueStmt*/
    @Override
    public LinkedList<Instruction> visit(ContinueStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        Instruction jump = new Instruction(TypeInstruction.JUMP,lastStartLabel.getFirst());
        instructions.add(jump);
        return instructions;
    }
    
    
    /*Visit BinOpExpr*/
    @Override
    public LinkedList<Instruction> visit(BinOpExpr expr) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(expr.getLeftOperand()));
        Expression op1 = lastExpression;
        instructions.addAll(this.visit(expr.getRightOperand()));
        Expression op2 = lastExpression;
        VarLocation result = new VarLocation("temp"+getNextIdTemp());
        offset -= bytes;
        result.setOffset(offset);
        Instruction instruction;
        switch(expr.getOperator()){
            case PLUS:
		if (expr.getType().equals(Type.INT)){
                    result.setType(Type.INT);
                    lastExpression = result;
                    instruction = new Instruction(TypeInstruction.SUMINT,op1,op2,result);
                    instructions.add(instruction);
                    
                    
                }
                else{
                    instruction = new Instruction(TypeInstruction.SUMFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.FLOAT);
                    lastExpression = result;
                }
                break;
            case MINUS:
		if (expr.getType().equals(Type.INT)){
                    instruction = new Instruction(TypeInstruction.SUBINT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.INT);
                    lastExpression = result;
                }
                else{
                    instruction = new Instruction(TypeInstruction.SUBFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.FLOAT);
                    lastExpression = result;
                }
                break;
            case TIMES:
		if (expr.getType().equals(Type.INT)){
                    instruction = new Instruction(TypeInstruction.MULTINT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.INT);
                    lastExpression = result;
                }
                else{
                    instruction = new Instruction(TypeInstruction.MULTFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.FLOAT);
                    lastExpression = result;
                }
                break;
            case DIVIDE:
		if (expr.getType().equals(Type.INT)){
                    instruction = new Instruction(TypeInstruction.DIVIDEINT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.INT);
                    lastExpression = result;
                }
                else{
                    instruction = new Instruction(TypeInstruction.DIVIDEFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.FLOAT);
                    lastExpression = result;
                }
                break;
            case MOD:
                if (expr.getType().equals(Type.INT)){
                    instruction = new Instruction(TypeInstruction.MOD,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.INT);
                    lastExpression = result;
                }
                else{
                    instruction = new Instruction(TypeInstruction.MOD,op1,op2,result);
                    instructions.add(instruction);
                    result.setType(Type.FLOAT);
                    lastExpression = result;
                }
                break;
            case LESS:
                instruction = new Instruction(TypeInstruction.LESS,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case LESS_EQ:
                instruction = new Instruction(TypeInstruction.LESS_EQ,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case GTR:
		
                instruction = new Instruction(TypeInstruction.GTR,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case GTR_EQ:
		instruction = new Instruction(TypeInstruction.GTR_EQ,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case EQ:
		instruction = new Instruction(TypeInstruction.EQ,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case NOT_EQ:
		instruction = new Instruction(TypeInstruction.NOT_EQ,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case AND:
		instruction = new Instruction(TypeInstruction.AND,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;
            case OR:
		instruction = new Instruction(TypeInstruction.OR,op1,op2,result);
                instructions.add(instruction);
                result.setType(Type.BOOLEAN);
                lastExpression = result;
                break;       
        }
        return instructions;
    }
    
    
    
    /*Visit UnopExpr*/
    @Override
    public LinkedList<Instruction> visit(UnOpExpr expr) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(expr.getExpression()));
        VarLocation result = new VarLocation("temp"+getNextIdTemp());
        offset -= bytes;
        result.setOffset(offset);
        Instruction instruction;
        switch (expr.getOperator()){
            case MINUS:
                if (expr.getType().equals(Type.INT)){
                    instruction = new Instruction(TypeInstruction.MINUSINT,lastExpression,result);
                    instructions.add(instruction);
                    result.setType(Type.INT);
                    lastExpression = result;
                    
                }
                else{
                    instruction = new Instruction(TypeInstruction.MINUSFLOAT,lastExpression,result);
                    instructions.add(instruction);
                    result.setType(Type.FLOAT);
                    lastExpression = result;
                }
                break;
            case LOGIC_NEGATION:
                    instruction = new Instruction(TypeInstruction.NEGATION,lastExpression,result);
                    instructions.add(instruction);
                    result.setType(Type.BOOLEAN);
                    lastExpression = result;
                break;
        }
        return instructions;
    }
    
    /*Visit ArrayLocation*/
    @Override
    public LinkedList<Instruction> visit(ArrayLocation loc) { 
        LinkedList<Instruction> instructions = new LinkedList();
        offset -= bytes;
        loc.setOffset(offset);
        int arraySize = ((ArrayLocation)loc).getSize();
        offset -= (arraySize -1) * bytes; //Reserved memory for array
        return instructions;
    }
    
    /*Visit VarLocation*/
    @Override
    public LinkedList<Instruction> visit(VarLocation loc) {  
        LinkedList<Instruction> instructions = new LinkedList();
        offset -= bytes;
        loc.setOffset(offset);
        switch (loc.getType()){
                case INT:
                    instructions.add(new Instruction(TypeInstruction.ASSMNT, new IntLiteral("0"), loc));
                    break;
                case FLOAT:
                    instructions.add(new Instruction(TypeInstruction.ASSMNT, new FloatLiteral("0.0"), loc));
                    break;
                case BOOLEAN:
                    instructions.add(new Instruction(TypeInstruction.ASSMNT, new BooleanLiteral("false"), loc));
                    break;
                default:
                    break;
            }
        return instructions;
    }
    
    
    
    /*Visit FieldDecl*/
    @Override
    public LinkedList<Instruction> visit(FieldDecl fd) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (Location l : fd.getLocations()){
            
            instructions.addAll(this.visit(l));
        }
        return instructions;
    }
    
    /*Visit Block*/
    @Override
    public LinkedList<Instruction> visit(Block block) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (FieldDecl f : block.getFields()){
            instructions.addAll(this.visit(f));
        }
        for (Statement s : block.getStatements()){
            instructions.addAll(this.visit(s));
        }
        return instructions;
    }

    /*Visit MethodDecl*/
    @Override
    public LinkedList<Instruction> visit(MethodDecl m) {
        //offset = 0;
        LinkedList<Instruction> instructions = new LinkedList();
        Label label = new Label(m.getId());
        Label endlabel = new Label("end" + m.getId()+":");
        if (!m.ifExtern()){
            Instruction method = new Instruction(TypeInstruction.METHODDECL, label);
            offset = 0;
            int offsetp = bytes;
            int integers = 1;
            int floats = 0;
            LinkedList<Instruction> parameters = new LinkedList();
            for (int i = m.getParameters().size()-1; i>=0; i--){
                VarLocation var = m.getParameters().get(i).getVarLocation();
                if (var.getType().equals(Type.FLOAT)){
                    if(floats<8){
                        var.setOffset(floats);
                        parameters.addAll(this.visit(m.getParameters().get(i)));
                        var.setOffset(offset);
                        floats++;
                    }
                    else{
                        offsetp += bytes;
                        var.setOffset(offsetp);
                    }      
                }
                else{
                    if(integers<7){
                        var.setOffset(integers);
                        parameters.addAll(this.visit(m.getParameters().get(i)));
                        var.setOffset(offset);
                        integers++;
                    }
                    else{
                        offsetp += bytes;
                        var.setOffset(offsetp); 
                    }
                }
                    
            }
            
            
            LinkedList<Instruction> block = this.visit(m.getBlock());
            if (offset != 0) method.SetOp1(new IntLiteral(-offset + ((-offset/bytes)%2) * bytes  )); //Local bytes
            else method.SetOp1(new IntLiteral(0));
            instructions.add(method);
            instructions.addAll(parameters);
            instructions.addAll(block);
            
            

            instructions.add(new Instruction(TypeInstruction.LABEL, endlabel));
            
        }
        else{
            instructions.add(new Instruction(TypeInstruction.METHODDECLEXTERN, label));
        }
        return instructions;
    }
    
    @Override
    public LinkedList<Instruction> visit(Parameter p) {
        LinkedList<Instruction> instructions = new LinkedList();
        offset -= bytes;
        VarLocation res = new VarLocation("temp"+getNextIdTemp());
        VarLocation var = new VarLocation(p.getVarLocation().getId());
        var.setType(p.getVarLocation().getType());
        var.setOffset(p.getVarLocation().getOffset());
        res.setOffset(offset);
        res.setType(var.getType());
        Instruction assign = new Instruction(TypeInstruction.ASSMNT,var,res);
        instructions.add(assign);
        return instructions;
    }

    /*Visit ClassDecl*/
    @Override
    public LinkedList<Instruction> visit(ClassDecl c) {
        LinkedList<Instruction> instructions = new LinkedList();
        if (c.getId().equals("main")){ //Only visit Class main
            //Add global declarations
            for (FieldDecl f : c.getFields()){
                for(Location loc : f.getLocations()){
                    loc.setIsGlobal(true);
                    if (loc.isArray()){
                        instructions.add(new Instruction(TypeInstruction.GLOBALARRAY,loc));
                    }
                    else{
                        instructions.add(new Instruction(TypeInstruction.GLOBALVAR, loc));
                    }
                }
            }
            for (MethodDecl m : c.getMethods()){
                if (!m.ifExtern()){
                    Label label = new Label("globl " + m.getId());
                    instructions.add(new Instruction(TypeInstruction.LABEL, label));
                    label = new Label("type " + m.getId() + ", @function");
                    instructions.add(new Instruction(TypeInstruction.LABEL, label));
                    instructions.addAll(this.visit(m));
                    label = new Label("size " + m.getId() + ", .-" + m.getId());
                    instructions.add(new Instruction(TypeInstruction.LABEL, label));
                    label = new Label("align " + bytes );
                    instructions.add(new Instruction(TypeInstruction.LABEL, label));
                }
            }
            //Used for detecte access out of bond in arrays
            instructions.add(new Instruction(TypeInstruction.ARRAYEXCEPTION)); 
        }
        return instructions;
    }

    /*Visit Program*/
    @Override
    public LinkedList<Instruction> visit(Program p) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (ClassDecl c : p.getClasses()){
            instructions.addAll(this.visit(c));
        }
        return instructions;
    }
    
    
}