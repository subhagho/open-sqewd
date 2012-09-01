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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.utils.AbstractParam;
import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.api.utils.ListParam;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.api.utils.ValueParam;
import com.sqewd.open.dal.api.utils.XMLUtils;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.core.persistence.query.SimpleDbQuery;

/**
 * @author subhagho
 * 
 */
public class H2DbPersister extends AbstractDbPersister {
	private static final Logger log = LoggerFactory
			.getLogger(H2DbPersister.class);

	private static final long _DEFAULT_LOCK_TIMEOUT_ = 100;

	public static final String _PARAM_POOL_SIZE_ = "poolsize";

	public static final String _PARAM_CONN_URL_ = "url";

	public static final String _PARAM_CONN_USER_ = "user";

	public static final String _PARAM_CONN_PASSWD_ = "password";

	public static final String _PARAM_DBCONFIG_ = "setup";

	public static final String _CONFIG_SETUP_VERSION_ = "db[@version]";

	public static final String _CONFIG_SETUP_ENTITIES_ = "/h2/db/entities/entity";

	public static final String _CONFIG_SETUP_INDEXES_ = "./index";

	private int cpoolsize = 10;

	private int mincpoolsize = cpoolsize / 4;

	private String dbconfig = null;

	private String connurl = null;

	private String username = null;

	private String password = null;

	private Connection[] conns = null;

	private Queue<Connection> freeconns = new LinkedBlockingQueue<Connection>();

	private boolean checksetup = false;

