package lib.ir.ast;

import lib.ir.ASTVisitor;

public class VarLocation extends Location {
	private int blockId;

	public VarLocation(String id) {
		this.id = id;
		this.blockId = -1;
                this.isArray = false;
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
