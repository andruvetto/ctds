/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.semcheck;

import ir.ASTVisitor;
import ir.ast.*;


/**
 *
 * @author daniel
 */
public class PrintVisitor implements ASTVisitor<String>{

    @Override
    public String visit(AssignStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(ReturnStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(IfStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(ForStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(WhileStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(BreakStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(ContinueStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(ExternStmt stmt) {
        System.out.println(stmt);
        return (null);
    }

    @Override
    public String visit(BinOpExpr expr) {
        System.out.println(expr);
        return (null);
    }

    @Override
    public String visit(UnOpExpr expr) {
        System.out.println(expr);
        return (null);
    }

    @Override
    public String visit(IntLiteral lit) {
        System.out.println(lit);
        return (null);
    }

    @Override
    public String visit(FloatLiteral lit) {
        System.out.println(lit);
        return (null);
    }

    @Override
    public String visit(BooleanLiteral lit) {
        System.out.println(lit);
        return (null);
    }

    @Override
    public String visit(MethodCall m) {
        System.out.println(m);
        return (null);
    }

    @Override
    public String visit(MethodCallStmt m) {
        System.out.println(m);
        return (null);
    }

    @Override
    public String visit(VarLocation loc) {
        System.out.println(loc);
        return (null);
    }

    @Override
    public String visit(ArrayLocation loc) {
        System.out.println(loc);
        return (null);
    }

    @Override
    public String visit(Block block) {
        System.out.println(block);
        return (null);
    }

    @Override
    public String visit(FieldDecl fd) {
        System.out.println(fd);
        return (null);
    }

    @Override
    public String visit(Parameter p) {
        System.out.println(p);
        return (null);
    }

    @Override
    public String visit(MethodDecl m) {
        System.out.println(m);
        return (null);
    }

    @Override
    public String visit(ClassDecl c) {
        System.out.println(c);
        return (null);
    }

    @Override
    public String visit(Program p) {     
        System.out.println(p);
        return (null);
    }

    @Override
    public String visit(Statement stmt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
