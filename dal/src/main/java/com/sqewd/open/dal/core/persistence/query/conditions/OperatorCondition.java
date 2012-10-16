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
 * @filename OperatorCondition.java
 * @created Sep 30, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

/**
 * Represents a condition operation.
 * 
 * @author subhagho
 * 
 */
public class OperatorCondition implements QueryCondition {
	protected EnumConditionOperator operator;

	protected Condition left;

	protected Condition right;

	protected Condition parent;

	protected boolean consumed = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#evaluate
	 * (java.lang.Object, java.lang.Object)
	 */
	public boolean evaluate(final Object src, final Object value)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the left
	 */
	public Condition getLeft() {
		return left;
	}

	/**
	 * @return the operator
	 */
	public EnumConditionOperator getOperator() {
		return operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.Condition#getParent
	 * ()
	 */
	public Condition getParent() {
		return parent;
	}

	/**
	 * @return the right
	 */
	public Condition getRight() {
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#
	 * isComplete()
	 */
	public boolean isComplete() {
		if (left != null && right != null && operator != null)
			return true;
		return false;
	}

	/**
	 * @return the consumed
	 */
	public boolean isConsumed() {
		return consumed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.Condition#prettyPrint
	 * (int)
	 */
	public String prettyPrint(final int offset) {
		StringBuffer buff = new StringBuffer();
		for (int ii = 0; ii < offset; ii++) {
			buff.append(_OFFSET_CHAR_);
		}
		buff.append("|--").append(toString()).append("\n");
		return buff.toString();
	}

	/**
	 * @param consumed
	 *            the consumed to set
	 */
	public void setConsumed(final boolean consumed) {
		this.consumed = consumed;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(final Condition left) throws Exception {
		if (left instanceof ArithmeticOperatorCondition
				|| left instanceof ConditionAttribute
				|| left instanceof ConditionValue) {
			left.setParent(this);
			this.left = left;
		} else if (left instanceof GroupCondition
				&& ((GroupCondition) left).isArithmeticGroup()) {
			left.setParent(this);
			this.left = left;
		} else
			throw new Exception("Condition of type ["
					+ right.getClass().getCanonicalName() + "] not supported.");
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(final EnumConditionOperator operator)
			throws Exception {
		if (operator != EnumConditionOperator.Between
				&& operator != EnumConditionOperator.Equals
				&& operator != EnumConditionOperator.In
				&& operator != EnumConditionOperator.LessThan
				&& operator != EnumConditionOperator.LessThanEquals
				&& operator != EnumConditionOperator.Like
				&& operator != EnumConditionOperator.MoreThan
				&& operator != EnumConditionOperator.MoreThanEquals
				&& operator != EnumConditionOperator.NotEqualTo)
			throw new Exception("Should be a condition operator.");
		this.operator = operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.Condition#setParent
	 * (com.sqewd.open.dal.core.persistence.query.conditions.Condition)
	 */
	public void setParent(final Condition parent) {
		this.parent = parent;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(final Condition right) throws Exception {
		if (right instanceof ArithmeticOperatorCondition
				|| right instanceof ConditionAttribute
				|| right instanceof ConditionValue) {
			right.setParent(this);
			this.right = right;
		} else if (right instanceof GroupCondition
				&& ((GroupCondition) right).isArithmeticGroup()) {
			right.setParent(this);
			this.right = right;
		} else
			throw new Exception("Condition of type ["
					+ right.getClass().getCanonicalName() + "] not supported.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		if (left != null) {
			buff.append(left.toString());
		} else {
			buff.append("<NULL>");
		}
		buff.append(" ").append(operator.name()).append(" ");
		if (right != null) {
			buff.append(right.toString());
		} else {
			buff.append("<NULL>");
		}
		return buff.toString();
	}

}
