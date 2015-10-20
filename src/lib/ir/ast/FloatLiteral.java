package lib.ir.ast;

import lib.ir.ASTVisitor;

public class FloatLiteral extends Literal {
	private String rawValue;

	public FloatLiteral(String val){
		rawValue = val; 
		value = Float.valueOf(val);
	}
        
        public FloatLiteral(Float val){
		rawValue = String.valueOf(val);
		value = val;
	}

	@Override
	public Type getType() {
		return Type.FLOAT;
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