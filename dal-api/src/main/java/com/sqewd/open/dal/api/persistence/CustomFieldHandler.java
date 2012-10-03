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

/**
 * Interface to be implemented for setting/getting custom data elements for an
 * Entity.
 * 
 * @author subhagho
 * 
 */
public interface CustomFieldHandler {
	/**
	 * Convert the passed data object to the specified type.
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object get(Object data) throws Exception;

	/**
	 * Convert the Entity field value to be persisted.
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object set(Object data) throws Exception;

	/**
	 * Get the data type used to persist this record.
	 * 
	 * @return
	 */
	public Class<?> getDataType();
}
