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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.Reference;

/**
 * @author subhagho
 * 
 */
@Entity(recordset = "TEAM")
@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.NONE)
public class Team extends AbstractEntity {
	@Attribute(name = "ID", keyattribute = true, size = 256)
	@XmlElement(name = "id")
	private String id;

	@Attribute(name = "NAME", size = 256)
	@XmlElement(name = "name")
	private String name;

	@Attribute(name = "MANAGER", size = 256)
	@XmlElement(name = "manager")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee manager;

	@Attribute(name = "STATUS", size = 256)
	@XmlElement(name = "STATUS")
	private EnumStatus status;

	@Attribute(name = "CREATEDON", size = 256)
	@XmlElement(name = "createdon")
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

}
