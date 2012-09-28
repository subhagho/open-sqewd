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

import java.util.List;

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.statement.select.PlainSelect;

/**
 * A list of expressions, as in SELECT A FROM TAB WHERE B IN (expr1,expr2,expr3)
 */
public class ExpressionList implements ItemsList {
	private List<Expression> expressions;

	public ExpressionList() {
	}

	public ExpressionList(final List<Expression> expressions) {
		this.expressions = expressions;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(final List<Expression> list) {
		expressions = list;
	}

	public void accept(final ItemsListVisitor itemsListVisitor) {
		itemsListVisitor.visit(this);
	}

	@Override
	public String toString() {
		return PlainSelect.getStringList(expressions, true, true);
	}
}
