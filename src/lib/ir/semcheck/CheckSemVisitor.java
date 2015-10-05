package ir.semcheck;
import ir.ast.*;
import error.Error;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CheckSemVisitor extends Visitor<Type> {
    private TableSymbol table;
    private List<Error> errors;
    
    public CheckSemVisitor(){
        errors = new LinkedList();
    }
    
    private void addError(AST a, String desc) {
	errors.add(new Error(a.getLineNumber(), a.getColumnNumber(), desc));
    }

    public List<Error> getErrors() {
    	return errors;
    }

    public void setErrors(List<Error> errors) {
    	this.errors = errors;
    }

    @Override
    public Type visit(AssignStmt stmt) {
        Location l = stmt.getLocation();
        Expression e = stmt.getExpression();
        AssignOpType o = stmt.getOperator();
        
       // if (l.getClass().getSimpleName().equals("ArrayLocation")){
       //     System.out.println("ES ARRAY");
        //}
        
        //this.visit(e);
        
        
        
        l.setType(table.typeDeclarated(l));
        if (l.getType() == null){
            addError(l, "Error variable '" + l + "' not declarated");
            stmt.setType(Type.UNDEFINED);
            return Type.UNDEFINED;
        }
        
        
        e.setType(table.typeDeclarated(e));
        if (e.getType() == null) e.setType(this.visit(e));
        if (e.getType() == null){
            addError(e, "Error in expression '" + e +"'");
            stmt.setType(Type.UNDEFINED);
            return Type.UNDEFINED;
        }

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

    @Override
    public Type visit(ReturnStmt stmt) {
        if (stmt.getExpression() == null){
            stmt.setType(Type.VOID);
             return stmt.getType();
        }
        else{
            if (table.declarated(stmt.getExpression())){
                AST expdeclarated = table.getDeclarated(stmt.getExpression());
                stmt.setType(expdeclarated.getType());
                return stmt.getType();
            }
            else{
                System.out.println("Error return");
                return Type.UNDEFINED;
            }
        } 
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
        this.visit(stmt.getForStatement());
        stmt.setType(Type.UNDEFINED);
        return stmt.getType();
        //TODO comprobations
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
        
        
        //this.visit(left);
        //this.visit(right);
        
        
        left.setType(table.typeDeclarated(left));
        if (left.getType() == null) left.setType(this.visit(left));
        if (left.getType() == null) System.out.println("ERROR EXPRESSION left");
        
        right.setType(table.typeDeclarated(right));
        if (right.getType() == null) right.setType(this.visit(right));
        if (right.getType() == null) System.out.println("ERROR EXPRESSION right");
        

        if (!(left.getType().equals(right.getType()))){
            System.out.println("ERROR Types expressions of binOpexpr");
            return Type.UNDEFINED;
        }
        if (left.getType().equals(Type.INT) || left.getType().equals(Type.FLOAT)){
            if (operator.equals(BinOpType.AND) || (operator.equals(BinOpType.OR))){
                System.out.println("ERROR Type operator of binOpexpr");
                return Type.UNDEFINED;
            }
            if (operator.equals(BinOpType.EQ) || operator.equals(BinOpType.GTR) || operator.equals(BinOpType.GTR_EQ) || operator.equals(BinOpType.LESS) || operator.equals(BinOpType.LESS_EQ)){
                expr.setType(Type.BOOLEAN);
                return expr.getType();
            }
        }
      
        if (left.getType().equals(Type.BOOLEAN)) {
            if (!(operator.equals(BinOpType.AND) || operator.equals(BinOpType.OR) || operator.equals(BinOpType.EQ) || operator.equals(BinOpType.NOT_EQ))){
                System.out.println("ERROR Type operator of binOpexpr");
                return Type.UNDEFINED;
            }
        }
        
        if (left.getType().equals(Type.VOID) || left.getType().equals(Type.UNDEFINED)){
            System.out.println("ERROR Types expressions of binOpexpr");
            return Type.UNDEFINED;
        }
        
        expr.setType(left.getType());
        return expr.getType();
        
        
    }

    @Override
    public Type visit(UnOpExpr expr) {
        
        Expression e = expr.getExpression();
        
        //this.visit(e);

        e.setType(table.typeDeclarated(e));
        if (e.getType() == null) e.setType(this.visit(e));
        if (e.getType() == null) System.out.println("ERROR EXPRESSION -----------------");
        
        
        if (expr.getOperator().equals(UnOpType.LOGIC_NEGATION) && e.getType().equals(Type.BOOLEAN)){
            expr.setType(e.getType());
            return expr.getType();
        }
        if ( expr.getOperator().equals(UnOpType.MINUS) && e.getType().equals(Type.INT) || e.getType().equals(Type.FLOAT)){
            expr.setType(e.getType());
            return expr.getType();
        }
        System.out.println("ERROR TYPE OF UNOPEXPR");
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
       // switch (lit.getRawValue()){
       //     case "true":
       //         lit.setValue(true);
       //     case "false":
       //         lit.setValue(false);
       //     default:
       //         System.out.println("ERROR type boolean");
       // }
       // System.out.println("------------------------------" + lit.getRawValue());
        lit.setType(Type.BOOLEAN);
        
        return lit.getType();                
    }

    @Override
    public Type visit(MethodCall m) {
        //TODO COMPROBATE EXPRESSIONS
        
        if (table.declarated(m.getLocation())){
            MethodDecl methoddec = (MethodDecl) table.getDeclarated(m.getLocation());

             //Comprobation parameters
            List<Parameter> parameters = methoddec.getParameters();
            List<Expression> expressions = m.getExpressions();
            
            if (parameters.size() == expressions.size()){
                for (int i = 0; i<parameters.size(); i++){
                    //this.visit(parameters.get(i));
                    expressions.get(i).setType(table.typeDeclarated(expressions.get(i)));
                    if (expressions.get(i).getType() == null) expressions.get(i).setType(this.visit(expressions.get(i)));
                    if (expressions.get(i).getType() == null) System.out.println("ERROR EXPRESSION");
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
        return loc.getType();
    }

    @Override
    public Type visit(ArrayLocation loc) {
        if (!loc.getExpression().getType().equals(Type.INT)){
            addError(loc, "Type of array expression " + loc.getExpression().getType()  + " != int " + loc);
        }
        else{
            IntLiteral literal = (IntLiteral) loc.getExpression();
            this.visit(literal);
            
            if (literal.getValue() <= 0){
                addError(loc, "Error array expression " + loc);
            }
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
                Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(CheckSemVisitor.class.getName()).log(Level.SEVERE, null, ex);
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
