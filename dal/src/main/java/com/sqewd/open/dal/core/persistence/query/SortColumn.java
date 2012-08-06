/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * @author subhagho
 * 
 */
public class SortColumn {
	private String column;
	private EnumSortOrder order = EnumSortOrder.DSC;

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

	/**
	 * @return the order
	 */
	public EnumSortOrder getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(EnumSortOrder order) {
		this.order = order;
	}

}
