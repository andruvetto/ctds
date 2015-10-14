package lib.ir.interpreter;

import lib.ir.Visitor;
import lib.ir.ast.*;
import lib.error.Error;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InterpreterVisitor extends Visitor<Object> {
    
    private LinkedList<VarLocation> variables;
    
    public InterpreterVisitor(){
        super();
        variables = new LinkedList();
    }
    
    @Override
    public Object visit(AssignStmt stmt) {
        Object evalue = this.visit(stmt.getExpression());      
        Object lvalue = getDeclaratedValue((VarLocation)stmt.getLocation());
        
        VarLocation location = getDeclarated((VarLocation)stmt.getLocation());
        
        switch (stmt.getOperator()){
            case ASSMNT:
                location.setValue(evalue);
                break;
            case ASSMNT_INC:
                if (stmt.getLocation().getType().equals(Type.INT)){
                    location.setValue((Integer)lvalue + (Integer)evalue);
                }
                else{
                    location.setValue((Float)lvalue + (Float)evalue);
                }
                break;                
        }
        stmt.setValue(location.getValue());
        return stmt.getValue();
    }

    @Override
    public Object visit(ReturnStmt stmt) {
        if (stmt.getExpression() != null) stmt.setValue(this.visit(stmt.getExpression()));
        else stmt.setValue(null);
        System.out.println(stmt + " ------ " + stmt.getValue());
        return stmt.getValue();
    }

    @Override
    public Object visit(IfStmt stmt) {
        Boolean condition = (Boolean)(this.visit(stmt.getCondition()));
        if (condition) stmt.setValue(this.visit(stmt.getIfStatement()));
        else stmt.setValue(this.visit(stmt.getElseStatement()));
        return stmt.getValue();
    }

    @Override
    public Object visit(ForStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visit(WhileStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visit(BreakStmt stmt) {
        return null;
    }

    @Override
    public Object visit(ContinueStmt stmt) {
        return null;
    }

    @Override
    public Object visit(BinOpExpr expr) {
        Object vloperand = this.visit(expr.getLeftOperand());
        Object vroperand = this.visit(expr.getRightOperand());
        
        switch(expr.getOperator()){
            case PLUS:
		if (expr.getType().equals(Type.INT)){
                    expr.setValue((Integer)vloperand + (Integer)vroperand);
                }
                else{
                    expr.setValue((Float)vloperand + (Float)vroperand);
                }
                break;
            case MINUS:
		if (expr.getType().equals(Type.INT)){
                    expr.setValue((Integer)vloperand - (Integer)vroperand);
                }
                else{
                    expr.setValue((Float)vloperand - (Float)vroperand);
                }
                break;
            case TIMES:
		if (expr.getType().equals(Type.INT)){
                    expr.setValue((Integer)vloperand * (Integer)vroperand);
                }
                else{
                    expr.setValue((Float)vloperand * (Float)vroperand);
                }
                break;
            case DIVIDE:
		if (expr.getType().equals(Type.INT)){
                    expr.setValue((Integer)vloperand / (Integer)vroperand);
                }
                else{
                    expr.setValue((Float)vloperand / (Float)vroperand);
                }
                break;
            case MOD:
                if (expr.getType().equals(Type.INT)){
                    expr.setValue((Integer)vloperand % (Integer)vroperand);
                }
                else{
                    expr.setValue((Float)vloperand % (Float)vroperand);
                }
                break;
            case LESS:
		expr.setValue(((Comparable)vloperand).compareTo(vroperand) < 0 );
                break;
            case LESS_EQ:
		expr.setValue(((Comparable)vloperand).compareTo(vroperand) <= 0 );
                break;
            case GTR:
		expr.setValue(((Comparable)vloperand).compareTo(vroperand) > 0 );
                break;
            case GTR_EQ:
		expr.setValue(((Comparable)vloperand).compareTo(vroperand) >= 0 );
                break;
            case EQ:
		expr.setValue(vloperand.equals(vroperand));
                break;
            case NOT_EQ:
		expr.setValue(!vloperand.equals(vroperand));
                break;
            case AND:
		expr.setValue((Boolean)vloperand && (Boolean)vroperand);
                break;
            case OR:
		expr.setValue((Boolean)vloperand || (Boolean)vroperand);
                break;
                    
        }
        return expr.getValue();
    }

    @Override
    public Object visit(UnOpExpr expr) {
        switch (expr.getOperator()){
            case MINUS:
                if (expr.getType().equals(Type.INT)) expr.setValue(-(Integer)this.visit(expr.getExpression()));
                else expr.setValue(-(Float)this.visit(expr.getExpression()));
                break;
            case LOGIC_NEGATION:
                expr.setValue(!(Boolean)this.visit(expr.getExpression()));
                break;
        }
        return expr.getValue();
    }

    @Override
    public Object visit(IntLiteral lit) {
        return lit.getValue();
    }

    @Override
    public Object visit(FloatLiteral lit) {
        return lit.getValue();
    }

    @Override
    public Object visit(BooleanLiteral lit) {
        return lit.getValue();
    }

    @Override
    public Object visit(MethodCall m) {
        List<Parameter> parameters = m.getMethodDecl().getParameters();
        List<Expression> expressions = m.getExpressions();
        for (int i = 0; i<parameters.size(); i++){
            
            Object evalue = this.visit(expressions.get(i));
            
            VarLocation var = parameters.get(i).getVarLocation();
            
            this.visit(var);
            var.setValue(evalue);
            
            
            //var.setValue(this.visit(expressions.get(i)));
            //variables.push(var);
            
            
        }
        m.setValue(this.visit(m.getMethodDecl()));
        
        return m.getValue();
    }
    
    @Override
    public Object visit(Parameter p) {
        
        return null;
    }

    @Override
    public Object visit(MethodCallStmt m) {
        return this.visit(m.getMethod());
    }

    @Override
    public Object visit(VarLocation loc) {
        
        loc.setValue(getDeclaratedValue(loc));
        
        if (loc.getValue() == null){
            switch (loc.getType()){
                case INT:
                    loc.setValue(0);
                    break;
                case FLOAT:
                    loc.setValue(0.0);
                    break;
                case BOOLEAN:
                    loc.setValue(false);
                    break;
                default:
                    loc.setValue(null);
                    break;
            }
            variables.push(loc);
        }
        //variables.add(loc);
        
        return loc.getValue();
    }
    
    private Object getDeclaratedValue(VarLocation loc){
        for (VarLocation v : variables){
            if (v.getId().equals(loc.getId())){
               // System.out.println(v.getValue());
                return v.getValue();
            }
        }
        //System.out.println("Retorno null!!");
        return null;
    }
    
     private VarLocation getDeclarated(VarLocation loc){
        for (VarLocation v : variables){
            if (v.getId().equals(loc.getId())){
               // System.out.println(v.getValue());
                return v;
            }
        }
        //System.out.println("Retorno null!!");
        return null;
    }

    @Override
    public Object visit(ArrayLocation loc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object visit(Block block) {
        for (FieldDecl f : block.getFields()){
            this.visit(f);
        }
        for (Statement s : block.getStatements()){
            if (s.getClass().getSimpleName().equals("ReturnStmt")) return this.visit(s);
            else this.visit(s);
            System.out.println(s + " ------ " + s.getValue());
        }
        return null;
    }

    @Override
    public Object visit(FieldDecl fd) {
        for (Location l : fd.getLocations()){
            this.visit(l);
            System.out.println(l + " ------ " + l.getValue());
        }
        return null;
    }


    @Override
    public Object visit(MethodDecl m) {
        m.setValue(this.visit(m.getBlock()));
        return m.getValue();
    }

    @Override
    public Object visit(ClassDecl c) {
        for (FieldDecl f : c.getFields()){
            this.visit(f);
        }
        for (MethodDecl m : c.getMethods()){
            if (m.getId().equals("main")){
                this.visit(m);
            }
        }
        return null;
    }

    @Override
    public Object visit(Program p) {
        for (ClassDecl c : p.getClasses()){
            this.visit(c);
        }
        return null;
    }

}