package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class Location extends Expression {
    protected boolean isArray;
    
    @Override
    public String toString() {
       return id;
    }
    
    @Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
    
}
