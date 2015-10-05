package ir.semcheck;

import ir.ASTVisitor;
import ir.ast.*;

abstract class Visitor<T> implements ASTVisitor {
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
    
 
    
}
