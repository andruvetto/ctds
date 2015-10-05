package ir.semcheck;
import error.Error;
import ir.ast.*;
import java.util.LinkedList;


public class TableSymbol {
    private LinkedList<Error> errors;
    private LinkedList<LinkedList<AST>> stack;
    
    public TableSymbol(){
        stack = new LinkedList();
    }
    
    public LinkedList<AST> pop(){
        return stack.pop();
    }
    
    public void push(LinkedList<AST> astlist){
        stack.push(astlist);
    }
    
    public void newBlock(){
        stack.push(new LinkedList<AST>());
    }
    
    public void insert(AST ast) throws Exception{
        for (AST s : stack.element()){
            if (s.getId().equals(ast.getId())) {
                throw new Exception("Error to insert ast duplicated " + ast.getId());
            }
        }    
        stack.element().push(ast);
    }
    
    public boolean declarated(AST ast){
        for(LinkedList<AST> block : stack){
            for (AST a : block){
                if (a.getId().equals(ast.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public AST getDeclarated(AST ast){
            for(LinkedList<AST> block : stack){
                for (AST a : block){
                    if (a.getId().equals(ast.getId())) {
                        return a;
                    }
                }
            }
            return null;
    }
    
    public Type typeDeclarated(AST ast){
            for(LinkedList<AST> block : stack){
                for (AST a : block){
                    if (a.getId().equals(ast.getId())) {
                        return a.getType();
                    }
                }
            }
            return null;
    }
    
    @Override
    public String toString(){
        String res = "";
         for(LinkedList<AST> block : stack){
            res += block + "\n";
        }
        return res;
    }
    
}
