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
 * @filename AttributeDef.java
 * @created Oct 1, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import java.lang.reflect.Field;

import com.sqewd.open.dal.api.persistence.CustomFieldHandler;

/**
 * Class represents the Attribute definitions for an Entity.
 * 
 * @author subhagho
 * 
 */
public class AttributeDef {

	private String name;

	private boolean key;

	private CustomFieldHandler handler;

	private int size;

	private boolean autoIncrement;

	private AttributeTypeDef type;

	private Field field;

	public AttributeDef(final String name, final Field field,
			final AttributeTypeDef type) {
		this.name = name;
		this.type = type;
		this.field = field;
	}

	/**
	 * @return the key
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(final boolean key) {
		this.key = key;
	}

	/**
	 * @return the handler
	 */
	public CustomFieldHandler getHandler() {
		return handler;
	}

	/**
	 * @param handler
	 *            the handler to set
	 */
	public void setHandler(final CustomFieldHandler handler) {
		this.handler = handler;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(final int size) {
		this.size = size;
	}

	/**
	 * @return the autoIncrement
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * @param autoIncrement
	 *            the autoIncrement to set
	 */
	public void setAutoIncrement(final boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public AttributeTypeDef getType() {
		return type;
	}

	/**
	 * Is this attribute an entity reference.
	 * 
	 * @return
	 */
	public boolean isRefrenceAttr() {
		if (type != null && (type instanceof AttributeReferenceDef))
			return true;
		return false;
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}
}
