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
 * @filename PlanGenerator.java
 * @created Oct 23, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.persistence.query;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.reflect.EntityDef;

/**
 * Interface to Generate an execution plan.
 * 
 * @author subhagho
 * 
 */
public interface PlanGenerator {
	/**
	 * Add the entity as a node to the specified plan.
	 * 
	 * @param entity
	 *            - Entity to generate plan for.
	 * @param parent
	 *            - Parent plan node.
	 * @throws Exception
	 */
	public void addContext(AbstractEntity entity, PlanNode parent)
			throws Exception;

	/**
	 * Add the entity as a node to the specified plan.
	 * 
	 * @param entity
	 *            - Entity to generate plan for.
	 * @param parent
	 *            - Parent plan node.
	 * @throws Exception
	 */
	public void addContext(EntityDef entity, PlanNode parent) throws Exception;

	/**
	 * Create a execution plan for the specified entity.
	 * 
	 * @param entity
	 *            - Root entity to generate the plan for.
	 * @return
	 * @throws Exception
	 */
	public PlanContext get(AbstractEntity entity) throws Exception;
}
