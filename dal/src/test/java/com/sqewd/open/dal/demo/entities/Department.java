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
 * @filename Department.java
 * @created Aug 15, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.demo.entities;

import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
@Entity(recordset = "DEPARTMENT")
public class Department extends AbstractPersistedEntity {
	@Attribute(name = "ID", keyattribute = true, size = 256)
	private String id;

	@Attribute(name = "NAME", size = 256)
	private String name;

	@Attribute(name = "STATUS", size = 256)
	private EnumStatus status;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public EnumStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(EnumStatus status) {
		this.status = status;
	}
}