	public H2DbPersister() {
		key = this.getClass().getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.db.AbstractDbPersister#getConnection()
	 */
	@Override
	protected Connection getConnection(boolean blocking) throws Exception {
		if (state != EnumInstanceState.Running)
			throw new Exception(
					"Db Persister is not running. Either it has been disposed or errored out. Check log file for details.");
		while (true) {
			synchronized (freeconns) {
				if (freeconns.size() > 0) {
					return freeconns.remove();
				}
			}
			if (blocking)
				Thread.sleep(_DEFAULT_LOCK_TIMEOUT_);
			else
				break;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.db.AbstractDbPersister#releaseConnection
	 * (java.sql.Connection)
	 */
	@Override
	protected void releaseConnection(Connection conn) {
		synchronized (freeconns) {
			freeconns.add(conn);
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
	public void init(ListParam params) throws Exception {
		try {
			AbstractParam pkey = params.get(_PARAM_KEY_);
			if (pkey == null)
				throw new Exception(
						"Invalid Configuration : Missing paramter ["
								+ _PARAM_KEY_ + "]");
			if (!(pkey instanceof ValueParam)) {
				throw new Exception(
						"Invalid Configuration : Invalid Parameter type for ["
								+ _PARAM_KEY_ + "]");
			}
			key = ((ValueParam) pkey).getValue();
			if (key == null || key.isEmpty())
				throw new Exception("Invalid Configuration : Param ["
						+ _PARAM_KEY_ + "] is NULL or empty.");

			Class.forName("org.h2.Driver");

			AbstractParam param = params.get(_PARAM_POOL_SIZE_);
			if (param != null) {
				if (param instanceof ValueParam) {
					String ps = ((ValueParam) param).getValue();
					cpoolsize = Integer.parseInt(ps);
					mincpoolsize = cpoolsize / 4;
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

			conns = new Connection[cpoolsize];
			for (int ii = 0; ii < cpoolsize; ii++) {
				conns[ii] = DriverManager.getConnection(connurl, username,
						password);
				conns[ii].setAutoCommit(true);
				freeconns.add(conns[ii]);
			}
			state = EnumInstanceState.Running;

			param = params.get(_PARAM_DBCONFIG_);
			if (param != null) {
				if (!(param instanceof ValueParam))
					throw new Exception(
							"Invalid Configuration : Invalid parameter type ["
									+ _PARAM_CONN_PASSWD_ + "]");
				dbconfig = ((ValueParam) param).getValue();
				checksetup = true;
			}

			log.info("Created connection pool [size=" + cpoolsize
					+ "], H2 Database [" + connurl + "]");
		} catch (Exception e) {
			state = EnumInstanceState.Exception;
			throw e;
		}
	}

	private void checkSetup() throws Exception {
		XMLConfiguration config = new XMLConfiguration(dbconfig);
		String version = config.getString(_CONFIG_SETUP_VERSION_);
		if (version == null || version.isEmpty())
			throw new Exception(
					"Invalid DB Setup Configuration : Missing parameter ["
							+ _CONFIG_SETUP_VERSION_ + "]");

		if (!checkSchema()) {
			SimpleDbQuery dbq = new SimpleDbQuery();
			List<String> createsql = dbq.getCreateTableDDL(DBVersion.class);
			Connection conn = getConnection(true);
			Statement stmnt = conn.createStatement();
			try {
				for (String sql : createsql) {
					log.debug("TABLE SQL [" + sql + "]");
					stmnt.execute(sql);
				}

				DBVersion dbv = (DBVersion) DataManager
						.newInstance(DBVersion.class);
				dbv.setVersion(version);
				save(dbv, false);

				NodeList nl = XMLUtils.search(_CONFIG_SETUP_ENTITIES_, config
						.getDocument().getDocumentElement());
				if (nl != null && nl.getLength() > 0) {
					for (int ii = 0; ii < nl.getLength(); ii++) {
						Element elm = (Element) nl.item(ii);
						String eclass = elm.getAttribute("class");
						if (eclass != null && !eclass.isEmpty()) {
							Class<?> cls = Class.forName(eclass);
							createsql = dbq.getCreateTableDDL(cls);
							for (String sql : createsql) {
								log.debug("TABLE SQL [" + sql + "]");
								stmnt.execute(sql);
							}
							createIndex(elm, cls, dbq, stmnt);
						}
					}
				}

			} finally {
				if (stmnt != null && !stmnt.isClosed())
					stmnt.close();

			}
		} else {
			List<AbstractEntity> versions = read("", DBVersion.class, 1);
			if (versions == null || versions.isEmpty()) {
				throw new Exception(
						"Error retrieving Schema Version. Database might be corrupted.");
			}
			for (AbstractEntity ver : versions) {
				if (ver instanceof DBVersion) {
					DBVersion dbv = (DBVersion) ver;
					if (dbv.getVersion().compareTo(version) != 0) {
						throw new Exception(
								"Database Version missmatch, Expection version ["
										+ version + "], current DB version ["
										+ dbv.getVersion() + "]");
					}
				}
			}
		}
	}

	private void createIndex(Element parent, Class<?> cls, SimpleDbQuery dbq,
			Statement stmnt) throws Exception {
		NodeList nl = XMLUtils.search(_CONFIG_SETUP_INDEXES_, parent);
		if (nl != null && nl.getLength() > 0) {
			for (int ii = 0; ii < nl.getLength(); ii++) {
				Element elm = (Element) nl.item(ii);

				String iname = elm.getAttribute("name");
				if (iname == null || iname.isEmpty())
					throw new Exception(
							"Invalid Configuration : Missing or empty attribute [name]");
				String icolumns = elm.getAttribute("columns");
				if (icolumns == null || icolumns.isEmpty())
					throw new Exception(
							"Invalid Configuration : Missing or empty attribute [columns]");
				List<KeyValuePair<String>> columns = new ArrayList<KeyValuePair<String>>();
				KeyValuePair<String> cp = new KeyValuePair<String>();
				cp.setKey(iname);
				cp.setValue(icolumns);
				columns.add(cp);

				List<String> createsql = dbq.getCreateIndexDDL(cls, columns);
				for (String sql : createsql) {
					log.debug("INDEX SQL [" + sql + "]");
					stmnt.execute(sql);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#dispose()
	 */
	public void dispose() {
		try {
			if (conns != null && conns.length > 0) {
				for (Connection conn : conns) {
					if (conn != null) {
						if (!conn.isClosed()) {
							log.debug("Closing H2 connection...");
							conn.close();
						}
					}
				}
				conns = null;
			}
			freeconns.clear();
			state = EnumInstanceState.Closed;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			LogUtils.stacktrace(log, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.AbstractPersister#postinit()
	 */
	@Override
	public void postinit() throws Exception {
		if (checksetup)
			checkSetup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.db.AbstractDbPersister#getSequenceValue(
	 * com.wookler.core.persistence.Entity,
	 * com.wookler.core.persistence.AttributeReflection, java.sql.Connection)
	 */
	@Override
	protected Object getSequenceValue(Entity entity,
			StructAttributeReflect attr, Connection conn) throws Exception {
		if (EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
			EnumPrimitives prim = EnumPrimitives.type(attr.Field.getType());
			if (prim == EnumPrimitives.ELong || prim == EnumPrimitives.EInteger) {
				String seqname = SimpleDbQuery.getSequenceName(entity, attr);

				String sql = "select NEXT VALUE FOR " + seqname;
				Statement stmnt = conn.createStatement();

				try {
					ResultSet rs = stmnt.executeQuery(sql);
					while (rs.next()) {
						return rs.getLong(1);
					}
				} finally {
					if (stmnt != null && !stmnt.isClosed())
						stmnt.close();
				}
			}
		} else if (attr.Field.getType().equals(String.class)) {
			UUID uuid = UUID.randomUUID();
			return uuid.toString();
		}
		throw new Exception("Cannot generate sequence for type ["
				+ attr.Field.getType().getCanonicalName() + "]");
	}
}
