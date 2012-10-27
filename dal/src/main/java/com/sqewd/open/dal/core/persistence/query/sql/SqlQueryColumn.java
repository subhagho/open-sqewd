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
 * @filename SqlQueryColumn.java
 * @created Oct 20, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import com.sqewd.open.dal.core.persistence.query.QueryColumn;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumSortDirection;

/**
 * Class represents a Query Column for SQL Queries.
 * 
 * @author subhagho
 * 
 */
public class SqlQueryColumn extends QueryColumn {
	private String table;

	private String alias;

	/**
	 * When creating a sort column this constructor should be called to ensure
	 * that the sort direction is never null.
	 * 
	 * @param direction
	 */
	public SqlQueryColumn(final EnumSortDirection direction) {
		super(direction);
	}

	/**
	 * Create a new SQL column with the specified name.
	 * 
	 * @param name
	 */
	public SqlQueryColumn(final String name) {
		super(name);
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(final String table) {
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.query.QueryColumn#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("( ");
		if (table != null && !table.isEmpty()) {
			buff.append(table).append(".");
		}
		buff.append(name);
		if (alias != null && !alias.isEmpty()) {
			buff.append(" AS ").append(alias).append(" ");
		}
		if (direction != null) {
			buff.append(" [").append(direction.name()).append("] ");
		}
		buff.append(" )");
		return buff.toString();
	}

}
