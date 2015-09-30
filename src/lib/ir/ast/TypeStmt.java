package ir.ast;

public enum TypeStmt {
	ASSIGN,
        METHODCALL,
        IF,
	FOR,
        WHILE,
        RETURN,
        BREAK,
        CONTINUE,
        BLOCK,
	UNDEFINED;
	
        /*
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
	*/
	
}
