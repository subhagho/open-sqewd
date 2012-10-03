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
 * @filename AttributeReferenceDef.java
 * @created Oct 2, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.reflect;

import com.sqewd.open.dal.api.persistence.EnumRefereceType;

/**
 * Represents a Entity reference definition.
 * 
 * @author subhagho
 * 
 */
public class AttributeReferenceDef extends AttributeTypeDef {
	private EntityDef reference;
	private AttributeDef referenceAttribute;
	private EnumRefereceType cardinality;
	private boolean cascade;

	public AttributeReferenceDef(final EntityDef reference,
			final AttributeDef referenceAttribute, final Class<?> type) {
		super(type);
		this.reference = reference;
		this.referenceAttribute = referenceAttribute;
	}

	/**
	 * @return the reference
	 */
	public EntityDef getReference() {
		return reference;
	}

	/**
	 * @return the referenceAttribute
	 */
	public AttributeDef getReferenceAttribute() {
		return referenceAttribute;
	}

	/**
	 * @return the cascade
	 */
	public boolean isCascade() {
		return cascade;
	}

	/**
	 * @param cascade
	 *            the cascade to set
	 */
	public void setCascade(final boolean cascade) {
		this.cascade = cascade;
	}

	/**
	 * @return the cardinality
	 */
	@Override
	public EnumRefereceType getCardinality() {
		return cardinality;
	}

	/**
	 * @param cardinality
	 *            the cardinality to set
	 */
	@Override
	public void setCardinality(final EnumRefereceType cardinality) {
		this.cardinality = cardinality;
	}

}
