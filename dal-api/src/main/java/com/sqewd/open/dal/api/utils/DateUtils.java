/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.api.utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author subhagho
 * 
 */
public class DateUtils {
	public static final String _DEFAULT_DATE_FORMAT_ = "MM-dd-yyyy HH:mm:ss";

	/**
	 * Parse the date string using the default date format.
	 * 
	 * @param ds
	 *            - Date String
	 * @return
	 * @throws Exception
	 */
	public static final Date parse(String ds) throws Exception {
		return parse(ds, _DEFAULT_DATE_FORMAT_);
	}

	/**
	 * Parse the date string using the specified date format.
	 * 
	 * @param ds
	 *            - Date String
	 * @param format
	 *            - Date Format
	 * @return
	 * @throws Exception
	 */
	public static final Date parse(String ds, String format) throws Exception {
		DateFormat formatter = new SimpleDateFormat(format);
		return (Date) formatter.parse(ds);
	}

	/**
	 * Format the date using the default date format.
	 * 
	 * @param dt
	 *            - Date
	 * @return
	 * @throws Exception
	 */
	public static final String format(Date dt) throws Exception {
		DateFormat formatter = new SimpleDateFormat(_DEFAULT_DATE_FORMAT_);
		return formatter.format(dt);
	}

	/**
	 * Format the date using the specified date format.
	 * 
	 * @param dt
	 *            - Date
	 * @param format
	 *            - Date Format
	 * @return
	 * @throws Exception
	 */
	public static final String format(Date dt, String format) throws Exception {
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(dt);
	}

	/**
	 * Parse a Data value form the String. Auto-detect the date string format.
	 * 
	 * Formats: <long>, <datestring> // Using the default format.,
	 * <datestring;format>
	 * 
	 * @param dts
	 * @return
	 * @throws Exception
	 */
	public static final Date fromString(String dts) throws Exception {
		Date ds = null;
		try {
			long dtvalue = Long.parseLong(dts);
			ds = new Date(dtvalue);
		} catch (Exception e) {
			if (dts.indexOf(';') > 0) {
				String[] parts = dts.split(";");
				ds = DateUtils.parse(parts[0], parts[1]);
			} else {
				ds = DateUtils.parse(dts);
			}
		}
		return ds;
	}
}
