package lib.ir.ast;

import lib.ir.ASTVisitor;

public class BreakStmt extends Statement{
    
    @Override
    public String toString() {
       return "break";
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
