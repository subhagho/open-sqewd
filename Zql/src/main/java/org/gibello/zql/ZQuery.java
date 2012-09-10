/*
 * This file is part of Zql.
 *
 * Zql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zql.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gibello.zql;

import java.util.*;

/**
 * ZQuery: an SQL SELECT statement
 */
@SuppressWarnings("serial")
public class ZQuery implements ZStatement, ZExp {

	Vector<ZSelectItem> select_;
	boolean distinct_ = false;
	Vector<ZFromItem> from_;
	ZExp where_ = null;
	ZGroupBy groupby_ = null;
	ZExpression setclause_ = null;
	Vector<ZOrderBy> orderby_ = null;
	boolean forupdate_ = false;
	int top_ = -1;

	/**
	 * Create a new SELECT statement
	 */
	public ZQuery() {
	}

	/**
	 * Insert the SELECT part of the statement
	 * 
	 * @param s
	 *            A vector of ZSelectItem objects
	 */
	public void addSelect(Vector<ZSelectItem> s) {
		select_ = s;
	}

	/**
	 * Insert the FROM part of the statement
	 * 
	 * @param f
	 *            a Vector of ZFromItem objects
	 */
	public void addFrom(Vector<ZFromItem> f) {
		from_ = f;
	}

	/**
	 * Insert a WHERE clause
	 * 
	 * @param w
	 *            An SQL Expression
	 */
	public void addWhere(ZExp w) {
		where_ = w;
	}

	public void appendWhere(String op, ZExp a) {
		if (where_ != null) {
			if (where_ instanceof ZExpression) {
				ZExpression zx = new ZExpression(op, a);
				((ZExpression) where_).addOperand(zx);
			}
		} else {
			where_ = new ZExpression(op, a);
		}
	}

	/**
	 * Insert a GROUP BY...HAVING clause
	 * 
	 * @param g
	 *            A GROUP BY...HAVING clause
	 */
	public void addGroupBy(ZGroupBy g) {
		groupby_ = g;
	}

	/**
	 * Insert a SET clause (generally UNION, INTERSECT or MINUS)
	 * 
	 * @param s
	 *            An SQL Expression (generally UNION, INTERSECT or MINUS)
	 */
	public void addSet(ZExpression s) {
		setclause_ = s;
	}

	/**
	 * Insert an ORDER BY clause
	 * 
	 * @param v
	 *            A vector of ZOrderBy objects
	 */
	public void addOrderBy(Vector<ZOrderBy> v) {
		orderby_ = v;
	}

	/**
	 * Get the SELECT part of the statement
	 * 
	 * @return A vector of ZSelectItem objects
	 */
	public Vector<ZSelectItem> getSelect() {
		return select_;
	}

	/**
	 * Get the FROM part of the statement
	 * 
	 * @return A vector of ZFromItem objects
	 */
	public Vector<ZFromItem> getFrom() {
		return from_;
	}

	/**
	 * Get the WHERE part of the statement
	 * 
	 * @return An SQL Expression or sub-query (ZExpression or ZQuery object)
	 */
	public ZExp getWhere() {
		return where_;
	}

	/**
	 * Get the GROUP BY...HAVING part of the statement
	 * 
	 * @return A GROUP BY...HAVING clause
	 */
	public ZGroupBy getGroupBy() {
		return groupby_;
	}

	/**
	 * Get the SET clause (generally UNION, INTERSECT or MINUS)
	 * 
	 * @return An SQL Expression (generally UNION, INTERSECT or MINUS)
	 */
	public ZExpression getSet() {
		return setclause_;
	}

	/**
	 * Get the ORDER BY clause
	 * 
	 * @param v
	 *            A vector of ZOrderBy objects
	 */
	public Vector<ZOrderBy> getOrderBy() {
		return orderby_;
	}

	/**
	 * @return true if it is a SELECT DISTINCT query, false otherwise.
	 */
	public boolean isDistinct() {
		return distinct_;
	}

	public void setDistinct(boolean distinct_) {
		this.distinct_ = distinct_;
	}

	/**
	 * @return true if it is a FOR UPDATE query, false otherwise.
	 */
	public boolean isForUpdate() {
		return forupdate_;
	}

	public void setForUpdate(boolean forupdate_) {
		this.forupdate_ = forupdate_;
	}

	public int getTop() {
		return top_;
	}

	public void setTop(int top_) {
		this.top_ = top_;
	}

