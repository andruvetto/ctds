package lib.ir.icode;

import lib.ir.ast.*;

public class Label extends Expression{
    public Label (String id){
        this.id = id;
    }
    
    @Override
    public String toString() {
        return this.id;
    }
}
