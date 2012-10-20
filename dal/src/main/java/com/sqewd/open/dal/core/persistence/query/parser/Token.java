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
 * @filename Token.java
 * @created Oct 6, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.parser;

/**
 * Class represents a String token as extracted from the input stream.
 * 
 * @author subhagho
 * 
 */
public class Token {
	/**
	 * String part that this token represents.
	 */
	protected String token;

	/**
	 * The start index of this token in the stream.
	 */
	protected int startIndex;
	/**
	 * The end index of this token in the stream.
	 */
	protected int endIndex;

	protected String value;

	/**
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Extract the original value that represents this token from the stream.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getValue() throws Exception {
		return value;
	}

	/**
	 * Check if the current token represents an AND operator
	 * 
	 * @return
	 */
	public boolean isAnd() {
		if (token != null && token.compareTo(";") == 0)
			return true;
		return false;
	}

	/**
	 * Check if token is an arithmetic operator.
	 * 
	 * @return
	 */
	public boolean isArithmeticOperator() {
		if (token != null) {
			if (token.compareTo("+") == 0 || token.compareTo("-") == 0
					|| token.compareTo("*") == 0 || token.compareTo("/") == 0)
				return true;
		}
		return false;
	}

	/**
	 * Check if the current token represents an open Brace
	 * 
	 * @return
	 */
	public boolean isCloseBrace() {
		if (token != null && token.compareTo(")") == 0)
			return true;
		return false;
	}

	/**
	 * Check if the token is an empty or blank string.
	 * 
	 * @return
	 */
	public boolean isEmptyToken() {
		if (!isSpecialToken()) {
			if (value == null || value.isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Check if the current token represents the list terminator
	 * 
	 * @return
	 */
	public boolean isListEnd() {
		if (token != null && token.compareTo("]") == 0)
			return true;
		return false;
	}

	/**
	 * Check if the current token represents the list opener
	 * 
	 * @return
	 */
	public boolean isListStart() {
		if (token != null && token.compareTo("[") == 0)
			return true;
		return false;
	}

	/**
	 * Check if the current token represents a close Brace
	 * 
	 * @return
	 */
	public boolean isOpenBrace() {
		if (token != null && token.compareTo("(") == 0)
			return true;
		return false;
	}

	/**
	 * Check if the current token represents an operator
	 * 
	 * @return
	 */
	public boolean isOperator() {
		if (token != null
				&& (token.compareTo("==") == 0 || token.compareTo("!=") == 0 || token
						.compareTo(">") == 0) || token.compareTo(">=") == 0
				|| token.compareTo("<") == 0 || token.compareTo("<=") == 0
				|| token.compareTo("!=") == 0
				|| token.compareToIgnoreCase("in") == 0
				|| token.compareToIgnoreCase("like") == 0
				|| token.compareToIgnoreCase("between") == 0)
			return true;
		return false;
	}

	/**
	 * Check if the current token represents an OR operator
	 * 
	 * @return
	 */
	public boolean isOr() {
		if (token != null && token.compareTo(",") == 0)
			return true;
		return false;
	}

	/**
	 * Check if the current token represents an Sort keyword.
	 * 
	 * @return
	 */
	public boolean isSort() {
		if (token != null) {
			if (token.compareToIgnoreCase("sort") == 0)
				return true;
		}
		return false;
	}

	/**
	 * Check if this is a special token or a value token.
	 * 
	 * @return
	 */
	public boolean isSpecialToken() {
		return !(token == null);
	}

	/**
	 * @param endIndex
	 *            the endIndex to set
	 */
	public void setEndIndex(final int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(final int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @param part
	 *            the token to set
	 */
	public void setToken(final String part) {
		token = part;
	}

	/**
	 * Set the value based on the start and end indexes.
	 * 
	 * @param stream
	 * @throws Exception
	 */
	public void setValue(final char[] stream) throws Exception {
		if (value == null) {
			value = new String(stream, startIndex, endIndex - startIndex)
					.trim();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value;
	}
}
