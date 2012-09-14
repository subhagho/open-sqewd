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
 * @created Sep 11, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public enum EnumOperator {
	Equal("="), NotEqual("!="), GreaterThan(">"), GreaterThanEqual(">="), LessThan(
			"<"), LessThanEqual("<="), Null("IS NULL"), NotNull("IS NOT NULL"), In(
			"IN"), NotIn("NOT IN"), Between("BETWEEN"), Like("LIKE"), Plus("+"), Minus(
			"-"), Multiply("*"), Divide("/"), Contains("CONTAINS");

	private static List<String> _OPERATOR_TOKENS_ = null;
	private static List<String> _OPERATOR_KEYWORDS_ = null;

	private String value;

	private EnumOperator(final String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return value;
	}

	/**
	 * Parse the input value as a Condition operator.
	 * 
	 * @param value
	 *            - String value of the the operator. Note: this is not the enum
	 *            name.
	 * @return
	 * @throws Exception
	 */
	public static EnumOperator parse(final String value) throws Exception {
		String v = value.trim();
		for (EnumOperator opr : EnumOperator.values()) {
			if (v.compareToIgnoreCase(opr.value) == 0)
				return opr;
		}
		throw new Exception("No enum constant found for [" + value + "]");
	}

	/**
	 * Try to parse the give input as an operator.
	 * 
	 * @param value
	 * @return
	 */
	public static EnumOperator tryParse(final String value) {
		try {
			return parse(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static List<String> getOperators() {
		synchronized (_OPERATOR_TOKENS_) {
			if (_OPERATOR_TOKENS_ == null) {
				_OPERATOR_TOKENS_ = new ArrayList<String>();
				_OPERATOR_TOKENS_.add(Equal.value);
				_OPERATOR_TOKENS_.add(NotEqual.value);
				_OPERATOR_TOKENS_.add(LessThan.value);
				_OPERATOR_TOKENS_.add(LessThanEqual.value);
				_OPERATOR_TOKENS_.add(GreaterThan.value);
				_OPERATOR_TOKENS_.add(GreaterThanEqual.value);
				_OPERATOR_TOKENS_.add(Plus.value);
				_OPERATOR_TOKENS_.add(Minus.value);
				_OPERATOR_TOKENS_.add(Divide.value);
				_OPERATOR_TOKENS_.add(Multiply.value);
			}
		}
		return _OPERATOR_TOKENS_;
	}

	public static List<String> getKeywords() {
		synchronized (_OPERATOR_KEYWORDS_) {
			if (_OPERATOR_KEYWORDS_ == null) {
				_OPERATOR_KEYWORDS_ = new ArrayList<String>();
				_OPERATOR_KEYWORDS_.add(Between.value);
				_OPERATOR_KEYWORDS_.add(Contains.value);
				_OPERATOR_KEYWORDS_.add(In.value);
				_OPERATOR_KEYWORDS_.add(Like.value);
				_OPERATOR_KEYWORDS_.add(NotIn.value);
				_OPERATOR_KEYWORDS_.add(Null.value);
				_OPERATOR_KEYWORDS_.add(NotNull.value);
			}
		}
		return _OPERATOR_KEYWORDS_;
	}
}
