package ir.ast;

import ir.ASTVisitor;
import java.util.LinkedList;
import java.util.List;

public class ClassDecl extends AST {
    private List<FieldDecl> fields;
    private List<MethodDecl> methods;
    
    public ClassDecl(){
        fields = new LinkedList<FieldDecl>();
        methods = new LinkedList<MethodDecl>();        
    }
    
    public ClassDecl(List<FieldDecl> flist, List<MethodDecl> mlist){
        this.fields = flist;
        this.methods = mlist;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
    
}
