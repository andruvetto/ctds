package ir.ast;

import ir.ASTVisitor;

public abstract class Statement extends AST {
    protected Statement expr;
	protected TypeStmt type;
        
    public TypeStmt getType() {
		return this.type;
	}
	
	public void setType(TypeStmt t) {
		this.type = t;
	}
	
	@Override
	public <T> T accept(ASTVisitor<T> v) {
            return v.visit(this);
	}
}
