/**
 * 
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.Date;

import com.sqewd.open.dal.api.persistence.EnumPrimitives;

/**
 * @author subhagho
 * 
 */
public enum SQLDataType {
	/**
	 * java.lang.Short
	 */
	SMALLINT,
	/**
	 * java.lang.Integer
	 */
	INTEGER,
	/**
	 * java.lang.Long
	 */
	BIGINT,
	/**
	 * java.lang.Boolean
	 */
	BOOLEAN,
	/**
	 * java.lang.Double
	 */
	DOUBLE,
	/**
	 * java.lang.Float
	 */
	REAL,
	/**
	 * java.lang.String (MAX SIZE 1024)
	 */
	VARCHAR2;

	public static SQLDataType type(Class<?> type) throws Exception {
		if (EnumPrimitives.isPrimitiveType(type)) {
			EnumPrimitives prim = EnumPrimitives.type(type);
			switch (prim) {
			case ECharacter:
				return VARCHAR2;
			case EShort:
				return SMALLINT;
			case EInteger:
				return INTEGER;
			case ELong:
				return BIGINT;
			case EFloat:
				return REAL;
			case EDouble:
				return DOUBLE;
			default:
				break;
			}
		} else if (type.equals(String.class)) {
			return VARCHAR2;
		} else if (type.equals(Date.class)) {
			return BIGINT;
		} else if (type.isEnum()) {
			return VARCHAR2;
		}
		throw new Exception("Unsupport SQL Data type ["
				+ type.getCanonicalName() + "]");
	}
}
