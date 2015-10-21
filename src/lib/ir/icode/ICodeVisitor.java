package lib.ir.icode;
import java.util.ArrayList;
import lib.ir.Visitor;
import lib.ir.ast.*;
import java.util.List;
import java.util.LinkedList;

/**
 * This class represents the intermediate code visitor for generate 3-way code
 * 
 */

public class ICodeVisitor extends Visitor<List<Instruction>> {
    private int numtemp;
    private Expression lastExpression;
    
    private String getNextIdTemp(){
        numtemp++;
        return "temp" + numtemp;
    }
    
    public ICodeVisitor(){
        super();
        numtemp = 0;
        lastExpression = null;
    }

    /*Visit ForStmt*/
    @Override
    public List<Instruction> visit(ForStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        String id = getNextIdTemp();
        Label startLabel = new Label("startFor"+id);
        Label endLabel = new Label("endFor"+id);
        Label checkLabel = new Label("checkFor"+id);
        instructions.add(new Instruction(TypeInstruction.LABEL,startLabel));
        Location var = stmt.getAssign().getLocation();
        instructions.addAll(this.visit(var));
        instructions.addAll(this.visit(stmt.getAssign()));
        instructions.addAll(this.visit(stmt.getCondition()));
        Expression max = lastExpression;
        instructions.add(new Instruction(TypeInstruction.LABEL,checkLabel));
        VarLocation res = new VarLocation("res");
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
        return instructions;
    }
    
    /*Visit WhileStmt*/
    @Override
    public List<Instruction> visit(WhileStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        String id = getNextIdTemp();
        Label startLabel = new Label("startWhile"+id);
        Label endLabel = new Label("endWhile"+id);
        instructions.add(new Instruction(TypeInstruction.LABEL,startLabel));
        instructions.addAll(this.visit(stmt.getCondition()));
        Expression condition = lastExpression;
        Instruction jumpfalse = new Instruction (TypeInstruction.JUMPFALSE,condition,endLabel);
        instructions.add(jumpfalse);
        instructions.addAll(this.visit(stmt.getWhileStatement()));
        instructions.add(new Instruction(TypeInstruction.JUMP,startLabel));
        instructions.add(new Instruction(TypeInstruction.LABEL,endLabel));
        return instructions;
    }
    
