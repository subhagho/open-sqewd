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
 * @filename SqlShortDatatype.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Represents a SQL SMALLINT type.
 * 
 * @author subhagho
 * 
 */
public class SqlShortType extends SqlDataType<Short> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#parse(
	 * java.lang.String)
	 */
	@Override
	public Short parse(final String value) throws Exception {
		return Short.parseShort(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#compare
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Short source, final Short target) {
		return source - target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SMALLINT";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#setValue(java
	 * .sql.PreparedStatement, int, java.lang.Object)
	 */
	@Override
	public void setValue(final PreparedStatement pstmnt, final int index,
			final Short value) throws Exception {
		pstmnt.setShort(index, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, int)
	 */
	@Override
	public Short getValue(final ResultSet rs, final int index) throws Exception {
		return rs.getShort(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, java.lang.String)
	 */
	@Override
	public Short getValue(final ResultSet rs, final String column)
			throws Exception {
		return rs.getShort(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#equals
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final Short source, final Short target) {
		int ret = compare(source, target);
		if (ret == 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#lessThan
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean lessThan(final Short source, final Short target) {
		int ret = compare(source, target);
		if (ret < 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#lessThanEqual
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean lessThanEqual(final Short source, final Short target) {
		int ret = compare(source, target);
		if (ret <= 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#moreThan
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean moreThan(final Short source, final Short target) {
		int ret = compare(source, target);
		if (ret > 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#moreThanEqual
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean moreThanEqual(final Short source, final Short target) {
		int ret = compare(source, target);
		if (ret >= 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#notEqual
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean notEqual(final Short source, final Short target) {
		int ret = compare(source, target);
		if (ret != 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#in(java
	 * .lang.Object, java.util.List)
	 */
	@Override
	public boolean in(final Short source, final List<Short> target) {
		for (short ss : target) {
			int ret = compare(source, ss);
			if (ret == 0)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#between
	 * (java.lang.Object, java.util.List)
	 */
	@Override
	public boolean between(final Short source, final List<Short> target) {
		int rl = compare(source, target.get(0));
		int rr = compare(source, target.get(1));
		if (rl > 0 && rr < 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#isNull
	 * (java.lang.Object)
	 */
	@Override
	public boolean isNull(final Short source) {
		return (source == null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#isNotNull
	 * (java.lang.Object)
	 */
	@Override
	public boolean isNotNull(final Short source) {
		return (source != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#add(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public Short add(final Short source, final Short value) {
		return (short) (source + value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#subtract
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public Short subtract(final Short source, final Short value) {
		return (short) (source - value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#multiply
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public Short multiply(final Short source, final Short value) {
		return (short) (source * value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#divide
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public Short divide(final Short source, final Short value) {
		return (short) (source / value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#like(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public boolean like(final Short source, final Short target) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#toString
	 * (java.lang.Object)
	 */
	@Override
	public String toString(final Object value) {
		if (value instanceof Short)
			return value.toString();
		else
			return null;
	}

}