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

import com.sqewd.open.dal.api.persistence.query.Condition;
import com.sqewd.open.dal.api.persistence.query.QueryCondition;

/**
 * Represents an Or Condition node.
 * 
 * @author subhagho
 * 
 */
public class OrCondition implements QueryCondition {
	protected QueryCondition left;
	protected QueryCondition right;
	protected Condition parent;

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
	public QueryCondition getRight() {
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#
	 * isComplete()
	 */
	public boolean isComplete() {
		if (left != null && right != null)
			return true;
		return false;
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
		buff.append(left.prettyPrint(offset + 6));
		for (int ii = 0; ii < offset; ii++) {
			buff.append(_OFFSET_CHAR_);
		}
		buff.append("--OR--|\n");
		buff.append(right.prettyPrint(offset + 6));
		return buff.toString();
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(final QueryCondition left) {
		left.setParent(this);
		this.left = left;
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
	public void setRight(final QueryCondition right) {
		right.setParent(this);
		this.right = right;
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
		buff.append(" OR ");
		if (right != null) {
			buff.append(right.toString());
		} else {
			buff.append("<NULL>");
		}
		return buff.toString();
	}

}
