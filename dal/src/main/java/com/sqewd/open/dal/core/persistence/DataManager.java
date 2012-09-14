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
package com.sqewd.open.dal.core.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.InitializedHandle;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumEntityState;
import com.sqewd.open.dal.api.persistence.EnumJoinType;
import com.sqewd.open.dal.api.persistence.OperationResponse;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.InstanceParam;
import com.sqewd.open.dal.api.utils.ListParam;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.api.utils.XMLUtils;
import com.sqewd.open.dal.core.Env;
import com.sqewd.open.dal.core.reflect.EntityClassLoader;
import com.sqewd.open.dal.core.reflect.EntityScanner;

/**
 * The DataManager class manages persistence and data query.
 * 
 * @author subhagho
 * 
 */
public class DataManager implements InitializedHandle {
	public static final String _CONFIG_XPATH_ = "/core/datamanager";
	public static final String _CONFIG_PERSIST_XPATH_ = "./persistence";
	public static final String _CONFIG_PERSISTER_XPATH_ = "./persister";
	public static final String _CONFIG_PERSISTMAP_XPATH_ = "./classmap";
	public static final String _CONFIG_ATTR_PERSISTER_ = "persister";
	public static final String _CONFIG_ENTITY_PACKAGES_ = "./packages/jar";

	private static final Logger log = LoggerFactory
			.getLogger(DataManager.class);

	private EnumInstanceState state = EnumInstanceState.Unknown;

	private final HashMap<String, AbstractPersister> persistmap = new HashMap<String, AbstractPersister>();
	private final HashMap<String, List<String>> scanjars = new HashMap<String, List<String>>();

	private void init(final XMLConfiguration config) throws Exception {
		try {
			state = EnumInstanceState.Running;

			if (Env.get().getEntityLoader() == null) {
				Env.get()
						.setEntityLoader(
								new EntityClassLoader(this.getClass()
										.getClassLoader()));
			}

			String rootpath = Env._CONFIG_XPATH_ROOT_ + _CONFIG_XPATH_;
			NodeList nl = XMLUtils.search(rootpath, config.getDocument()
					.getDocumentElement());
			if (nl == null || nl.getLength() <= 0)
				throw new Exception(
						"Invalid Configuration : DataManager configuration node ["
								+ rootpath + "] not found.");
			Element dmroot = (Element) nl.item(0);

			NodeList packnl = XMLUtils.search(_CONFIG_ENTITY_PACKAGES_, dmroot);
			if (packnl != null && packnl.getLength() > 0) {
				for (int ii = 0; ii < packnl.getLength(); ii++) {
					Element jelm = (Element) packnl.item(ii);

					String jar = jelm.getAttribute("name");
					if (jar != null) {
						String pack = jelm.getAttribute("package");
						if (pack != null && !pack.isEmpty()) {
							List<String> packs = null;
							if (scanjars.containsKey(jar)) {
								packs = scanjars.get(jar);
							} else {
								packs = new ArrayList<String>();
								scanjars.put(jar, packs);
							}

							packs.add(pack);
						}
					}
				}
				scanEntities();
			}

			NodeList pernl = XMLUtils.search(_CONFIG_PERSIST_XPATH_, dmroot);
			if (pernl != null && pernl.getLength() > 0) {
				initPersisters((Element) pernl.item(0));
			}

			log.debug("DataManager initialzied...");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			state = EnumInstanceState.Exception;
			LogUtils.stacktrace(log, e);
		}
	}

	private void scanEntities() throws Exception {
		if (scanjars.size() > 0) {
			EntityScanner scanner = new EntityScanner();
			for (String key : scanjars.keySet()) {
				List<String> packs = scanjars.get(key);
				if (packs.size() > 0) {
					for (String pack : packs) {
						scanner.scan(pack);
						scanEntities(scanner.getClasses());
					}
				}
			}
		}
	}

	private void scanEntities(final List<Class<?>> classes) throws Exception {
		for (Class<?> type : classes) {
			if (type.isAnnotationPresent(Entity.class)) {
				log.debug("Found entity : [" + type.getCanonicalName() + "]["
						+ type.getClassLoader().getClass().getCanonicalName()
						+ "]");
				ReflectionUtils.get().load(type);
			}
		}
	}

