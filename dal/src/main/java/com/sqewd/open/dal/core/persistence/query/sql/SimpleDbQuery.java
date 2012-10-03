/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.api.reflect.AttributeReferenceDef;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.core.persistence.model.EntityModelLoader;

/**
 * Class encapsulates the Query definition for a JDBC compliant Database.
 * 
 * @author subhagho
 * 
 */
public class SimpleDbQuery {
	public static enum EnumQueryType {
		SELECT, INSERT, UPDATE, DELETE;
	}

	private static final Logger log = LoggerFactory
			.getLogger(SimpleDbQuery.class);

	private static HashMap<String, HashMap<String, String>> queryCache = new HashMap<String, HashMap<String, String>>();

	public SimpleDbQuery() {
	}

	private synchronized void addToCache(final EnumQueryType type,
			final Class<?> cls, final String sql) {
		if (!queryCache.containsKey(type.name())) {
			queryCache.put(type.name(), new HashMap<String, String>());
		}
		HashMap<String, String> cache = queryCache.get(type.name());
		if (cache.containsKey(cls.getName())) {
			cache.remove(cls.getName());
		}
		cache.put(cls.getName(), sql);
	}

	private String getCachedQuery(final EnumQueryType type, final Class<?> cls) {
		if (queryCache.containsKey(type.name())) {
			HashMap<String, String> cache = queryCache.get(type.name());
			if (cache.containsKey(cls.getName()))
				return cache.get(cls.getName());
		}
		return null;
	}

	/**
	 * Get the insert Query(s) for the entity and all referenced entities.
	 * 
	 * @param type
	 *            - Entity Type.
	 * @return
	 * @throws Exception
	 */
	public String getInsertQuery(final Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		String isql = getCachedQuery(EnumQueryType.INSERT, type);
		if (isql != null)
			return isql;

		// Get table name
		EntityDef enref = EntityModelLoader.get().getEntityDef(type);
		String table = enref.getName();

		StringBuffer query = new StringBuffer();
		query.append("insert into ").append(table).append(" ( ");
		StringBuffer values = new StringBuffer();
		values.append(" values (");

		// Get Columns
		boolean first = true;

		for (AttributeDef attr : enref.getAttributes()) {
			if (attr == null) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				query.append(',');
				values.append(',');
			}
			query.append(attr.getName());
			values.append('?');

		}
		query.append(" ) ");
		values.append(" ) ");

		if (values != null) {
			query.append(values);
		}
		addToCache(EnumQueryType.INSERT, type, query.toString());

