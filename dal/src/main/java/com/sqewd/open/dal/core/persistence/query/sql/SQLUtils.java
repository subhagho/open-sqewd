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
 * @filename SQLUtils.java
 * @created Aug 21, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import com.sqewd.open.dal.api.reflect.SchemaObjectAttribute;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumConditionOperator;

/**
 * Utility functions for SQL implementation.
 * 
 * @author subhagho
 * 
 */
public class SQLUtils {

	/**
	 * Convert the Condition operator to the SQL operator.
	 * 
	 * @param operator
	 * @return
	 */
	public static String convertOperator(final EnumConditionOperator operator) {
		switch (operator) {
		case Equals:
			return "=";
		case MoreThan:
			return ">";
		case MoreThanEquals:
			return ">=";
		case LessThan:
			return "<";
		case LessThanEquals:
			return "<=";
		case NotEqualTo:
			return "<>";
		case Like:
			return "LIKE";
		case Between:
			return "BETWEEN";
		case In:
			return "IN";
		case Add:
			return "+";
		case Subtract:
			return "-";
		case Multiply:
			return "*";
		case Divide:
			return "/";
		default:
			break;
		}
		return null;
	}

	/**
	 * Check if Field is of type String and quote the value.
	 * 
	 * @param value
	 *            - Value Object
	 * @param fd
	 *            - Field type.
	 * @return
	 * @throws Exception
	 */
	public static Object getQuotedString(final Object value, final Field fd)
			throws Exception {
		if (fd.getType().equals(String.class))
			return "'" + value.toString() + "'";
		return value;
	}

	/**
	 * Get the corresponding Data type for the specified SQL datatype.
	 * 
	 * @param name
	 *            - SQL Data type.
	 * @return
	 * @throws Exception
	 */
	public static SqlDataType<?> getSqlDatatype(final String name)
			throws Exception {
		if (name.compareToIgnoreCase("CHAR") == 0)
			return new SqlCharType();
		else if (name.compareToIgnoreCase("VARCHAR") == 0)
			return new SqlVarcharType();
		else if (name.compareToIgnoreCase("LONGVARCHAR") == 0)
			return new SqlLongVarcharType();
		else if (name.compareToIgnoreCase("NUMERIC") == 0)
			return new SqlNumericType();
		else if (name.compareToIgnoreCase("DECIMAL") == 0)
			return new SqlDecimalType();
		else if (name.compareToIgnoreCase("BIT") == 0)
			return new SqlBitType();
		else if (name.compareToIgnoreCase("TINYINT") == 0)
			return new SqlTinyIntType();
		else if (name.compareToIgnoreCase("SMALLINT") == 0)
			return new SqlShortType();
		else if (name.compareToIgnoreCase("INTEGER") == 0)
			return new SqlIntType();
		else if (name.compareToIgnoreCase("BIGINT") == 0)
			return new SqlBigIntType();
		else if (name.compareToIgnoreCase("REAL") == 0)
			return new SqlRealType();
		else if (name.compareToIgnoreCase("FLOAT") == 0)
			return new SqlFloatType();
		else if (name.compareToIgnoreCase("DOUBLE") == 0)
			return new SqlDoubleType();
		else if (name.compareToIgnoreCase("DATE") == 0)
			return new SqlDateType();
		else if (name.compareToIgnoreCase("TIME") == 0)
			return new SqlDateType();
		else if (name.compareToIgnoreCase("TIMESTAMP") == 0)
			return new SqlDateType();
		throw new Exception("No supported datatype found for [" + name + "]");
	}

	/**
	 * Convert a SQL Match pattern to a Regex match pattern and compare with
	 * passed source.
	 * 
	 * @param source
	 *            - Source String
	 * @param pattern
	 *            - DB Pattern or Regex to match with.
	 * @return
	 */
	public static boolean match(final String source, String pattern) {
		if (pattern.indexOf('%') >= 0) {
			pattern = pattern.replaceAll("%", ".*");
		}
		return Pattern.matches(pattern, source);
	}

	/**
	 * Convert the given value to a String.
	 * 
	 * @param value
	 * @param attr
	 * @return
	 */
	public String getSqlValue(final Object value,
			final SchemaObjectAttribute attr) {
		if (attr.getType() instanceof SqlVarcharType)
			return "'" + value.toString() + "'";
		else if (attr.getType() instanceof SqlLongVarcharType)
			return "'" + value.toString() + "'";
		else if (value instanceof String)
			return (String) value;
		return attr.getType().toString(value);
	}
}
