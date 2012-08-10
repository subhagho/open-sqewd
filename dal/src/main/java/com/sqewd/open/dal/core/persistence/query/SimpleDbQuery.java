/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.core.persistence.db.SQLDataType;
import com.sqewd.open.dal.core.persistence.db.SqlConditionTransformer;

/**
 * Class encapsulates the Query definition for a JDBC compliant Database.
 * 
 * @author subhagho
 * 
 */
public class SimpleDbQuery extends SimpleFilterQuery {
	public static enum EnumQueryType {
		SELECT, INSERT, UPDATE, DELETE;
	}

	private static final Logger log = LoggerFactory
			.getLogger(SimpleDbQuery.class);

	private ConditionTransformer transformer = null;

	private List<FilterCondition> joinconditions = null;

	private List<FilterCondition> postconditions = null;

	private static HashMap<String, HashMap<String, String>> queryCache = new HashMap<String, HashMap<String, String>>();

	public SimpleDbQuery() {
		transformer = new SqlConditionTransformer();
		parser = new FilterConditionParser();
	}

	private synchronized void addToCache(EnumQueryType type, Class<?> cls,
			String sql) {
		if (!queryCache.containsKey(type.name())) {
			queryCache.put(type.name(), new HashMap<String, String>());
		}
		HashMap<String, String> cache = queryCache.get(type.name());
		if (cache.containsKey(cls.getName()))
			cache.remove(cls.getName());
		cache.put(cls.getName(), sql);
	}

