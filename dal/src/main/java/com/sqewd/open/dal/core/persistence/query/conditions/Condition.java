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
 * @filename ConditionElement.java
 * @created Sep 30, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

/**
 * Base interface for representing condition elements.
 * 
 * @author subhagho
 * 
 */
public interface Condition {
	public static final String _OFFSET_CHAR_ = " ";

	/**
	 * Get the parent condition this is embedded in.
	 * 
	 * @return
	 */
	public Condition getParent();

	/**
	 * Pretty print the query in a tree format.
	 * 
	 * @param offset
	 * @return
	 */
	public String prettyPrint(int offset);

	/**
	 * Set the parent condition this is embedded in.
	 * 
	 * @param parent
	 */
	public void setParent(Condition parent);
}
