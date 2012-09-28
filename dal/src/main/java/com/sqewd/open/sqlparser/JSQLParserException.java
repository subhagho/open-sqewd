/* ================================================================
 * JSQLParser : java based sql parser 
 * ================================================================
 *
 * Project Info:  http://jsqlparser.sourceforge.net
 * Project Lead:  Leonardo Francalanci (leoonardoo@yahoo.it);
 *
 * (C) Copyright 2004, by Leonardo Francalanci
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package com.sqewd.open.sqlparser;

/**
 * An exception class with stack trace informations
 */
@SuppressWarnings("serial")
public class JSQLParserException extends Exception {
	private Throwable cause = null;

	public JSQLParserException() {
		super();
	}

	public JSQLParserException(final String arg0) {
		super(arg0);
	}

	public JSQLParserException(final Throwable arg0) {
		this.cause = arg0;
	}

	public JSQLParserException(final String arg0, final Throwable arg1) {
		super(arg0);
		this.cause = arg1;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

	@Override
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	@Override
	public void printStackTrace(final java.io.PrintWriter pw) {
		super.printStackTrace(pw);
		if (cause != null) {
			pw.println("Caused by:");
			cause.printStackTrace(pw);
		}
	}

	@Override
	public void printStackTrace(final java.io.PrintStream ps) {
		super.printStackTrace(ps);
		if (cause != null) {
			ps.println("Caused by:");
			cause.printStackTrace(ps);
		}
	}

}
