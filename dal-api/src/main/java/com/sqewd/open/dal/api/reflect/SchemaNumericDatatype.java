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
 * @filename SchemaNumericDatatype.java
 * @created Oct 17, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

/**
 * Base class for defining supported Numeric Data types.
 * 
 * @author subhagho
 * 
 */
public interface SchemaNumericDatatype<T> {
	/**
	 * Add the specified value to the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public T add(T source, T value);

	/**
	 * Divide the specified value to the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public T divide(T source, T value);

	/**
	 * Get the modulus for source%value.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public T mod(T source, T value);

	/**
	 * Multiply the specified value to the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public T multiply(T source, T value);

	/**
	 * Get the value of source^value.
	 * 
	 * @param source
	 * @param value
	 * @return
	 */
	public T pow(T source, T value);

	/**
	 * Subtract the specified value from the source.
	 * 
	 * @param source
	 *            - Source Value
	 * @param value
	 * @return
	 */
	public T subtract(T source, T value);

}
