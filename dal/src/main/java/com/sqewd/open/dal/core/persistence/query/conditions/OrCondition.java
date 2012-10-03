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
 * @filename OrCondition.java
 * @created Sep 30, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

/**
 * Represents an Or Condition node.
 * 
 * @author subhagho
 * 
 */
public class OrCondition extends ConditionElement implements QueryCondition {
	protected QueryCondition left;
	protected QueryCondition right;

	public OrCondition() {
	}

	public OrCondition(final QueryCondition left, final QueryCondition right) {
		this.left = left;
		this.right = right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#evaluate
	 * (java.lang.Object, java.lang.Object)
	 */
	public boolean evaluate(final Object src, final Object value)
			throws Exception {
		if (!left.evaluate(src, value)) {
			if (right.evaluate(src, value))
				return true;
			else
				return false;
		} else
			return true;
	}

	/**
	 * @return the left
	 */
	public QueryCondition getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(final QueryCondition left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public QueryCondition getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(final QueryCondition right) {
		this.right = right;
	}

}
