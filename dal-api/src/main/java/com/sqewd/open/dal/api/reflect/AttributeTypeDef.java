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
 * @filename AttributeTypeDef.java
 * @created Oct 1, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import com.sqewd.open.dal.api.persistence.EnumRefereceType;

/**
 * Defines the attribute type.
 * 
 * @author subhagho
 * 
 */
public class AttributeTypeDef {
	private Class<?> type;

	private boolean cascadeUpdate = false;

	private EnumRefereceType cardinality = EnumRefereceType.One2One;

	private boolean lazyLoad = false;

	public AttributeTypeDef(final Class<?> type) {
		this.type = type;
	}

	/**
	 * @return the cascadeUpdate
	 */
	public boolean isCascadeUpdate() {
		return cascadeUpdate;
	}

	/**
	 * @param cascadeUpdate
	 *            the cascadeUpdate to set
	 */
	public void setCascadeUpdate(final boolean cascadeUpdate) {
		this.cascadeUpdate = cascadeUpdate;
	}

	/**
	 * @return the cardinality
	 */
	public EnumRefereceType getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality
	 *            the cardinality to set
	 */
	public void setCardinality(final EnumRefereceType cardinality) {
		this.cardinality = cardinality;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @return the lazyLoad
	 */
	public boolean isLazyLoad() {
		return lazyLoad;
	}

	/**
	 * @param lazyLoad
	 *            the lazyLoad to set
	 */
	public void setLazyLoad(final boolean lazyLoad) {
		this.lazyLoad = lazyLoad;
	}

	/**
	 * Is the current attribute a List type?
	 * 
	 * @return
	 */
	public boolean isList() {
		return (cardinality == EnumRefereceType.One2Many);
	}
}
