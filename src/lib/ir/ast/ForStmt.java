package ir.ast;

import ir.ASTVisitor;

public class ForStmt extends Statement {
	private AssignStmt assign;
	private Expression condition;
	private Statement forStatement;
	
	public ForStmt(AssignStmt stmt, Expression cond, Statement forSt) {
		this.assign = stmt;
		this.condition = cond;
		this.forStatement = forSt;
	}
	

	public AssignStmt getAssign() {
		return assign;
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression cond) {
		this.condition = cond;
	}

	public void setAssign(AssignStmt a) {
		this.assign = a;
	}

	public Statement getForStatement() {
		return forStatement;
	}

	public void setForStatement(Statement forSt) {
		this.forStatement = forSt;
	}
	
	@Override
	public String toString() {
		return "for " + assign + "," + condition + '\n' + forStatement.toString();
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}