	private String getCachedQuery(EnumQueryType type, Class<?> cls) {
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
	public String getInsertQuery(Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		String isql = getCachedQuery(EnumQueryType.INSERT, type);
		if (isql != null)
			return isql;

		// Get table name
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		String table = enref.Entity;

		StringBuffer query = new StringBuffer();
		query.append("insert into ").append(table).append(" ( ");
		StringBuffer values = new StringBuffer();
		values.append(" values (");

		// Get Columns
		List<Field> fields = enref.Fields;

		boolean first = true;

		for (Field field : fields) {
			StructAttributeReflect attr = enref.get(field.getName());
			if (attr == null)
				continue;
			if (first)
				first = false;
			else {
				query.append(',');
				values.append(',');
			}
			query.append(attr.Column);
			values.append('?');

		}
		query.append(" ) ");
		values.append(" ) ");

		if (values != null)
			query.append(values);
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
	public String getUpdateQuery(Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		String isql = getCachedQuery(EnumQueryType.UPDATE, type);
		if (isql != null)
			return isql;

		// Get table name
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		String table = enref.Entity;

		StringBuffer query = new StringBuffer();
		query.append("update ").append(table).append(" set ");
		StringBuffer where = null;

		// Get Columns
		List<Field> fields = enref.Fields;

		boolean first = true;
		boolean wfirst = true;

		for (Field field : fields) {
			StructAttributeReflect attr = enref.get(field.getName());
			if (attr == null)
				continue;
			if (attr.IsKeyColumn
					|| attr.Column
							.compareTo(AbstractEntity._TX_TIMESTAMP_COLUMN_) == 0) {
				if (wfirst) {
					where = new StringBuffer();
					where.append(" where ");
					wfirst = false;
				} else
					where.append(" and ");
				where.append(attr.Column).append("=?");
				if (attr.IsKeyColumn)
					continue;
			}

			if (first)
				first = false;
			else
				query.append(',');
			query.append(attr.Column).append("=?");

		}
		if (where != null)
			query.append(where);

		addToCache(EnumQueryType.UPDATE, type, query.toString());

		return query.toString();
	}

	public String getDeleteQuery(Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		String isql = getCachedQuery(EnumQueryType.DELETE, type);
		if (isql != null)
			return isql;

		// Get table name
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		String table = enref.Entity;

		StringBuffer query = new StringBuffer();
		query.append("delete from ").append(table);
		StringBuffer where = null;

		// Get Columns
		List<Field> fields = enref.Fields;

		boolean wfirst = true;

		for (Field field : fields) {
			StructAttributeReflect attr = enref.get(field.getName());
			if (attr == null)
				continue;
			if (attr.IsKeyColumn) {
				if (wfirst) {
					where = new StringBuffer();
					where.append(" where ");
					wfirst = false;
				} else
					where.append(" and ");
				where.append(attr.Column).append("=?");
				if (attr.IsKeyColumn)
					continue;
			}
		}
		if (where != null)
			query.append(where);

		addToCache(EnumQueryType.DELETE, type, query.toString());
		log.debug("[DELETE : " + query.toString() + "]");
		return query.toString();
	}

	/**
	 * Parse the filter condition and generate a SQL Select Query.
	 * 
	 * @param type
	 *            - Entity Type
	 * @return
	 * @throws Exception
	 */
	public String getSelectQuery(Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		HashMap<String, String> tables = new HashMap<String, String>();
		int limit = parser.getLimit();
		StringBuffer sort = new StringBuffer();
		StringBuffer where = new StringBuffer();

		/*
		 * String columnstr = getCachedQuery(EnumQueryType.SELECT, type); if
		 * (columnstr == null) {
		 */
		List<String> columns = new ArrayList<String>();

		getColumns(type, tables, columns);

		boolean first = true;
		StringBuffer cbuff = new StringBuffer();
		for (String column : columns) {
			if (first)
				first = false;
			else
				cbuff.append(',');
			cbuff.append(' ').append(column);
		}
		String columnstr = cbuff.toString();
		/*
		 * addToCache(EnumQueryType.SELECT, type, columnstr); }
		 */

		// Get Where Clause
		first = true;
		if (conditions != null && conditions.size() > 0) {
			for (AbstractCondition condition : conditions) {
				if (condition instanceof FilterCondition) {
					FilterCondition fc = (FilterCondition) condition;
					if (fc.getComparator() == EnumOperator.Contains) {
						if (postconditions == null)
							postconditions = new ArrayList<FilterCondition>();
						postconditions.add(fc);
					}
					String strcond = transformer.transform(condition);
					if (first)
						first = false;
					else
						where.append(" and ");
					where.append(strcond);
				}
			}
		}
		if (joinconditions != null && joinconditions.size() > 0) {
			for (AbstractCondition condition : joinconditions) {
				if (condition instanceof FilterCondition) {
					FilterCondition fc = (FilterCondition) condition;
					String strcond = transformer.transform(fc);
					if (first)
						first = false;
					else
						where.append(" and ");
					where.append(strcond);
				}
			}
		}
		// Get Sort
		if (parser.getSort() != null && parser.getSort().size() > 0) {
			first = true;
			for (SortColumn column : parser.getSort()) {
				if (first)
					first = false;
				else {
					sort.append(",");
				}
				sort.append(' ').append(column.getColumn());
				if (column.getOrder() == EnumSortOrder.ASC) {
					sort.append(" ASC");
				} else {
					sort.append(" DESC");
				}
			}
		}

		// Create query
		StringBuffer qbuff = new StringBuffer("select ");
		if (limit > 0)
			qbuff.append(" top ").append(limit);
		qbuff.append(columnstr);

		first = true;
		for (String table : tables.keySet()) {
			if (first) {
				first = false;
				qbuff.append(" from ");
			} else
				qbuff.append(",");
			qbuff.append(table).append(' ');
		}
		if (where.length() > 0) {
			qbuff.append(" where ").append(where);
		}
		if (sort.length() > 0) {
			qbuff.append(" order by ").append(sort);
		}
		return qbuff.toString();
	}

	private void getColumns(Class<?> type, HashMap<String, String> tables,
			List<String> columns) throws Exception {
		// Get table name
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		String table = enref.Entity;
		if (tables.containsKey(table))
			return;

		tables.put(table, table);

		// Get Columns
		List<Field> fields = enref.Fields;

		List<StructAttributeReflect> refs = null;

		for (Field field : fields) {
			StructAttributeReflect attr = enref.get(field.getName());
			if (attr == null)
				continue;
			if (attr.Reference != null) {
				if (refs == null)
					refs = new ArrayList<StructAttributeReflect>();
				refs.add(attr);
			} else
				columns.add(table.concat(".").concat(attr.Column));

		}
		if (refs != null && refs.size() > 0) {
			for (StructAttributeReflect ref : refs) {
				Class<?> rtype = Class.forName(ref.Reference.Class);
				StructEntityReflect renref = ReflectionUtils.get()
						.getEntityMetadata(rtype);
				String rtable = renref.Entity;
				FilterCondition jcond = new FilterCondition(type, ref.Column,
						EnumOperator.Equal, rtype, ref.Reference.Field);

				if (joinconditions == null)
					joinconditions = new ArrayList<FilterCondition>();

				joinconditions.add(jcond);
				if (!tables.containsKey(rtable))
					getColumns(rtype, tables, columns);
			}
		}
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
	public List<String> getCreateTableDDL(Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		List<String> stmnts = new ArrayList<String>();
		List<String> columns = new ArrayList<String>();
		List<String> keycolumns = null;

		String table = null;

		// Get table name
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		table = enref.Entity;

		// Drop table statement
		stmnts.add("drop table if exists " + table + " cascade");

		// Get Columns
		List<Field> fields = enref.Fields;
		for (Field field : fields) {
			StructAttributeReflect attr = enref.get(field.getName());
			if (attr == null)
				continue;
			columns.add(getColumnDDL(attr));
			if (attr.IsKeyColumn) {
				if (keycolumns == null)
					keycolumns = new ArrayList<String>();
				keycolumns.add(attr.Column);
				if (attr.AutoIncrement) {
					if (EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
						EnumPrimitives prim = EnumPrimitives.type(attr.Field
								.getType());
						if (prim == EnumPrimitives.ELong
								|| prim == EnumPrimitives.EInteger) {
							Entity eann = (Entity) type
									.getAnnotation(Entity.class);
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
			if (first)
				first = false;
			else
				buff.append(",");
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
				if (first)
					first = false;
				else
					buff.append(",");
				buff.append(column);
			}
			buff.append(")");
			stmnts.add(buff.toString());
		}
		return stmnts;
	}

	private List<String> createSequenceDDL(Entity entity,
			StructAttributeReflect attr) throws Exception {
		List<String> ddls = new ArrayList<String>();
		String name = getSequenceName(entity, attr);
		ddls.add("drop sequence if exists " + name);
		ddls.add("create sequence if not exists " + name);
		return ddls;
	}

	public static String getSequenceName(Entity entity,
			StructAttributeReflect attr) {
		StringBuffer buff = new StringBuffer();
		buff.append("SEQ_").append(entity.recordset()).append("_")
				.append(attr.Column);
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
	public List<String> getCreateIndexDDL(Class<?> type,
			List<KeyValuePair<String>> keycolumns) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		List<String> stmnts = new ArrayList<String>();
		String table = null;

		// Get table name
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		table = enref.Entity;

		for (KeyValuePair<String> keys : keycolumns) {
			String idxname = keys.getKey();
			if (idxname == null || idxname.isEmpty())
				continue;
			String[] columns = keys.getValue().split(",");
			if (columns == null || columns.length <= 0)
				continue;

			String dropstmnt = "drop index if exists " + idxname;
			stmnts.add(dropstmnt);

			StringBuffer buff = new StringBuffer();
			buff.append("create index ").append(idxname).append(" on ")
					.append(table);
			buff.append(" ( ");
			boolean first = true;
			for (String column : columns) {
				if (first)
					first = false;
				else
					buff.append(",");
				String cname = column.trim();
				StructAttributeReflect attr = enref.get(cname);
				if (attr == null)
					throw new Exception(
							"No column definition found for column [" + column
									+ "] for Entity ["
									+ type.getCanonicalName() + "]");
				buff.append(attr.Column);
			}
			buff.append(")");
			stmnts.add(buff.toString());
		}
		return stmnts;
	}

	private String getColumnDDL(StructAttributeReflect attr) throws Exception {
		Class<?> type = attr.Field.getType();
		StructAttributeReflect tattr = attr;
		StringBuffer coldef = new StringBuffer();
		if (attr.Convertor != null) {
			type = attr.Convertor.getDataType();
		} else if (attr.Reference != null) {
			Class<?> rtype = Class.forName(attr.Reference.Class);
			StructAttributeReflect rattr = ReflectionUtils.get().getAttribute(
					rtype, attr.Reference.Field);
			type = rattr.Field.getType();
			tattr = rattr;
		}

		SQLDataType sqlt = SQLDataType.type(type);
		String def = sqlt.name();
		if (sqlt == SQLDataType.VARCHAR2) {
			int size = 128;
			if (tattr.Size > 0)
				size = tattr.Size;

			def = sqlt.name().concat("(").concat(String.valueOf(size))
					.concat(")");
		}
		coldef.append(attr.Column).append(' ').append(def);
		if (attr.IsKeyColumn) {
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
	public List<AbstractEntity> postSelect(List<AbstractEntity> entities)
			throws Exception {
		if (postconditions != null && postconditions.size() > 0) {
			List<AbstractEntity> filtered = new ArrayList<AbstractEntity>();
			for (AbstractEntity entity : entities) {
				boolean select = true;
				for (FilterCondition condition : postconditions) {
					AbstractConditionPredicate acp = condition.getLeft();
					if (!(acp instanceof ColumnConditionPredicate))
						throw new Exception(
								"Invalid Filter Condition : Expecting type ["
										+ ColumnConditionPredicate.class
												.getCanonicalName() + "]");
					ColumnConditionPredicate ccp = (ColumnConditionPredicate) acp;

					acp = condition.getRight();
					if (!(acp instanceof ValueConditionPredicate))
						throw new Exception(
								"Invalid Filter Condition : Expecting type ["
										+ ValueConditionPredicate.class
												.getCanonicalName() + "]");
					ValueConditionPredicate vcp = (ValueConditionPredicate) acp;

					if (!matcher.match(entity, ccp.getColumn(),
							condition.getComparator(), vcp.getValue())) {
						select = false;
						break;
					}
				}
				if (select)
					filtered.add(entity);
			}
			return filtered;
		}
		return entities;
	}
}
