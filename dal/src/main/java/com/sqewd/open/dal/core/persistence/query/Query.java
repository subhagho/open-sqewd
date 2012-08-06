/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.List;

import com.sqewd.open.dal.core.persistence.AbstractEntity;

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
