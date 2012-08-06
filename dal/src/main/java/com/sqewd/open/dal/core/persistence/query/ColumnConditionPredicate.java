/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import com.sqewd.open.dal.core.persistence.Entity;

/**
 * @author subhagho
 * 
 */
public class ColumnConditionPredicate extends AbstractConditionPredicate {
	private Class<?> type;

	private String column;

	public ColumnConditionPredicate(Class<?> type, String column) {
		this.type = type;
		this.column = column;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Entity eann = type.getAnnotation(Entity.class);
		return eann.recordset() + "." + column;
	}
}
