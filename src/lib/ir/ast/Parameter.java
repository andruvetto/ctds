package ir.ast;

import ir.ASTVisitor;

public class Parameter extends AST{
    
    private Type type;
    private String id;
   
    public Parameter(Type t, String i){
        this.type = t;
        this.id = i;
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
