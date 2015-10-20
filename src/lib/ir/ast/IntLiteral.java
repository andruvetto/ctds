package lib.ir.ast;

import lib.ir.ASTVisitor;

public class IntLiteral extends Literal {
	private String rawValue;
	
	public IntLiteral(String val){
            rawValue = val; 
            value = Integer.valueOf(rawValue);
	}
        
        public IntLiteral(int val){
            rawValue = String.valueOf(val);
            value = val;
	}

	@Override
	public Type getType() {
            return Type.INT;
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
