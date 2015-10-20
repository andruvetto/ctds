package lib.ir.ast;

import lib.ir.ASTVisitor;
import java.util.List;


public class MethodDecl extends AST {

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


    
    public List<Parameter> getParameters(){
        return this.parameters;
    }
    
    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
