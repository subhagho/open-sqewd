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
 * @filename SchemaReferenceType.java
 * @created Sep 29, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import java.util.List;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class SchemaReferenceType extends SchemaObjectDatatype<SchemaObject> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#parse(
	 * java.lang.String)
	 */
	@Override
	public SchemaObject parse(final String value) throws Exception {
		throw new Exception("Parse not implemented for SchemaObject type.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#compare
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final SchemaObject source, final SchemaObject target) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#equals
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equals(final SchemaObject source, final SchemaObject target) {
		// TODO Auto-generated method stub
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
	public boolean lessThan(final SchemaObject source, final SchemaObject target) {
		// TODO Auto-generated method stub
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
	public boolean lessThanEqual(final SchemaObject source,
			final SchemaObject target) {
		// TODO Auto-generated method stub
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
	public boolean moreThan(final SchemaObject source, final SchemaObject target) {
		// TODO Auto-generated method stub
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
	public boolean moreThanEqual(final SchemaObject source,
			final SchemaObject target) {
		// TODO Auto-generated method stub
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
	public boolean notEqual(final SchemaObject source, final SchemaObject target) {
		// TODO Auto-generated method stub
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
	public boolean in(final SchemaObject source, final List<SchemaObject> target) {
		// TODO Auto-generated method stub
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
	public boolean between(final SchemaObject source,
			final List<SchemaObject> target) {
		// TODO Auto-generated method stub
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
	public boolean isNull(final SchemaObject source) {
		// TODO Auto-generated method stub
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
	public boolean isNotNull(final SchemaObject source) {
		// TODO Auto-generated method stub
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
	public SchemaObject add(final SchemaObject source, final SchemaObject value) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#subtract
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public SchemaObject subtract(final SchemaObject source,
			final SchemaObject value) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#multiply
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public SchemaObject multiply(final SchemaObject source,
			final SchemaObject value) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#divide
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public SchemaObject divide(final SchemaObject source,
			final SchemaObject value) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.SchemaObjectDatatype#like(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public boolean like(final SchemaObject source, final SchemaObject target) {
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
		return null;
	}

}
