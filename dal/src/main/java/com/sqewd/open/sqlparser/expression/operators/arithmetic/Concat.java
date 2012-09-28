package com.sqewd.open.sqlparser.expression.operators.arithmetic;

import com.sqewd.open.sqlparser.expression.BinaryExpression;
import com.sqewd.open.sqlparser.expression.EnumOperator;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;

public class Concat extends BinaryExpression {

	public void accept(final ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	@Override
	public String getStringExpression() {
		return "||";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.sqlparser.expression.Expression#evaluate(java.lang.Object,
	 * java.lang.Object)
	 */
	public boolean evaluate(final Object src, final Object value)
			throws Exception {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.sqlparser.expression.BinaryExpression#getOperator()
	 */
	@Override
	public EnumOperator getOperator() {
		return EnumOperator.Concat;
	}

}
