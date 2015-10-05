package lib.ir.ast;

public enum Type {
	INT,
        FLOAT,
        BOOLEAN,
	INTARRAY,
	VOID,
	UNDEFINED;
	
	@Override
	public String toString() {
		switch(this) {
			case INT:
				return "int";
                        case BOOLEAN:
				return "boolean";
                        case FLOAT:
				return "float";
			case VOID:
				return "void";
			case UNDEFINED:
				return "undefined";
			case INTARRAY:
				return "int[]";
		}
		
		return null;
	}
	
	public boolean isArray() {
		if (this == Type.INTARRAY) {
			return true;
		}
		
		return false;
	}
}
