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
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.Statistics;
import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumEntityState;
import com.sqewd.open.dal.api.persistence.EnumPersistenceOperation;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.OperationResponse;
import com.sqewd.open.dal.api.persistence.PersistenceResponse;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.AbstractParam;
import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.api.utils.ListParam;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.api.utils.ValueParam;
import com.sqewd.open.dal.core.persistence.query.SQLQuery;
import com.sqewd.open.dal.core.persistence.query.sql.SimpleDbQuery;

/**
 * @author subhagho
 * 
 */
public abstract class AbstractDbPersister extends AbstractPersister {
	private static final Logger log = LoggerFactory
			.getLogger(AbstractDbPersister.class);

	public static final String _PARAM_MAXPOOL_SIZE_ = "maxpoolsize";

	public static final String _PARAM_MINPOOL_SIZE_ = "minpoolsize";

	public static final String _PARAM_PARTITIONS_ = "poolpartitions";

	public static final String _PARAM_CONN_URL_ = "url";

	public static final String _PARAM_CONN_USER_ = "user";

	public static final String _PARAM_CONN_PASSWD_ = "password";

	public static final String _PARAM_DBCONFIG_ = "setup";

	protected int partitionsize = 2;

	protected int maxcpoolsize = 10;

	protected int mincpoolsize = maxcpoolsize / 4;

	protected String connurl = null;

	protected String username = null;

	protected String password = null;

	protected BoneCP cpool = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#state()
	 */
	public EnumInstanceState state() {
		return state;
	}

	protected Connection getConnection(final boolean blocking) throws Exception {
		if (state != EnumInstanceState.Running)
			throw new Exception(
					"Db Persister is not running. Either it has been disposed or errored out. Check log file for details.");
		if (log.isDebugEnabled()) {
			Statistics stats = new Statistics(cpool);
			log.debug("Tot Conn Created:   "
					+ stats.getTotalCreatedConnections());
			log.debug("Tot Free Conn:      " + stats.getTotalFree());
			log.debug("Tot Leased Conn:    " + stats.getTotalLeased());
		}
		return cpool.getConnection();
	}

