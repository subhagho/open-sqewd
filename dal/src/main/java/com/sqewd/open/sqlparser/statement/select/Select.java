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

import java.util.Iterator;
import java.util.List;

import com.sqewd.open.sqlparser.statement.Statement;
import com.sqewd.open.sqlparser.statement.StatementVisitor;

public class Select implements Statement {
	private SelectBody selectBody;
	private List<WithItem> withItemsList;

	public void accept(final StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(final SelectBody body) {
		selectBody = body;
	}

	@Override
	public String toString() {
		StringBuffer retval = new StringBuffer();
		if (withItemsList != null && !withItemsList.isEmpty()) {
			retval.append("WITH ");
			for (Iterator<WithItem> iter = withItemsList.iterator(); iter
					.hasNext();) {
				WithItem withItem = iter.next();
				retval.append(withItem);
				if (iter.hasNext()) {
					retval.append(",");
				}
				retval.append(" ");
			}
		}
		retval.append(selectBody);
		return retval.toString();
	}

	public List<WithItem> getWithItemsList() {
		return withItemsList;
	}

	public void setWithItemsList(final List<WithItem> withItemsList) {
		this.withItemsList = withItemsList;
	}
}
