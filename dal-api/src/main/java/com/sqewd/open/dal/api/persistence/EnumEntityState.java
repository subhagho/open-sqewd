/**
 * 
 */
package com.sqewd.open.dal.api.persistence;

/**
 * Enum defines the memory state of an Entity instance.
 * 
 * @author subhagho
 * 
 */
public enum EnumEntityState {
	/**
	 * Newly created entity, hasn't yet been persisted.
	 */
	New,
	/**
	 * Memory instance of the entity has been updated. Not yet persisted.
	 */
	Updated,
	/**
	 * Entity has been deleted. Delete pending commit.
	 */
	Deleted,
	/**
	 * Entity loaded from the persistence store.
	 */
	Loaded,
	/**
	 * Force overwrite of data if timestamp conflict exists.
	 */
	Overwrite,
	/**
	 * Entity state unknown.
	 */
	Unknown;
}
