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
 * @filename EntitySchema.java
 * @created Aug 23, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.core.persistence.model.EntityModelLoader;

/**
 * @author subhagho
 * 
 */
@JsonRootName(value = "Entity")
public class EntitySchema {
	@JsonProperty(value = "Name")
	private String name;

	@JsonProperty(value = "JsonName")
	private String jsonname;

	@JsonProperty(value = "Classname")
	private String classname;

	@JsonProperty(value = "DataPersister")
	private String persister;

	@JsonProperty(value = "Properties")
	private List<PropertySchema> properties;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @param classname
	 *            the classname to set
	 */
	public void setClassname(final String classname) {
		this.classname = classname;
	}

	/**
	 * @return the properties
	 */
	public List<PropertySchema> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final List<PropertySchema> properties) {
		this.properties = properties;
	}

	/**
	 * @return the persister
	 */
	public String getPersister() {
		return persister;
	}

	/**
	 * @param persister
	 *            the persister to set
	 */
	public void setPersister(final String persister) {
		this.persister = persister;
	}

	/**
	 * @return the jsonname
	 */
	public String getJsonname() {
		return jsonname;
	}

	/**
	 * @param jsonname
	 *            the jsonname to set
	 */
	public void setJsonname(final String jsonname) {
		this.jsonname = jsonname;
	}

	public static EntitySchema loadSchema(final Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");

		EntityDef enref = EntityModelLoader.get().getEntityDef(type);

		return loadSchema(enref);
	}

	public static EntitySchema loadSchema(final EntityDef enref)
			throws Exception {
		EntitySchema entity = new EntitySchema();
		Class<?> type = enref.getClasstype();

		entity.name = enref.getName();
		AbstractPersister pers = DataManager.get().getPersister(type);
		entity.persister = pers.getClass().getCanonicalName();
		entity.classname = type.getCanonicalName();

		if (type.isAnnotationPresent(JsonRootName.class)) {
			JsonRootName re = type.getAnnotation(JsonRootName.class);
			entity.jsonname = re.value();
		}

		entity.properties = new ArrayList<PropertySchema>();

		for (AttributeDef attr : enref.getAttributes()) {
			PropertySchema pdef = PropertySchema.load(type, attr.getField()
					.getName());
			if (pdef != null) {
				entity.properties.add(pdef);
			}
		}
		return entity;
	}

}