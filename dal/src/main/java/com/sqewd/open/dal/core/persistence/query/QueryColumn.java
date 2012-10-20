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
 * @filename QueryColumn.java
 * @created Oct 19, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import com.sqewd.open.dal.core.persistence.query.conditions.EnumSortDirection;

/**
 * Class represents a Column definition for a Query.
 * 
 * @author subhagho
 * 
 */
public class QueryColumn {
	private String name;
	private EnumSortDirection direction = EnumSortDirection.Desc;

	/**
	 * @return the direction
	 */
	public EnumSortDirection getDirection() {
		return direction;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(final EnumSortDirection direction) {
		this.direction = direction;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
}
