package ir.ast;

import ir.ASTVisitor;

public abstract class Location extends Expression {

    @Override
    public String toString() {
       return id;
    }
    
    @Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
    
}
