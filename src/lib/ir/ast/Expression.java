package ir.ast;

import ir.ASTVisitor;

public abstract class Expression extends AST {

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
}
