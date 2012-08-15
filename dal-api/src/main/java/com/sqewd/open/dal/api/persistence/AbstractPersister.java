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

import java.util.List;

import com.sqewd.open.dal.api.EnumInstanceState;
import com.sqewd.open.dal.api.InitializedHandle;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
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
	 * Get the key to be used to lookup this Persister in the cache. Usually the
	 * key should be the absolute classname of the type T.
	 * 
	 * @return
	 */
	public String key() {
		return key;
	}

	/**
	 * Initialize the persistence handler.
	 * 
	 * @param params
	 *            - Initialization parameters.
	 * @throws Exception
	 */
	public abstract void init(ListParam params) throws Exception;

	/**
	 * Method do be called post initialization.
	 * 
	 * @throws Exception
	 */
	public abstract void postinit() throws Exception;

	/**
	 * Load a list of entity records based on the column keys specified.
	 * 
	 * @note Search keys are ANDED, no grouping operations are supported.
	 * @param columnkeys
	 *            - List of Column->Value to be used for searching.
	 * @param type
	 *            - Class type of the entity to search.
	 * @return
	 * @throws Exception
	 */
	public abstract List<AbstractEntity> read(String query, Class<?>... types)
			throws Exception;

	/**
	 * Persist the specified entity record.
	 * 
	 * @param record
	 *            - Entity record instance.
	 * @throws Exception
	 */
	public abstract int save(AbstractEntity record, boolean overwrite)
			throws Exception;

	/**
	 * Bulk save a list of entity records.
	 * 
	 * @param records
	 *            - List of entity records.
	 * @throws Exception
	 */
	public abstract int save(List<AbstractEntity> records, boolean overwrite)
			throws Exception;

}
