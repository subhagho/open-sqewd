/**
 * Copyright 2012 Subho Ghosh (subho.ghosh at outlook dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @filename SqlDataType.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.sqewd.open.dal.api.reflect.SchemaObjectDatatype;

/**
 * Abstract SQL Data type.
 * 
 * @author subhagho
 * 
 */
public abstract class SqlDataType<T> extends SchemaObjectDatatype<T> {

	/**
	 * Set the column value for the prepared statement.
	 * 
	 * @param pstmnt
	 *            - SQL Prepared Statement
	 * @param index
	 *            - Index to set the value at.
	 * @param value
	 *            - Value to set.
	 * @throws Exception
	 */
	public abstract void setValue(PreparedStatement pstmnt, int index, T value)
			throws Exception;

	/**
	 * Get the column value from the result set.
	 * 
	 * @param rs
	 *            - SQL Result Set
	 * @param index
	 *            - Column index.
	 * @return
	 * @throws Exception
	 */
	public abstract T getValue(ResultSet rs, int index) throws Exception;

	/**
	 * Get the column value from the result set.
	 * 
	 * @param rs
	 *            - SQL Result Set
	 * @param column
	 *            - Column Name/Alias
	 * @return
	 * @throws Exception
	 */
	public abstract T getValue(ResultSet rs, String column) throws Exception;

	/**
	 * Get the corresponding SQL type for specified Java type.
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static EnumSqlTypes getSqlType(final Class<?> type) throws Exception {
		if (type.equals(String.class))
			return EnumSqlTypes.VARCHAR2;
		else if (type.equals(Boolean.class))
			return EnumSqlTypes.BIT;
		else if (type.equals(Character.class))
			return EnumSqlTypes.CHAR;
		else if (type.equals(Date.class))
			return EnumSqlTypes.TIMESTAMP;
		else if (type.equals(Double.class))
			return EnumSqlTypes.DOUBLE;
		else if (type.equals(Float.class))
			return EnumSqlTypes.FLOAT;
		else if (type.equals(Short.class))
			return EnumSqlTypes.SHORT;
		else if (type.equals(Byte.class))
			return EnumSqlTypes.TINYINT;
		throw new Exception("Unsupported SQL type [" + type.getCanonicalName()
				+ "]");
	}
}