	protected void releaseConnection(final Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			log.error(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.AbstractPersister#init(com.wookler.utils
	 * .ListParam)
	 */
	@Override
	public void init(final ListParam params) throws Exception {
		try {
			AbstractParam pkey = params.get(_PARAM_KEY_);
			if (pkey == null)
				throw new Exception(
						"Invalid Configuration : Missing paramter ["
								+ _PARAM_KEY_ + "]");
			if (!(pkey instanceof ValueParam))
				throw new Exception(
						"Invalid Configuration : Invalid Parameter type for ["
								+ _PARAM_KEY_ + "]");
			key = ((ValueParam) pkey).getValue();
			if (key == null || key.isEmpty())
				throw new Exception("Invalid Configuration : Param ["
						+ _PARAM_KEY_ + "] is NULL or empty.");

			AbstractParam param = params.get(_PARAM_MAXPOOL_SIZE_);
			if (param != null) {
				if (param instanceof ValueParam) {
					String ps = ((ValueParam) param).getValue();
					maxcpoolsize = Integer.parseInt(ps);
					mincpoolsize = maxcpoolsize / 4;
				}
			}

			param = params.get(_PARAM_MINPOOL_SIZE_);
			if (param != null) {
				if (param instanceof ValueParam) {
					String ps = ((ValueParam) param).getValue();
					mincpoolsize = Integer.parseInt(ps);
				}
			}

			param = params.get(_PARAM_PARTITIONS_);
			if (param != null) {
				if (param instanceof ValueParam) {
					String ps = ((ValueParam) param).getValue();
					partitionsize = Integer.parseInt(ps);
				}
			}

			param = params.get(_PARAM_CONN_URL_);
			if (param == null)
				throw new Exception(
						"Invalid Configuration : Missing parameter ["
								+ _PARAM_CONN_URL_ + "]");
			if (!(param instanceof ValueParam))
				throw new Exception(
						"Invalid Configuration : Invalid parameter type ["
								+ _PARAM_CONN_URL_ + "]");
			connurl = ((ValueParam) param).getValue();

			param = params.get(_PARAM_CONN_USER_);
			if (param == null)
				throw new Exception(
						"Invalid Configuration : Missing parameter ["
								+ _PARAM_CONN_USER_ + "]");
			if (!(param instanceof ValueParam))
				throw new Exception(
						"Invalid Configuration : Invalid parameter type ["
								+ _PARAM_CONN_USER_ + "]");

			username = ((ValueParam) param).getValue();

			param = params.get(_PARAM_CONN_PASSWD_);
			if (param == null)
				throw new Exception(
						"Invalid Configuration : Missing parameter ["
								+ _PARAM_CONN_PASSWD_ + "]");
			if (!(param instanceof ValueParam))
				throw new Exception(
						"Invalid Configuration : Invalid parameter type ["
								+ _PARAM_CONN_PASSWD_ + "]");

			password = ((ValueParam) param).getValue();

			setupConnectionPool();

			state = EnumInstanceState.Running;

		} catch (Exception e) {
			state = EnumInstanceState.Exception;
			throw e;
		}
	}

	private void setupConnectionPool() throws Exception {
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connurl);
		config.setUsername(username);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(mincpoolsize);
		config.setMaxConnectionsPerPartition(maxcpoolsize);
		config.setPartitionCount(partitionsize);

		cpool = new BoneCP(config);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#dispose()
	 */
	public void dispose() {
		try {
			cpool.shutdown();
			state = EnumInstanceState.Closed;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			LogUtils.stacktrace(log, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.AbstractPersister#read(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public List<AbstractEntity> read(final String query, final Class<?> type,
			final int limit) throws Exception {

		Connection conn = getConnection(true);
		try {
			return read(query, type, limit, conn);
		} finally {
			if (conn != null) {
				releaseConnection(conn);
			}
		}

	}

	private List<AbstractEntity> read(final String query, final Class<?> type,
			final int limit, final Connection conn) throws Exception {
		// Make sure the type for the class is available.
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		boolean joinedList = AbstractJoinGraph.hasJoinedList(enref);

		SQLQuery parser = new SQLQuery(type);

		String selectsql = parser.parse(query, limit);
		Statement stmnt = conn.createStatement();
		List<AbstractEntity> entities = new ArrayList<AbstractEntity>();
		HashMap<String, AbstractEntity> refindx = null;

		try {
			log.debug("SELECT SQL [" + selectsql + "]");
			ResultSet rs = stmnt.executeQuery(selectsql);
			try {
				if (joinedList) {
					refindx = new HashMap<String, AbstractEntity>();
				}

				while (rs.next()) {

					if (!joinedList) {
						AbstractJoinGraph gr = AbstractJoinGraph.lookup(type);

						Object obj = type.newInstance();
						if (!(obj instanceof AbstractEntity))
							throw new Exception("Unsupported Entity type ["
									+ type.getCanonicalName() + "]");
						AbstractEntity entity = (AbstractEntity) obj;
						Stack<KeyValuePair<Class<?>>> path = new Stack<KeyValuePair<Class<?>>>();
						KeyValuePair<Class<?>> cls = new KeyValuePair<Class<?>>();
						cls.setValue(entity.getClass());
						path.push(cls);
						EntityHelper.setEntity(entity, rs, gr, path);
						entities.add(entity);
					} else {
						EntityHelper.setEntity(enref, refindx, rs);
					}
				}
			} finally {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			}
			if (joinedList) {
				for (String key : refindx.keySet()) {
					entities.add(refindx.get(key));
				}
			}
			return entities;
		} finally {
			if (stmnt != null && !stmnt.isClosed()) {
				stmnt.close();
			}
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
	public OperationResponse save(final AbstractEntity record,
			final boolean overwrite) throws Exception {
		Connection conn = getConnection(true);
		try {
			return save(record, conn, overwrite);

		} finally {
			if (conn != null) {
				releaseConnection(conn);
			}
		}
	}

	public boolean recordExists(final AbstractEntity entity) throws Exception {
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

	private String getEntityKey(final AbstractEntity entity) throws Exception {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				entity.getClass());
		for (String key : enref.FieldMaps.keySet()) {
			StructAttributeReflect attr = enref.get(key);
			if (attr == null || !attr.IsKeyColumn) {
				continue;
			}

			if (first) {
				first = false;
			} else {
				buff.append(':');
			}

			String value = null;

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
				value = String.valueOf(PropertyUtils.getSimpleProperty(dvalue,
						rattr.Field.getName()));
				if (!EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
					value = "'" + value + "'";
				}
			}
			buff.append(value);
		}
		return buff.toString();
	}

	private String getQueryByKey(final AbstractEntity entity) throws Exception {
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				entity.getClass());
		for (String key : enref.FieldMaps.keySet()) {
			StructAttributeReflect attr = enref.get(key);
			if (attr == null || !attr.IsKeyColumn) {
				continue;
			}

			if (first) {
				first = false;
			} else {
				buff.append(';');
			}

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

	private OperationResponse save(final AbstractEntity record,
			final Connection conn, final boolean overwrite) throws Exception {
		if (record == null)
			throw new Exception("Invalid entity record : Null record");

		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				record.getClass());

		if (enref.IsView)
			throw new Exception("Entity ["
					+ record.getClass().getCanonicalName()
					+ "] is defined as a View and cannot me modified.");

		OperationResponse response = new OperationResponse();

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
				else {
					response.setEntity(enref.Entity);
					response.setKey(getEntityKey(record));
					response.setOperation(EnumPersistenceOperation.Ignored);
				}
			} else
				return insert(record, conn);
		}
		conn.commit();
		return response;
	}

	private OperationResponse insert(final AbstractEntity record,
			final Connection conn) throws Exception {
		Class<?> type = record.getClass();
		OperationResponse response = new OperationResponse();

		SimpleDbQuery parser = new SimpleDbQuery();

		String sql = parser.getInsertQuery(type);
		PreparedStatement pstmnt = conn.prepareStatement(sql);
		try {
			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(type);
			response.setEntity(enref.Entity);
			response.setKey(getEntityKey(record));

			int index = 1;
			for (StructAttributeReflect attr : enref.Attributes) {
				if (attr == null) {
					continue;
				}

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
							.getAttribute(value.getClass(),
									attr.Reference.Field);
					value = PropertyUtils.getProperty(value,
							rattr.Field.getName());
				} else if (attr.Column
						.compareTo(AbstractPersistedEntity._TX_TIMESTAMP_COLUMN_) == 0) {
					value = new Date();
				}
				setPreparedValue(pstmnt, index, attr, value, record);
				index++;
			}
			int count = pstmnt.executeUpdate();
			if (count > 0) {
				response.setOperation(EnumPersistenceOperation.Inserted);
			} else {
				response.setOperation(EnumPersistenceOperation.Ignored);
			}
			log.debug("[" + record.getClass().getCanonicalName()
					+ "] created [count=" + count + "]");
			return response;
		} finally {
			if (pstmnt != null && !pstmnt.isClosed()) {
				pstmnt.close();
			}
		}
	}

	protected abstract Object getSequenceValue(Entity entity,
			StructAttributeReflect attr, Connection conn) throws Exception;

	private void setPreparedValue(final PreparedStatement pstmnt,
			final int index, final StructAttributeReflect attr, Object value,
			final AbstractEntity entity) throws Exception {
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
				if (value != null) {
					dtval = ((Date) value).getTime();
				}
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
			} else
				throw new Exception("Unsupported field type ["
						+ type.getCanonicalName() + "]");
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T extends Enum> String getEnumValue(final Object value) {
		return ((T) value).name();
	}

	private OperationResponse update(final AbstractEntity record,
			final Connection conn) throws Exception {
		Class<?> type = record.getClass();
		OperationResponse response = new OperationResponse();

		SimpleDbQuery parser = new SimpleDbQuery();

		String sql = parser.getUpdateQuery(type);

		PreparedStatement pstmnt = conn.prepareStatement(sql);

		try {
			List<StructAttributeReflect> keyattrs = new ArrayList<StructAttributeReflect>();

			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(type);

			response.setEntity(enref.Entity);
			response.setKey(getEntityKey(record));

			int index = 1;
			for (StructAttributeReflect attr : enref.Attributes) {
				if (attr == null) {
					continue;
				}

				if (attr.IsKeyColumn) {
					keyattrs.add(attr);
					continue;
				}

				Object value = PropertyUtils.getSimpleProperty(record,
						attr.Field.getName());
				if (attr.Reference != null && attr.Reference.CascadeUpdate) {
					save((AbstractEntity) value, conn, true);
					StructAttributeReflect rattr = ReflectionUtils.get()
							.getAttribute(value.getClass(),
									attr.Reference.Field);
					value = PropertyUtils.getProperty(value,
							rattr.Field.getName());
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
			if (count > 0) {
				response.setOperation(EnumPersistenceOperation.Updated);
			} else {
				response.setOperation(EnumPersistenceOperation.Ignored);
			}
			log.debug("[" + record.getClass().getCanonicalName()
					+ "] updated [count=" + count + "]");
			return response;
		} finally {
			if (pstmnt != null && !pstmnt.isClosed()) {
				pstmnt.close();
			}
		}
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
			if (conn != null) {
				releaseConnection(conn);
			}
		}
		return found;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.AbstractPersister#save(java.util.List)
	 */
	@Override
	public PersistenceResponse save(final List<AbstractEntity> records,
			final boolean overwrite) throws Exception {

		PersistenceResponse response = new PersistenceResponse();
		Connection conn = getConnection(true);
		try {
			for (AbstractEntity record : records) {
				if (!record.getClass().isAnnotationPresent(Entity.class))
					throw new Exception("Class ["
							+ record.getClass().getCanonicalName()
							+ "] has not been annotated as an Entity.");

				OperationResponse or = save(record, conn, overwrite);
				response.add(or);
			}
			return response;
		} finally {
			if (conn != null) {
				releaseConnection(conn);
			}
		}

	}

	private OperationResponse delete(final AbstractEntity record,
			final Connection conn) throws Exception {
		OperationResponse response = new OperationResponse();
		Class<?> type = record.getClass();

		SimpleDbQuery parser = new SimpleDbQuery();

		String sql = parser.getDeleteQuery(type);

		PreparedStatement pstmnt = conn.prepareStatement(sql);

		try {
			List<StructAttributeReflect> keyattrs = new ArrayList<StructAttributeReflect>();

			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(type);
			response.setEntity(enref.Entity);
			response.setKey(getEntityKey(record));

			for (StructAttributeReflect attr : enref.Attributes) {
				if (attr == null) {
					continue;
				}
				if (attr.IsKeyColumn) {
					keyattrs.add(attr);
				}
			}
			for (int ii = 0; ii < keyattrs.size(); ii++) {
				Object value = PropertyUtils.getSimpleProperty(record,
						keyattrs.get(ii).Field.getName());
				setPreparedValue(pstmnt, ii + 1, keyattrs.get(ii), value,
						record);
			}

			int count = pstmnt.executeUpdate();
			if (count > 0) {
				response.setOperation(EnumPersistenceOperation.Deleted);
			} else {
				response.setOperation(EnumPersistenceOperation.Ignored);
			}
			log.debug("[" + record.getClass().getCanonicalName()
					+ "] deleted [count=" + count + "]");
			return response;
		} finally {
			if (pstmnt != null && !pstmnt.isClosed()) {
				pstmnt.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#select(java.lang
	 * .String, java.lang.Class, int)
	 */
	@Override
	public ResultSet select(final String query,
			final List<KeyValuePair<Class<?>>> types, final int limit)
			throws Exception {
		Connection conn = getConnection(true);
		try {
			return select(query, types, limit, conn);
		} finally {
			if (conn != null) {
				releaseConnection(conn);
			}
		}
	}

	private ResultSet select(final String query,
			final List<KeyValuePair<Class<?>>> types, final int limit,
			final Connection conn) throws Exception {

		NativeJoinGraph jg = new NativeJoinGraph(types, query);

		// Make sure the type for the class is available.
		SQLQuery parser = new SQLQuery(jg);

		String selectsql = parser.parse("", limit);
		log.debug("SELECT SQL [" + selectsql + "]");
		Statement stmnt = conn.createStatement();
		LocalResultSet entities = new LocalResultSet();

		try {
			log.debug("SELECT SQL [" + selectsql + "]");
			ResultSet rs = stmnt.executeQuery(selectsql);
			try {
				entities.create(key, rs);
			} finally {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			}
			return entities;
		} finally {
			if (stmnt != null && !stmnt.isClosed()) {
				stmnt.close();
			}
		}
	}
}