    /*Visit IFStmt*/
    @Override
    public List<Instruction> visit(IfStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getCondition()));
        Expression condition = lastExpression;
        Label elseLabel = new Label("else"+getNextIdTemp());
        Label endLabel = new Label("endif"+getNextIdTemp());
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
    public List<Instruction> visit(AssignStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getExpression()));
        Expression operand = lastExpression;
        
        Location location = stmt.getLocation();
        switch (stmt.getOperator()){
            case ASSMNT:
                if (location.isArray()){
                    ArrayLocation array = ((ArrayLocation)location);
                    instructions.addAll(this.visit(array.getExpression()));
                    Expression pos = lastExpression;
                    Instruction assmnt;
                    switch (location.getType()){
                    case INT:
                        assmnt = new Instruction(TypeInstruction.INTARRAYASSMNT,operand,pos,array);
                        instructions.add(assmnt);
                        break;
                    case FLOAT:
                        assmnt = new Instruction(TypeInstruction.FLOATARRAYASSMNT,operand,pos,array);
                        instructions.add(assmnt);
                        break;
                    case BOOLEAN:
                        assmnt = new Instruction(TypeInstruction.BOOLARRAYASSMNT,operand,pos,array);
                        instructions.add(assmnt);
                        break;
                    default:
                        break;
                    }
                }
                else{
                    Instruction instruction = new Instruction(TypeInstruction.ASSMNT,operand,location);
                    instructions.add(instruction);
                }
                break;
            case ASSMNT_INC:
                if (location.isArray()){
                    ArrayLocation array = ((ArrayLocation)location);
                    instructions.addAll(this.visit(array.getExpression()));
                    Expression pos = lastExpression;
                    Instruction assmnt;
                    switch (location.getType()){
                    case INT:
                    {
                        VarLocation res = new VarLocation(getNextIdTemp());
                        Instruction access = new Instruction(TypeInstruction.INTARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        Instruction sum = new Instruction(TypeInstruction.SUMINT,res,operand,res);
                        instructions.add(sum);
                        assmnt = new Instruction(TypeInstruction.INTARRAYASSMNT,res,pos,array);
                        instructions.add(assmnt);
                    }
                        break;
                    
                    case FLOAT:
                    {
                        VarLocation res = new VarLocation(getNextIdTemp());
                        Instruction access = new Instruction(TypeInstruction.FLOATARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        Instruction sum = new Instruction(TypeInstruction.SUMFLOAT,res,operand,res);
                        instructions.add(sum);
                        assmnt = new Instruction(TypeInstruction.FLOATARRAYASSMNT,res,pos,array);
                        instructions.add(assmnt);
                    }
                        break;
                    }
                }
                else{
                    if (stmt.getLocation().getType().equals(Type.INT)){
                        Instruction instruction = new Instruction(TypeInstruction.SUMINT,location,operand,location);
                        instructions.add(instruction); 
                        lastExpression = location;
                    }
                    else{
                        Instruction instruction = new Instruction(TypeInstruction.SUMFLOAT,location,operand,location);
                        instructions.add(instruction); 
                        lastExpression = location;
                    }
                }
                break;
            case ASSMNT_DEC:
                if (location.isArray()){
                    ArrayLocation array = ((ArrayLocation)location);
                    instructions.addAll(this.visit(array.getExpression()));
                    Expression pos = lastExpression;
                    Instruction assmnt;
                    switch (location.getType()){
                    case INT:
                    {
                        VarLocation res = new VarLocation(getNextIdTemp());
                        Instruction access = new Instruction(TypeInstruction.INTARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        Instruction sub = new Instruction(TypeInstruction.SUBINT,res,operand,res);
                        instructions.add(sub);
                        assmnt = new Instruction(TypeInstruction.INTARRAYASSMNT,res,pos,array);
                        instructions.add(assmnt);
                    }
                        break;
                    
                    case FLOAT:
                    {
                        VarLocation res = new VarLocation(getNextIdTemp());
                        Instruction access = new Instruction(TypeInstruction.FLOATARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        Instruction sub = new Instruction(TypeInstruction.SUBFLOAT,res,operand,res);
                        instructions.add(sub);
                        assmnt = new Instruction(TypeInstruction.FLOATARRAYASSMNT,res,pos,array);
                        instructions.add(assmnt);
                    }
                        break;
                    }
                }
                else{
                    if (stmt.getLocation().getType().equals(Type.INT)){
                        Instruction instruction = new Instruction(TypeInstruction.SUBINT,location,operand,location);
                        instructions.add(instruction); 
                        lastExpression = location;
                    }
                    else{
                        Instruction instruction = new Instruction(TypeInstruction.SUBFLOAT,location,operand,location);
                        instructions.add(instruction); 
                        lastExpression = location;
                    }
                }
                break;
        }
        
        return  instructions;
    }
    
    /*Visit Expression*/
    @Override
    public List<Instruction> visit(Expression exp) {
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
                lastExpression = exp;
                return instructions;
            case "ArrayLocation" :
                ArrayLocation array = ((ArrayLocation)exp);
                instructions.addAll(this.visit(array.getExpression()));
                Expression pos = lastExpression;
                
                Instruction access;
                VarLocation res = new VarLocation(getNextIdTemp());
                switch (exp.getType()){
                    case INT:
                        access = new Instruction(TypeInstruction.INTARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        break;
                    case FLOAT:
                        access = new Instruction(TypeInstruction.FLOATARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        break;
                    case BOOLEAN:
                        access = new Instruction(TypeInstruction.BOOLARRAYACCESS,array,pos,res);
                        instructions.add(access);
                        break;
                    default:
                        break;
                }
                lastExpression = res;
                return instructions;
            default:
                return null;
        }
    }
    
    /*Visit ReturnStmt*/
    @Override
    public List<Instruction> visit(MethodCallStmt m) {
        LinkedList<Instruction> instructions = new LinkedList();
        List<Expression> expressions = m.getExpressions();
        for (Expression e : expressions){
            
            instructions.addAll(this.visit(e));
            Instruction instruction = new Instruction(TypeInstruction.PUSH, lastExpression);
            instructions.add(instruction);
        }
        VarLocation result = new VarLocation(getNextIdTemp());
        Instruction instruction = new Instruction(TypeInstruction.CALL, m.getLocation(),result);
        lastExpression = result;
        instructions.add(instruction);
        return instructions;
    }
    
    
    /*Visit MethodCall*/
    @Override
    public List<Instruction> visit(MethodCall m) {
        return this.visit(m.getMethod());
    }
    
    
    /*Visit ReturnStmt*/
    @Override
    public List<Instruction> visit(ReturnStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getExpression()));
        Instruction instruction = new Instruction(TypeInstruction.RETURN, lastExpression);
        instructions.add(instruction);
        return instructions;
        
    }
    
    /*Visit BreakStmt*/
    @Override
    public List<Instruction> visit(BreakStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        return instructions;
        //FALTA IMPLEMENTAR
    }

    /*Visit ContinueStmt*/
    @Override
    public List<Instruction> visit(ContinueStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        return instructions;
         //FALTA IMPLEMENTAR
    }
    
    
    /*Visit BinOpExpr*/
    @Override
    public List<Instruction> visit(BinOpExpr expr) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(expr.getLeftOperand()));
        Expression op1 = lastExpression;
        instructions.addAll(this.visit(expr.getRightOperand()));
        Expression op2 = lastExpression;
        
        switch(expr.getOperator()){
            case PLUS:
		if (expr.getType().equals(Type.INT)){
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.SUMINT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                else{
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.SUMFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                break;
            case MINUS:
		if (expr.getType().equals(Type.INT)){
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.SUBINT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                else{
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.SUBFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                break;
            case TIMES:
		if (expr.getType().equals(Type.INT)){
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.MULTINT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                else{
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.MULTFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                break;
            case DIVIDE:
		if (expr.getType().equals(Type.INT)){
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.DIVIDEINT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                else{
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.DIVIDEFLOAT,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                break;
            case MOD:
                if (expr.getType().equals(Type.INT)){
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.MOD,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                else{
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.MOD,op1,op2,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                break;
            case LESS:
                {VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.LESS,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case LESS_EQ:
                {VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.LESS_EQ,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case GTR:
		{VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.GTR,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case GTR_EQ:
		{VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.GTR_EQ,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case EQ:
		{VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.EQ,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case NOT_EQ:
		{VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.NOT_EQ,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case AND:
		{VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.AND,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;
            case OR:
		{VarLocation result = new VarLocation(getNextIdTemp());
                Instruction instruction = new Instruction(TypeInstruction.OR,op1,op2,result);
                instructions.add(instruction);
                lastExpression = result;}
                break;       
        }
        return instructions;
    }
    
    
    
    /*Visit UnopExpr*/
    @Override
    public List<Instruction> visit(UnOpExpr expr) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(expr.getExpression()));
        switch (expr.getOperator()){
            case MINUS:
                if (expr.getType().equals(Type.INT)){
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.MINUSINT,lastExpression,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                else{
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.MINUSFLOAT,lastExpression,result);
                    instructions.add(instruction);
                    lastExpression = result;
                }
                break;
            case LOGIC_NEGATION:
                    VarLocation result = new VarLocation(getNextIdTemp());
                    Instruction instruction = new Instruction(TypeInstruction.NEGATION,lastExpression,result);
                    instructions.add(instruction);
                    lastExpression = result;
                break;
        }
        return instructions;
    }
    
    /*Visit ArrayLocation*/
    @Override
    public List<Instruction> visit(ArrayLocation loc) { 
        LinkedList<Instruction> instructions = new LinkedList();
        ArrayList values = new ArrayList(loc.getSize());
        switch (loc.getType()){
                case INT:
                    instructions.add(new Instruction(TypeInstruction.DECLINTARRAY,loc));
                    break;
                case FLOAT:
                    instructions.add(new Instruction(TypeInstruction.DECLFLOATARRAY,loc));
                    break;
                case BOOLEAN:
                    instructions.add(new Instruction(TypeInstruction.DECLBOOLEANARRAY,loc));
                    break;
                default:
                    break;
        }   
        loc.setValues(values);
        return instructions;
    }
    
    /*Visit VarLocation*/
    @Override
    public List<Instruction> visit(VarLocation loc) {  
        LinkedList<Instruction> instructions = new LinkedList();
            switch (loc.getType()){
                case INT:
                    instructions.add(new Instruction(TypeInstruction.DECLINT, loc));
                    break;
                case FLOAT:
                    instructions.add(new Instruction(TypeInstruction.DECLFLOAT, loc));
                    break;
                case BOOLEAN:
                    instructions.add(new Instruction(TypeInstruction.DECLBOOLEAN, loc));
                    break;
                default:
                    break;
            }
            return instructions;
    }
    
    
    
    /*Visit FieldDecl*/
    @Override
    public List<Instruction> visit(FieldDecl fd) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (Location l : fd.getLocations()){
            instructions.addAll(this.visit(l));
        }
        return instructions;
    }
    
    /*Visit Block*/
    @Override
    public List<Instruction> visit(Block block) {
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
    public List<Instruction> visit(MethodDecl m) {
        LinkedList<Instruction> instructions = new LinkedList();
        Label label = new Label(m.getId());
        Label endlabel = new Label("end" + m.getId());
        if (!m.ifExtern()){
            instructions.add(new Instruction(TypeInstruction.METHODDECL, label));
            instructions.addAll(this.visit(m.getBlock()));          
        }
        else{
            instructions.add(new Instruction(TypeInstruction.METHODDECLEXTERN, label));
        }
        instructions.add(new Instruction(TypeInstruction.LABEL, endlabel)); 
        return instructions;
    }

    /*Visit ClassDecl*/
    @Override
    public List<Instruction> visit(ClassDecl c) {
        LinkedList<Instruction> instructions = new LinkedList();
        if (c.getId().equals("main")){ //Only visit Class main
            for (FieldDecl f : c.getFields()){
                instructions.addAll(this.visit(f));
            }
            for (MethodDecl m : c.getMethods()){
                instructions.addAll(this.visit(m)); 
            }
        }
        return instructions;
    }

    /*Visit Program*/
    @Override
    public List<Instruction> visit(Program p) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (ClassDecl c : p.getClasses()){
            instructions.addAll(this.visit(c));
        }
        return instructions;
    }
    
    
}