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
 * @filename SqlDoubleType.java
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
 * Represents a SQL DOUBLE type.
 * 
 * @author subhagho
 * 
 */
public class SqlDoubleType extends SqlDataType<Double> implements
		SchemaNumericDatatype<Double> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#add(java
	 * .lang.Object, java.lang.Object)
	 */
	public Double add(final Double source, final Double value) {
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
	public boolean between(final Double source, final List<Double> target) {
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
	public int compare(final Double source, final Double target) {
		return source.compareTo(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#divide
	 * (java.lang.Object, java.lang.Object)
	 */
	public Double divide(final Double source, final Double value) {
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
	public boolean equals(final Double source, final Double target) {
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
	public Double getValue(final ResultSet rs, final int index)
			throws Exception {
		return rs.getDouble(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, java.lang.String)
	 */
	@Override
	public Double getValue(final ResultSet rs, final String column)
			throws Exception {
		return rs.getDouble(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#in(java
	 * .lang.Object, java.util.List)
	 */
	@Override
	public boolean in(final Double source, final List<Double> target) {
		for (double db : target) {
			int ret = compare(source, db);
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
	public boolean isNotNull(final Double source) {
		if (source != null)
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
	public boolean isNull(final Double source) {
		if (source == null)
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
	public boolean lessThan(final Double source, final Double target) {
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
	public boolean lessThanEqual(final Double source, final Double target) {
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
	public boolean like(final Double source, final Double target) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#mod(java.lang.Object
	 * , java.lang.Object)
	 */
	public Double mod(final Double source, final Double value) {
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
	public boolean moreThan(final Double source, final Double target) {
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
	public boolean moreThanEqual(final Double source, final Double target) {
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
	public Double multiply(final Double source, final Double value) {
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
	public boolean notEqual(final Double source, final Double target) {
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
	public Double parse(final String value) throws Exception {
		return Double.parseDouble(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#pow(java.lang.Object
	 * , java.lang.Object)
	 */
	public Double pow(final Double source, final Double value) {
		return Math.pow(source, value);
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
			final Double value) throws Exception {
		pstmnt.setDouble(index, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#subtract
	 * (java.lang.Object, java.lang.Object)
	 */
	public Double subtract(final Double source, final Double value) {
		return source - value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DOUBLE";
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
		if (value instanceof Double)
			return value.toString();
		else
			return null;
	}

}
