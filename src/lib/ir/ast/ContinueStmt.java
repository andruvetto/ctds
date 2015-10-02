package ir.ast;

import ir.ASTVisitor;

public class ContinueStmt extends Statement{
    
    @Override
    public String toString() {
       return "continue";
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
