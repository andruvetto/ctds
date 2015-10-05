package lib.ir.ast;

public enum AssignOpType {
	ASSMNT_INC,
	ASSMNT_DEC,
	ASSMNT;
	
	@Override
	public String toString() {
		switch(this) {
			case ASSMNT_INC:
				return "+=";
			case ASSMNT_DEC:
				return "-=";
			case ASSMNT:
				return "=";
		}

		return null;		
	}
}
