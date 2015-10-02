package ir.semcheck;
import ir.ASTVisitor;
import ir.ast.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CheckSemVisitor extends Visitor<Type>{
    private TableSymbol table;



    @Override
    public Type visit(AssignStmt stmt) {
        Location l = stmt.getLocation();
        Expression e = stmt.getExpression();
        AssignOpType o = stmt.getOperator();
        
        e.setType(this.visit(e));
        
        if (table.declarated(l)){
            AST ldeclarated = table.getDeclarated(l);
            l.setType(ldeclarated.getType());
        }
        else System.out.println("ERROR VARIABLE NOT DECLARATED");
        
        if (l.getType().equals(e.getType()))
            return l.getType();
        else System.out.println("ERROR TYPES IN ASSIGNATION");
        
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(ReturnStmt stmt) {
        if (stmt.getExpression() == null){
            stmt.setType(Type.VOID);
        }
        else stmt.setType(this.visit(stmt.getExpression()));
        return stmt.getType();
    }

    @Override
    public Type visit(IfStmt stmt) {
        this.visit(stmt.getIfStatement());
        if (stmt.getElseStatement() != null) this.visit(stmt.getElseStatement());
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
    }

    @Override
    public Type visit(ForStmt stmt) {
        this.visit(stmt.getForStatement());
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
        //TODO comprobations
    }

    @Override
    public Type visit(WhileStmt stmt) {
        this.visit(stmt.getWhileStatement());
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
        //TODO COMPROBATE CONDITION
    }

    @Override
    public Type visit(BreakStmt stmt) {
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(ContinueStmt stmt) {
        //stmt.setId("continue");
        //table.insert(stmt);
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(BinOpExpr expr) {
        Expression left = expr.getLeftOperand();
        Expression right = expr.getRightOperand();
        left.setType(this.visit(left));
        right.setType(this.visit(right));
        //TODO implements comprobations
        return left.getType();
        
        
    }

    @Override
    public Type visit(UnOpExpr expr) {
        expr.setType(this.visit(expr));
        return expr.getType();
        //TODO implemetns comprobations types and operand;
    }


    @Override
    public Type visit(IntLiteral lit) {
        Integer value = Integer.valueOf(lit.getRawValue());
        lit.setValue(value);
        lit.setType(Type.INT);
        return lit.getType();      
    }

    @Override
    public Type visit(FloatLiteral lit) {
        Float value = Float.valueOf(lit.getRawValue());
        lit.setValue(value);
        lit.setType(Type.FLOAT);
        return lit.getType();               
    }

    @Override
    public Type visit(BooleanLiteral lit) {
        switch (lit.getRawValue()){
            case "true":
                lit.setValue(true);
            case "false":
                lit.setValue(false);
            default:
                System.out.println("ERROR type boolean");
        }
        lit.setType(Type.BOOLEAN);
        return lit.getType();                
    }

    @Override
    public Type visit(MethodCall m) {
        //TODO COMPROBATE EXPRESSIONS
        if (table.declarated(m.getLocation())){
            m.setType(m.getLocation().getType());
            return m.getType();
        }
        else{
            System.out.println("ERROR Method not declarated");
            return Type.UNDEFINED;
        }
    }

    @Override
    public Type visit(MethodCallStmt m) {
        m.setType(this.visit(m.getMethod()));
        return m.getType();
    }



    @Override
    public Type visit(VarLocation loc) {
        //TODO SET BLOCK ID
        try {
            table.insert(loc);
        } catch (Exception ex) {
            Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loc.getType();
    }

    @Override
    public Type visit(ArrayLocation loc) {
        //TODO SET BLOCK ID
        try {
            table.insert(loc);
        } catch (Exception ex) {
            Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loc.getType();
    }

    @Override
    public Type visit(Block block) {
        for (FieldDecl f : block.getFields()){
            this.visit(f);
        }
        for (Statement s : block.getStatements()){
            this.visit(s);
        }
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(FieldDecl fd) {
        for(Location l : fd.getLocations()){
            this.visit(l);
        }
        return fd.getType();
    }

    @Override
    public Type visit(Parameter p) {
        try {
            table.insert(p);
        } catch (Exception ex) {
            Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p.getType();
    }

    @Override
    public Type visit(MethodDecl m) {
        table.newBlock();
        for(Parameter p : m.getParameters()){
            this.visit(p);
        }
        if (!m.ifExtern()) this.visit(m.getBlock());
        table.pop();
        return m.getType();
    }

    @Override
    public Type visit(ClassDecl c) {
        table.newBlock();
        for(FieldDecl f : c.getFields()){
            this.visit(f);
        }
        for(MethodDecl m : c.getMethods()){
            try {
                table.insert(m);
                this.visit(m);
            } catch (Exception ex) {
                Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        table.pop();
        c.setType(Type.UNDEFINED);
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(Program p) {
        table = new TableSymbol();
        table.newBlock();
        for(ClassDecl c : p.getClasses()){
            try {
                table.insert(c);
            } catch (Exception ex) {
                Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.visit(c);
        }
        table.pop();
        return Type.UNDEFINED;
    }


    
}
