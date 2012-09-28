package com.sqewd.open.sqlparser.expression.operators.relational;

import com.sqewd.open.sqlparser.expression.BinaryExpression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;

public class Matches extends BinaryExpression {
	public void accept(final ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public String getStringExpression() {
		return "@@";
	}
}
