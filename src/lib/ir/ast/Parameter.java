package lib.ir.ast;

import lib.ir.ASTVisitor;

public class Parameter extends AST{
    private VarLocation var;
    public Parameter(Type t, String i){
        this.type = t;
        this.id = i;
        var = new VarLocation(id);
        var.setType(t);
    }
    
    public VarLocation getVarLocation(){
        return this.var;
    }
    

    @Override
    public String toString() {
       return type.toString() + " " + id;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
       return v.visit(this);
    }
    
}
