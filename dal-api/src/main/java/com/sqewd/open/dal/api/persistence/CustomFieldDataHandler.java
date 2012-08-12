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
import com.sqewd.open.dal.api.persistence.AbstractEntity;

/**
 * Interface to be implemented for setting/getting custom data elements for an
 * Entity.
 * 
 * @author subhagho
 * 
 */
public interface CustomFieldDataHandler {
	/**
	 * Load a field value from the data record.
	 * 
	 * @param entity
	 *            - Entity Instance
	 * @param field
	 *            - Field to load
	 * @param record
	 *            - Data record
	 * @throws Exception
	 */
	public void load(AbstractEntity entity, String field, Object data)
			throws Exception;

	/**
	 * Save a field value into the Data record.
	 * 
	 * @param entity
	 *            - Entity Instance
	 * @param field
	 *            - Field to Save
	 * @return
	 * @throws Exception
	 */
	public Object save(AbstractEntity entity, String field) throws Exception;

	/**
	 * Get the data type used to persist this record.
	 * 
	 * @return
	 */
	public Class<?> getDataType();
}