	private void initPersisters(final Element root) throws Exception {
		List<AbstractPersister> pers = new ArrayList<AbstractPersister>();

		NodeList pernl = XMLUtils.search(_CONFIG_PERSISTER_XPATH_, root);
		if (pernl != null && pernl.getLength() > 0) {
			for (int ii = 0; ii < pernl.getLength(); ii++) {
				Element pelm = (Element) pernl.item(ii);
				String vk = pelm.getAttribute(XMLUtils._PARAM_ATTR_NAME_);
				String cn = pelm.getAttribute(InstanceParam._PARAM_ATTR_CLASS_);
				InstanceParam ip = new InstanceParam(vk, cn);
				ip.parse(pelm);
				Class<?> cls = Class.forName(ip.getClassname());
				Object pobj = cls.newInstance();
				if (pobj instanceof AbstractPersister) {
					AbstractPersister ap = (AbstractPersister) pobj;
					ap.init(ip.getParams());
					persistmap.put(cls.getCanonicalName(), ap);
					persistmap.put(ap.key(), ap);
					pers.add(ap);
				} else
					throw new Exception(
							"Invalid Configuration : Persister class ["
									+ cls.getCanonicalName()
									+ "] does not extend ["
									+ AbstractPersister.class
											.getCanonicalName() + "]");
			}
		}
		NodeList mapnl = XMLUtils.search(_CONFIG_PERSISTMAP_XPATH_, root);
		if (mapnl != null && mapnl.getLength() > 0) {
			for (int ii = 0; ii < mapnl.getLength(); ii++) {
				Element melm = (Element) mapnl.item(ii);
				String classname = melm
						.getAttribute(InstanceParam._PARAM_ATTR_CLASS_);
				if (classname == null || classname.isEmpty())
					throw new Exception(
							"Invalid Configuration : Missing map parameter ["
									+ InstanceParam._PARAM_ATTR_CLASS_ + "]");
				String persister = melm.getAttribute(_CONFIG_ATTR_PERSISTER_);
				if (persister == null || persister.isEmpty())
					throw new Exception(
							"Invalid Configuration : Missing map parameter ["
									+ _CONFIG_ATTR_PERSISTER_ + "]");
				if (persistmap.containsKey(persister)) {
					persistmap.put(classname, persistmap.get(persister));
				} else
					throw new Exception(
							"Invalid Configuration : Persister class ["
									+ persister + "] does not exist.");
			}
		}
		for (AbstractPersister per : pers) {
			per.postinit();
		}
	}

	/**
	 * Get a handle to the Persistence Handler based on the name specified.
	 * 
	 * @param name
	 *            - Persister Class name.
	 * @return
	 * @throws Exception
	 */
	public AbstractPersister getPersisterByName(final String name)
			throws Exception {
		if (persistmap.containsKey(name))
			return persistmap.get(name);

		throw new Exception("No persistence handler found for class [" + name
				+ "]");
	}

	/**
	 * Get the persistence handler defined for the specified entity type. If no
	 * persistence handler found for the current class, search thru the super
	 * classes to see if a handler is defined for any?
	 * 
	 * @param type
	 *            - Class of the Entity.
	 * @return
	 * @throws Exception
	 */
	public AbstractPersister getPersister(final Class<?> type) throws Exception {
		String key = type.getCanonicalName();
		if (persistmap.containsKey(key))
			return persistmap.get(key);
		Class<?> ttype = type;
		key = type.getPackage().getName();
		if (persistmap.containsKey(key))
			return persistmap.get(key);
		while (true) {
			ttype = ttype.getSuperclass();
			if (ttype.getCanonicalName().compareTo(
					Object.class.getCanonicalName()) == 0) {
				break;
			}
			key = ttype.getCanonicalName();
			if (persistmap.containsKey(key))
				return persistmap.get(key);
		}
		throw new Exception("No persistence handler found for class ["
				+ type.getCanonicalName() + "]");
	}

