package lib.ir.semcheck;
import lib.error.Error;
import lib.*;
import lib.ir.ast.*;
import java.util.LinkedList;
import java.util.List;


public class TableSymbol {
    private List<Error> errors;
    private LinkedList<LinkedList<AST>> stack;
    
    protected void addError(AST a, String desc) {
	errors.add(new Error(a.getLineNumber(), a.getColumnNumber(), desc));
    }

    public List<Error> getErrors() {
    	return errors;
    }

    public void setErrors(List<Error> errors) {
    	this.errors = errors;
    }
    
    public TableSymbol(){
        stack = new LinkedList();
        errors = new LinkedList();
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
    
    public Type typeDeclarated(AST ast) throws Exception {
            for(LinkedList<AST> block : stack){
                for (AST a : block){
                    if (a.getId().equals(ast.getId()) && a.getClass().getSimpleName().equals(ast.getClass().getSimpleName()) ) {
                        return a.getType();
                    }
                }
            }
          
            throw new Exception("Error variable not declarated " + ast.getId());
           
    }
    
    public MethodDecl getLastMethodDecl(){
        for(LinkedList<AST> block : stack){
                for (AST a : block){
                    if (a.getClass().getSimpleName().equals("MethodDecl")) {
                        return (MethodDecl)a;
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
