package ir.ast;

import ir.ASTVisitor;

public class ForStmt extends Statement {
	private Expression condition1;
	private Expression condition2;
	private Statement forStatement;
	
	public ForStmt(Expression cond1, Expression cond2, Statement forSt) {
		this.condition1 = cond1;
		this.condition2 = cond2;
		this.forStatement = forSt;
	}
	

	public Expression getCondition1() {
		return condition1;
	}

	public Expression getCondition2() {
		return condition2;
	}

	public void setCondition1(Expression condition1) {
		this.condition1 = condition1;
	}

	public void setCondition2(Expression condition2) {
		this.condition2 = condition2;
	}

	public Statement getForStatement() {
		return forStatement;
	}

	public void setForStatement(Statement forSt) {
		this.forStatement = forSt;
	}
	
	@Override
	public String toString() {
		return "for " + condition1 + "," + condition2 + '\n' + forStatement.toString();
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}