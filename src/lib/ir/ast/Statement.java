package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class Statement extends AST {
    protected Statement expr;
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
