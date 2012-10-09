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
	 * @see com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition#
	 * isComplete()
	 */
	public boolean isComplete() {
		return complete;
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
		this.condition = condition;
	}

}
