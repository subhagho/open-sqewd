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

package com.sqewd.open.sqlparser.expression.operators.relational;

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;

public class InExpression implements Expression {
	private Expression leftExpression;
	private ItemsList itemsList;
	private boolean not = false;

	public InExpression() {
	}

	public InExpression(final Expression leftExpression,
			final ItemsList itemsList) {
		setLeftExpression(leftExpression);
		setItemsList(itemsList);
	}

	public ItemsList getItemsList() {
		return itemsList;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public void setItemsList(final ItemsList list) {
		itemsList = list;
	}

	public void setLeftExpression(final Expression expression) {
		leftExpression = expression;
	}

	public boolean isNot() {
		return not;
	}

	public void setNot(final boolean b) {
		not = b;
	}

	public void accept(final ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	@Override
	public String toString() {
		return leftExpression + " " + ((not) ? "NOT " : "") + "IN " + itemsList
				+ "";
	}
}
