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
 * @filename SQLTable.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.reflect.SchemaObject;
import com.sqewd.open.dal.api.reflect.SchemaObjectAttribute;
import com.sqewd.open.dal.core.persistence.db.AbstractDbPersister;

/**
 * Class represents a SQL (RDBMS) table as a Schema Object.
 * 
 * @author subhagho
 * 
 */
public class SqlTable extends SchemaObject {
	private String schema;

	private String alias;

	private HashMap<String, Integer> aliasindx;

	public SqlTable(final String name, final String alias,
			final AbstractPersister persister) throws Exception {
		super(name, persister);
		if (alias == null) {
			this.alias = name;
		} else {
			this.alias = alias;
		}
		if (!(persister instanceof AbstractDbPersister))
			throw new Exception(
					"Invalid Persister. Specified persister is not of type ["
							+ AbstractDbPersister.class.getCanonicalName()
							+ "]");
		List<SqlColumn> columns = ((AbstractDbPersister) persister)
				.getTableDefinition(schema, name);
		if (columns == null || columns.isEmpty())
			throw new Exception("Cannot find schema definition for "
					+ (schema != null && !schema.isEmpty() ? "[" + schema
							+ "]." : "") + "[" + name + "]");
		for (SqlColumn column : columns) {
			addAttribute(column);
		}
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(final String schema) {
		this.schema = schema;
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

	public List<String> getColumnSet() {
		List<String> columns = new ArrayList<String>();
		for (SchemaObjectAttribute attr : attributes) {
			if (attr instanceof SqlColumn) {
				columns.add(((SqlColumn) attr).getAlias());
			}
		}
		return columns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObject#get(java.lang.
	 * String)
	 */
	@Override
	public SchemaObjectAttribute get(final String name) throws Exception {
		String attrname = name;
		if (name.indexOf('.') > 0) {
			String[] parts = name.split("\\.");
			if (name.compareTo(parts[0]) != 0 && alias.compareTo(parts[0]) != 0)
				throw new Exception("Invalid Attribute Schema. Schema ["
						+ parts[0] + "] is different.");
			attrname = parts[1];
		}
		if (aliasindx.containsKey(attrname))
			return attributes.get(aliasindx.get(attrname));
		else if (nameindx.containsKey(attrname))
			return attributes.get(nameindx.get(attrname));
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObject#addAttribute(com
	 * .sqewd.open.dal.core.persistence.query.SchemaObjectAttribute)
	 */
	@Override
	public void addAttribute(final SchemaObjectAttribute attr) throws Exception {

		if (aliasindx == null) {
			aliasindx = new HashMap<String, Integer>();
		} else {
			if (attr instanceof SqlColumn) {
				((SqlColumn) attr).setAlias(this.alias + "." + attr.getName());

				if (aliasindx.containsKey(((SqlColumn) attr).getAlias()))
					throw new Exception(
							"Attribute already registered with alias ["
									+ ((SqlColumn) attr).getAlias() + "]");
			}
		}
		super.addAttribute(attr);
		if (attr instanceof SqlColumn) {
			aliasindx.put(((SqlColumn) attr).getAlias(), attributes.size() - 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObject#isPartitioned()
	 */
	@Override
	public boolean isPartitioned() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.query.SchemaObject#getKey()
	 */
	@Override
	public String getKey() {
		return alias;
	}

}
