package ir.ast;

import ir.ASTVisitor;
import java.util.List;


public class Program extends AST{
    private List<ClassDecl> classes;
    
    public Program(List<ClassDecl> clist){
        this.classes = clist;        
    }
    
    public List<ClassDecl> getClasses(){
        return classes;
    }
    
    @Override
    public String toString() {
        String res = "";
        for (ClassDecl c: classes){
            res = res + c.toString();
        }
        return res;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
