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
 * @filename SchemaObjectDatatype.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import java.util.List;

/**
 * Base class for defining supported Data types.
 * 
 * @author subhagho
 * 
 */
public abstract class SchemaObjectDatatype<T> {
	/**
	 * Get the String value of an object of type T.
	 * 
	 * @param value
	 * @return
	 */
	public abstract String toString(Object value);

	/**
	 * Parse the input string value as the current type.
	 * 
	 * @param value
	 *            - String value
	 * @return
	 * @throws Exception
	 */
	public abstract T parse(String value) throws Exception;

	/**
	 * A comparator function for comparing type values.
	 * 
	 * @param source
	 *            - Source Value, return values are with reference to the
	 *            source.
	 * @param target
	 *            - Target Value to compare to.
	 * @return - 0 -- EQUALS, -1 -- LESS THAN, 1 -- GREATER THAN
	 */
	public abstract int compare(T source, T target);

	/**
	 * Check if the values are equal.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean equals(T source, T target);

	/**
	 * Check if the source value is less than target value.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean lessThan(T source, T target);

	/**
	 * Check if the source value is less than or equal to target value.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean lessThanEqual(T source, T target);

	/**
	 * Check if the source value is more than to target value.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean moreThan(T source, T target);

	/**
	 * Check if the source value is more than or equal to target value.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean moreThanEqual(T source, T target);

	/**
	 * Check if the source value is not equal to target value.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean notEqual(T source, T target);

	/**
	 * Regex match function. Might be different for implementing platform.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value
	 * @return
	 */
	public abstract boolean like(T source, T target);

	/**
	 * Check if the source value is contained in the target value list.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value List
	 * @return
	 */
	public abstract boolean in(T source, List<T> target);

	/**
	 * Check if the source value is between the target value list.
	 * 
	 * @param source
	 *            - Source Value
	 * @param target
	 *            - Target Value List
	 * @return
	 */
	public abstract boolean between(T source, List<T> target);

	/**
	 * Check if the source value is NULL.
	 * 
	 * @param source
	 *            - Source Value
	 * @return
	 */
	public abstract boolean isNull(T source);

	/**
	 * Check if the source value is not NULL.
	 * 
	 * @param source
	 *            - Source Value
	 * @return
	 */
	public abstract boolean isNotNull(T source);

	/**
	 * Add the specified value to the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public abstract T add(T source, T value);

	/**
	 * Subtract the specified value from the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public abstract T subtract(T source, T value);

	/**
	 * Multiply the specified value to the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public abstract T multiply(T source, T value);

	/**
	 * Divide the specified value to the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public abstract T divide(T source, T value);
}
