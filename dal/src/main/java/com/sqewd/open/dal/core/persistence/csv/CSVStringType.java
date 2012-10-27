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
 * @filename CSVStringType.java
 * @created Oct 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import java.util.List;

import com.sqewd.open.dal.api.reflect.SchemaObjectDatatype;

/**
 * Class represents a String data type for use with CSV data source.
 * 
 * @author subhagho
 * 
 */
public class CSVStringType extends SchemaObjectDatatype<String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#between(java.lang
	 * .Object, java.util.List)
	 */
	@Override
	public boolean between(final String source, final List<String> target) {
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
	public int compare(final String source, final String target) {
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
	public boolean equals(final String source, final String target) {
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
	public boolean in(final String source, final List<String> target) {
		for (String ss : target) {
			if (source.equals(ss))
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
	public boolean isNotNull(final String source) {
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
	public boolean isNull(final String source) {
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
	public boolean lessThan(final String source, final String target) {
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
	public boolean lessThanEqual(final String source, final String target) {
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
	public boolean like(final String source, final String target) {
		return source.matches(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#moreThan(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean moreThan(final String source, final String target) {
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
	public boolean moreThanEqual(final String source, final String target) {
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
	public boolean notEqual(final String source, final String target) {
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
	public String parse(final String value) throws Exception {
		return value;
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
		return value.toString();
	}

}
