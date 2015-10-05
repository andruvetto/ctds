package lib.ir.semcheck;
import lib.ir.ast.*;
import lib.error.Error;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CheckSemVisitor extends Visitor<Type> {
    private TableSymbol table;
    
    public CheckSemVisitor(){
        super();
    }

    @Override
    public Type visit(AssignStmt stmt) {
        Location l = stmt.getLocation();
        Expression e = stmt.getExpression();
        AssignOpType o = stmt.getOperator();
        this.visit(e);
        this.visit(l);
        
        
      //  l.setType(table.typeDeclarated(l));
      //  if (l.getType() == null){
      //      addError(l, "Error variable '" + l + "' not declarated");
      //      stmt.setType(Type.UNDEFINED);
      //      return Type.UNDEFINED;
      //  }
        
        
       // e.setType(table.typeDeclarated(e));
       // if (e.getType() == null) e.setType(this.visit(e));
       // if (e.getType() == null){
       //     addError(e, "Error in expression '" + e +"'");
        //    stmt.setType(Type.UNDEFINED);
        //    return Type.UNDEFINED;
       // }
        if (e.getType() != null && l.getType() != null){
            if (l.getType().equals(e.getType())){
                stmt.setType(l.getType());
                if ((o.equals(AssignOpType.ASSMNT_INC) || o.equals(AssignOpType.ASSMNT_DEC)) && !(l.getType().equals(Type.FLOAT) || l.getType().equals(Type.INT))){
                 addError(stmt , "Error assignement type");
                }
                return stmt.getType();
            }
            else{
                addError(stmt , "Error type " + l.getType() + " != " + e.getType());
                stmt.setType(Type.UNDEFINED);
                return Type.UNDEFINED; 
            } 
        }
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(ReturnStmt stmt) {
        if (stmt.getExpression() == null){
            stmt.setType(Type.VOID);
        }
        else{
            this.visit(stmt.getExpression());
            stmt.setType(stmt.getExpression().getType());
        }
        MethodDecl lastMethod = table.getLastMethodDecl();
        if (!(lastMethod.getType().equals(stmt.getType()))){
             addError(stmt,"Error The type of return does not match with the Method Declaration");
        }
        return stmt.getType();
    }

    @Override
    public Type visit(IfStmt stmt) {
        this.visit(stmt.getIfStatement());
        this.visit(stmt.getCondition());
        if (stmt.getElseStatement() != null) this.visit(stmt.getElseStatement());
        if (!stmt.getCondition().getType().equals(Type.BOOLEAN)){
            addError(stmt.getCondition(), "Error type if condition: " + stmt.getCondition().getType());
        } 
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
    }

    @Override
    public Type visit(ForStmt stmt) {
        table.newBlock();
        try {
            table.insert(stmt.getAssign().getLocation());
            this.visit(stmt.getAssign());
            this.visit(stmt.getCondition());
            if(!(stmt.getAssign().getType().equals(Type.INT) && stmt.getCondition().getType().equals(Type.INT))){
                addError(stmt, "Error type of 'for' expression " );
            }
        } catch (Exception ex) {
            //Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
            addError(stmt.getAssign().getLocation(), "Error variable not declarated '" + stmt.getAssign().getLocation().getId() + "'");
        }
        this.visit(stmt.getForStatement());
        table.pop();
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
    }

    @Override
    public Type visit(WhileStmt stmt) {
        this.visit(stmt.getWhileStatement());
        this.visit(stmt.getCondition());
        if (!stmt.getCondition().getType().equals(Type.BOOLEAN)){
            addError(stmt.getCondition(), "Error type while condition: " + stmt.getCondition().getType());
        }
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
    }

    @Override
    public Type visit(BreakStmt stmt) {
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(ContinueStmt stmt) {
        return Type.UNDEFINED;
    }

    @Override
    public Type visit(BinOpExpr expr) {
        Expression left = expr.getLeftOperand();
        Expression right = expr.getRightOperand();
        BinOpType operator = expr.getOperator();
        this.visit(left);
        this.visit(right);
        
        
        //left.setType(table.typeDeclarated(left));
        //if (left.getType() == null) left.setType(this.visit(left));
        //if (left.getType() == null) System.out.println("ERROR EXPRESSION left");
        
       // right.setType(table.typeDeclarated(right));
       // if (right.getType() == null) right.setType(this.visit(right));
       // if (right.getType() == null) System.out.println("ERROR EXPRESSION right");
        

        if (!(left.getType().equals(right.getType()))){
            addError(expr, "ERROR Types expressions of binOpexpr");
            return Type.UNDEFINED;
        }
        if (left.getType().equals(Type.INT) || left.getType().equals(Type.FLOAT)){
            if (operator.equals(BinOpType.AND) || (operator.equals(BinOpType.OR))){
                addError(expr, "ERROR Type operator of binOpexpr");
                return Type.UNDEFINED;
            }
            if (operator.equals(BinOpType.EQ) || operator.equals(BinOpType.GTR) || operator.equals(BinOpType.GTR_EQ) || operator.equals(BinOpType.LESS) || operator.equals(BinOpType.LESS_EQ)){
                expr.setType(Type.BOOLEAN);
                return expr.getType();
            }
        }
      
        if (left.getType().equals(Type.BOOLEAN)) {
            if (!(operator.equals(BinOpType.AND) || operator.equals(BinOpType.OR) || operator.equals(BinOpType.EQ) || operator.equals(BinOpType.NOT_EQ))){
                
                addError(expr, "ERROR Type operator of binOpexpr");
                return Type.UNDEFINED;
            }
        }
        
        if (left.getType().equals(Type.VOID) || left.getType().equals(Type.UNDEFINED)){
            addError(left, "Error in expr");
            return Type.UNDEFINED;
        }
        
        expr.setType(left.getType());
        return expr.getType();
    }

    @Override
    public Type visit(UnOpExpr expr) {
        Expression e = expr.getExpression();
        this.visit(e);

        //e.setType(table.typeDeclarated(e));
        //if (e.getType() == null) e.setType(this.visit(e));
        //if (e.getType() == null) System.out.println("ERROR EXPRESSION -----------------");
        if (e.getType() != null){
            if (expr.getOperator().equals(UnOpType.LOGIC_NEGATION) && e.getType().equals(Type.BOOLEAN)){
                expr.setType(e.getType());
                return expr.getType();
            }
            if ( expr.getOperator().equals(UnOpType.MINUS) && e.getType().equals(Type.INT) || e.getType().equals(Type.FLOAT)){
             expr.setType(e.getType());
             return expr.getType();
            }    
        }
        addError(expr, "Error in unopexpr");
        expr.setType(Type.UNDEFINED);
        return expr.getType();
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
        lit.setType(Type.BOOLEAN);
        return lit.getType();                
    }

    @Override
    public Type visit(MethodCall m) {   
        if (table.declarated(m.getLocation())){
            MethodDecl methoddec = (MethodDecl) table.getDeclarated(m.getLocation());
             //Comprobation parameters
            List<Parameter> parameters = methoddec.getParameters();
            List<Expression> expressions = m.getExpressions();
            if (parameters.size() == expressions.size()){
                for (int i = 0; i<parameters.size(); i++){
                    try {
                        //this.visit(parameters.get(i));
                        expressions.get(i).setType(table.typeDeclarated(expressions.get(i)));
                    } catch (Exception ex) {
                       // Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (expressions.get(i).getType() == null) expressions.get(i).setType(this.visit(expressions.get(i)));
                    if (expressions.get(i).getType() == null) addError(expressions.get(i), "Error in expression");
                    if (!parameters.get(i).getType().equals(expressions.get(i).getType())){
                        addError(m, "parameter type incorrect" );
                    }
                }
            }
            else{
                addError(m, "number of parameters is incorrect" );
            }
            m.setType(methoddec.getType());
            if (m.getType().equals(Type.VOID)){
                addError(m, "Error the method must return a result" );
            }
            return m.getType();
        }
        else{
            addError(m, "Method not declarated '" + m.getId() + "'");
            return Type.UNDEFINED;
        }
    }

    @Override
    public Type visit(MethodCallStmt m) {
         if (table.declarated(m.getMethod().getLocation())){
            MethodDecl methoddec = (MethodDecl) table.getDeclarated(m.getMethod().getLocation());
             //Comprobation parameters
            List<Parameter> parameters = methoddec.getParameters();
            List<Expression> expressions = m.getMethod().getExpressions();
            if (parameters.size() == expressions.size()){
                for (int i = 0; i<parameters.size(); i++){
                    try {
                        expressions.get(i).setType(table.typeDeclarated(expressions.get(i)));
                    } catch (Exception ex) {
                        //Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (expressions.get(i).getType() == null) expressions.get(i).setType(this.visit(expressions.get(i)));
                    if (expressions.get(i).getType() == null) addError(expressions.get(i), "Error in expression");
                    if (!parameters.get(i).getType().equals(expressions.get(i).getType())){
                        addError(m, "parameter type incorrect" );
                    }
                }
            }
            else{
                addError(m, "number of parameters is incorrect" );
            }
            m.setType(methoddec.getType());
            return m.getType();
        }
        else{
            addError(m, "Method not declarated '" + m.getId() + "'");
            return Type.UNDEFINED;
        }
        
    }

    @Override
    public Type visit(VarLocation loc) {
        if (loc.getType() == null){
            try {
                loc.setType(table.typeDeclarated(loc));
            } catch (Exception ex) {
                //Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
                addError(loc, "Error variable not declarated '" + loc.getId() + "'");
            }
        }
        return loc.getType();
    }

    @Override
    public Type visit(ArrayLocation loc) {
        if (loc.getType() == null){
            try {
                loc.setType(table.typeDeclarated(loc));
            } catch (Exception ex) {
                //Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
                addError(loc, "Error variable not declarated '" + loc.getId() + "'");
            }
        }
        this.visit(loc.getExpression());
        if (!loc.getExpression().getType().equals(Type.INT)){
            addError(loc, "Type of array expression " + loc.getExpression().getType()  + " != int " + loc);
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
            l.setType(this.visit(l));
            try {
                table.insert(l);
            } catch (Exception ex) {
                addError(l, "Error variable duplicated '" + l.getId() + "'");
            }
        }
        return fd.getType();
    }

    @Override
    public Type visit(Parameter p) {
        return p.getType();
    }

    @Override
    public Type visit(MethodDecl m) {
        table.newBlock();
        if ( m.getId().equals("main") && !m.getParameters().isEmpty()){
            addError(m, "Error: main method contains parameters");
        }        
        for(Parameter p : m.getParameters()){
            try {
                table.insert(p);
            } catch (Exception ex) {
                addError(p, "Error variable duplicated '" + p.getId() + "'"); 
            }
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
                //Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
                addError(m, "Error method duplicated '" + m.getId() + "'"); 
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
                //Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
                addError(c, "Error Class duplicated '" + c.getId() + "'"); 
            }
            this.visit(c);
        }
        table.pop();
        return Type.UNDEFINED;
    } 
}
