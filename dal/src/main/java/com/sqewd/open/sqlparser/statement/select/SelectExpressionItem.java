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

package com.sqewd.open.sqlparser.statement.select;

import com.sqewd.open.sqlparser.expression.Expression;

/**
 * An expression as in "SELECT expr1 AS EXPR"
 */
public class SelectExpressionItem implements SelectItem {
	private Expression expression;
	private String alias;

	public String getAlias() {
		return alias;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setAlias(final String string) {
		alias = string;
	}

	public void setExpression(final Expression expression) {
		this.expression = expression;
	}

	public void accept(final SelectItemVisitor selectItemVisitor) {
		selectItemVisitor.visit(this);
	}

	@Override
	public String toString() {
		return expression + ((alias != null) ? " AS " + alias : "");
	}
}
