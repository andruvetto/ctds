package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class Expression extends AST {

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
}
