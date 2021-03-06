package lib.ir.ast;

import lib.ir.ASTVisitor;
import java.util.LinkedList;
import java.util.List;

public class MethodCallStmt extends Statement {
    private List<Expression> expressions;
    private VarLocation location;
    private MethodDecl methodDecl;
    
    public MethodDecl getMethodDecl(){
        return this.methodDecl;
    }
    
    public void setMethodDecl(MethodDecl m){
        this.methodDecl = m;
    }
    
    public MethodCallStmt(VarLocation l, List<Expression> e){
        expressions = e;
        location = l;
    }
    
    public MethodCallStmt(VarLocation l){
        expressions = new LinkedList();
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
       if (exprs.length() > 0) exprs = exprs.substring(0, exprs.length() - 1); // remove last ,
       return location.toString() + "(" + exprs + ")" ;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }

}
