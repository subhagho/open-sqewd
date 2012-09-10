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
 * @filename SalaryView.java
 * @created Sep 3, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.demo.ext.entities;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EntityJoin;
import com.sqewd.open.dal.api.persistence.Reference;
import com.sqewd.open.dal.demo.entities.Employee;
import com.sqewd.open.dal.demo.entities.Organization;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
@Entity(recordset = "SALARY_V", isview = true, isjoin = true)
@EntityJoin(join = "SALARY.ID = EMPLOYEEKEY.ID;ORGANIZATION.EMPLOYEE = EMPLOYEEKEY.ID")
public class SalaryView extends AbstractEntity {
	@Attribute(name = "EMPLOYEEKEY", keyattribute = true)
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee employee;

	@Attribute(name = "ORGANIZATION")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Organization", attribute = "ID")
	private Organization organization;

	@Attribute(name = "SALARY")
	@Reference(target = "com.sqewd.open.dal.demo.ext.entities.Salary", attribute = "ID")
	private Salary salary;

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

	/**
	 * @return the salary
	 */
	public Salary getSalary() {
		return salary;
	}

	/**
	 * @param salary
	 *            the salary to set
	 */
	public void setSalary(Salary salary) {
		this.salary = salary;
	}

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

}
