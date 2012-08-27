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
 * @filename OrganizationView.java
 * @created Aug 16, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.demo.entities;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EntityJoin;
import com.sqewd.open.dal.api.persistence.Reference;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
// @Entity(recordset = "ORGANIZATION_V", isview = true, query =
// "select DEPARTMENT.NAME \"DEPARTMENT.NAME\", MANAGER.ID \"MANAGER.ID\", EMPLOYEE.ID \"EMPLOYEE.ID\", EMPLOYEE.FIRSTNAME \"EMPLOYEE.FIRSTNAME\", MANAGER.DATEOFBIRTH \"MANAGER.DATEOFBIRTH\", ORGANIZATION.DEPARTMENT \"ORGANIZATION.DEPARTMENT\", DEPARTMENT.STATUS \"DEPARTMENT.STATUS\", ORGANIZATION.TX_TIMESTAMP \"ORGANIZATION.TX_TIMESTAMP\", MANAGER.JOINDATE \"MANAGER.JOINDATE\", EMPLOYEE.TX_TIMESTAMP \"EMPLOYEE.TX_TIMESTAMP\", EMPLOYEE.LASTNAME \"EMPLOYEE.LASTNAME\", EMPLOYEE.DATEOFBIRTH \"EMPLOYEE.DATEOFBIRTH\", MANAGER.TITLE \"MANAGER.TITLE\", DEPARTMENT.TX_TIMESTAMP \"DEPARTMENT.TX_TIMESTAMP\", MANAGER.TX_TIMESTAMP \"MANAGER.TX_TIMESTAMP\", ORGANIZATION.EMPLOYEE \"ORGANIZATION.EMPLOYEE\", EMPLOYEE.JOINDATE \"EMPLOYEE.JOINDATE\", ORGANIZATION.MANAGER \"ORGANIZATION.MANAGER\", MANAGER.LASTNAME \"MANAGER.LASTNAME\", MANAGER.FIRSTNAME \"MANAGER.FIRSTNAME\", ORGANIZATION.CREATEDON \"ORGANIZATION.CREATEDON\", EMPLOYEE.TITLE \"EMPLOYEE.TITLE\", DEPARTMENT.ID \"DEPARTMENT.ID\" from EMPLOYEE EMPLOYEE, EMPLOYEE MANAGER, ORGANIZATION ORGANIZATION, DEPARTMENT DEPARTMENT where ((ORGANIZATION.EMPLOYEE = EMPLOYEE.ID)  AND (ORGANIZATION.MANAGER = MANAGER.ID) AND (ORGANIZATION.DEPARTMENT = DEPARTMENT.ID))")
@Entity(recordset = "ORGANIZATION_V", isview = true, isjoin = true)
@EntityJoin(entities = "ORGANIZATION,EMPLOYEE", join = "ORGANIZATION.EMPLOYEE = EMPLOYEE.ID")
public class OrganizationView extends AbstractEntity {
	@Attribute(name = "ORGANIZATION")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Organization", attribute = "ID")
	private Organization organization;

	@Attribute(name = "EMPLOYEE")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee employee;

	/**
	 * @return the organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
