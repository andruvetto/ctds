package ir.ast;

import ir.ASTVisitor;
import java.util.List;


public class MethodDecl extends AST {
    private Type type;
    private String id;
    private List<Parameter> parameters;
    private Block block;
    private boolean extern;
    
    public MethodDecl(Type t, String id, List<Parameter> plist, Block b){
        this.type = t;
        this.id = id;
        this.parameters = plist;
        this.block = b;
        extern = false;        
    }
    
    public MethodDecl(Type t, String id, List<Parameter> plist){
        this.type = t;
        this.id = id;
        this.parameters = plist;
        extern = true;        
    }
    
    public boolean ifExtern(){
        return this.extern;
    }
    
    public Block getBlock(){
        return this.block;
    }

    public String getId(){
        return this.id;
    }
    
    public List<Parameter> getParameters(){
        return this.parameters;
    }
    
    @Override
    public String toString() {
       
       String params = "";
       for (Parameter p: parameters){
        params = params + p.toString() + ",";
       }
       if (params.length() > 0) params = params.substring(0, params.length() - 1); // remove last ,
       if (extern){
          return type.toString() + " " + id + "(" + params + ") extern";
       }
       else{
          return type.toString() + " " + id + "(" + params + ")" + block.toString();
       }
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
