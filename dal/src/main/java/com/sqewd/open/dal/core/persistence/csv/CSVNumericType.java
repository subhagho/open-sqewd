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
 * @filename CSVNumericType.java
 * @created Oct 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import java.util.List;

import com.sqewd.open.dal.api.reflect.SchemaNumericDatatype;
import com.sqewd.open.dal.api.reflect.SchemaObjectDatatype;

/**
 * Class represents a Numeric data type for use with CSV data source.
 * 
 * @author subhagho
 * 
 */
public class CSVNumericType extends SchemaObjectDatatype<Double> implements
		SchemaNumericDatatype<Double> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#add(java.lang.Object
	 * , java.lang.Object)
	 */
	public Double add(final Double source, final Double value) {
		return source + value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#between(java.lang
	 * .Object, java.util.List)
	 */
	@Override
	public boolean between(final Double source, final List<Double> target) {
		if (source > target.get(0) && source < target.get(1))
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
	public int compare(final Double source, final Double target) {
		return source.compareTo(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#divide(java.lang
	 * .Object, java.lang.Object)
	 */
	public Double divide(final Double source, final Double value) {
		return source / value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#equals(java.lang.
	 * Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final Double source, final Double target) {
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
	public boolean in(final Double source, final List<Double> target) {
		for (Double dd : target) {
			if (dd.equals(target))
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
	public boolean isNotNull(final Double source) {
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
	public boolean isNull(final Double source) {
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
	public boolean lessThan(final Double source, final Double target) {
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
	public boolean lessThanEqual(final Double source, final Double target) {
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
	public boolean like(final Double source, final Double target) {
		return equals(source, target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#mod(java.lang.Object
	 * , java.lang.Object)
	 */
	public Double mod(final Double source, final Double value) {
		return source % value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#moreThan(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean moreThan(final Double source, final Double target) {
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
	public boolean moreThanEqual(final Double source, final Double target) {
		return compare(source, target) >= 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#multiply(java.lang
	 * .Object, java.lang.Object)
	 */
	public Double multiply(final Double source, final Double value) {
		return source * value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#notEqual(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean notEqual(final Double source, final Double target) {
		return !source.equals(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.reflect.SchemaObjectDatatype#parse(java.lang.String
	 * )
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
	 * com.sqewd.open.dal.api.reflect.SchemaNumericDatatype#subtract(java.lang
	 * .Object, java.lang.Object)
	 */
	public Double subtract(final Double source, final Double value) {
		return source - value;
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
