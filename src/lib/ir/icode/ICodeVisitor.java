package lib.ir.icode;
import java.util.ArrayList;
import lib.ir.Visitor;
import lib.ir.ast.*;
import java.util.List;
import java.util.LinkedList;


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
    
    
    @Override
    public List<Instruction> visit(AssignStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getExpression()));
        //System.out.println("VISITO EXPRESSION");
        Expression operand = lastExpression;
        
        Location location = stmt.getLocation();
        switch (stmt.getOperator()){
            case ASSMNT:
                if (location.isArray()){
                    //Integer pos = (Integer)this.visit(((ArrayLocation)location).getExpression());
                    //((ArrayLocation)locationDeclarated).setValueAt(pos, evalue);
                    //stmt.setValue(((ArrayLocation)locationDeclarated).getValueAt(pos));
                }
                else{
                    Instruction instruction = new Instruction(TypeInstruction.ASSMNT,operand,location);
                    instructions.add(instruction);
                }
                break;
            case ASSMNT_INC:
                if (location.isArray()){
                    //Integer pos = (Integer)this.visit(((ArrayLocation)location).getExpression());
                    //if (stmt.getLocation().getType().equals(Type.INT)){
                    //    ((ArrayLocation)locationDeclarated).setValueAt(pos, (Integer)lvalue + (Integer)evalue);
                    //    stmt.setValue(((ArrayLocation)locationDeclarated).getValueAt(pos));
                    //}
                    //else{
                    //    ((ArrayLocation)locationDeclarated).setValueAt(pos, (Float)lvalue + (Float)evalue);
                    //    stmt.setValue(((ArrayLocation)locationDeclarated).getValueAt(pos));
                    //}
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
                    //Integer pos = (Integer)this.visit(((ArrayLocation)location).getExpression());
                    //if (stmt.getLocation().getType().equals(Type.INT)){
                    //    ((ArrayLocation)locationDeclarated).setValueAt(pos, (Integer)lvalue - (Integer)evalue);
                    //    stmt.setValue(((ArrayLocation)locationDeclarated).getValueAt(pos));
                    //}
                    //else{
                    //    ((ArrayLocation)locationDeclarated).setValueAt(pos, (Float)lvalue - (Float)evalue);
                    //    stmt.setValue(((ArrayLocation)locationDeclarated).getValueAt(pos));
                    //}
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
                lastExpression = exp;
                return instructions;
            default:
                return null;
        }
    }
    
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
    
    @Override
    public List<Instruction> visit(MethodCall m) {
        return this.visit(m.getMethod());
    }
    
    @Override
    public List<Instruction> visit(ReturnStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        instructions.addAll(this.visit(stmt.getExpression()));
        Instruction instruction = new Instruction(TypeInstruction.RETURN, lastExpression);
        instructions.add(instruction);
        return instructions;
        
    }
    
    @Override
    public List<Instruction> visit(BreakStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        return instructions;
        //FALTA IMPLEMENTAR
    }

    @Override
    public List<Instruction> visit(ContinueStmt stmt) {
        LinkedList<Instruction> instructions = new LinkedList();
        return instructions;
         //FALTA IMPLEMENTAR
    }
    
    
    
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
    
    @Override
    public List<Instruction> visit(ArrayLocation loc) { 
        LinkedList<Instruction> instructions = new LinkedList();
        ArrayList values = new ArrayList(loc.getSize());
        switch (loc.getType()){
                case INT:
                    for (int i = 0; i < loc.getSize(); i++ ){
                        values.add(0);
                    }
                    instructions.add(new Instruction(TypeInstruction.DECLINTARRAY,loc));
                    break;
                case FLOAT:
                   for (int i = 0; i < loc.getSize(); i++ ){
                        values.add(0.0);
                    }
                    instructions.add(new Instruction(TypeInstruction.DECLFLOATARRAY,loc));
                    break;
                case BOOLEAN:
                    for (int i = 0; i < loc.getSize(); i++ ){
                        values.add(false);
                    }
                    instructions.add(new Instruction(TypeInstruction.DECLBOOLEANARRAY,loc));
                    break;
                default:
                    values.add(null);
                    break;
        }   
        loc.setValues(values);
        return instructions;
    }
        
    @Override
    public List<Instruction> visit(VarLocation loc) {  
        LinkedList<Instruction> instructions = new LinkedList();
            switch (loc.getType()){
                case INT:
                    loc.setValue(0);
                    instructions.add(new Instruction(TypeInstruction.DECLINT, loc));
                    break;
                case FLOAT:
                    loc.setValue(0.0);
                    instructions.add(new Instruction(TypeInstruction.DECLFLOAT, loc));
                    break;
                case BOOLEAN:
                    loc.setValue(false);
                    instructions.add(new Instruction(TypeInstruction.DECLBOOLEAN, loc));
                    break;
                default:
                    loc.setValue(null);
                    break;
            }
            return instructions;
    }
    
    
    
    
    @Override
    public List<Instruction> visit(FieldDecl fd) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (Location l : fd.getLocations()){
            instructions.addAll(this.visit(l));
        }
        return instructions;
    }
    
    
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

    @Override
    public List<Instruction> visit(ClassDecl c) {
        LinkedList<Instruction> instructions = new LinkedList();
        if (c.getId().equals("main")){
            for (FieldDecl f : c.getFields()){
                instructions.addAll(this.visit(f));
            }
            for (MethodDecl m : c.getMethods()){
                instructions.addAll(this.visit(m)); 
            }
        }
        return instructions;
    }

    @Override
    public List<Instruction> visit(Program p) {
        LinkedList<Instruction> instructions = new LinkedList();
        for (ClassDecl c : p.getClasses()){
            instructions.addAll(this.visit(c));
        }
        return instructions;
    }
    
    
}