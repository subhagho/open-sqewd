/**
 * 
 */
package com.sqewd.open.dal.core.persistence.db;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;

/**
 * @author subhagho
 * 
 */
@Entity(recordset = "DBVERSION")
public class DBVersion extends AbstractEntity {
	@Attribute(name = "VERSION", keyattribute = true)
	private String version;

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
