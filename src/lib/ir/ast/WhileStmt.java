package ir.ast;

import ir.ASTVisitor;

public class WhileStmt extends Statement {
	private Expression condition;
	private Statement whileStatement;
	
	public WhileStmt(Expression cond, Statement whileSt) {
		this.condition = cond;
		this.whileStatement = whileSt;
	}
	

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public Statement getWhileStatement() {
		return whileStatement;
	}

	public void setWhileStatement(Statement whileSt) {
		this.whileStatement = whileSt;
	}
	
	@Override
	public String toString() {
		return "while " + condition + " " + whileStatement.toString();
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}