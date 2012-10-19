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
 * @filename SqlRealType.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.sqewd.open.dal.api.reflect.SchemaNumericDatatype;

/**
 * Represents a SQL REAL type.
 * 
 * @author subhagho
 * 
 */
public class SqlRealType extends SqlDataType<Float> implements
		SchemaNumericDatatype<Float> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#add(java
	 * .lang.Object, java.lang.Object)
	 */
	public Float add(final Float source, final Float value) {
		return source + value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#between
	 * (java.lang.Object, java.util.List)
	 */
	@Override
	public boolean between(final Float source, final List<Float> target) {
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
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#compare
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final Float source, final Float target) {
		return source.compareTo(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#divide
	 * (java.lang.Object, java.lang.Object)
	 */
	public Float divide(final Float source, final Float value) {
		return source / value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#equals
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final Float source, final Float target) {
		int ret = compare(source, target);
		if (ret == 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, int)
	 */
	@Override
	public Float getValue(final ResultSet rs, final int index) throws Exception {
		return rs.getFloat(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, java.lang.String)
	 */
	@Override
	public Float getValue(final ResultSet rs, final String column)
			throws Exception {
		return rs.getFloat(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#in(java
	 * .lang.Object, java.util.List)
	 */
	@Override
	public boolean in(final Float source, final List<Float> target) {
		for (float ff : target) {
			int ret = compare(source, ff);
			if (ret == 0)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#isNotNull
	 * (java.lang.Object)
	 */
	@Override
	public boolean isNotNull(final Float source) {
		return source != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#isNull
	 * (java.lang.Object)
	 */
	@Override
	public boolean isNull(final Float source) {
		return source == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#lessThan
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean lessThan(final Float source, final Float target) {
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
	public boolean lessThanEqual(final Float source, final Float target) {
		int ret = compare(source, target);
		if (ret <= 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#like(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public boolean like(final Float source, final Float target) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#mod(java.lang.Object
	 * , java.lang.Object)
	 */
	public Float mod(final Float source, final Float value) {
		return Math.abs(source / value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#moreThan
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean moreThan(final Float source, final Float target) {
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
	public boolean moreThanEqual(final Float source, final Float target) {
		int ret = compare(source, target);
		if (ret >= 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#multiply
	 * (java.lang.Object, java.lang.Object)
	 */
	public Float multiply(final Float source, final Float value) {
		return source * value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#notEqual
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean notEqual(final Float source, final Float target) {
		int ret = compare(source, target);
		if (ret != 0)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#parse(
	 * java.lang.String)
	 */
	@Override
	public Float parse(final String value) throws Exception {
		return Float.parseFloat(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#pow(java.lang.Object
	 * , java.lang.Object)
	 */
	public Float pow(final Float source, final Float value) {
		return (float) Math.pow(source, value);
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
			final Float value) throws Exception {
		pstmnt.setFloat(index, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#subtract
	 * (java.lang.Object, java.lang.Object)
	 */
	public Float subtract(final Float source, final Float value) {
		return source - value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "REAL";
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
		if (value instanceof Float)
			return value.toString();
		else
			return null;
	}

}
