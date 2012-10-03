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
 * @filename ReferenceCache.java
 * @created Oct 1, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.utils.ListParam;

/**
 * Singleton Object for accessing the reference cache. Reference cache is used
 * to store runtime definitions. This cache shouldn't contain any data elements.
 * 
 * @author subhagho
 * 
 */
public class ReferenceCache implements InitializedHandle {
	public static final String _CACHE_ENTITYREFERENCE_ = "reference.entity";

	private EnumInstanceState state = EnumInstanceState.Unknown;

	private CacheManager referenceCacheManager = null;

	private Cache entityReferenceCache = null;

	private void init(final String config) throws Exception {
		try {
			referenceCacheManager = CacheManager.newInstance(config);
			entityReferenceCache = referenceCacheManager
					.getCache(_CACHE_ENTITYREFERENCE_);
			state = EnumInstanceState.Running;
		} catch (Exception ex) {
			state = EnumInstanceState.Exception;
			throw ex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.InitializedHandle#key()
	 */
	public String key() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.InitializedHandle#init(com.sqewd.open.dal.api.
	 * utils.ListParam)
	 */
	public void init(final ListParam param) throws Exception {
		throw new Exception(
				"Method not implemented. Initialization should be done via the Singleton.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.InitializedHandle#state()
	 */
	public EnumInstanceState state() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.InitializedHandle#dispose()
	 */
	public void dispose() {
		if (referenceCacheManager != null && state == EnumInstanceState.Running) {
			referenceCacheManager.shutdown();
		}
	}

	/**
	 * @return the entityReferenceCache
	 */
	public Cache getEntityReferenceCache() {
		return entityReferenceCache;
	}

	/**
	 * Add the entity definition to the cache.
	 * 
	 * @param entitydef
	 * @throws Exception
	 */
	public void addEntityDef(final EntityDef entitydef) throws Exception {
		String ckey = entitydef.getClasstype().getCanonicalName();

		if (entityReferenceCache.get(ckey) != null)
			throw new Exception("Entity definition with entity class [" + ckey
					+ "] already added to cache.");
		Element elm = new Element(ckey, entitydef);
		entityReferenceCache.put(elm);
	}

	/**
	 * Get the Entity Definition based on the entity name.
	 * 
	 * @param ec
	 *            - Entity class
	 * @return
	 * @throws Exception
	 */
	public EntityDef getEntityDef(final Class<?> ec) throws Exception {
		Element elm = entityReferenceCache.get(ec.getCanonicalName());
		if (elm != null)
			return (EntityDef) elm.getObjectValue();
		return null;
	}

	// Singleton
	/**
	 * @param entityReferenceCache
	 *            the entityReferenceCache to set
	 */
	public void setEntityReferenceCache(final Cache entityReferenceCache) {
		this.entityReferenceCache = entityReferenceCache;
	}

	private static final ReferenceCache _instance = new ReferenceCache();

	public static final void create(final String config) throws Exception {
		_instance.init(config);
	}

	/**
	 * Get the handle to the Reference Cache singleton.
	 * 
	 * @return
	 */
	public static final ReferenceCache get() throws Exception {
		if (_instance.state != EnumInstanceState.Running)
			throw new Exception("Invalid Cache state ["
					+ _instance.state.name() + "]. Cache is not available.");
		return _instance;
	}

}
