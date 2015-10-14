package lib.ir.ast;

import lib.ir.ASTVisitor;

public class BooleanLiteral extends Literal {
	private String rawValue;
	
	/*
	 * Constructor for int literal that takes a string as an input
	 * @param: String integer
	 */
	public BooleanLiteral(String val){
		rawValue = val; // Will convert to int value in semantic check
		value = null;
	}
        
        public BooleanLiteral(boolean val){
		rawValue = null; // Will convert to int value in semantic check
		value = val;
	}

	@Override
	public Type getType() {
		return Type.BOOLEAN;
	}

	public String getStringValue() {
		return rawValue;
	}

	public void setStringValue(String stringValue) {
		this.rawValue = stringValue;
	}

	
	public String getRawValue() {
		return rawValue;
	}

	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
	}

	@Override
	public String toString() {
		return rawValue;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}
