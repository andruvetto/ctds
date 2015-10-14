package lib.ir.semcheck;
import lib.ir.Visitor;
import lib.ir.ast.*;
import java.util.List;
import lib.error.Error;

public class CheckCycleSentencesVisitor extends Visitor <List<Error>> {

    public CheckCycleSentencesVisitor(){
        super();
    }
    
    @Override
    public List<Error> visit(AssignStmt stmt) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(ReturnStmt stmt) {
       return this.getErrors();
    }

    @Override
    public List<Error> visit(IfStmt stmt) {
       
        this.visit(stmt.getIfStatement());
        if (stmt.getElseStatement()!=null){
            this.visit(stmt.getElseStatement());
        }
        return this.getErrors();
        
    }

    @Override
    public List<Error> visit(ForStmt stmt) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(WhileStmt stmt) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(BreakStmt stmt) {
        addError(stmt, "Error statement ''break'' is not in the body of a cycle");
        return this.getErrors();
    }

    @Override
    public List<Error> visit(ContinueStmt stmt) {
        addError(stmt, "Error statement ''continue'' is not in the body of a cycle");
        return this.getErrors();
    }

    @Override
    public List<Error> visit(BinOpExpr expr) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(UnOpExpr expr) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(IntLiteral lit) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(FloatLiteral lit) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(BooleanLiteral lit) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(MethodCall m) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(MethodCallStmt m) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(VarLocation loc) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(ArrayLocation loc) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(Block block) {
        for(Statement s : block.getStatements()){
            this.visit(s);
        }
        return this.getErrors();
    }

    @Override
    public List<Error> visit(FieldDecl fd) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(Parameter p) {
        return this.getErrors();
    }

    @Override
    public List<Error> visit(MethodDecl m) {
        if (!m.ifExtern()) {
            this.visit(m.getBlock());
        }
        return this.getErrors();
    }

    @Override
    public List<Error> visit(ClassDecl c) {
        for (MethodDecl m : c.getMethods()){
            this.visit(m);
        }
        return this.getErrors();
    }

    @Override
    public List<Error> visit(Program p) {
        for (ClassDecl c: p.getClasses()){
            this.visit(c);
        }
       return this.getErrors();
    }
}