package com.sqewd.open.sqlparser.statement.select;

/**
 * A top clause in the form [TOP row_count]
 */
public class Top {
	private long rowCount;
	private boolean rowCountJdbcParameter = false;

	public long getRowCount() {
		return rowCount;
	}

	public void setRowCount(final long l) {
		rowCount = l;
	}

	public boolean isRowCountJdbcParameter() {
		return rowCountJdbcParameter;
	}

	public void setRowCountJdbcParameter(final boolean b) {
		rowCountJdbcParameter = b;
	}

	@Override
	public String toString() {
		return "TOP " + (rowCountJdbcParameter ? "?" : rowCount + "");
	}

}
