package lib.ir.ast;

public enum BinOpType {
	PLUS, 
	MINUS,
	TIMES,
	DIVIDE,
	MOD,
	LESS, 
	LESS_EQ,
	GTR,
	GTR_EQ,
	NOT_EQ,
	EQ, 
	AND, 
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
