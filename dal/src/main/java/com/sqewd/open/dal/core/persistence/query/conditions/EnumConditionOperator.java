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
 * @filename EnumConditionOperator.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.conditions;

/**
 * Enumeration type for Condition Operators.
 * 
 * @author subhagho
 * 
 */
public enum EnumConditionOperator {
	/**
	 * Equal to (=)
	 */
	Equals,
	/**
	 * Less Than (<)
	 */
	LessThan,
	/**
	 * Less Than Equal To (<=)
	 */
	LessThanEquals,
	/**
	 * More Than (>)
	 */
	MoreThan,
	/**
	 * More Than Equal To (>=)
	 */
	MoreThanEquals,
	/**
	 * Not Equal To (!=)
	 */
	NotEqualTo,
	/**
	 * Contained in List (IN)
	 */
	In,
	/**
	 * Between Given values. (BETWEEN)
	 */
	Between,
	/**
	 * String match operator, Implementing source dependent functionality
	 */
	Like,
	/**
	 * Add (+)
	 */
	Add,
	/**
	 * Subtract (-)
	 */
	Subtract,
	/**
	 * Multiply (*)
	 */
	Multiply,
	/**
	 * Divide (/)
	 */
	Divide;

	/**
	 * Parse the passed value as a Condition operator enum.
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static EnumConditionOperator parse(final String value)
			throws Exception {
		if (value.compareTo("==") == 0)
			return Equals;
		else if (value.compareTo("!=") == 0)
			return NotEqualTo;
		else if (value.compareTo("<") == 0)
			return LessThan;
		else if (value.compareTo("<=") == 0)
			return LessThanEquals;
		else if (value.compareTo(">") == 0)
			return MoreThan;
		else if (value.compareTo(">=") == 0)
			return MoreThanEquals;
		else if (value.compareTo("+") == 0)
			return Add;
		else if (value.compareTo("-") == 0)
			return Subtract;
		else if (value.compareTo("*") == 0)
			return Multiply;
		else if (value.compareTo("/") == 0)
			return Divide;
		else if (value.compareToIgnoreCase("in") == 0)
			return In;
		else if (value.compareToIgnoreCase("between") == 0)
			return In;
		else if (value.compareToIgnoreCase("like") == 0)
			return In;
		return null;
	}
}
