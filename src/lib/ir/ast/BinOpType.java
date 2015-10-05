package lib.ir.ast;

public enum BinOpType {
	PLUS, // Arithmetic
	MINUS,
	TIMES,
	DIVIDE,
	MOD,
	LESS, // Relational
	LESS_EQ,
	GTR,
	GTR_EQ,
	NOT_EQ, // Equal
	EQ, 
	AND, // Conditional
	OR;
	
	@Override
	public String toString() {
		switch(this) {
			case PLUS:
				return "+";
			case MINUS:
				return "-";
			case TIMES:
				return "*";
			case DIVIDE:
				return "/";
			case MOD:
				return "%";
			case LESS:
				return "<";
			case LESS_EQ:
				return "<=";
			case GTR:
				return ">";
			case GTR_EQ:
				return ">=";
			case EQ:
				return "==";
			case NOT_EQ:
				return "!=";
			case AND:
				return "&&";
			case OR:
				return "||";
		}
		
		return null;
	}
}
