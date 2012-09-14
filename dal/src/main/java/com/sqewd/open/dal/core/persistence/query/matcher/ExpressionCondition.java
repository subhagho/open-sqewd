/**
 * Copyright 2012 Subho Ghosh (subho.ghosh at outlook dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @filename ExpressionCondition.java
 * @created Sep 12, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.matcher;

import com.sqewd.open.dal.core.persistence.db.ConditionMatchHelper;
import com.sqewd.open.dal.core.persistence.db.LocalResultSet;
import com.sqewd.open.dal.core.persistence.db.StructDbColumn;
import com.sqewd.open.dal.core.persistence.query.EnumOperator;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class ExpressionCondition implements SQLCondition {
	private SQLConstant left;
	private SQLConstant right;
	private EnumOperator oper;

	public ExpressionCondition(final SQLConstant left, final SQLConstant right,
			final EnumOperator oper) {
		this.left = left;
		this.right = right;
		this.oper = oper;
	}

	/**
	 * @return the left
	 */
	public SQLConstant getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public SQLConstant getRight() {
		return right;
	}

	/**
	 * @return the oper
	 */
	public EnumOperator getOper() {
		return oper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + left.toString() + " " + oper.toString() + " "
				+ right.toString() + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.matcher.SQLCondition#match(
	 * com.sqewd.open.dal.core.persistence.db.LocalResult,
	 * com.sqewd.open.dal.core.persistence.db.LocalResult)
	 */
	public boolean match(final LocalResultSet src, final LocalResultSet tgt)
			throws Exception {
		Object sval = getValue(left, src, tgt);
		Object rval = getValue(right, src, tgt);
		try {
			if (sval != null && rval != null) {
				Class<?> type = getType(src, tgt);
				if (type == null) {
					type = sval.getClass();
				}
				return ConditionMatchHelper.compare(sval, rval, oper, type);
			}

			return false;
		} catch (ClassCastException cce) {
			throw cce;
		}
	}

	private Class<?> getType(final LocalResultSet src, final LocalResultSet tgt)
			throws Exception {
		if (left instanceof ColumnConstant) {
			ColumnConstant cc = (ColumnConstant) left;
			StructDbColumn column = findColumn(src, tgt, cc);
			if (column == null)
				throw new Exception("Column [" + cc.toString() + "] not found.");
			return column.Type;
		} else if (right instanceof ColumnConstant) {
			ColumnConstant cc = (ColumnConstant) right;
			StructDbColumn column = findColumn(src, tgt, cc);
			if (column == null)
				throw new Exception("Column [" + cc.toString() + "] not found.");
			return column.Type;
		}
		return null;
	}

	private StructDbColumn findColumn(final LocalResultSet s1,
			final LocalResultSet s2, final ColumnConstant col) {
		StructDbColumn column = null;
		column = s2.getColumn(col.toString());
		if (column == null) {
			column = s1.getColumn(col.toString());
		}
		return column;
	}

	private Object getValue(final SQLConstant cond, final LocalResultSet tgt,
			final LocalResultSet src) throws Exception {
		if (cond instanceof ColumnConstant) {
			ColumnConstant cc = (ColumnConstant) cond;
			StructDbColumn column = src.getColumn(cc.toString());
			if (column == null) {
				column = tgt.getColumn(cc.toString());
				if (column == null)
					throw new Exception("Column [" + cc.toString()
							+ "] not found.");
				else
					return tgt.getObject(column.Index);
			} else
				return src.getObject(column.Index);
		} else if (cond instanceof ValueConstant)
			return ((ValueConstant) cond).getValue();

		return null;
	}
}
