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
 * @filename EntityModelLoader.java
 * @created Oct 1, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.ReferenceCache;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.CustomFieldHandler;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumRefereceType;
import com.sqewd.open.dal.api.persistence.Reference;
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.api.reflect.AttributeReferenceDef;
import com.sqewd.open.dal.api.reflect.AttributeTypeDef;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.reflect.ReflectionHelper;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.core.reflect.EntityScanner;

/**
 * Loads the Entity Model definitions into the cache at startup time. This is a
 * singleton class and only one instance should exist at runtime.
 * 
 * @author subhagho
 * 
 */
public class EntityModelLoader {
	private static final Logger log = LoggerFactory
			.getLogger(EntityModelLoader.class);

	private EnumInstanceState state = EnumInstanceState.Unknown;

	private void init(final HashMultimap<String, String> packages)
			throws Exception {
		try {
			if (packages.size() > 0) {
				EntityScanner scanner = new EntityScanner();
				for (String key : packages.keySet()) {
					Set<String> packs = packages.get(key);
					if (packs.size() > 0) {
						for (String pack : packs) {
							log.debug("Scanning JAR [" + key
									+ "] for package [" + pack + "]");
							scanner.scan(pack);
							scanEntities(scanner.getClasses());
						}
					}
				}
			}
			state = EnumInstanceState.Running;
		} catch (Exception ex) {
			state = EnumInstanceState.Exception;
			throw ex;
		}
	}

	private void scanEntities(final List<Class<?>> classes) throws Exception {
		for (Class<?> type : classes) {
			if (ReflectionHelper.isEntityType(type)) {
				log.debug("Found entity : [" + type.getCanonicalName() + "]["
						+ type.getClassLoader().getClass().getCanonicalName()
						+ "]");
				load(type);
			}
		}
	}

	private synchronized EntityDef load(final Class<?> type) throws Exception {
		Entity eann = type.getAnnotation(Entity.class);

		EntityDef ed = new EntityDef(eann.recordset(), type);
		ed.setPersister(DataManager.get().getPersister(type));

		// Is entity cached.
		if (eann.cached()) {
			ed.setCached();
		}
		loadattributes(ed);

		// Add the Entity deinfition to the cache.
		ReferenceCache.get().addEntityDef(ed);

		return ed;
	}

	private void loadattributes(final EntityDef entitydef) throws Exception {
		List<Field> fields = ReflectionHelper.getFields(entitydef
				.getClasstype());
		if (fields != null && !fields.isEmpty()) {
			for (Field fd : fields) {
				if (fd.isAnnotationPresent(Attribute.class)) {
					Class<?> ft = fd.getType();
					Attribute atann = fd.getAnnotation(Attribute.class);
					AttributeTypeDef atd = null;

					if (fd.isAnnotationPresent(Reference.class)) {
						Reference rfann = fd.getAnnotation(Reference.class);
						String refc = rfann.target();
						Class<?> refcls = Class.forName(refc);

						// Get the reference entity definition.
						// NOTE: This doesn't handle recursive calls.
						EntityDef refed = getEntityDef(refcls);
						AttributeDef refat = refed.getAttribute(rfann
								.attribute());
						if (refat == null)
							throw new Exception(
									"Cannot find referenced attribute ["
											+ rfann.attribute()
											+ "] in Entity ["
											+ refed.getClasstype()
													.getCanonicalName() + "]");
						atd = new AttributeReferenceDef(refed, refat, ft);
						atd.setCascadeUpdate(rfann.cascade());
						Class<?> listdef = ReflectionHelper
								.isEntityListType(fd);

						// Check if reference is a List
						if (listdef == null) {
							atd.setCardinality(EnumRefereceType.One2One);
						} else {
							// Check if specified Reference class matches the
							// List type.
							if (!ReflectionHelper
									.hasSuperClass(refcls, listdef))
								throw new Exception(
										"Specified Reference target definition doesnot match the List type for ["
												+ atann.name()
												+ "] in Entity ["
												+ entitydef.getClasstype()
														.getCanonicalName()
												+ "]");
							atd.setCardinality(EnumRefereceType.One2Many);
						}
					} else {
						atd = new AttributeTypeDef(ft);
						atd.setCardinality(EnumRefereceType.One2One);
					}
					AttributeDef ad = new AttributeDef(atann.name(), fd, atd);
					String handler = atann.handler();
					if (handler != null && !handler.isEmpty()) {
						Class<?> hc = Class.forName(handler);
						Object ho = hc.newInstance();
						if (ho instanceof CustomFieldHandler) {
							ad.setHandler((CustomFieldHandler) ho);
						} else
							throw new Exception(
									"Invalid custom field data handler specified. Class ["
											+ hc.getCanonicalName()
											+ "] doesnot implement ["
											+ CustomFieldHandler.class
													.getCanonicalName() + "]");
					}
					ad.setKey(atann.keyattribute());

					entitydef.addAttribute(ad);
				}
			}
		}
	}

	public EntityDef getEntityDef(final Class<?> type) throws Exception {
		EntityDef ed = ReferenceCache.get().getEntityDef(type);
		if (ed == null) {
			ed = load(type);
		}
		return ed;
	}

	// Singleton
	private static final EntityModelLoader _instance = new EntityModelLoader();

	/**
	 * Create an instance of the Entity Model. Singleton class, should be called
	 * only once.
	 * 
	 * @param packages
	 * @throws Exception
	 */
	public static final void create(final HashMultimap<String, String> packages)
			throws Exception {
		_instance.init(packages);
	}

	/**
	 * Get a handle to the Entity Model Loader.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final EntityModelLoader get() throws Exception {
		if (_instance.state != EnumInstanceState.Running)
			throw new Exception("Entity Model Loader is in an invalid state ["
					+ _instance.state.name() + "]");
		return _instance;
	}
}
