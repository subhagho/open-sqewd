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
 * @filename BasePlanGenerator.java
 * @created Oct 24, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import net.sf.ehcache.Cache;

import com.sqewd.open.dal.api.DataCache;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.query.PlanContext;
import com.sqewd.open.dal.api.persistence.query.PlanGenerator;
import com.sqewd.open.dal.api.persistence.query.PlanNode;
import com.sqewd.open.dal.core.persistence.DataManager;

/**
 * Base class for generating a plan context.
 * 
 * @author subhagho
 * 
 */
public abstract class BasePlanGenerator implements PlanGenerator {
	private Cache cache = null;

	public BasePlanGenerator() throws Exception {
		cache = DataCache.instance().get(DataManager._CACHE_KEY_PLAN_);
	}

	private synchronized PlanContext create(final AbstractEntity entity)
			throws Exception {
		// Check again if entity plan context is already created.
		if (cache.isKeyInCache(entity.getClass().getCanonicalName()))
			return (PlanContext) cache
					.get(entity.getClass().getCanonicalName()).getObjectValue();

		// Not found in cache. Create new Plan
		PlanContext ctx = new PlanContext();

		process(ctx, entity);

		return ctx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.PlanGenerator#create(com.sqewd
	 * .open.dal.api.persistence.AbstractEntity)
	 */
	public PlanContext get(final AbstractEntity entity) throws Exception {
		PlanContext ctx = null;
		// Check if entity is already present in the cache.
		if (cache.isKeyInCache(entity.getClass().getCanonicalName())) {
			ctx = (PlanContext) cache.get(entity.getClass().getCanonicalName())
					.getObjectValue();
		} else {
			// Create plan context for entity.
			ctx = create(entity);
		}
		return ctx;
	}

	/**
	 * Create a plan node for the specified entity.
	 * 
	 * @param ctx
	 *            - Plan Context
	 * @param entity
	 *            - Entity to generate plan node for.
	 * @return
	 * @throws Exception
	 */
	protected abstract PlanNode process(PlanContext ctx, AbstractEntity entity)
			throws Exception;
}
