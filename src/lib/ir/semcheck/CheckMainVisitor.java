package lib.ir.semcheck;
import lib.ir.Visitor;
import lib.ir.ast.*;
import java.util.List;

/**
 * This class represents a visitor that check the amount of main methods.
 * 
 */

public class CheckMainVisitor extends Visitor<Integer> {

    @Override
    public Integer visit(MethodDecl m) {
        int res = 0;
        if (m.getId().equals("main")) res = 1;
        return res;
    }

    @Override
    public Integer visit(ClassDecl c) {
        
        int res = 0;
        if (c.getId().equals("main")){
            for (MethodDecl m: c.getMethods()){
                res+= this.visit(m);
            }
        }
        return res;
    }

    @Override
    public Integer visit(Program p) {
        List<ClassDecl> classes = p.getClasses();
        int res = 0;
        for (ClassDecl c: classes){
            res+= this.visit(c);
        }
        if (res == 0){
            addError(p, "Error not declarated class \"main\" with method \"main\"");
        }
        return res;
    }
    
}
