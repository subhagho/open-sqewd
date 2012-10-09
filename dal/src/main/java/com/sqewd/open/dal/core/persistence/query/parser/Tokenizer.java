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
 * @filename Tokenizer.java
 * @created Oct 7, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Tokenize the query string.
 * 
 * @author subhagho
 * 
 */
public class Tokenizer {
	public static final String[] _TOKENS_ = { "(", ";", ",", ")", "==", "!=",
			">", ">=", "<", "<=", "in", "between", "like", "null", "+", "-",
			"*", "/", "sort", "[", "]", ":" };

	private char[] buffer;
	private int offset;

	private List<Token> tokens = new ArrayList<Token>();

	/**
	 * Create an instance of the tokenizer based on the data buffer.
	 * 
	 * @param buffer
	 *            - data buffer
	 */
	public Tokenizer(final char[] buffer) {
		this.buffer = buffer;
		offset = 0;
	}

	private boolean checkToken(String token, final int index) {
		token = token.toLowerCase();
		if (token.compareTo(">") == 0 || token.compareTo("<") == 0) {
			if (buffer[index + 1] == '=')
				return false;
		} else if (token.compareTo("in") == 0
				|| token.compareTo("between") == 0) {
			if (index > 0 && !Character.isWhitespace(buffer[index - 1]))
				return false;
			if (Character.isWhitespace(buffer[index + token.length()]))
				return true;
			else if (buffer[index + token.length()] == '[')
				return true;
			else
				return false;
		} else if (token.compareTo("like") == 0 || token.compareTo("not") == 0
				|| token.compareTo("null") == 0 || token.compareTo("sort") == 0) {
			if (index > 0 && !Character.isWhitespace(buffer[index - 1]))
				return false;
			if (!Character.isWhitespace(buffer[index + token.length()]))
				return false;
		}
		return true;
	}

	private boolean match(String token, final int index) {
		token = token.toLowerCase();
		char[] part = token.toCharArray();
		for (int ii = 0; ii < part.length; ii++) {
			if (part[ii] != buffer[index + ii])
				return false;
		}
		return true;
	}

	private void skipWhitespace() {
		for (int ii = offset; ii < buffer.length; ii++) {
			if (!Character.isWhitespace(buffer[ii])) {
				offset = ii;
				return;
			}
		}
		offset = buffer.length;
	}

	/**
	 * Tokenize the query string based on the available tokens.
	 * 
	 */
	public void tokenize() {
		skipWhitespace();
		Token tk = null;
		int lastoffset = 0;

		while (offset < buffer.length) {
			boolean incr = true;
			for (String token : _TOKENS_) {
				if (match(token, offset)) {
					if (checkToken(token, offset)) {
						if (tk == null && offset > 0) {
							tk = new Token();
							tk.setStartIndex(lastoffset);
							tokens.add(tk);
						}
						if (tk != null) {
							tk.setEndIndex(offset);
						}
						tk = new Token();
						tk.setStartIndex(offset);
						tk.setToken(token);
						tk.setEndIndex(offset + token.length());
						tokens.add(tk);
						tk = null;
						offset += token.length();
						lastoffset = offset;
						incr = false;
						break;
					}
				}
			}
			if (incr) {
				offset++;
			}
		}
		if (lastoffset < buffer.length) {
			tk = new Token();
			tk.setStartIndex(lastoffset);
			tk.setEndIndex(buffer.length);
			tokens.add(tk);
		}
	}

	/**
	 * Get the list of parsed tokens.
	 * 
	 * @return
	 */
	public List<Token> tokens() {
		return tokens;
	}
}
