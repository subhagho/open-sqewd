package com.sqewd.open.sqlparser.expression;

import com.sqewd.open.sqlparser.statement.select.SubSelect;

public class AnyComparisonExpression implements Expression {
	private SubSelect subSelect;

	public AnyComparisonExpression(final SubSelect subSelect) {
		this.subSelect = subSelect;
	}

	public SubSelect GetSubSelect() {
		return subSelect;
	}

	public void accept(final ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

}
