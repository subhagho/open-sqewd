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
 * @filename SqlDecimalType.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Represents a SQL DECIMAL type.
 * 
 * @author subhagho
 * 
 */
public class SqlDecimalType extends SqlDataType<BigDecimal> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#parse(
	 * java.lang.String)
	 */
	@Override
	public BigDecimal parse(final String value) throws Exception {
		return new BigDecimal(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#compare
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final BigDecimal source, final BigDecimal target) {
		return source.compareTo(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DECIMAL";
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
			final BigDecimal value) throws Exception {
		pstmnt.setBigDecimal(index, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, int)
	 */
	@Override
	public BigDecimal getValue(final ResultSet rs, final int index)
			throws Exception {
		return rs.getBigDecimal(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.sql.SqlDataType#getValue(java
	 * .sql.ResultSet, java.lang.String)
	 */
	@Override
	public BigDecimal getValue(final ResultSet rs, final String column)
			throws Exception {
		return rs.getBigDecimal(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#equals
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final BigDecimal source, final BigDecimal target) {
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
	public boolean lessThan(final BigDecimal source, final BigDecimal target) {
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
	public boolean lessThanEqual(final BigDecimal source,
			final BigDecimal target) {
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
	public boolean moreThan(final BigDecimal source, final BigDecimal target) {
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
	public boolean moreThanEqual(final BigDecimal source,
			final BigDecimal target) {
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
	public boolean notEqual(final BigDecimal source, final BigDecimal target) {
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
	public boolean in(final BigDecimal source, final List<BigDecimal> target) {
		for (BigDecimal bd : target) {
			int ret = compare(source, bd);
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
	public boolean between(final BigDecimal source,
			final List<BigDecimal> target) {
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
	public boolean isNull(final BigDecimal source) {
		if (source == null)
			return true;
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
	public boolean isNotNull(final BigDecimal source) {
		if (source != null)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#add(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public BigDecimal add(final BigDecimal source, final BigDecimal value) {
		return source.add(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#subtract
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public BigDecimal subtract(final BigDecimal source, final BigDecimal value) {
		return source.subtract(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#multiply
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public BigDecimal multiply(final BigDecimal source, final BigDecimal value) {
		return source.multiply(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#divide
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public BigDecimal divide(final BigDecimal source, final BigDecimal value) {
		return source.divide(value);
	}

}
