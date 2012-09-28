/* ================================================================
 * JSQLParser : java based sql parser 
 * ================================================================
 *
 * Project Info:  http://jsqlparser.sourceforge.net
 * Project Lead:  Leonardo Francalanci (leoonardoo@yahoo.it);
 *
 * (C) Copyright 2004, by Leonardo Francalanci
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package com.sqewd.open.sqlparser.expression.operators.conditional;

import com.sqewd.open.sqlparser.expression.BinaryExpression;
import com.sqewd.open.sqlparser.expression.EnumOperator;
import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;

public class OrExpression extends BinaryExpression {
	public OrExpression(final Expression leftExpression,
			final Expression rightExpression) {
		setLeftExpression(leftExpression);
		setRightExpression(rightExpression);
	}

	public void accept(final ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	@Override
	public String getStringExpression() {
		return "OR";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.QueryCondition#evaluate(java
	 * .lang.Object, java.lang.Object)
	 */
	public boolean evaluate(final Object src, final Object value)
			throws Exception {
		boolean result = leftExpression.evaluate(src, value);
		if (!result) {
			result = rightExpression.evaluate(src, value);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.sqlparser.expression.BinaryExpression#getOperator()
	 */
	@Override
	public EnumOperator getOperator() {
		return EnumOperator.Or;
	}

}
