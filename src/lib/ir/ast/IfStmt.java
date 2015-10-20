package lib.ir.ast;

import lib.ir.ASTVisitor;

public class IfStmt extends Statement {
	private Expression condition;
	private Statement ifStatement;
	private Statement elseStatement;
	
	public IfStmt(Expression cond, Statement ifSt, Statement elseSt) {
		this.condition = cond;
		this.ifStatement = ifSt;
		this.elseStatement = elseSt;
	}
	
	public IfStmt(Expression cond, Statement ifSt) {
		this.condition = cond;
		this.ifStatement = ifSt;
		this.elseStatement = null;
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public Statement getIfStatement() {
		return ifStatement;
	}

	public void setIfStatement(Statement ifSt) {
		this.ifStatement = ifSt;
	}

	public Statement getElseStatement() {
		return elseStatement;
	}

	public void setElseStatement(Statement elseSt) {
		this.elseStatement = elseSt;
	}
	
	@Override
	public String toString() {
		String rtn = "if " + condition + '\n' + ifStatement.toString() ;
		
		if (elseStatement != null) {
			rtn += "\nelse \n" + elseStatement.toString();
		}
		
		return rtn;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}