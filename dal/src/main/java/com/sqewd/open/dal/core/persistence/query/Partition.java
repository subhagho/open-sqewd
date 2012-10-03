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
 * @filename Partition.java
 * @created Sep 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.reflect.SchemaObject;

/**
 * Base class for defining data partitions.
 * 
 * @author subhagho
 * 
 */
public abstract class Partition<T extends PartitionKey, S extends SchemaObject> {
	protected T key;

	protected S type;

	protected AbstractPersister persister = null;

	protected Partition(final S type) {
		this.type = type;
	}

	/**
	 * @return the key
	 */
	public T getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(final T key) {
		this.key = key;
	}

	/**
	 * @return the type
	 */
	public S getType() {
		return type;
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

}
