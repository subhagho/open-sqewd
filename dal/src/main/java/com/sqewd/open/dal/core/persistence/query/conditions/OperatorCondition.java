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
public class OperatorCondition extends ConditionElement implements
		QueryCondition {
	protected EnumConditionOperator operator;

	protected ConditionElement left;

	protected ConditionElement right;

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
	 * @return the operator
	 */
	public EnumConditionOperator getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(final EnumConditionOperator operator) {
		this.operator = operator;
	}

	/**
	 * @return the left
	 */
	public ConditionElement getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(final ConditionElement left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public ConditionElement getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(final ConditionElement right) {
		this.right = right;
	}

}
