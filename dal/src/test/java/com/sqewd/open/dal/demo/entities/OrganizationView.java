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

import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EntityJoin;
import com.sqewd.open.dal.api.persistence.EnumRefereceType;
import com.sqewd.open.dal.api.persistence.Reference;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
@Entity(recordset = "ORGANIZATION_V", isview = true, isjoin = true)
@EntityJoin(entities = "ORGANIZATION,EMPLOYEE", join = "ORGANIZATION.MANAGER = ORGMANAGER.ID")
public class OrganizationView extends AbstractEntity {
	@Attribute(name = "ORGANIZATION")
	@Reference(target = "com.sqewd.open.dal.demo.entities.Organization", attribute = "ID", association = EnumRefereceType.One2Many)
	private List<Organization> organization;

	@Attribute(name = "ORGMANAGER", keyattribute = true)
	@Reference(target = "com.sqewd.open.dal.demo.entities.Employee", attribute = "ID")
	private Employee manager;

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
	 * @return the organization
	 */
	public List<Organization> getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}

}
