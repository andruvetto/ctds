/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.semcheck;
import ir.ASTVisitor;
import ir.ast.*;
import java.util.List;

/**
 *
 * @author daniel
 */
public class CheckCycleSentencesVisitor implements ASTVisitor<Boolean> {

    @Override
    public Boolean visit(AssignStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(ReturnStmt stmt) {
       return true;
    }

    @Override
    public Boolean visit(IfStmt stmt) {
        return (this.visit(stmt.getIfStatement()) && this.visit(stmt.getElseStatement()));
    }

    @Override
    public Boolean visit(ForStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(WhileStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(BreakStmt stmt) {
        return false;
    }

    @Override
    public Boolean visit(ContinueStmt stmt) {
        return false;
    }

    @Override
    public Boolean visit(ExternStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(BinOpExpr expr) {
        return true;
    }

    @Override
    public Boolean visit(UnOpExpr expr) {
        return true;
    }

    @Override
    public Boolean visit(IntLiteral lit) {
        return true;
    }

    @Override
    public Boolean visit(FloatLiteral lit) {
        return true;
    }

    @Override
    public Boolean visit(BooleanLiteral lit) {
        return true;
    }

    @Override
    public Boolean visit(MethodCall m) {
        return true;
    }

    @Override
    public Boolean visit(MethodCallStmt m) {
        return true;
    }

    @Override
    public Boolean visit(VarLocation loc) {
        return true;
    }

    @Override
    public Boolean visit(ArrayLocation loc) {
        return true;
    }

    @Override
    public Boolean visit(Block block) {
        boolean res = true;
        for(Statement s : block.getStatements()){
            res = res && this.visit(s);
        }
        return res;
    }

    @Override
    public Boolean visit(FieldDecl fd) {
        return true;
    }

    @Override
    public Boolean visit(Parameter p) {
        return true;
    }

    @Override
    public Boolean visit(MethodDecl m) {
        boolean res = true;
        if (!m.ifExtern()) {
            res = this.visit(m.getBlock());
        }
        return res;
    }

    @Override
    public Boolean visit(ClassDecl c) {
        boolean res = true;
        for (MethodDecl m : c.getMethods()){
            res = res && this.visit(m);
        }
        return res;
    }

    @Override
    public Boolean visit(Program p) {
        boolean res = true;
        for (ClassDecl c: p.getClasses()){
            res = res && this.visit(c);
        }
        return res;
    }

    @Override
    public Boolean visit(Statement stmt) {
        boolean res = true;
        TypeStmt type = stmt.getType();
        switch (type){
            case ASSIGN:
                res = this.visit((AssignStmt) stmt);
                break;
            case METHODCALL:
                res = this.visit((MethodCallStmt) stmt);
                break;
            case IF:
                res = this.visit((IfStmt) stmt);
                break;
            case FOR:
                res = this.visit((ForStmt) stmt);
                break;
            case WHILE:
                res = this.visit((WhileStmt) stmt);
                break;
            case RETURN:
                res = this.visit((ReturnStmt) stmt);
                break;
            case BREAK:
                res = this.visit((BreakStmt) stmt);
                break;
            case CONTINUE:
                res = this.visit((ContinueStmt) stmt);
                break;
            case BLOCK:
                res = this.visit((Block) stmt);
                break;
            default :
                System.err.println("Error in visit Statement");
                break;
        }
        return res;
        
    }


    
}
