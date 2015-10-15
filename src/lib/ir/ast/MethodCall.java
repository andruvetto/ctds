package lib.ir.ast;

import lib.ir.ASTVisitor;

public class MethodCall extends Expression {
    private MethodCallStmt method;
    
    public MethodCall(MethodCallStmt m){
        this.method = m;
    }
    
    public MethodCallStmt getMethod(){
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