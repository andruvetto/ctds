package ir.semcheck;
import ir.ASTVisitor;
import ir.ast.*;
import java.util.List;


public class CheckCycleSentencesVisitor extends Visitor <Boolean> {

    @Override
    public Boolean visit(AssignStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(ReturnStmt stmt) {
       return true;
    }

    @Override
    public Boolean visit(IfStmt stmt) {
        boolean ifstatement, elsestatement;
        ifstatement = this.visit(stmt.getIfStatement());
        if (stmt.getElseStatement()!=null){
            elsestatement = this.visit(stmt.getElseStatement());
            return ifstatement && elsestatement;
        }
        return ifstatement;
        
    }

    @Override
    public Boolean visit(ForStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(WhileStmt stmt) {
        return true;
    }

    @Override
    public Boolean visit(BreakStmt stmt) {
        return false;
    }

    @Override
    public Boolean visit(ContinueStmt stmt) {
        return false;
    }

    @Override
    public Boolean visit(BinOpExpr expr) {
        return true;
    }

    @Override
    public Boolean visit(UnOpExpr expr) {
        return true;
    }

    @Override
    public Boolean visit(IntLiteral lit) {
        return true;
    }

    @Override
    public Boolean visit(FloatLiteral lit) {
        return true;
    }

    @Override
    public Boolean visit(BooleanLiteral lit) {
        return true;
    }

    @Override
    public Boolean visit(MethodCall m) {
        return true;
    }

    @Override
    public Boolean visit(MethodCallStmt m) {
        return true;
    }

    @Override
    public Boolean visit(VarLocation loc) {
        return true;
    }

    @Override
    public Boolean visit(ArrayLocation loc) {
        return true;
    }

    @Override
    public Boolean visit(Block block) {
        boolean res = true;
        for(Statement s : block.getStatements()){
            res = res && this.visit(s);
        }
        return res;
    }

    @Override
    public Boolean visit(FieldDecl fd) {
        return true;
    }

    @Override
    public Boolean visit(Parameter p) {
        return true;
    }

    @Override
    public Boolean visit(MethodDecl m) {
        boolean res = true;
        if (!m.ifExtern()) {
            res = this.visit(m.getBlock());
        }
        return res;
    }

    @Override
    public Boolean visit(ClassDecl c) {
        boolean res = true;
        for (MethodDecl m : c.getMethods()){
            res = res && this.visit(m);
        }
        return res;
    }

    @Override
    public Boolean visit(Program p) {
        boolean res = true;
        for (ClassDecl c: p.getClasses()){
            res = res && this.visit(c);
        }
        return res;
    }
}