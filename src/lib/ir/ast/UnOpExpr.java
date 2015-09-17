package ir.ast;

import ir.ASTVisitor;

public class UnOpExpr extends Expression {
	private UnOpType operator; //operator in the expr = expr operator expr
	private Expression expression; //left expression
	
	
	public UnOpExpr(UnOpType op, Expression e){
		operator = op;
		expression = e;
	}

	
	public UnOpType getOperator() {
		return operator;
	}

	public void setOperator(UnOpType operator) {
		this.operator = operator;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression e) {
		this.expression = e;
	}

	@Override
	public String toString() {
		return operator + " " + expression;
	}

	@Override
	public <T> T accept(ASTVisitor<T> v) {
		return v.visit(this);
	}
}
