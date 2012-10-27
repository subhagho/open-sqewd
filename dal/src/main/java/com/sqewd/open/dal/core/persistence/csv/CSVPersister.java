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

import org.apache.commons.beanutils.PropertyUtils;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.sqewd.open.dal.api.DataCache;
import com.sqewd.open.dal.api.EnumInstanceState;
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
import com.sqewd.open.dal.core.persistence.query.sql.SQLUtils;

/**
 * @author subhagho
 * 
 */
public class CSVPersister extends AbstractPersister {
	private static final Logger log = LoggerFactory
			.getLogger(CSVPersister.class);
	public static final String _PARAM_DATADIR_ = "datadir";

	private String datadir;
	private Cache cache = null;

	private EnumImportFormat format = EnumImportFormat.CSV;

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
		if (cache != null) {
			try {
				DataCache.instance().remove(cache.getName());
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.AbstractPersister#getSchemaObject(
	 * java.lang.String)
	 */
	@Override
	public SchemaObject getSchemaObject(final String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#init(com.wookler.utils.ListParam)
	 */
	@Override
	public void init(final ListParam param) throws Exception {
		try {
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
		synchronized (cache) {
			if (cache.containsKey(type.getCanonicalName()))
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
			cache.put(type.getCanonicalName(), entities);
			reader.close();
		}
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
	 * @see com.wookler.core.persistence.AbstractPersister#read(java.util.List)
	 */
	@Override
	public List<AbstractEntity> read(final String query, final Class<?> type,
			final int limit) throws Exception {
		List<AbstractEntity> result = null;
		String cname = type.getCanonicalName();
		if (!cache.containsKey(cname)) {
			load(type);
		}

		// Make sure the type for the class is available.
		ReflectionUtils.get().getEntityMetadata(type);

		List<AbstractEntity> records = cache.get(cname);
		if (query != null && !query.isEmpty()) {
			SimpleFilterQuery filter = new SimpleFilterQuery();

			filter.parse(new Class<?>[] { type }, query);
			result = filter.select(records);
		} else {
			result = records;
		}
		return result;
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
		// TODO Auto-generated method stub
		return null;
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