		return query.toString();
	}

	/**
	 * Get the update Query(s) for the entity and all referenced entities.
	 * 
	 * @param type
	 *            - Entity Type.
	 * @return
	 * @throws Exception
	 */
	public String getUpdateQuery(final Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		String isql = getCachedQuery(EnumQueryType.UPDATE, type);
		if (isql != null)
			return isql;

		// Get table name
		EntityDef enref = EntityModelLoader.get().getEntityDef(type);
		String table = enref.getName();

		StringBuffer query = new StringBuffer();
		query.append("update ").append(table).append(" set ");
		StringBuffer where = null;

		// Get Columns
		boolean first = true;
		boolean wfirst = true;

		for (AttributeDef attr : enref.getAttributes()) {
			if (attr == null) {
				continue;
			}
			if (attr.isKey()
					|| attr.getName().compareTo(
							AbstractPersistedEntity._TX_TIMESTAMP_COLUMN_) == 0) {
				if (wfirst) {
					where = new StringBuffer();
					where.append(" where ");
					wfirst = false;
				} else {
					where.append(" and ");
				}
				where.append(attr.getName()).append("=?");
				if (attr.isKey()) {
					continue;
				}
			}

			if (first) {
				first = false;
			} else {
				query.append(',');
			}
			query.append(attr.getName()).append("=?");

		}
		if (where != null) {
			query.append(where);
		}

		addToCache(EnumQueryType.UPDATE, type, query.toString());

		return query.toString();
	}

	public String getDeleteQuery(final Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		String isql = getCachedQuery(EnumQueryType.DELETE, type);
		if (isql != null)
			return isql;

		// Get table name
		EntityDef enref = EntityModelLoader.get().getEntityDef(type);
		String table = enref.getName();

		StringBuffer query = new StringBuffer();
		query.append("delete from ").append(table);
		StringBuffer where = null;

		boolean wfirst = true;

		for (AttributeDef attr : enref.getAttributes()) {
			if (attr == null) {
				continue;
			}
			if (attr.isKey()) {
				if (wfirst) {
					where = new StringBuffer();
					where.append(" where ");
					wfirst = false;
				} else {
					where.append(" and ");
				}
				where.append(attr.getName()).append("=?");
				if (attr.isKey()) {
					continue;
				}
			}
		}
		if (where != null) {
			query.append(where);
		}

		addToCache(EnumQueryType.DELETE, type, query.toString());
		log.debug("[DELETE : " + query.toString() + "]");
		return query.toString();
	}

	/**
	 * Generate the CREATE TABLE script for an Entity type. This will also
	 * generate the DROP and PRIMARY KEY constraints (if applicable).
	 * 
	 * @param type
	 *            - Entity Type
	 * @return List of generated SQL statements.
	 * 
	 * @throws Exception
	 */
	public List<String> getCreateTableDDL(final Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		List<String> stmnts = new ArrayList<String>();
		List<String> columns = new ArrayList<String>();
		List<String> keycolumns = null;

		String table = null;

		// Get table name
		EntityDef enref = EntityModelLoader.get().getEntityDef(type);
		table = enref.getName();

		// Drop table statement
		stmnts.add("drop table if exists " + table + " cascade");

		// Get Columns
		for (AttributeDef attr : enref.getAttributes()) {
			if (attr == null) {
				continue;
			}
			columns.add(getColumnDDL(attr));
			if (attr.isKey()) {
				if (keycolumns == null) {
					keycolumns = new ArrayList<String>();
				}
				keycolumns.add(attr.getName());
				if (attr.isAutoIncrement()) {
					if (EnumPrimitives.isPrimitiveType(attr.getField()
							.getType())) {
						EnumPrimitives prim = EnumPrimitives.type(attr
								.getField().getType());
						if (prim == EnumPrimitives.ELong
								|| prim == EnumPrimitives.EInteger) {
							Entity eann = type.getAnnotation(Entity.class);
							List<String> ddls = createSequenceDDL(eann, attr);
							if (ddls.size() > 0) {
								stmnts.addAll(ddls);
							}
						}
					}
				}
			}
		}

		// Create table statement
		StringBuffer buff = new StringBuffer();
		buff.append("create table ").append(table).append(" ( ");
		boolean first = true;
		for (String column : columns) {
			if (first) {
				first = false;
			} else {
				buff.append(",");
			}
			buff.append(column);
		}
		buff.append(")");

		stmnts.add(buff.toString());

		// Create primary key (if any)
		if (keycolumns != null && keycolumns.size() > 0) {
			buff = new StringBuffer();
			buff.append("alter table ").append(table)
					.append(" add primary key (");
			first = true;
			for (String column : keycolumns) {
				if (first) {
					first = false;
				} else {
					buff.append(",");
				}
				buff.append(column);
			}
			buff.append(")");
			stmnts.add(buff.toString());
		}
		return stmnts;
	}

	private List<String> createSequenceDDL(final Entity entity,
			final AttributeDef attr) throws Exception {
		List<String> ddls = new ArrayList<String>();
		String name = getSequenceName(entity, attr);
		ddls.add("drop sequence if exists " + name);
		ddls.add("create sequence if not exists " + name);
		return ddls;
	}

	public static String getSequenceName(final Entity entity,
			final AttributeDef attr) {
		StringBuffer buff = new StringBuffer();
		buff.append("SEQ_").append(entity.recordset()).append("_")
				.append(attr.getName());
		return buff.toString();
	}

	/**
	 * Generate a CREATE INDEX statement for the specified Entity.
	 * 
	 * @param type
	 *            - Entity Type.
	 * @param keycolumns
	 *            - Set of indexed columns.
	 * @return
	 * @throws Exception
	 */
	public List<String> getCreateIndexDDL(final Class<?> type,
			final List<KeyValuePair<String>> keycolumns) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		List<String> stmnts = new ArrayList<String>();
		String table = null;

		// Get table name
		EntityDef enref = EntityModelLoader.get().getEntityDef(type);
		table = enref.getName();

		for (KeyValuePair<String> keys : keycolumns) {
			String idxname = keys.getKey();
			if (idxname == null || idxname.isEmpty()) {
				continue;
			}
			String[] columns = keys.getValue().split(",");
			if (columns == null || columns.length <= 0) {
				continue;
			}

			String dropstmnt = "drop index if exists " + idxname;
			stmnts.add(dropstmnt);

			StringBuffer buff = new StringBuffer();
			buff.append("create index ").append(idxname).append(" on ")
					.append(table);
			buff.append(" ( ");
			boolean first = true;
			for (String column : columns) {
				if (first) {
					first = false;
				} else {
					buff.append(",");
				}
				String cname = column.trim();
				AttributeDef attr = enref.getAttribute(cname);
				if (attr == null)
					throw new Exception(
							"No column definition found for column [" + column
									+ "] for Entity ["
									+ type.getCanonicalName() + "]");
				buff.append(attr.getName());
			}
			buff.append(")");
			stmnts.add(buff.toString());
		}
		return stmnts;
	}

	private String getColumnDDL(final AttributeDef attr) throws Exception {
		Class<?> type = attr.getField().getType();
		AttributeDef tattr = attr;
		StringBuffer coldef = new StringBuffer();
		if (attr.getHandler() != null) {
			type = attr.getHandler().getDataType();
		} else if (attr.isRefrenceAttr()) {
			AttributeReferenceDef refd = (AttributeReferenceDef) attr.getType();
			tattr = refd.getReferenceAttribute();
			type = tattr.getType().getType();
		}

		EnumSqlTypes sqlt = SqlDataType.getSqlType(type);
		String def = sqlt.name();
		if (sqlt == EnumSqlTypes.VARCHAR2) {
			int size = 128;
			if (tattr.getSize() > 0) {
				size = tattr.getSize();
			}

			def = sqlt.name().concat("(").concat(String.valueOf(size))
					.concat(")");
		}
		coldef.append(attr.getName()).append(' ').append(def);
		if (attr.isKey()) {
			coldef.append(" not null");
		}
		return coldef.toString();
	}

	/**
	 * Post Selects further refine the result set based on conditions that
	 * aren't supported by SQL. (CONTAINS)
	 * 
	 * @param entities
	 * @return
	 * @throws Exception
	 */
	public List<AbstractEntity> postSelect(final List<AbstractEntity> entities)
			throws Exception {
		return entities;
	}
}
