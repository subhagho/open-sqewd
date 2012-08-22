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
package com.sqewd.open.dal.core.persistence.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumEntityState;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.core.persistence.query.SQLQuery;
import com.sqewd.open.dal.core.persistence.query.SimpleDbQuery;

/**
 * @author subhagho
 * 
 */
public abstract class AbstractDbPersister extends AbstractPersister {
	private static final Logger log = LoggerFactory
			.getLogger(AbstractDbPersister.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#state()
	 */
	public EnumInstanceState state() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.AbstractPersister#read(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public List<AbstractEntity> read(String query, Class<?> type, int limit)
			throws Exception {

		Connection conn = getConnection(true);
		try {
			return read(query, type, limit, conn);
		} finally {
			if (conn != null)
				releaseConnection(conn);
		}

	}

	private List<AbstractEntity> read(String query, Class<?> type, int limit,
			Connection conn) throws Exception {
		SQLQuery parser = new SQLQuery(type);

		// Make sure the type for the class is available.
		ReflectionUtils.get().getEntityMetadata(type);

		String selectsql = parser.parse(query, limit);
		Statement stmnt = conn.createStatement();
		try {
			log.debug("SELECT SQL [" + selectsql + "]");
			ResultSet rs = stmnt.executeQuery(selectsql);
			JoinGraph gr = JoinGraph.lookup(type);

			List<AbstractEntity> entities = new ArrayList<AbstractEntity>();
			while (rs.next()) {
				Object obj = type.newInstance();
				if (!(obj instanceof AbstractEntity))
					throw new Exception("Unsupported Entity type ["
							+ type.getCanonicalName() + "]");
				AbstractEntity entity = (AbstractEntity) obj;
				Stack<KeyValuePair<Class<?>>> path = new Stack<KeyValuePair<Class<?>>>();
				KeyValuePair<Class<?>> cls = new KeyValuePair<Class<?>>();
				cls.setValue(entity.getClass());
				path.push(cls);
				setEntity(entity, rs, gr, path);
				entities.add(entity);
			}
			return entities;
		} finally {
			if (stmnt != null && !stmnt.isClosed())
				stmnt.close();
		}
	}

	private void setEntity(AbstractEntity entity, ResultSet rs, JoinGraph gr,
			Stack<KeyValuePair<Class<?>>> path) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				entity.getClass());

		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr == null)
				continue;
			setColumnValue(rs, attr, entity, gr, path);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setColumnValue(ResultSet rs, StructAttributeReflect attr,
			AbstractEntity entity, JoinGraph gr,
			Stack<KeyValuePair<Class<?>>> path) throws Exception {

		KeyValuePair<String> alias = gr.getAliasFor(path, attr.Column, 0);
		String tabprefix = alias.getKey();

		if (EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
			EnumPrimitives prim = EnumPrimitives.type(attr.Field.getType());
			switch (prim) {
			case ECharacter:
				String sv = rs.getString(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), sv.charAt(0));
				}
				break;
			case EShort:
				short shv = rs.getShort(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), shv);
				}
				break;
			case EInteger:
				int iv = rs.getInt(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), iv);
				}
				break;
			case ELong:
				long lv = rs.getLong(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), lv);
				}
				break;
			case EFloat:
				float fv = rs.getFloat(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), fv);
				}
				break;
			case EDouble:
				double dv = rs.getDouble(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), dv);
				}
				break;
			default:
				throw new Exception("Unsupported Data type [" + prim.name()
						+ "]");
			}
		} else if (attr.Convertor != null) {
			String value = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				attr.Convertor.load(entity, attr.Column, value);
			}
		} else if (attr.Field.getType().equals(String.class)) {
			String value = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
						value);
			}
		} else if (attr.Field.getType().equals(Date.class)) {
			long value = rs.getLong(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				Date dt = new Date(value);
				PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
						dt);
			}
		} else if (attr.Field.getType().isEnum()) {
			String value = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				Class ecls = attr.Field.getType();
				Object evalue = Enum.valueOf(ecls, value);
				PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
						evalue);
			}
		} else if (attr.Reference != null) {
			Class<?> rt = Class.forName(attr.Reference.Class);
			Object obj = rt.newInstance();
			if (!(obj instanceof AbstractEntity))
				throw new Exception("Unsupported Entity type ["
						+ rt.getCanonicalName() + "]");
			AbstractEntity rentity = (AbstractEntity) obj;
			path.peek().setKey(attr.Column);

			KeyValuePair<Class<?>> cls = new KeyValuePair<Class<?>>();
			cls.setValue(rentity.getClass());
			path.push(cls);
			setEntity(rentity, rs, gr, path);
			PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
					rentity);
			path.pop();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.AbstractPersister#save(com.wookler.core.
	 * persistence.AbstractEntity)
	 */
	@Override
	public int save(AbstractEntity record, boolean overwrite) throws Exception {
		Connection conn = getConnection(true);
		try {
			return save(record, conn, overwrite);

		} finally {
			if (conn != null)
				releaseConnection(conn);
		}
	}

	public boolean recordExists(AbstractEntity entity) throws Exception {
		String query = getQueryByKey(entity);
		if (query != null && !query.isEmpty()) {
			List<AbstractEntity> exists = read(query, entity.getClass(), 1);
			if (exists == null || exists.size() == 0)
				return false;
			else {
				if (entity.getState() == EnumEntityState.Overwrite) {
					AbstractEntity en = exists.get(0);
					if (en instanceof AbstractPersistedEntity) {
						AbstractPersistedEntity ape = (AbstractPersistedEntity) en;
						AbstractPersistedEntity spe = (AbstractPersistedEntity) entity;
						spe.setTimestamp(ape.getTimestamp());
					}
				}
				return true;
			}
		}
		return false;
	}

	private String getQueryByKey(AbstractEntity entity) throws Exception {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				entity.getClass());
		for (String key : enref.FieldMaps.keySet()) {
			StructAttributeReflect attr = enref.get(key);
			if (attr == null || !attr.IsKeyColumn)
				continue;

			if (first)
				first = false;
			else
				buff.append(';');

			String value = null;
			StringBuffer column = new StringBuffer();
			column.append(attr.Column);

			if (attr.Reference == null) {
				if (attr.Field.getType().equals(Date.class)) {
					Date dt = (Date) PropertyUtils.getSimpleProperty(entity,
							attr.Field.getName());
					value = String.valueOf(dt.getTime());
				} else {
					value = String.valueOf(PropertyUtils.getSimpleProperty(
							entity, attr.Field.getName()));
					if (!EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
						value = "'" + value + "'";
					}
				}
			} else {
				Object dvalue = PropertyUtils.getSimpleProperty(entity,
						attr.Field.getName());
				StructAttributeReflect rattr = ReflectionUtils.get()
						.getAttribute(Class.forName(attr.Reference.Class),
								attr.Reference.Field);
				column.append('.').append(attr.Reference.Field);
				value = String.valueOf(PropertyUtils.getSimpleProperty(dvalue,
						rattr.Field.getName()));
				if (!EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
					value = "'" + value + "'";
				}
			}
			buff.append(enref.Entity).append('.').append(column.toString())
					.append("=").append(value);
		}
		return buff.toString();
	}

	private int save(AbstractEntity record, Connection conn, boolean overwrite)
			throws Exception {
		if (record == null)
			throw new Exception("Invalid entity record : Null record");

		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				record.getClass());

		if (enref.IsView)
			throw new Exception("Entity ["
					+ record.getClass().getCanonicalName()
					+ "] is defined as a View and cannot me modified.");

		if (record.getState() == EnumEntityState.New)
			return insert(record, conn);
		else if (record.getState() == EnumEntityState.Deleted)
			return delete(record, conn);
		else if (record.getState() == EnumEntityState.Loaded)
			return update(record, conn);
		else {
			if (recordExists(record)) {
				if (overwrite)
					return update(record, conn);
				else
					return -1;
			} else
				return insert(record, conn);
		}
	}

	private int insert(AbstractEntity record, Connection conn) throws Exception {
		Class<?> type = record.getClass();

		SimpleDbQuery parser = new SimpleDbQuery();

		String sql = parser.getInsertQuery(type);
		PreparedStatement pstmnt = conn.prepareStatement(sql);

		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);

		int index = 1;
		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr == null)
				continue;

			Object value = PropertyUtils.getSimpleProperty(record,
					attr.Field.getName());
			if (value == null) {
				if (attr.IsKeyColumn && attr.AutoIncrement) {
					Entity entity = record.getClass().getAnnotation(
							Entity.class);
					value = getSequenceValue(entity, attr, conn);
				}
			}
			if (attr.Reference != null) {
				boolean overwrite = false;
				if (attr.Reference.CascadeUpdate) {
					overwrite = true;
				}
				save((AbstractEntity) value, conn, overwrite);
				StructAttributeReflect rattr = ReflectionUtils.get()
						.getAttribute(value.getClass(), attr.Reference.Field);
				value = PropertyUtils.getProperty(value, rattr.Field.getName());
			} else if (attr.Column
					.compareTo(AbstractPersistedEntity._TX_TIMESTAMP_COLUMN_) == 0) {
				value = new Date();
			}
			setPreparedValue(pstmnt, index, attr, value, record);
			index++;
		}
		int count = pstmnt.executeUpdate();
		log.debug("[" + record.getClass().getCanonicalName()
				+ "] created [count=" + count + "]");
		return count;
	}

	protected abstract Object getSequenceValue(Entity entity,
			StructAttributeReflect attr, Connection conn) throws Exception;

	private void setPreparedValue(PreparedStatement pstmnt, int index,
			StructAttributeReflect attr, Object value, AbstractEntity entity)
			throws Exception {
		Class<?> type = attr.Field.getType();
		if (EnumPrimitives.isPrimitiveType(type)) {
			EnumPrimitives prim = EnumPrimitives.type(type);
			switch (prim) {
			case ECharacter:
				pstmnt.setString(index, String.valueOf(value));
				break;
			case EShort:
				pstmnt.setShort(index, (Short) value);
				break;
			case EInteger:
				pstmnt.setInt(index, (Integer) value);
				break;
			case ELong:
				pstmnt.setLong(index, (Long) value);
				break;
			case EFloat:
				pstmnt.setFloat(index, (Float) value);
				break;
			case EDouble:
				pstmnt.setDouble(index, (Double) value);
				break;
			default:
				throw new Exception("Unsupported Data type [" + prim.name()
						+ "]");
			}
		} else {
			if (type.equals(String.class)) {
				pstmnt.setString(index, (String) value);
			} else if (type.equals(Date.class)) {
				long dtval = new Date().getTime();
				if (value != null)
					dtval = ((Date) value).getTime();
				pstmnt.setLong(index, dtval);
			} else if (value instanceof Enum) {
				pstmnt.setString(index, getEnumValue(value));
			} else if (attr.Convertor != null) {
				pstmnt.setString(
						index,
						(String) attr.Convertor.save(entity,
								attr.Field.getName()));
			} else if (attr.Reference != null) {
				Class<?> cls = Class.forName(attr.Reference.Class);
				StructAttributeReflect rattr = ReflectionUtils.get()
						.getAttribute(cls, attr.Reference.Field);
				Object refval = PropertyUtils.getSimpleProperty(entity,
						attr.Field.getName());
				value = PropertyUtils.getSimpleProperty(refval,
						rattr.Field.getName());
				setPreparedValue(pstmnt, index, rattr, value, entity);
			} else {
				throw new Exception("Unsupported field type ["
						+ type.getCanonicalName() + "]");
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T extends Enum> String getEnumValue(Object value) {
		return ((T) value).name();
	}

	private int update(AbstractEntity record, Connection conn) throws Exception {
		Class<?> type = record.getClass();

		SimpleDbQuery parser = new SimpleDbQuery();

		String sql = parser.getUpdateQuery(type);

		PreparedStatement pstmnt = conn.prepareStatement(sql);

		List<StructAttributeReflect> keyattrs = new ArrayList<StructAttributeReflect>();

		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);

		int index = 1;
		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr == null)
				continue;

			if (attr.IsKeyColumn) {
				keyattrs.add(attr);
				continue;
			}

			Object value = PropertyUtils.getSimpleProperty(record,
					attr.Field.getName());
			if (attr.Reference != null && attr.Reference.CascadeUpdate) {
				save((AbstractEntity) value, conn, true);
				StructAttributeReflect rattr = ReflectionUtils.get()
						.getAttribute(value.getClass(), attr.Reference.Field);
				value = PropertyUtils.getProperty(value, rattr.Field.getName());
			} else if (attr.Column
					.compareTo(AbstractPersistedEntity._TX_TIMESTAMP_COLUMN_) == 0) {
				value = new Date();
				keyattrs.add(attr);
			}
			setPreparedValue(pstmnt, index, attr, value, record);
			index++;
		}
		for (int ii = 0; ii < keyattrs.size(); ii++) {
			Object value = PropertyUtils.getSimpleProperty(record,
					keyattrs.get(ii).Field.getName());
			setPreparedValue(pstmnt, (index + ii), keyattrs.get(ii), value,
					record);
		}

		int count = pstmnt.executeUpdate();
		log.debug("[" + record.getClass().getCanonicalName()
				+ "] updated [count=" + count + "]");
		return count;
	}

	protected boolean checkSchema() throws Exception {
		Connection conn = getConnection(true);
		boolean found = false;
		try {
			DatabaseMetaData dbm = conn.getMetaData();
			Entity entity = DBVersion.class.getAnnotation(Entity.class);
			String table = entity.recordset();

			ResultSet rs = dbm.getTables(null, null, table,
					new String[] { "TABLE" });
			while (rs.next()) {
				found = true;
				break;
			}
			rs.close();

		} finally {
			if (conn != null)
				releaseConnection(conn);
		}
		return found;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.AbstractPersister#save(java.util.List)
	 */
	@Override
	public int save(List<AbstractEntity> records, boolean overwrite)
			throws Exception {

		Connection conn = getConnection(true);
		int count = 0;
		try {
			for (AbstractEntity record : records) {
				if (!record.getClass().isAnnotationPresent(Entity.class))
					throw new Exception("Class ["
							+ record.getClass().getCanonicalName()
							+ "] has not been annotated as an Entity.");

				count += save(record, conn, overwrite);
			}
			return count;
		} finally {
			if (conn != null)
				releaseConnection(conn);
		}

	}

	private int delete(AbstractEntity record, Connection conn) throws Exception {
		Class<?> type = record.getClass();

		SimpleDbQuery parser = new SimpleDbQuery();

		String sql = parser.getDeleteQuery(type);

		PreparedStatement pstmnt = conn.prepareStatement(sql);

		List<StructAttributeReflect> keyattrs = new ArrayList<StructAttributeReflect>();

		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);

		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr == null)
				continue;
			if (attr.IsKeyColumn) {
				keyattrs.add(attr);
			}
		}
		for (int ii = 0; ii < keyattrs.size(); ii++) {
			Object value = PropertyUtils.getSimpleProperty(record,
					keyattrs.get(ii).Field.getName());
			setPreparedValue(pstmnt, ii + 1, keyattrs.get(ii), value, record);
		}

		int count = pstmnt.executeUpdate();
		log.debug("[" + record.getClass().getCanonicalName()
				+ "] deleted [count=" + count + "]");
		return count;
	}

	/**
	 * Get a handle to the DB Connection.
	 * 
	 * @param blocking
	 *            - Request connection in blocking mode.
	 * @return
	 * @throws Exception
	 */
	protected abstract Connection getConnection(boolean blocking)
			throws Exception;

	/**
	 * Release the connection back to the Queue.
	 * 
	 * @param conn
	 */
	protected abstract void releaseConnection(Connection conn);

}
