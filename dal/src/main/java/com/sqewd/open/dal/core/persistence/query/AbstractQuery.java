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
 * @filename AbstractQuery.java
 * @created Oct 20, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.ArrayList;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.query.QueryCondition;

/**
 * Represents an abstract Query handle. Should be inherited from to represent
 * different data sources.
 * 
 * @author subhagho
 * 
 */
public abstract class AbstractQuery {
	protected List<QueryColumn> columns;
	protected QueryCondition condition;
	protected List<QueryColumn> sortColumns;

	protected AbstractEntity entity;

	protected AbstractQuery(final AbstractEntity entity) {
		this.entity = entity;
	}

	/**
	 * Add a new Column.
	 * 
	 * @param column
	 */
	public void addColumn(final QueryColumn column) {
		if (columns == null) {
			columns = new ArrayList<QueryColumn>();
		}
		columns.add(column);
	}

	/**
	 * @return the columns
	 */
	public List<QueryColumn> getColumns() {
		return columns;
	}

	/**
	 * @return the condition
	 */
	public QueryCondition getCondition() {
		return condition;
	}

	/**
	 * @return the entity
	 */
	public AbstractEntity getEntity() {
		return entity;
	}

	/**
	 * @return the sortColumns
	 */
	public List<QueryColumn> getSortColumns() {
		return sortColumns;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(final List<QueryColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(final QueryCondition condition) {
		this.condition = condition;
	}

	/**
	 * @param sortColumns
	 *            the sortColumns to set
	 */
	public void setSortColumns(final List<QueryColumn> sortColumns) {
		this.sortColumns = sortColumns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		if (columns != null && columns.size() > 0) {
			boolean first = false;
			buff.append("[COLUMNS: ");
			for (QueryColumn column : columns) {
				if (first) {
					first = false;
				} else {
					buff.append(", ");
				}
				buff.append(column.toString());
			}
			buff.append("]\n");

		}
		if (condition != null) {
			buff.append("[CONDITION: ").append(condition.toString())
					.append("]\n");
		}
		if (sortColumns != null && sortColumns.size() > 0) {
			boolean first = false;
			buff.append("[COLUMNS: ");
			for (QueryColumn column : sortColumns) {
				if (first) {
					first = false;
				} else {
					buff.append(", ");
				}
				buff.append(column.toString());
			}
			buff.append("]\n");
		}
		return buff.toString();
	}

}
