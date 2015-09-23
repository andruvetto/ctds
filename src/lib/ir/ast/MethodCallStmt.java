package ir.ast;

import ir.ASTVisitor;
import java.util.List;

public class MethodCallStmt extends Statement {
    private List<Expression> expressions;
    private VarLocation location;
    
    public MethodCallStmt(VarLocation l, List<Expression> e){
        expressions = e;
        location = l;
    }
    
    public MethodCallStmt(VarLocation l){
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
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }

}