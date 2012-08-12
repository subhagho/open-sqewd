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

/**
 * @author subhagho
 * 
 */
@Entity(recordset = "EMPLOYEE")
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.NONE)
public class Employee extends AbstractEntity {
	@Attribute(name = "ID", keyattribute = true, size = 256)
	@XmlElement(name = "id")
	private String id;

	@Attribute(name = "FIRSTNAME", size = 256)
	@XmlElement(name = "firstname")
	private String firstname;

	@Attribute(name = "LASTNAME", size = 256)
	@XmlElement(name = "lastname")
	private String lastname;

	@Attribute(name = "DATEOFBIRTH")
	@XmlElement(name = "firstname")
	private Date dateOfBirth;

	@Attribute(name = "JOINDATE")
	@XmlElement(name = "joindate")
	private Date joinDate;

	@Attribute(name = "TITLE", size = 256)
	@XmlElement(name = "title")
	private String title;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
