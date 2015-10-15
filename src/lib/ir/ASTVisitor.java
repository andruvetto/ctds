package lib.ir;

import lib.ir.ast.*;

// Abstract visitor
public interface ASTVisitor<T> {
// visit statements
        T visit(Statement stmt);
       	T visit(AssignStmt stmt);
	T visit(ReturnStmt stmt);
	T visit(IfStmt stmt);
	T visit(ForStmt stmt);
	T visit(WhileStmt stmt);
        T visit(BreakStmt stmt);
        T visit(ContinueStmt stmt);
	
// visit expressions
        T visit(Expression expr);
	T visit(BinOpExpr expr);
        T visit(UnOpExpr expr);
	
// visit literals	
        T visit(Literal lit);
	T visit(IntLiteral lit);
        T visit(FloatLiteral lit);
        T visit(BooleanLiteral lit);

// visit methodCall
        T visit(MethodCallStmt m);
        T visit(MethodCall m);
        
// visit locations	
        T visit(Location loc);
	T visit(VarLocation loc);
        T visit(ArrayLocation loc);
        
// visit blocks	
	T visit(Block block); 
        
// visit field decl
        T visit(FieldDecl fd); 
// visit parameters
        T visit(Parameter p);
// visit method decls
        T visit(MethodDecl m);
        
// visit class decl
        T visit(ClassDecl c);
        
// visit program
        T visit(Program p);
}