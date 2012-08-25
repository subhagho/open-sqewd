/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.demo.entities;

import java.util.Date;

import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.Reference;

/**
 * @author subhagho
 * 
 */
@Entity(recordset = "TEAM")
public class Team extends AbstractPersistedEntity {
	@Attribute(name = "ID", keyattribute = true, size = 256)
	private String id;

	@Attribute(name = "NAME", size = 256)
	private String name;

	@Attribute(name = "MANAGER", size = 256)
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee manager;

	@Attribute(name = "ADMIN", size = 256)
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee admin;

	@Attribute(name = "STATUS", size = 256)
	private EnumStatus status;

	@Attribute(name = "CREATEDON", size = 256)
	private Date createdOn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public EnumStatus getStatus() {
		return status;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
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
	 * @param status
	 *            the status to set
	 */
	public void setStatus(EnumStatus status) {
		this.status = status;
	}

	/**
	 * @return the admin
	 */
	public Employee getAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(Employee admin) {
		this.admin = admin;
	}

}
