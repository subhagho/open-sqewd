/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query.test;

import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.Reference;

/**
 * @author subhagho
 *
 */

@Entity(recordset = "RL2")
public class ReferenceL2 {
	@Attribute(name = "REF")
	@Reference(target = "com.sqewd.open.dal.core.persistence.query.test.ReferenceL1", attribute = "LV")
	private ReferenceL1 ref;
	@Attribute(name = "STR")
	private String str = null;

	/**
	 * @return the ref
	 */
	public ReferenceL1 getRef() {
		return ref;
	}

	/**
	 * @param ref
	 *            the ref to set
	 */
	public void setRef(ReferenceL1 ref) {
		this.ref = ref;
	}

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}

	/**
	 * @param str
	 *            the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}

}

