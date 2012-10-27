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
 * @filename CSVDateType.java
 * @created Oct 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sqewd.open.dal.api.reflect.SchemaObjectDatatype;
import com.sqewd.open.dal.core.Env;

/**
 * Class represents a Date data type for use with CSV data source.
 * 
 * @author subhagho
 * 
 */
public class CSVDateType extends SchemaObjectDatatype<Date> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#between(java.lang
	 * .Object, java.util.List)
	 */
	@Override
	public boolean between(final Date source, final List<Date> target) {
		if (compare(source, target.get(0)) > 0
				&& compare(source, target.get(1)) < 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#compare(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public int compare(final Date source, final Date target) {
		return source.compareTo(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#equals(java.lang.
	 * Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final Date source, final Date target) {
		return source.equals(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#in(java.lang.Object,
	 * java.util.List)
	 */
	@Override
	public boolean in(final Date source, final List<Date> target) {
		for (Date dt : target) {
			if (equals(source, dt))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#isNotNull(java.lang
	 * .Object)
	 */
	@Override
	public boolean isNotNull(final Date source) {
		return source != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#isNull(java.lang.
	 * Object)
	 */
	@Override
	public boolean isNull(final Date source) {
		return source == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#lessThan(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean lessThan(final Date source, final Date target) {
		return compare(source, target) < 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#lessThanEqual(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public boolean lessThanEqual(final Date source, final Date target) {
		return compare(source, target) <= 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#like(java.lang.Object
	 * , java.lang.Object)
	 */
	@Override
	public boolean like(final Date source, final Date target) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#moreThan(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean moreThan(final Date source, final Date target) {
		return compare(source, target) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#moreThanEqual(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public boolean moreThanEqual(final Date source, final Date target) {
		return compare(source, target) >= 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#notEqual(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean notEqual(final Date source, final Date target) {
		return !equals(source, target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#parse(java.lang.String
	 * )
	 */
	@Override
	public Date parse(final String value) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(Env.get().getDateFormat());
		return df.parse(value);
	}

	/**
	 * Parse the date string using the specified format.
	 * 
	 * @param value
	 *            - Date String
	 * @param format
	 *            - Date Format
	 * @return
	 * @throws Exception
	 */
	public Date parse(final String value, final String format) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#toString(java.lang
	 * .Object)
	 */
	@Override
	public String toString(final Object value) {
		if (value instanceof Date) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Env.get()
						.getDateFormat());
				return sdf.format((Date) value);
			} catch (Exception ex) {
				return null;
			}
		} else
			return null;
	}

	/**
	 * Get the string representation of the date value based on the specified
	 * format.
	 * 
	 * @param value
	 *            - Date Value
	 * @param format
	 *            - Date Format
	 * @return
	 */
	public String toString(final Object value, final String format) {
		if (value instanceof Date) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				return sdf.format((Date) value);
			} catch (Exception ex) {
				return null;
			}
		} else
			return null;
	}

}
