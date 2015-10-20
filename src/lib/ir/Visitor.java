package lib.ir;

import lib.ir.ast.*;
import java.util.LinkedList;
import java.util.List;
import lib.error.Error;

//This abstract class implements partially ASTVisitor 
public abstract class Visitor<T> implements ASTVisitor {
    
    private List<Error> errors;
    
    public Visitor(){
        errors = new LinkedList();
    }
    
    protected void addError(AST a, String desc) {
	errors.add(new Error(a.getLineNumber(), a.getColumnNumber(), desc));
    }

    public List<Error> getErrors() {
    	return errors;
    }

    public void setErrors(List<Error> errors) {
    	this.errors = errors;
    }
    
    @Override
    public T visit(Statement stmt) {
        String c = stmt.getClass().getSimpleName();
        switch (c) {
            case "Block" :
                return (T) this.visit((Block) stmt);                
            case "BreakStmt":
                return (T) this.visit((BreakStmt) stmt);
            case "ContinueStmt":
                return (T) this.visit((ContinueStmt) stmt);
            case "FieldDecl":
                return (T) this.visit((FieldDecl) stmt);
            case "ForStmt":
                return (T) this.visit((ForStmt) stmt);
            case "IfStmt":
                return (T) this.visit((IfStmt) stmt);
            case "MethodCallStmt":
                return (T) this.visit((MethodCallStmt) stmt);
            case "ReturnStmt":
                return (T) this.visit((ReturnStmt) stmt);
            case "WhileStmt":
                return (T) this.visit((WhileStmt) stmt);
             case "AssignStmt":
                return (T) this.visit((AssignStmt) stmt);
            default:
                System.out.println(c);
                System.out.println("----------------------ERROR");
                return null;
        }
    }
    
    @Override
    public T visit(Expression exp) {
        String c = exp.getClass().getSimpleName();
        switch (c) {
            case "BinOpExpr" :
                return (T) this.visit((BinOpExpr) exp);
            case "UnOpExpr" :
                return (T) this.visit((UnOpExpr) exp);
            case "MethodCall" :
                return (T) this.visit((MethodCall) exp);
            case "Location" :
                return (T) this.visit((Location) exp);
            case "Literal" :
                return (T) this.visit((Literal) exp);
            case "IntLiteral" :
                return (T) this.visit((IntLiteral) exp);
            case "FloatLiteral" :
                return (T) this.visit((FloatLiteral) exp);
            case "BooleanLiteral" :
                return (T) this.visit((BooleanLiteral) exp);
            case "VarLocation" :
                return (T) this.visit((VarLocation) exp);
            case "ArrayLocation" :
                return (T) this.visit((ArrayLocation) exp);
            default:
                return null;
        }
    }
    
    @Override
    public T visit(Location loc) {
        String c = loc.getClass().getSimpleName();
        switch (c) {
            case "VarLocation" :
                return (T) this.visit((VarLocation) loc);
            case "ArrayLocation" :
                return (T) this.visit((ArrayLocation) loc);
            default:
                return null;
        }
    }
    
    @Override
    public T visit(Literal lit) {
        String c = lit.getClass().getSimpleName();
        switch (c) {
            case "IntLiteral" :
                return (T) this.visit((IntLiteral) lit);
            case "FloatLiteral" :
                return (T) this.visit((FloatLiteral) lit);
            case "BooleanLiteral" :
                return (T) this.visit((BooleanLiteral) lit);
            default:
                return null;
        }
    }
    
    @Override
    public T visit(AssignStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(ReturnStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(IfStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(ForStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(WhileStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(BreakStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(ContinueStmt stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(BinOpExpr expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(UnOpExpr expr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(IntLiteral lit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(FloatLiteral lit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(BooleanLiteral lit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(MethodCallStmt m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(MethodCall m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(VarLocation loc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(ArrayLocation loc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(Block block) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(FieldDecl fd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T visit(Parameter p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
 
    
}
