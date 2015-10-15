package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class Location extends Expression {
    protected boolean isArray;
    protected Location declarated;
    
    public Location getDeclarated(){
        return declarated;
    }
    
    public void setDeclarated(Location l){
        this.declarated = l;
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
