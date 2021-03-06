package lib.ir.ast;

import lib.ir.ASTVisitor;
import java.util.List;


public class FieldDecl extends Statement{
    private List<Location> idList;
    
    public FieldDecl(Type t, List<Location> l){
        this.type = t;
        this.idList = l;
        for(Location loc : idList){
            loc.setType(t);
        }
    }

    public List<Location> getLocations(){
        return this.idList;
    }
    
     @Override
    public String toString() {
       String ids = "";
       for (Location l: idList){
        ids = ids + l.toString() + ",";
       }
       if (ids.length() > 0) ids = ids.substring(0, ids.length() - 1); // remove last ,
       return type.toString() + " " +  ids ;
    }
    
    @Override
    public <T> T accept(ASTVisitor<T> v) {
        return v.visit(this);
    }
    
}
