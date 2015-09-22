package ir.ast;

import ir.ASTVisitor;

public class ArrayLocation extends Location {
	private int blockId;
        private Expression expression;

	public ArrayLocation(String id, Expression e) {
		this.id = id;
                expression = e;
		this.blockId = -1;
	}
	
	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	
	@Override
	public String toString() {
		return id;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}