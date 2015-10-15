package lib.ir.interpreter;

import java.util.ArrayList;
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
    
    public InterpreterVisitor(){
        super();
    }
    
    @Override
    public Object visit(AssignStmt stmt) {
        Object evalue = this.visit(stmt.getExpression());
        Object lvalue;
        
        Location location;

        if (stmt.getLocation().isArray()){
           location = stmt.getLocation();
           Integer pos = (Integer) this.visit(((ArrayLocation)location).getExpression());
           lvalue = ((ArrayLocation)location.getDeclarated()).getValueAt(pos);
        }
        else{
           location = stmt.getLocation().getDeclarated();
           lvalue = this.visit(location);
        }
        
        switch (stmt.getOperator()){
            case ASSMNT:
                if (location.isArray()){
                    Integer pos = (Integer) this.visit(((ArrayLocation)location).getExpression());
                    ((ArrayLocation)location.getDeclarated()).setValueAt(pos, evalue);
                    stmt.setValue(((ArrayLocation)location.getDeclarated()).getValueAt(pos));

                    
                }else{
                   location.setValue(evalue);
                   stmt.setValue(location.getValue());
                }
                
                break;
            case ASSMNT_INC:
                if (stmt.getLocation().getType().equals(Type.INT)){
                    location.setValue((Integer)lvalue + (Integer)evalue);
                }
                else{
                    location.setValue((Float)lvalue + (Float)evalue);
                    stmt.setValue(location.getValue());
                }
                break;                
        }

        System.out.println("Assignated: " + stmt + " -------- " + stmt.getValue());
        
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
        else{
            if (stmt.getElseStatement() != null) stmt.setValue(this.visit(stmt.getElseStatement()));
        }
        return stmt.getValue();
    }

    @Override
    public Object visit(ForStmt stmt) {
        System.out.println("Executing " + stmt);
        Integer i = (Integer)this.visit(stmt.getAssign());
        Integer n = (Integer)this.visit(stmt.getCondition());

        while (i<n){
            //System.out.println(this.visit(stmt.getForStatement()));
            this.visit(stmt.getForStatement());
            i++;
        }
        System.out.println("End " + stmt);
        return null;
    }

    @Override
    public Object visit(WhileStmt stmt) {
        System.out.println("Executing " + stmt);
        while((Boolean)this.visit(stmt.getCondition())){
            this.visit(stmt.getWhileStatement());
        }
        System.out.println("End " + stmt);
        return null;
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
    public Object visit(MethodCallStmt m) {
        List<Parameter> parameters = m.getMethodDecl().getParameters();
        List<Expression> expressions = m.getExpressions();
        for (int i = 0; i<parameters.size(); i++){
            parameters.get(i).getVarLocation().setValue(this.visit(expressions.get(i)));
        }
        m.setValue(this.visit(m.getMethodDecl()));
        return m.getValue();
    }
    
    @Override
    public Object visit(MethodCall m) {
        return this.visit(m.getMethod());
    }
    
    @Override
    public Object visit(VarLocation loc) {  
        if (loc.getDeclarated() != null){
            loc.setValue(loc.getDeclarated().getValue());
        }
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
        }
        return loc.getValue();
    }

    @Override
    public Object visit(ArrayLocation loc) {
        //System.out.println("size loc -------- " + loc.getSize());
        //System.out.println("size locDec -------- " + ((ArrayLocation)loc.getDeclarated()).getSize());
        //System.out.println(loc.getValues());
        //System.out.println(loc.getValues().size());
        
        
        if (loc.getValues() != null){
            ArrayList values = new ArrayList(loc.getSize());
            for (int i = 0; i < loc.getSize(); i++ ){
                switch (loc.getType()){
                case INT:
                    values.add(0);
                    break;
                case FLOAT:
                    values.add(0.0);
                    break;
                case BOOLEAN:
                    values.add(false);
                    break;
                default:
                    values.add(null);
                    break;
                }
            }
            loc.setValues(values);
            return null;
        }
        else{
            ArrayLocation locDeclarated = (ArrayLocation)loc.getDeclarated();
            Integer pos = (Integer) this.visit(loc.getExpression());
            if (pos>=locDeclarated.getSize()){
                System.out.println("ERROR ARREGLO FUERA DE RANGO");
                return null;
            }
            else{
                return locDeclarated.getValueAt(pos);
            }
            
            
        }
    }

    @Override
    public Object visit(Block block) {
        for (FieldDecl f : block.getFields()){
            this.visit(f);
        }
        for (Statement s : block.getStatements()){
            if (s.getClass().getSimpleName().equals("ReturnStmt")) return this.visit(s);
            else this.visit(s);
        }
        return null;
    }

    @Override
    public Object visit(FieldDecl fd) {
        for (Location l : fd.getLocations()){
            this.visit(l);
            System.out.println("Declarated " + l + " ----------- " + l.getValue());
        }
        return null;
    }


    @Override
    public Object visit(MethodDecl m) {
        System.out.println("Visiting " + m);
        m.setValue(this.visit(m.getBlock()));
        System.out.println("End visiting " + m);
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