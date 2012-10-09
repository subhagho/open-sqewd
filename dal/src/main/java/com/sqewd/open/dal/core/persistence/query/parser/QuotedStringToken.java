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
 * @filename QuotedString.java
 * @created Oct 6, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.parser;

/**
 * Represents a Quoted String. Both single and double quotes are accepted.
 * 
 * @author subhagho
 * 
 */
public class QuotedStringToken extends Token {
	public static final String _QUOTED_PREFIX_ = "__QUOTEKEY__";
	private char quote;

	private String replacementKey;

	private static Integer count = 0;

	private static final String getQuoteKey() {
		synchronized (count) {
			return new StringBuffer().append(_QUOTED_PREFIX_).append(count++)
					.append("__").toString();
		}
	}

	/**
	 * Create a new instance of a Quoted String token.
	 * 
	 * @param quote
	 *            - Quote type ('/")
	 */
	public QuotedStringToken(final char quote) {
		this.quote = quote;
		replacementKey = getQuoteKey();
	}

	/**
	 * Get the replacement String for this quoted key instance. Replacement keys
	 * are put in place of quoted strings to simplify processing of the stream.
	 * 
	 * @return
	 */
	public String getKey() {
		return replacementKey;
	}

	/**
	 * Get the char representation of the quoting character.
	 * 
	 * @return
	 */
	public char getQuote() {
		return quote;
	}

	/**
	 * For this class the value contains the quoted string that was replaced.
	 * 
	 * @param value
	 */
	public void setValue(final String value) {
		this.value = value;
	}
}
