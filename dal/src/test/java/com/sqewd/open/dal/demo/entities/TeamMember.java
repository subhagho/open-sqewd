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

import com.sqewd.open.dal.api.persistence.AbstractPersistedEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.Reference;

/**
 * @author subhagho
 * 
 */
@Entity(recordset = "MEMBERSHIP")
@XmlRootElement(name = "membership")
@XmlAccessorType(XmlAccessType.NONE)
public class TeamMember extends AbstractPersistedEntity {
	@Attribute(name = "TEAM", keyattribute = true, size = 256)
	@XmlElement(name = "team")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Team", attribute = "ID")
	private Team team;

	@Attribute(name = "MEMBER", keyattribute = true, size = 256)
	@XmlElement(name = "member")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee member;

	@Attribute(name = "ROLE", keyattribute = true, size = 256)
	@XmlElement(name = "role")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Role", attribute = "NAME")
	private Role role;

	@Attribute(name = "STATUS", size = 256)
	@XmlElement(name = "STATUS")
	private EnumStatus status;

	@Attribute(name = "CREATEDON", size = 256)
	@XmlElement(name = "createdon")
	private Date createdOn;

	/**
	 * @return the member
	 */
	public Employee getMember() {
		return member;
	}

	/**
	 * @param member
	 *            the member to set
	 */
	public void setMember(Employee member) {
		this.member = member;
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

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

}
