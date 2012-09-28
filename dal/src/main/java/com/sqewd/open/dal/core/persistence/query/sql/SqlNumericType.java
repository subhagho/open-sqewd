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
 * @filename SqlNumericDatatype.java
 * @created Sep 28, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Represents a SQL NUMERIC type.
 * 
 * @author subhagho
 * 
 */
public class SqlNumericType extends SqlDataType<BigDecimal> {

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
		return "NUMERIC";
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

}
