/**
 * 
 */
package com.sqewd.open.dal.core.persistence.csv;

/**
 * @author subhagho
 * 
 */
public enum EnumImportFormat {
	CSV, TSV;

	/**
	 * Parse the string as the format enum.
	 * 
	 * @param value
	 *            - format value.
	 * @return
	 * @throws Exception
	 */
	public static EnumImportFormat parse(String value) throws Exception {
		return EnumImportFormat.valueOf(value.trim().toUpperCase());
	}
}
