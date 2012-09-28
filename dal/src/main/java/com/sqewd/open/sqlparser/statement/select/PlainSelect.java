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

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.schema.Table;

/**
 */
/**
 * The core of a "SELECT" statement (no UNION, no ORDER BY)
 */
public class PlainSelect implements SelectBody {
	private Distinct distinct = null;
	private List selectItems;
	private Table into;
	private FromItem fromItem;
	private List<Join> joins;
	private Expression where;
	private List groupByColumnReferences;
	private List orderByElements;
	private Expression having;
	private Limit limit;
	private Top top;

	/**
	 * The {@link FromItem} in this query
	 * 
	 * @return the {@link FromItem}
	 */
	public FromItem getFromItem() {
		return fromItem;
	}

	public Table getInto() {
		return into;
	}

	/**
	 * The {@link SelectItem}s in this query (for example the A,B,C in
	 * "SELECT A,B,C")
	 * 
	 * @return a list of {@link SelectItem}s
	 */
	public List getSelectItems() {
		return selectItems;
	}

	public Expression getWhere() {
		return where;
	}

	public void setFromItem(final FromItem item) {
		fromItem = item;
	}

	public void setInto(final Table table) {
		into = table;
	}

	public void setSelectItems(final List list) {
		selectItems = list;
	}

	public void setWhere(final Expression where) {
		this.where = where;
	}

	/**
	 * The list of {@link Join}s
	 * 
	 * @return the list of {@link Join}s
	 */
	public List<Join> getJoins() {
		return joins;
	}

	public void setJoins(final List<Join> list) {
		joins = list;
	}

	public void accept(final SelectVisitor selectVisitor) {
		selectVisitor.visit(this);
	}

	public List getOrderByElements() {
		return orderByElements;
	}

	public void setOrderByElements(final List orderByElements) {
		this.orderByElements = orderByElements;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(final Limit limit) {
		this.limit = limit;
	}

	public Top getTop() {
		return top;
	}

	public void setTop(final Top top) {
		this.top = top;
	}

	public Distinct getDistinct() {
		return distinct;
	}

	public void setDistinct(final Distinct distinct) {
		this.distinct = distinct;
	}

	public Expression getHaving() {
		return having;
	}

	public void setHaving(final Expression expression) {
		having = expression;
	}

	/**
	 * A list of {@link Expression}s of the GROUP BY clause. It is null in case
	 * there is no GROUP BY clause
	 * 
	 * @return a list of {@link Expression}s
	 */
	public List getGroupByColumnReferences() {
		return groupByColumnReferences;
	}

	public void setGroupByColumnReferences(final List list) {
		groupByColumnReferences = list;
	}

	@Override
	public String toString() {
		String sql = "";

		sql = "SELECT ";
		sql += ((distinct != null) ? "" + distinct + " " : "");
		sql += ((top != null) ? "" + top + " " : "");
		sql += getStringList(selectItems);
		sql += " FROM " + fromItem;
		if (joins != null) {
			Iterator<Join> it = joins.iterator();
			while (it.hasNext()) {
				Join join = it.next();
				if (join.isSimple()) {
					sql += ", " + join;
				} else {
					sql += " " + join;
				}
			}
		}
		// sql += getFormatedList(joins, "", false, false);
		sql += ((where != null) ? " WHERE " + where : "");
		sql += getFormatedList(groupByColumnReferences, "GROUP BY");
		sql += ((having != null) ? " HAVING " + having : "");
		sql += orderByToString(orderByElements);
		sql += ((limit != null) ? limit + "" : "");

		return sql;
	}

	public static String orderByToString(final List orderByElements) {
		return getFormatedList(orderByElements, "ORDER BY");
	}

	public static String getFormatedList(final List list,
			final String expression) {
		return getFormatedList(list, expression, true, false);
	}

	public static String getFormatedList(final List list,
			final String expression, final boolean useComma,
			final boolean useBrackets) {
		String sql = getStringList(list, useComma, useBrackets);

		if (sql.length() > 0) {
			if (expression.length() > 0) {
				sql = " " + expression + " " + sql;
			} else {
				sql = " " + sql;
			}
		}

		return sql;
	}

	/**
	 * List the toString out put of the objects in the List comma separated. If
	 * the List is null or empty an empty string is returned.
	 * 
	 * The same as getStringList(list, true, false)
	 * 
	 * @see #getStringList(List, boolean, boolean)
	 * @param list
	 *            list of objects with toString methods
	 * @return comma separated list of the elements in the list
	 */
	public static String getStringList(final List<?> list) {
		return getStringList(list, true, false);
	}

	/**
	 * List the toString out put of the objects in the List that can be comma
	 * separated. If the List is null or empty an empty string is returned.
	 * 
	 * @param list
	 *            list of objects with toString methods
	 * @param useComma
	 *            true if the list has to be comma separated
	 * @param useBrackets
	 *            true if the list has to be enclosed in brackets
	 * @return comma separated list of the elements in the list
	 */
	public static String getStringList(final List<?> list,
			final boolean useComma, final boolean useBrackets) {
		String ans = "";
		String comma = ",";
		if (!useComma) {
			comma = "";
		}
		if (list != null) {
			if (useBrackets) {
				ans += "(";
			}

			for (int i = 0; i < list.size(); i++) {
				ans += "" + list.get(i).toString()
						+ ((i < list.size() - 1) ? comma + " " : "");
			}

			if (useBrackets) {
				ans += ")";
			}
		}

		return ans;
	}
}
