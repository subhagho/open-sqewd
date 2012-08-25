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
 * @filename Organization.java
 * @created Aug 15, 2012
 * @author subhagho
 *
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
 *         TODO: <comment>
 * 
 */
@Entity(recordset = "ORGANIZATION")
public class Organization extends AbstractPersistedEntity {
	@Attribute(name = "DEPARTMENT", keyattribute = true, size = 256)
	@Reference(target = "com.sqewd.open.dal.demo.entities.Department", attribute = "ID")
	private Department department;

	@Attribute(name = "EMPLOYEE", keyattribute = true, size = 256)
	private String employee;

	@Attribute(name = "MANAGER", size = 256)
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee manager;

	@Attribute(name = "CREATEDON", size = 256)
	private Date createdOn;

	/**
	 * @return the employee
	 */
	public String getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(String employee) {
		this.employee = employee;
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
	 * @return the manager
	 */
	public Employee getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	public void setManager(Employee manager) {
		this.manager = manager;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

}
