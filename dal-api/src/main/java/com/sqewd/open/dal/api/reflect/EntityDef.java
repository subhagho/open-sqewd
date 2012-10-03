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
 * @filename EntityDef.java
 * @created Oct 1, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.CacheSetting;
import com.sqewd.open.dal.api.persistence.EnumEntityType;

/**
 * Entity Definition extracted from the specified annotations.
 * 
 * @author subhagho
 * 
 */
public class EntityDef {
	public static final String _ENTITY_PARAM_QUERY_ = "query";

	private String name;

	private Class<?> classtype;

	private String schemaObject;

	private AbstractPersister persister = null;

	private CacheSetting cacheSettings = null;

	private EnumEntityType type = EnumEntityType.Basic;

	private HashMap<String, Object> parameters = null;

	private List<AttributeDef> attributes = new ArrayList<AttributeDef>();

	private HashMap<String, Integer> attrindex = new HashMap<String, Integer>();

	private List<AttributeDef> keys = null;

	public EntityDef(final String name, final Class<?> classtype) {
		this.name = name;
		this.classtype = classtype;
	}

	/**
	 * Add an attribute definition to this entity.
	 * 
	 * @param attr
	 * @throws Exception
	 */
	public void addAttribute(final AttributeDef attr) throws Exception {
		if (attrindex.containsKey(attr.getName()))
			throw new Exception("Attribute with name [" + attr.getName()
					+ "] laready exists.");
		attributes.add(attr);
		attrindex.put(attr.getName(), attributes.size() - 1);
		if (attr.isKey()) {
			if (keys == null) {
				keys = new ArrayList<AttributeDef>();
			}
			keys.add(attr);
		}
	}

	/**
	 * @return the schemaObject
	 */
	public String getSchemaObject() {
		return schemaObject;
	}

	/**
	 * @param schemaObject
	 *            the schemaObject to set
	 */
	public void setSchemaObject(final String schemaObject) {
		this.schemaObject = schemaObject;
	}

	/**
	 * @return the persister
	 */
	public AbstractPersister getPersister() {
		return persister;
	}

	/**
	 * @param persister
	 *            the persister to set
	 */
	public void setPersister(final AbstractPersister persister) {
		this.persister = persister;
	}

	/**
	 * @return the type
	 */
	public EnumEntityType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final EnumEntityType type) {
		this.type = type;
	}

	/**
	 * @return the cacheSettings
	 */
	public CacheSetting getCacheSettings() {
		return cacheSettings;
	}

	/**
	 * Turn on caching for this entity.
	 * 
	 */
	public void setCached() {
		cacheSettings = new CacheSetting();
		cacheSettings.setCached(true);
	}

	/**
	 * Is the entity data cached.
	 * 
	 * @return
	 */
	public boolean isCached() {
		if (cacheSettings != null)
			return cacheSettings.isCached();
		return false;
	}

	/**
	 * Add a new parameter to this entity definition. Parameters will always be
	 * overwritten if added multiple times.
	 * 
	 * @param key
	 *            - Parameter name
	 * @param value
	 *            - Value object
	 */
	public void addParameter(final String key, final Object value) {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		} else if (parameters.containsKey(key)) {
			parameters.remove(key);
		}
		parameters.put(key, value);
	}

	/**
	 * Get the parameter value for the specified key.
	 * 
	 * @param key
	 *            - Parameter name.
	 * @return
	 */
	public Object getParameter(final String key) {
		if (parameters.containsKey(key))
			return parameters.get(key);
		return null;
	}

	/**
	 * @return the keys
	 */
	public List<AttributeDef> getKeys() {
		return keys;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the classtype
	 */
	public Class<?> getClasstype() {
		return classtype;
	}

	/**
	 * @return the attributes
	 */
	public List<AttributeDef> getAttributes() {
		return attributes;
	}

	/**
	 * Get the Attribute definition referenced by the name.
	 * 
	 * @param name
	 *            - Attribute name annotation.
	 * @return
	 */
	public AttributeDef getAttribute(final String name) {
		if (attrindex.containsKey(name))
			return attributes.get(attrindex.get(name));
		return null;
	}
}
