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
 * @filename DataCache.java
 * @created Oct 23, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import com.sqewd.open.dal.api.utils.ListParam;

/**
 * Local Data Cache to be used for caching data on each instance.
 * 
 * @author subhagho
 * 
 */
public class DataCache implements InitializedHandle {
	public static final String _CACHE_DATA_ = "cache.local.data";

	public static final String _CONFIG_CACHE_CONFIG_ = "cache.local.data.config";

	private EnumInstanceState state = EnumInstanceState.Unknown;

	private CacheManager dataCacheManager = null;

	private static final DataCache _cache_ = new DataCache();

	/**
	 * Initialize the Data Cache instance.
	 * 
	 * @param config
	 * @throws Exception
	 */
	public static final void create(final String config) throws Exception {
		synchronized (_cache_) {
			if (_cache_.state == EnumInstanceState.Running)
				throw new Exception("Cache is already initialized and running.");
			_cache_.init(config);
		}
	}

	/**
	 * Get the handle to the Data Cache.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final DataCache instance() throws Exception {
		if (_cache_.state != EnumInstanceState.Running)
			throw new Exception(
					"Invalid Cache State : Cache not initialized or initialization failed. [state="
							+ _cache_.state.name() + "]");
		return _cache_;
	}

	/**
	 * Create a new Cache handle.
	 * 
	 * @param name
	 * @param config
	 * @throws Exception
	 */
	public Cache createCache(final String name, final CacheConfiguration config)
			throws Exception {
		if (state != EnumInstanceState.Running)
			throw new Exception(
					"Cache Manager : Invalid State, initialization failed or not called. [state="
							+ state.name() + "]");
		if (dataCacheManager.cacheExists(name))
			throw new Exception("Cache with name [" + name
					+ "] already exists.");

		Cache cache = new Cache(config);
		cache.setName(name);

		dataCacheManager.addCache(cache);

		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.InitializedHandle#dispose()
	 */
	public void dispose() {
		if (dataCacheManager != null) {
			dataCacheManager.clearAll();
		}
		dataCacheManager = null;
	}

	/**
	 * Get the handle to a named cache.
	 * 
	 * @param name
	 *            - Cache name.
	 * @return
	 * @throws Exception
	 */
	public Cache get(final String name) throws Exception {
		if (!dataCacheManager.cacheExists(name))
			throw new Exception("Cache with name [" + name + "] not found.");
		return dataCacheManager.getCache(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.InitializedHandle#init(com.sqewd.open.dal.api.
	 * utils.ListParam)
	 */
	public void init(final ListParam param) throws Exception {
		throw new Exception("Method should never be called.");
	}

	private void init(final String config) throws Exception {
		try {
			dataCacheManager = CacheManager.newInstance(config);
			state = EnumInstanceState.Running;
		} catch (Exception e) {
			state = EnumInstanceState.Exception;
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.InitializedHandle#key()
	 */
	public String key() {
		return _CACHE_DATA_;
	}

	/**
	 * Remove the specified cache handle.
	 * 
	 * @param name
	 */
	public void remove(final String name) {
		if (dataCacheManager.cacheExists(name)) {
			dataCacheManager.removeCache(name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.InitializedHandle#state()
	 */
	public EnumInstanceState state() {
		return state;
	}
}
