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
 * @filename ParseStackException.java
 * @created Oct 14, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.parser;

/**
 * Thrown when the stack state while parsing a Query condition is invalid.
 * 
 * @author subhagho
 * 
 */
public class ParseStackException extends Exception {
	private static final String _PREFIX_ = "Invalid Stack State : ";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4115286574127929754L;

	/**
	 * Create a new Parse Stack Exception
	 * 
	 * @param mesg
	 *            - Exception message.
	 */
	public ParseStackException(final String mesg) {
		super(_PREFIX_ + mesg);
	}

	/**
	 * Create a new Parse Stack Exception
	 * 
	 * @param mesg
	 *            - Exception message
	 * @param ex
	 *            - Inner exception
	 */
	public ParseStackException(final String mesg, final Throwable ex) {
		super(_PREFIX_ + mesg, ex);
	}
}
