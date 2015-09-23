package ir.ast;

import ir.ASTVisitor;
import java.util.List;


public class FieldDecl extends Statement{
    
    private Type type;
    private List<Location> idList;
    
    public FieldDecl(Type t, List<Location> l){
        this.type = t;
        this.idList = l;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
