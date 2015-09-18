package ir;

import ir.ast.*;

// Abstract visitor
public interface ASTVisitor<T> {
// visit statements
	T visit(AssignStmt stmt);
	T visit(ReturnStmt stmt);
	T visit(IfStmt stmt);
	
// visit expressions
	T visit(BinOpExpr expr);
        T visit(UnOpExpr expr);
	
// visit literals	
	T visit(IntLiteral lit);
        T visit(FloatLiteral lit);
        T visit(BooleanLiteral lit);

// visit methodCall
        T visit(MethodCall m);
        
// visit locations	
	T visit(VarLocation loc);
        
// visit blocks	
	T visit(Block block);        
}
