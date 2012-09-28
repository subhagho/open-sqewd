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
 * @filename SchemaType.java
 * @created Sep 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * Class is used to defining a Schema object such as table, column group, file,
 * etc.
 * 
 * @author subhagho
 * 
 */
public abstract class SchemaObject {
	protected String name;

	protected String alias;

	protected SchemaObject(final String name, String alias) {
		this.name = name;
		if (alias == null || alias.isEmpty()) {
			alias = name;
		}
		this.alias = alias;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
