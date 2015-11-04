package lib.ir.ast;

import lib.ir.ASTVisitor;

public abstract class Location extends Expression {
    protected boolean isArray;
    protected Location declarated;
    protected boolean isGlobal;
    protected int offset;
    
    public void setIsGlobal(boolean g){
        isGlobal = g;
    }
    
    public boolean isGlobal(){
        return this.isGlobal;
    }
    
    public int getOffset(){
        return this.offset;
    }
    
    public void setOffset(int offset){
        this.offset = offset;
    }
    
    public Location getDeclarated(){
        return declarated;
    }
    
    public void setDeclarated(Location l){
        this.declarated = l;
    }
    
    public boolean isArray(){
        return isArray;
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
