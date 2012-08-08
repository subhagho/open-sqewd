/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query.test;

import java.util.Date;

import com.sqewd.open.dal.core.persistence.Attribute;
import com.sqewd.open.dal.core.persistence.Entity;

/**
 * @author subhagho
 *
 */
@Entity(recordset = "RL1")
public class ReferenceL1 {
	@Attribute(name = "STR")
	private String str = null;
	@Attribute(name = "DT")
	private Date dt = new Date();
	@Attribute(name = "LV")
	private long lv;

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

	/**
	 * @return the dt
	 */
	public Date getDt() {
		return dt;
	}

	/**
	 * @param dt
	 *            the dt to set
	 */
	public void setDt(Date dt) {
		this.dt = dt;
	}

	/**
	 * @return the lv
	 */
	public long getLv() {
		return lv;
	}

	/**
	 * @param lv
	 *            the lv to set
	 */
	public void setLv(long lv) {
		this.lv = lv;
	}
}
