
package ir.ast;

import ir.ASTVisitor;

public class ExternStmt extends Statement {
    
    @Override
    public String toString() {
       return "extern";
    }

    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