	/**
	 * Fetch the entity records for the Class filtered by Query.
	 * 
	 * @param query
	 *            - Query Condition string.
	 * @param type
	 *            - Entity Type.
	 * @return
	 * @throws Exception
	 */
	public List<AbstractEntity> read(final String query, final Class<?> type,
			final int limit) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		if (enref.IsJoin) {
			if (enref.Join.Type == null
					|| enref.Join.Type == EnumJoinType.Unknown) {
				isNativeJoin(enref);
			}
		}
		if (!enref.IsJoin || enref.Join.Type == EnumJoinType.Native) {
			AbstractPersister persister = getPersister(type);
			return persister.read(query, type, limit);
		} else {
			DistributedJoinHandler jh = new DistributedJoinHandler(type, query,
					limit);
			return jh.read();
		}
	}

	private void isNativeJoin(final StructEntityReflect enref) throws Exception {
		AbstractPersister pers = null;

		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr.Reference == null) {
				continue;
			}
			Class<?> type = Class.forName(attr.Reference.Class);
			StructEntityReflect subref = ReflectionUtils.get()
					.getEntityMetadata(type);
			if (subref == null)
				throw new Exception("No entity defined for name ["
						+ attr.Column + "]");
			AbstractPersister p = getPersister(type);
			if (pers == null) {
				pers = p;
			} else if (!p.equals(pers)) {
				enref.Join.Type = EnumJoinType.Virtual;
				return;
			}
		}
		enref.Join.Type = EnumJoinType.Native;
	}

	/**
	 * Fetch the entity records for the Class filtered by Query using the
	 * specified persistence handler.
	 * 
	 * @param query
	 *            - Query Condition string.
	 * @param type
	 *            - Entity Type.
	 * @param persister
	 *            - Persistence Handler
	 * @return
	 * @throws Exception
	 */
	public List<AbstractEntity> read(final String query,
			final AbstractPersister persister, final Class<?> type,
			final int limit) throws Exception {
		return persister.read(query, type, limit);
	}

	/**
	 * Save the entity. (Insert/Update/Delete) based on entity status.
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public OperationResponse save(final AbstractEntity entity) throws Exception {
		if (!(entity instanceof AbstractPersistedEntity))
			throw new Exception("Entity ["
					+ entity.getClass().getCanonicalName()
					+ "] does not extend from ["
					+ AbstractPersistedEntity.class.getCanonicalName() + "]");
		AbstractPersister persister = getPersister(entity.getClass());
		return persister.save(entity, false);
	}

	public List<String> getEntityPackages() {
		List<String> packages = new ArrayList<String>();

		for (String key : scanjars.keySet()) {
			List<String> packs = scanjars.get(key);
			packages.addAll(packs);
		}
		return packages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#key()
	 */
	public String key() {
		return DataManager.class.getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.InitializedHandle#init(com.wookler.utils.ListParam)
	 */
	public void init(final ListParam param) throws Exception {
		throw new Exception("Should not be called.");
	}

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
	 * @see com.wookler.core.InitializedHandle#dispose()
	 */
	public void dispose() {
		if (persistmap != null && persistmap.size() > 0) {
			for (String key : persistmap.keySet()) {
				AbstractPersister pers = persistmap.get(key);
				if (pers != null) {
					pers.dispose();
				}
			}
		}
		return;
	}

	// Instance
	private static DataManager _instance = new DataManager();

	/**
	 * Initialize and create the DataManager instance.
	 * 
	 * @param config
	 *            - Configuration
	 * @throws Exception
	 */
	public static void create(final XMLConfiguration config) throws Exception {
		synchronized (_instance) {
			if (_instance.state == EnumInstanceState.Running)
				return;
			log.debug("Initialzing the DataManager...");
			_instance.init(config);
		}
	}

	/**
	 * Get the DataManager instance handle.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataManager get() throws Exception {
		synchronized (_instance) {
			if (_instance.state != EnumInstanceState.Running)
				throw new Exception(
						"Invalid Instance State : Instance not available [state="
								+ _instance.state.name() + "]");
			return _instance;
		}
	}

	/**
	 * Dispose the DataManager instance.
	 */
	public static void release() {
		synchronized (_instance) {
			if (_instance.state == EnumInstanceState.Running) {
				_instance.dispose();
				_instance.state = EnumInstanceState.Closed;
				log.info("Dispoing the DataManager instance...");
			}
		}
	}

	/**
	 * Create a new instance of an AbstractEntity type. This method should be
	 * used when creating new AbstarctEntity types.
	 * 
	 * @param type
	 *            - Class (type) to create instance of.
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> T newInstance(final Class<?> type)
			throws Exception {
		Object obj = type.newInstance();
		if (!(obj instanceof AbstractEntity))
			throw new Exception("Invalid Class : [" + type.getCanonicalName()
					+ "] does not extend ["
					+ AbstractEntity.class.getCanonicalName() + "]");

		((AbstractEntity) obj).setState(EnumEntityState.New);

		return (T) obj;
	}
}
