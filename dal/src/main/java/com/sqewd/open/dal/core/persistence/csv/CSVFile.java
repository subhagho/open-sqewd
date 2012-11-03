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
 * @filename CSVFile.java
 * @created Oct 29, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.reflect.SchemaObject;
import com.sqewd.open.dal.api.reflect.SchemaObjectAttribute;

/**
 * Class represents a CSV File.
 * 
 * @author subhagho
 * 
 */
public class CSVFile extends SchemaObject {
	private String filename;

	/**
	 * Default Constructor.
	 * 
	 * @param filename
	 *            - CSV Filename.
	 * @param entity
	 *            - CSV persisted entity.
	 * @param persister
	 *            - CSV Persister.
	 */
	protected CSVFile(final String filename, final EntityDef entity,
			final AbstractPersister persister) throws Exception {
		super(entity.getName(), persister);
		this.filename = filename;
		setColumns(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.api.reflect.SchemaObject#get(java.lang.String)
	 */
	@Override
	public SchemaObjectAttribute get(final String name) throws Exception {
		if (nameindx.containsKey(name)) {
			int index = nameindx.get(name);
			return attributes.get(index);
		}
		throw new Exception("Attribute with name [" + name + "] not found.");
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
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

	private void setColumns(final EntityDef entity) throws Exception {
		for (AttributeDef ad : entity.getAttributes()) {
			CSVColumn column = new CSVColumn(ad.getName(), new CSVStringType(),
					this);
			addAttribute(column);
		}
	}

}
