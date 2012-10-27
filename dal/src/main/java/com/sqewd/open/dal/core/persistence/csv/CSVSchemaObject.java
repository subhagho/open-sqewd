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
 * @filename CSVSchemaObject.java
 * @created Oct 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.reflect.SchemaObject;
import com.sqewd.open.dal.api.reflect.SchemaObjectAttribute;

/**
 * Class represents a CSV Schema Object.
 * 
 * @author subhagho
 * 
 */
public class CSVSchemaObject extends SchemaObject {

	/**
	 * TODO: <comment>
	 * 
	 * @param name
	 * @param persister
	 */
	protected CSVSchemaObject(final String name,
			final AbstractPersister persister) {
		super(name, persister);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.reflect.SchemaObject#get(java.lang.String)
	 */
	@Override
	public SchemaObjectAttribute get(final String name) throws Exception {
		if (nameindx.containsKey(name))
			return attributes.get(nameindx.get(name));
		throw new Exception("No Object attributes found for name [" + name
				+ "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.reflect.SchemaObject#getKey()
	 */
	@Override
	public String getKey() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.reflect.SchemaObject#isPartitioned()
	 */
	@Override
	public boolean isPartitioned() {
		return false;
	}

}
