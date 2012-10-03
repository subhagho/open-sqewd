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
 * @filename SchemaType.java
 * @created Sep 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractPersister;

/**
 * Class is used to defining a Schema object such as table, column group, file,
 * etc.
 * 
 * @author subhagho
 * 
 */
public abstract class SchemaObject {
	protected String name;

	protected List<SchemaObjectAttribute> attributes;

	protected HashMap<String, Integer> nameindx;

	protected AbstractPersister persister = null;

	protected SchemaObject(final String name, final AbstractPersister persister) {
		this.name = name;
		this.persister = persister;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the persistence handler for this Object.
	 * 
	 * @return
	 */
	public AbstractPersister getPersister() {
		return persister;
	}

	/**
	 * Add an attribute to this Schema Object.
	 * 
	 * @param attr
	 * @throws Exception
	 */
	public void addAttribute(final SchemaObjectAttribute attr) throws Exception {
		if (attributes == null) {
			attributes = new ArrayList<SchemaObjectAttribute>();
			nameindx = new HashMap<String, Integer>();
		} else {
			if (nameindx.containsKey(attr.name))
				throw new Exception("Attribute already registered with name ["
						+ attr.name + "]");
		}
		attributes.add(attr);
		int index = attributes.size() - 1;
		nameindx.put(attr.name, index);
	}

	/**
	 * Get an attribute based on the name/alias.
	 * 
	 * @param name
	 *            - Attribute name or Alias.
	 * @return
	 * @throws Exception
	 */
	public abstract SchemaObjectAttribute get(final String name)
			throws Exception;

	/**
	 * Get the name/alias key this object is reference by.
	 * 
	 * @return
	 */
	public abstract String getKey();

	/**
	 * Is this schema object partitioned?
	 * 
	 * @return
	 */
	public abstract boolean isPartitioned();

}
