package lib.ir.ast;

import lib.ir.ASTVisitor;

public class ArrayLocation extends Location {
	private int blockId;
        private Expression expression;

	public ArrayLocation(String id, Expression e) {
		this.id = id;
                expression = e;
		this.blockId = -1;
                this.isArray = true;
	}
	
	public int getBlockId() {
		return blockId;
	}
        
        public Expression getExpression(){
            return this.expression;
        }

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	
	@Override
	public String toString() {
		return id+"["+expression+"]";
	}
        

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}