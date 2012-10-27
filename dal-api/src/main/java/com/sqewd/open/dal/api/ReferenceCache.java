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

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.sqewd.open.dal.api.reflect.EntityDef;

/**
 * Singleton Object for accessing the reference cache. Reference cache is used
 * to store runtime definitions. This cache shouldn't contain any data elements.
 * 
 * @author subhagho
 * 
 */
public class ReferenceCache {
	public static final String _CACHE_ENTITYREFERENCE_ = "reference.entity";

	private static final ReferenceCache _instance = new ReferenceCache();

	/**
	 * Get the handle to the Reference Cache singleton.
	 * 
	 * @return
	 */
	public static final ReferenceCache get() throws Exception {
		return _instance;
	}

	/**
	 * Add the entity definition to the cache.
	 * 
	 * @param entitydef
	 * @throws Exception
	 */
	public void addEntityDef(final EntityDef entitydef) throws Exception {
		String ckey = entitydef.getClasstype().getCanonicalName();

		Cache cache = DataCache.instance().get(_CACHE_ENTITYREFERENCE_);

		if (cache.get(ckey) != null)
			throw new Exception("Entity definition with entity class [" + ckey
					+ "] already added to cache.");
		Element elm = new Element(ckey, entitydef);
		cache.put(elm);
	}

	/**
	 * Get the Entity Definition based on the entity class.
	 * 
	 * @param ec
	 *            - Entity class
	 * @return
	 * @throws Exception
	 */
	public EntityDef getEntityDef(final Class<?> ec) throws Exception {
		Cache cache = DataCache.instance().get(_CACHE_ENTITYREFERENCE_);

		Element elm = cache.get(ec.getCanonicalName());
		if (elm != null)
			return (EntityDef) elm.getObjectValue();
		return null;
	}

	/**
	 * Get all the loaded entity definitions from the cache.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EntityDef> getEntityDefs() throws Exception {
		Cache cache = DataCache.instance().get(_CACHE_ENTITYREFERENCE_);

		if (cache != null) {
			List<?> keys = cache.getKeys();
			if (keys != null && !keys.isEmpty()) {
				List<EntityDef> defs = new ArrayList<EntityDef>();
				for (Object obj : keys) {
					Element elm = cache.get(obj);
					if (elm.getObjectValue() != null
							&& elm.getObjectValue() instanceof EntityDef) {
						defs.add((EntityDef) elm.getObjectValue());
					} else
						throw new Exception(
								"Invalid Cache Data : NULL value or incorrect Object type for EntityDef");
				}
				return defs;
			}
		} else
			throw new Exception(
					"Entity Reference Cache not initialized or has been shutdown.");
		return null;
	}

	/**
	 * @return the entityReferenceCache
	 */
	public Cache getEntityReferenceCache() throws Exception {
		return DataCache.instance().get(_CACHE_ENTITYREFERENCE_);
	}
}
