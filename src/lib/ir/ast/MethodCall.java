package ir.ast;

import ir.ASTVisitor;
import java.util.List;

public class MethodCall extends Expression {
    private List<Expression> expressions;
    private VarLocation location;
    
    public MethodCall(VarLocation l, List<Expression> e){
        expressions = e;
        location = l;
    }
    
    public MethodCall(VarLocation l){
        expressions = null;
        location = l;
    }
    
    public List<Expression> getExpressions(){
        return expressions;
    }
    
    public void setExpressions(List<Expression> e){
        this.expressions = e;
    }
    
    public VarLocation getLocation(){
        return location;
    }
    
    public void setLocation(VarLocation l){
        this.location = l;
    }
    
    @Override
    public String toString() {
       String exprs = "";
       for (Expression e: expressions){
        exprs = exprs + e.toString() + ",";
       }
       return location.toString() + "(" + exprs + ")" ;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }

}
