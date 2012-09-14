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
 * @filename AndCondition.java
 * @created Sep 11, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.matcher;

import com.sqewd.open.dal.core.persistence.db.LocalResultSet;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class AndCondition implements SQLCondition {
	private SQLCondition left;
	private SQLCondition right;

	public AndCondition(final SQLCondition left, final SQLCondition right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the left
	 */
	public SQLCondition getLeft() {
		return left;
	}

	/**
	 * @return the right
	 */
	public SQLCondition getRight() {
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + left.toString() + " AND " + right.toString() + ")";
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
		boolean retval = left.match(src, tgt);
		if (retval) {
			retval = right.match(src, tgt);
		}
		return retval;
	}
}
