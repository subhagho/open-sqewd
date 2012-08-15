/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.core.persistence.query;

public class FilterCondition extends AbstractCondition {
	public static final String _STRING_DEFAULT_ = "DEFAULT";
	public static final String _STRING_NULL_ = "NULL";

	private AbstractConditionPredicate left;
	private EnumOperator comparator;
	private AbstractConditionPredicate right;

	public FilterCondition(AbstractConditionPredicate left,
			EnumOperator comparator, AbstractConditionPredicate right) {
		this.left = left;
		this.comparator = comparator;
		this.right = right;
	}

	public FilterCondition(String alias, Class<?> type, String column,
			EnumOperator comparator, Object value) {
		this.left = new ColumnConditionPredicate(alias, type, column);
		this.comparator = comparator;
		this.right = new ValueConditionPredicate(value);
		this.conditiontype = EnumConditionType.Value;
	}

	public FilterCondition(String lalias, Class<?> ltype, String lcolumn,
			EnumOperator comparator, String ralias, Class<?> rtype,
			String rcolumn) {
		this.left = new ColumnConditionPredicate(lalias, ltype, lcolumn);
		this.comparator = comparator;
		this.right = new ColumnConditionPredicate(ralias, rtype, rcolumn);
		this.conditiontype = EnumConditionType.Join;
	}

	/**
	 * @return the comparator
	 */
	public EnumOperator getComparator() {
		return comparator;
	}

	/**
	 * @param comparator
	 *            the comparator to set
	 */
	public void setComparator(EnumOperator comparator) {
		this.comparator = comparator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(left.toString()).append(" ").append(comparator.name())
				.append(" ");
		if (right != null) {
			buff.append(right.toString());
		} else {
			buff.append("NULL");
		}
		return buff.toString();
	}

	/**
	 * @return the left
	 */
	public AbstractConditionPredicate getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AbstractConditionPredicate left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public AbstractConditionPredicate getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(AbstractConditionPredicate right) {
		this.right = right;
	}
}
