/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * @author subhagho
 * 
 */
public enum EnumSortOrder {
	/**
	 * Sort Ascending.
	 */
	ASC,
	/**
	 * Sort Descending.
	 */
	DSC;

	/**
	 * Parse the Sort Order string.
	 * 
	 * @param value
	 *            - Order String
	 * @return
	 */
	public static EnumSortOrder parse(String value) {
		value = value.toUpperCase();
		return valueOf(value);
	}
}
