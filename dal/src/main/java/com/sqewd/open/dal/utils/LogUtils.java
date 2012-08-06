package com.sqewd.open.dal.utils;

import java.io.PrintWriter;

import org.slf4j.Logger;

/**
 * Class defines utility functions for Logging.
 * 
 * @author subhagho
 * 
 */
public class LogUtils {
	/**
	 * Print the stacktrace for the exception in the logfile. This will print
	 * only if DEBUG mode is enabled.
	 * 
	 * @param logger
	 *            - Logger instance.
	 * @param ex
	 *            - Exception
	 */
	public static void stacktrace(Logger logger, Throwable ex) {
		if (logger.isDebugEnabled()) {
			logger.debug("*******************************************************************");
			logger.debug(ex.getLocalizedMessage());
			StackTraceElement[] ste = ex.getStackTrace();
			if (ste != null && ste.length > 0) {
				for (StackTraceElement st : ste) {
					logger.debug(st.getClassName() + "." + st.getMethodName()
							+ "() [" + st.getFileName() + ":"
							+ st.getLineNumber() + "]");
				}
			}
			if (ex.getCause() != null) {
				stacktrace(logger, ex.getCause());
			}
		}
	}

	public static String stacktrace(Throwable ex) {
		StringBuffer buffer = new StringBuffer();
		stacktrace(buffer, ex);
		return buffer.toString();
	}

	private static void stacktrace(StringBuffer buffer, Throwable ex) {
		try {
			buffer.append("*******************************************************************");
			buffer.append("<b>" + ex.getLocalizedMessage() + "</b></br>");
			StackTraceElement[] ste = ex.getStackTrace();
			if (ste != null && ste.length > 0) {
				for (StackTraceElement st : ste) {
					buffer.append(st.getClassName() + "." + st.getMethodName()
							+ "() [" + st.getFileName() + ":"
							+ st.getLineNumber() + "]<br>");
				}
			}
			if (ex.getCause() != null) {
				stacktrace(buffer, ex.getCause());
			}
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Print the HTML output of the stacktrace.
	 * 
	 * @param writer
	 *            - Response Writer
	 * @param ex
	 *            - Exception
	 */
	public static void stacktrace(PrintWriter writer, Throwable ex) {
		try {
			writer.write("*******************************************************************");
			writer.write("<b>" + ex.getLocalizedMessage() + "</b></br>");
			StackTraceElement[] ste = ex.getStackTrace();
			if (ste != null && ste.length > 0) {
				for (StackTraceElement st : ste) {
					writer.write(st.getClassName() + "." + st.getMethodName()
							+ "() [" + st.getFileName() + ":"
							+ st.getLineNumber() + "]<br>");
				}
			}
			if (ex.getCause() != null) {
				stacktrace(writer, ex.getCause());
			}
		} catch (Exception e) {
			return;
		}
	}
}