	public String getSql(ZQuery aq, int top) {
		ZExpression where__ = null;
		ZExp where = (aq != null ? aq.where_ : null);

		if (where != null) {
			if (where_ != null) {
				where__ = new ZExpression("AND", where_);
				where__.addOperand(new ZExpression("", where));
			} else {
				where__ = (ZExpression) where;
			}
		} else {
			where__ = (ZExpression) where_;
		}
		int i;

		StringBuffer buf = new StringBuffer();
		if (select_ != null) {
			buf.append("select ");

			if (top > 0) {
				buf.append("top ").append(top).append(" ");
			}

			if (distinct_)
				buf.append("distinct ");

			// buf.append(select_.toString());
			buf.append(select_.elementAt(0).toString());
			for (i = 1; i < select_.size(); i++) {
				buf.append(", " + select_.elementAt(i).toString());
			}
		}
		// buf.append(" from " + from_.toString());
		if (from_ != null) {
			buf.append(" from ");
			buf.append(from_.elementAt(0).toString());
			for (i = 1; i < from_.size(); i++) {
				buf.append(", " + from_.elementAt(i).toString());
			}
		}
		if (where__ != null) {
			buf.append(" where " + where__.toString());
		}
		if (groupby_ != null) {
			buf.append(" " + groupby_.toString());
		}
		if (setclause_ != null) {
			buf.append(" " + setclause_.toString());
		}
		if (aq != null && aq.orderby_ != null) {
			buf.append(" order by ");
			// buf.append(orderby_.toString());
			buf.append(aq.orderby_.elementAt(0).toString());
			for (i = 1; i < aq.orderby_.size(); i++) {
				buf.append(", " + aq.orderby_.elementAt(i).toString());
			}
		} else if (orderby_ != null) {
			buf.append(" order by ");
			// buf.append(orderby_.toString());
			buf.append(orderby_.elementAt(0).toString());
			for (i = 1; i < orderby_.size(); i++) {
				buf.append(", " + orderby_.elementAt(i).toString());
			}
		}
		if (forupdate_)
			buf.append(" for update");

		return buf.toString();
	}

	public String toString() {
		int i;

		StringBuffer buf = new StringBuffer();
		if (select_ != null) {
			buf.append("select ");

			if (top_ > 0) {
				buf.append("top ").append(top_).append(" ");
			}

			if (distinct_)
				buf.append("distinct ");

			// buf.append(select_.toString());
			buf.append(select_.elementAt(0).toString());
			for (i = 1; i < select_.size(); i++) {
				buf.append(", " + select_.elementAt(i).toString());
			}
		}
		// buf.append(" from " + from_.toString());
		if (from_ != null) {
			buf.append(" from ");
			buf.append(from_.elementAt(0).toString());
			for (i = 1; i < from_.size(); i++) {
				buf.append(", " + from_.elementAt(i).toString());
			}
		}
		if (where_ != null) {
			buf.append(" where " + where_.toString());
		}
		if (groupby_ != null) {
			buf.append(" " + groupby_.toString());
		}
		if (setclause_ != null) {
			buf.append(" " + setclause_.toString());
		}
		if (orderby_ != null) {
			buf.append(" order by ");
			// buf.append(orderby_.toString());
			buf.append(orderby_.elementAt(0).toString());
			for (i = 1; i < orderby_.size(); i++) {
				buf.append(", " + orderby_.elementAt(i).toString());
			}
		}
		if (forupdate_)
			buf.append(" for update");

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gibello.zql.ZExp#copy()
	 */
	public ZExp copy() {
		ZQuery zq = new ZQuery();
		zq.distinct_ = distinct_;
		zq.forupdate_ = forupdate_;
		if (from_ != null && from_.size() > 0) {
			zq.from_ = new Vector<ZFromItem>();
			for (ZFromItem fi : from_) {
				zq.from_.add(fi.copy());
			}
		}
		if (orderby_ != null && orderby_.size() > 0) {
			zq.orderby_ = new Vector<ZOrderBy>();
			for (ZOrderBy zo : orderby_) {
				zq.orderby_.add(zo.copy());
			}
		}
		if (select_ != null && select_.size() > 0) {
			zq.select_ = new Vector<ZSelectItem>();
			for (ZSelectItem si : select_) {
				zq.select_.add(si.copy());
			}
		}
		if (setclause_ != null)
			zq.setclause_ = (ZExpression) setclause_.copy();
		zq.top_ = top_;
		if (where_ != null)
			zq.where_ = where_.copy();
		return zq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gibello.zql.ZExp#clean()
	 */
	public boolean clean() {
		if (where_ != null) {
			where_.clean();
			if (where_ instanceof ZExpression) {
				if (((ZExpression) where_).count() <= 0)
					where_ = null;
			}
		}
		return false;
	}

};
