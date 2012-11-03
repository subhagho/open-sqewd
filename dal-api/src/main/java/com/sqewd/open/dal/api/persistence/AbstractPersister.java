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
package com.sqewd.open.dal.api.persistence;

import java.sql.ResultSet;
import java.util.List;

import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.InitializedHandle;
import com.sqewd.open.dal.api.persistence.query.PlanGenerator;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.reflect.SchemaObject;
import com.sqewd.open.dal.api.utils.ListParam;

/**
 * Abstract persistence handler. All handlers to persist entities should inherit
 * from this class.
 * 
 * @author subhagho
 * 
 * @param <T>
 *            - Entity Type(s).
 */
public abstract class AbstractPersister implements InitializedHandle {
	public static final String _PARAM_KEY_ = "key";

	protected EnumInstanceState state = EnumInstanceState.Unknown;

	protected String key;

	/**
	 * Get the Plan Context generator for this driver.
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract PlanGenerator getPlanGenerator() throws Exception;

	/**
	 * Load the persistence definition for the specified Schema Object.
	 * 
	 * @param entity
	 *            - Entity type.
	 * @return
	 * @throws Exception
	 */
	public abstract SchemaObject getSchemaObject(EntityDef entity)
			throws Exception;

	/**
	 * Initialize the persistence handler.
	 * 
	 * @param params
	 *            - Initialization parameters.
	 * @throws Exception
	 */
	public abstract void init(ListParam params) throws Exception;

	/**
	 * Get the key to be used to lookup this Persister in the cache. Usually the
	 * key should be the absolute classname of the type T.
	 * 
	 * @return
	 */
	public String key() {
		return key;
	}

	/**
	 * Method do be called post initialization.
	 * 
	 * @throws Exception
	 */
	public abstract void postinit() throws Exception;

	/**
	 * Read the entities of the specified from the persistence store.
	 * 
	 * @param query
	 *            - Query Condition to filter the result set by.
	 * @param type
	 *            - Entity type to fetch.
	 * @param limit
	 *            - Limit the size of the Results to.
	 * @param debug
	 *            - Turn debug trace on for this request.
	 * @return
	 * @throws Exception
	 */
	public abstract List<AbstractEntity> read(String query, Class<?> type,
			int limit, boolean debug) throws Exception;

	/**
	 * Persist the specified entity record.
	 * 
	 * @param record
	 *            - Entity record instance.
	 * @param overwrite
	 *            - Force overwrite if entity already exists else insert.
	 * @param debug
	 *            - Turn debug trace on for this request.
	 * @throws Exception
	 */
	public abstract OperationResponse save(AbstractEntity record,
			boolean overwrite, boolean debug) throws Exception;

	/**
	 * Bulk save a list of entity records.
	 * 
	 * @param records
	 *            - List of entity records.
	 * @param overwrite
	 *            - Force overwrite if entity already exists else insert.
	 * @param debug
	 *            - Turn debug trace on for this request.
	 * @throws Exception
	 */
	public abstract PersistenceResponse save(List<AbstractEntity> records,
			boolean overwrite, boolean debug) throws Exception;

	/**
	 * Load the entity records for the specified entity and return as a SQL
	 * ResultSet.
	 * 
	 * @param query
	 *            - Query Condition to filter the result set by.
	 * @param types
	 *            - Entity types to fetch.
	 * @param ctx
	 *            - Specify the Cursor Context for this request.
	 * @return - Will return ResultSet of type LocalResultSet.
	 * @throws Exception
	 */
	public abstract ResultSet select(String query, Class<?> types,
			CursorContext ctx) throws Exception;

}
