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
package com.sqewd.open.dal.core.persistence.query;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractEntity;

/**
 * Base class for definition of entity selection queries.
 * 
 * @author subhagho
 * 
 */
public abstract class Query {
	public static final String _QUERY_CONDITION_AND_ = ";";
	public static final String _QUERY_CONDITION_OR_ = ",";

	/**
	 * Parse the passed query.
	 * 
	 * @param query
	 *            - Query String
	 * @throws Exception
	 */
	public abstract void parse(List<Class<?>> tables, String query)
			throws Exception;

	/**
	 * @param tables
	 * @param query
	 * @throws Exception
	 */
	public abstract void parse(Class<?>[] tables, String query) throws Exception;

	/**
	 * Does the specified entity match the filter condition.
	 * 
	 * @param entity
	 *            - Entity to process.
	 * @return
	 * @throws Exception
	 */
	public abstract boolean doSelect(AbstractEntity entity) throws Exception;

	/**
	 * Filter the list of entities and return the filtered list.
	 * 
	 * @param entities
	 *            - Entities to filter.
	 * @return
	 * @throws Exception
	 */
	public abstract List<AbstractEntity> select(List<AbstractEntity> entities)
			throws Exception;
}
