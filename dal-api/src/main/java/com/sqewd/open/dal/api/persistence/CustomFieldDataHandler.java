/**
 * 
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
