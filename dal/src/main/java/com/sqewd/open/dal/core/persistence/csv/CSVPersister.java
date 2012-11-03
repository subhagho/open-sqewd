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
package com.sqewd.open.dal.core.persistence.csv;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.sqewd.open.dal.api.DataCache;
import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.ReferenceCache;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.CursorContext;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.EnumRefereceType;
import com.sqewd.open.dal.api.persistence.OperationResponse;
import com.sqewd.open.dal.api.persistence.PersistenceResponse;
import com.sqewd.open.dal.api.persistence.query.PlanGenerator;
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.api.reflect.AttributeReferenceDef;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.reflect.SchemaObject;
import com.sqewd.open.dal.api.utils.AbstractParam;
import com.sqewd.open.dal.api.utils.DateUtils;
import com.sqewd.open.dal.api.utils.ListParam;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.api.utils.ValueParam;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.core.persistence.model.EntityModelHelper;
import com.sqewd.open.dal.core.persistence.query.parser.ConditionParser;
import com.sqewd.open.dal.core.persistence.query.sql.SQLUtils;

/**
 * @author subhagho
 * 
 */
public class CSVPersister extends AbstractPersister {
	private static final Logger log = LoggerFactory
			.getLogger(CSVPersister.class);
	public static final String _PARAM_DATADIR_ = "datadir";
	public static final String _PARAM_USE_CACHE_ = "cache";
	public static final String _PARAM_CACHE_SCHEMA_SIZE_ = "cache.schema.size";
	public static final String _PARAM_CACHE_DATA_SIZE_ = "cache.data.size";

	public static final String _CACHE_SCHEMA_KEY_ = "csv.persister.schema";
	public static final String _CACHE_DATA_KEY_ = "csv.persister.data";

	private static final long _CACHE_ALL_MAX_DISK_ = 1024 * 1024 * 1024 * 5; // 5GB

	private String datadir;
	private Cache datacache = null;
	private Cache schemacache = null;

	private String cacheSchemaSize = "32M";
	private String cacheDataSize = "236M";

	private EnumImportFormat format = EnumImportFormat.CSV;

	private PlanGenerator planner = null;

	public CSVPersister() {
		key = this.getClass().getCanonicalName();
	}

