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
 * @filename GroupCondition.java
 * @created Oct 7, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

/**
 * Represents a condition group.
 * 
 * @author subhagho
 * 
 */
public class GroupCondition implements QueryCondition {
	private QueryCondition condition;

	private boolean complete = false;

	private Condition parent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#evaluate
	 * (java.lang.Object, java.lang.Object)
	 */
	public boolean evaluate(final Object src, final Object value)
			throws Exception {
		return condition.evaluate(src, value);
	}

	/**
	 * @return the condition
	 */
	public QueryCondition getCondition() {
		return condition;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#
	 * isComplete()
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Indicates the current GroupCondition is the root condition.
	 * 
	 * @return
	 */
	public boolean isRootCondition() {
		if (parent == null)
			return true;
		return false;
	}

	/**
	 * Set the Group condition as parse complete. Essentially brace has been
	 * closed. Only relevant to the parser.
	 * 
	 */
	public void setComplete() {
		complete = true;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(final QueryCondition condition) {
		condition.setParent(this);
		this.condition = condition;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + (condition != null ? condition.toString() : "<NULL>")
				+ ")";
	}

}
