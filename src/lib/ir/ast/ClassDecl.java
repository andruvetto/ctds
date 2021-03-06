package lib.ir.ast;

import lib.ir.ASTVisitor;
import java.util.LinkedList;
import java.util.List;

public class ClassDecl extends AST {
    private List<FieldDecl> fields;
    private List<MethodDecl> methods;

    
    public ClassDecl(String id){
        fields = new LinkedList<FieldDecl>();
        methods = new LinkedList<MethodDecl>();
        this.id = id;
    }
    
    public ClassDecl(List<FieldDecl> flist, List<MethodDecl> mlist){
        this.fields = flist;
        this.methods = mlist;
        id = null;
    }
    
    public List<MethodDecl> getMethods(){
        return this.methods;
    }
    
    public List<FieldDecl> getFields(){
        return this.fields;
    }

    
    @Override
    public String toString() {
       String res = "Class "+ id + "\n"; 
       for (FieldDecl f: fields){
           res = res + f.toString() + "\n";           
       }
       for (MethodDecl m: methods){
           res = res + m.toString() + "\n";
       }
       if (res.length() > 0) res = res.substring(0, res.length() - 1); // remove last new line
       return res;
    }
    
 
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
    
}
