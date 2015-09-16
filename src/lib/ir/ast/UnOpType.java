package ir.ast;

public enum UnOpType {
	MINUS,
    LOGIC_NEGATION;
	
	@Override
	public String toString() {
		switch(this) {
			
			case MINUS:
				return "-";
                        case LOGIC_NEGATION:
                                return "!";
		}
		
		return null;
	}
}
