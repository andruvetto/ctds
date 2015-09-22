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
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }

}
