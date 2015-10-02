package ir.ast;

import ir.ASTVisitor;

public class MethodCallStmt extends Statement {
    private MethodCall method;
    
    public MethodCallStmt(MethodCall m){
        this.method = m;
    }
    
    public MethodCall getMethod(){
        return this.method;
    }
    
    @Override
    public String toString() {
       return method.toString();
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }

}