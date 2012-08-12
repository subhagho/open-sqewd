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
