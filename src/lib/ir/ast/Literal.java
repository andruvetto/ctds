package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class Literal extends Expression {
	/*
	 * @return: returns Type of Literal instance
	 */
        
        @Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}
