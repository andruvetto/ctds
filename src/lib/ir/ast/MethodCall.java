package ir.ast;

import ir.ASTVisitor;

public class MethodCall extends Expression {
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }

}
