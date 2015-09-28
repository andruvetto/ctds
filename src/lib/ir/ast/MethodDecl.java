package ir.ast;

import ir.ASTVisitor;
import java.util.List;


public class MethodDecl extends AST {
    private Type type;
    private String id;
    private List<Parameter> parameters;
    private Statement body;
    
    public MethodDecl(Type t, String id, List<Parameter> plist, Statement b){
        this.type = t;
        this.id = id;
        this.parameters = plist;
        this.body = b;
        
    }

    @Override
    public String toString() {
       String params = ""
       for (Parameter p: parameters){
        params = params + p.toString() + ",";
       }
       return type.toString() + " " + id + "(" + params + ")" ;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