	public CSVPersister(final EnumImportFormat format) {
		this.format = format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#dispose()
	 */
	public void dispose() {
		if (datacache != null) {
			try {
				DataCache.instance().remove(datacache.getName());
			} catch (Exception e) {
				LogUtils.stacktrace(log, e);
				log.error(e.getLocalizedMessage());
			}
		}
		if (schemacache != null) {
			try {
				DataCache.instance().remove(schemacache.getName());
			} catch (Exception e) {
				LogUtils.stacktrace(log, e);
				log.error(e.getLocalizedMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#getPlanGenerator()
	 */
	@Override
	public PlanGenerator getPlanGenerator() throws Exception {
		return planner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#getSchemaObject(
	 * java.lang.String)
	 */
	@Override
	public SchemaObject getSchemaObject(final EntityDef entity)
			throws Exception {
		String name = entity.getName();
		if (schemacache.isKeyInCache(name)) {
			Element elm = schemacache.get(name);
			if (elm.getObjectValue() instanceof SchemaObject)
				return (SchemaObject) elm.getObjectValue();
			else
				throw new Exception("Invalid Cached Object : Type ["
						+ elm.getObjectValue().getClass().getCanonicalName()
						+ "]");
		} else {
			synchronized (schemacache) {
				if (schemacache.isKeyInCache(name)) {
					Element elm = schemacache.get(name);
					if (elm.getObjectValue() instanceof SchemaObject)
						return (SchemaObject) elm.getObjectValue();
					else
						throw new Exception("Invalid Cached Object : Type ["
								+ elm.getObjectValue().getClass()
										.getCanonicalName() + "]");
				} else {
					SchemaObject so = loadSchema(entity);
					Element elm = new Element(name, so);
					schemacache.put(elm);

					return so;
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#init(com.wookler.utils.ListParam)
	 */
	@Override
	public void init(final ListParam param) throws Exception {
		try {
			planner = new CSVPlanGenerator(this);

			AbstractParam pkey = param.get(_PARAM_KEY_);
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

			AbstractParam pdd = param.get(_PARAM_DATADIR_);
			if (pdd == null)
				throw new Exception(
						"Invalid Configuration : Missing paramter ["
								+ _PARAM_DATADIR_ + "]");
			if (!(pdd instanceof ValueParam))
				throw new Exception(
						"Invalid Configuration : Invalid Parameter type for ["
								+ _PARAM_DATADIR_ + "]");
			datadir = ((ValueParam) pdd).getValue();
			if (datadir == null || datadir.isEmpty())
				throw new Exception("Invalid Configuration : Param ["
						+ _PARAM_DATADIR_ + "] is NULL or empty.");

			AbstractParam pch = param.get(_PARAM_USE_CACHE_);
			if (!(pch instanceof ValueParam))
				throw new Exception(
						"Invalid Configuration : Invalid Parameter type for ["
								+ _PARAM_USE_CACHE_ + "]");
			boolean useCache = Boolean.parseBoolean(((ValueParam) pch)
					.getValue());
			if (useCache) {
				AbstractParam pcs = param.get(_PARAM_CACHE_SCHEMA_SIZE_);
				if (!(pcs instanceof ValueParam))
					throw new Exception(
							"Invalid Configuration : Invalid Parameter type for ["
									+ _PARAM_CACHE_SCHEMA_SIZE_ + "]");
				String value = ((ValueParam) pcs).getValue();
				if (value != null && !value.isEmpty()) {
					cacheSchemaSize = value;
				}

				// Create Schema Cache.
				CacheConfiguration config = new CacheConfiguration();
				config.setName(_CACHE_SCHEMA_KEY_);
				config.setMaxBytesLocalHeap(cacheSchemaSize);
				schemacache = DataCache.instance().createCache(
						_CACHE_SCHEMA_KEY_, config);

				// Create Data Cache.
				pcs = param.get(_PARAM_CACHE_DATA_SIZE_);
				if (!(pcs instanceof ValueParam))
					throw new Exception(
							"Invalid Configuration : Invalid Parameter type for ["
									+ _PARAM_CACHE_DATA_SIZE_ + "]");
				value = ((ValueParam) pcs).getValue();
				if (value != null && !value.isEmpty()) {
					cacheDataSize = value;
				}
				config = new CacheConfiguration();
				config.setName(_CACHE_DATA_KEY_);
				config.setMaxBytesLocalHeap(cacheDataSize);
				PersistenceConfiguration pc = new PersistenceConfiguration();
				pc.strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP);
				config.addPersistence(pc);
				config.setMaxBytesLocalDisk(_CACHE_ALL_MAX_DISK_);
				datacache = DataCache.instance().createCache(_CACHE_DATA_KEY_,
						config);
			}
			state = EnumInstanceState.Running;
		} catch (Exception e) {
			state = EnumInstanceState.Exception;
			throw e;
		}
	}

	protected void load(final Class<?> type, final boolean debug)
			throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");
		synchronized (datacache) {
			if (datacache.isKeyInCache(type.getCanonicalName()))
				return;

			Entity eann = type.getAnnotation(Entity.class);
			String fname = eann.recordset() + "." + format.name();
			String path = datadir + "/" + fname;

			File fi = new File(path);
			if (!fi.exists())
				throw new Exception("Cannot find file [" + path
						+ "] for entity [" + type.getCanonicalName() + "]");
			List<AbstractEntity> entities = new ArrayList<AbstractEntity>();

			char sep = ',';
			if (format == EnumImportFormat.TSV) {
				sep = '\t';
			}
			CSVReader reader = new CSVReader(new FileReader(path), sep, '"');
			String[] header = null;
			while (true) {
				String[] data = reader.readNext();
				if (data == null) {
					break;
				}
				if (header == null) {
					header = data;
					continue;
				}
				if (data.length < header.length) {
					continue;
				}
				AbstractEntity record = parseRecord(type, header, data, debug);
				if (record == null) {
					log.warn("Parse returned NULL");
					continue;
				}
				entities.add(record);
			}
			Element elm = new Element(type.getCanonicalName(), entities);
			datacache.put(elm);
			reader.close();
		}
	}

	private SchemaObject loadSchema(final EntityDef entity) throws Exception {
		String filename = datadir + "/" + entity.getName() + ".csv";
		SchemaObject so = new CSVFile(filename, entity, this);
		return so;
	}

	protected AbstractEntity parseRecord(final Class<?> type,
			final String[] header, final String[] data, final boolean debug)
			throws Exception {
		AbstractEntity entity = (AbstractEntity) type.newInstance();
		EntityDef edef = EntityModelHelper.get().getEntityDef(type);

		for (int ii = 0; ii < header.length; ii++) {
			AttributeDef attr = edef.getAttribute(header[ii]);
			if (attr != null) {
				if (attr.getHandler() != null) {
					Object value = attr.getHandler().get(data[ii]);
					setFieldValue(entity, attr.getField(), value);
				} else if (!attr.isRefrenceAttr()) {
					setFieldValue(entity, attr.getField(), data[ii]);
				} else {
					AttributeReferenceDef refd = (AttributeReferenceDef) attr
							.getType();

					EntityDef enref = refd.getReference();
					String query = enref.getName()
							+ "."
							+ refd.getReferenceAttribute().getName()
							+ "="
							+ SQLUtils.getQuotedString(data[ii], refd
									.getReferenceAttribute().getField());
					List<AbstractEntity> refs = DataManager.get().read(query,
							refd.getType(), -1, debug);
					if (refs != null && refs.size() > 0) {
						if (refd.getCardinality() == EnumRefereceType.One2One) {
							setFieldValue(entity, attr.getField(), refs.get(0));
						} else {
							setFieldValue(entity, attr.getField(), refs);
						}
					} else
						throw new Exception(
								"No record found for reference key : [QUERY:"
										+ query + "]");
				}
			}
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.AbstractPersister#postinit()
	 */
	@Override
	public void postinit() throws Exception {
		// Do nothing...
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#read(java.lang.String
	 * , java.lang.Class, int, boolean)
	 */
	@Override
	public List<AbstractEntity> read(final String query, final Class<?> type,
			final int limit, final boolean debug) throws Exception {
		List<AbstractEntity> result = null;
		String cname = type.getCanonicalName();
		if (!datacache.isKeyInCache(cname)) {
			load(type, debug);
		}

		// Make sure the type for the class is available.
		ReferenceCache.get().getEntityDef(type);

		Element elm = datacache.get(cname);
		if (!(elm.getObjectValue() instanceof List<?>))
			throw new Exception(
					"Invalid Cache state : Cached records are invalid.");

		if (query != null && !query.isEmpty()) {
			ConditionParser parser = new ConditionParser(query);

		} else {
			List<?> records = (List<?>) elm.getObjectValue();
			if (records != null && records.size() > 0) {
				result = new ArrayList<AbstractEntity>();
				for (Object obj : records) {
					if (obj instanceof AbstractEntity) {
						result.add((AbstractEntity) obj);
					} else
						throw new Exception(
								"Invalid Cache State : Result type mis-match ["
										+ type.getCanonicalName() + " != "
										+ obj.getClass().getCanonicalName()
										+ "]");
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#save(com.sqewd.open
	 * .dal.api.persistence.AbstractEntity, boolean, boolean)
	 */
	@Override
	public OperationResponse save(final AbstractEntity record,
			final boolean overwrite, final boolean debug) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#save(java.util.List,
	 * boolean, boolean)
	 */
	@Override
	public PersistenceResponse save(final List<AbstractEntity> records,
			final boolean overwrite, final boolean debug) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#select(java.lang
	 * .String, java.lang.Class,
	 * com.sqewd.open.dal.api.persistence.CursorContext)
	 */
	@Override
	public ResultSet select(final String query, final Class<?> types,
			final CursorContext ctx) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.AbstractPersister#setFieldValue(com.wookler
	 * .core.persistence.AbstractEntity, java.lang.reflect.Field,
	 * java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setFieldValue(final AbstractEntity entity, final Field fd,
			final Object value) throws Exception {
		Object pvalue = value;
		if (fd.getType().equals(String.class)) {
			pvalue = value;
		} else if (fd.getType().equals(Date.class)) {
			pvalue = DateUtils.fromString((String) value);
		} else if (EnumPrimitives.isPrimitiveType(fd.getType())) {
			EnumPrimitives pt = EnumPrimitives.type(fd.getType());
			switch (pt) {
			case ECharacter:
				pvalue = ((String) value).charAt(0);
				break;
			case EShort:
				pvalue = Short.parseShort((String) value);
				break;
			case EInteger:
				pvalue = Integer.parseInt((String) value);
				break;
			case ELong:
				pvalue = Long.parseLong((String) value);
				break;
			case EFloat:
				pvalue = Float.parseFloat((String) value);
				break;
			case EDouble:
				pvalue = Double.parseDouble((String) value);
				break;
			default:
				throw new Exception("Unsupported primitive type [" + pt.name()
						+ "]");
			}
		} else if (fd.getType().isEnum()) {
			Class ecls = fd.getType();
			pvalue = Enum.valueOf(ecls, (String) value);
		} else if (pvalue.getClass().isAnnotationPresent(Entity.class)) {
			pvalue = value;
		} else
			throw new Exception("Field type ["
					+ fd.getType().getCanonicalName() + "] is not supported.");
		PropertyUtils.setProperty(entity, fd.getName(), pvalue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#state()
	 */
	public EnumInstanceState state() {
		return state;
	}
}
