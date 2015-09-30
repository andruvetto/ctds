package ir;

import ir.ast.*;

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
        T visit(ExternStmt stmt);
	
// visit expressions
	T visit(BinOpExpr expr);
        T visit(UnOpExpr expr);
	
// visit literals	
	T visit(IntLiteral lit);
        T visit(FloatLiteral lit);
        T visit(BooleanLiteral lit);

// visit methodCall
        T visit(MethodCall m);
        T visit(MethodCallStmt m);
        
// visit locations	
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