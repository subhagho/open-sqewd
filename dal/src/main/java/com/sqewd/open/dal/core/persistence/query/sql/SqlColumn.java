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
 * @filename SqlColumn.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import com.sqewd.open.dal.api.reflect.SchemaObjectAttribute;

/**
 * Represent a SQL Column.
 * 
 * @author subhagho
 * 
 */
public class SqlColumn extends SchemaObjectAttribute {
	private String alias;

	/**
	 * @param name
	 * @param type
	 */
	public SqlColumn(final String name, final SqlDataType<?> type,
			final SqlTable table) {
		super(name, type, table);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append(((SqlTable) parent).getAlias()).append('.').append(name);
		return bf.toString();
	}
}
