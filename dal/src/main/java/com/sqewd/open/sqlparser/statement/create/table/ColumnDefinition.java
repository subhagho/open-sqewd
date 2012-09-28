package com.sqewd.open.sqlparser.statement.create.table;

import java.util.List;

import com.sqewd.open.sqlparser.statement.select.PlainSelect;

/**
 * A column definition in a CREATE TABLE statement.<br>
 * Example: mycol VARCHAR(30) NOT NULL
 */
public class ColumnDefinition {
	private String columnName;
	private ColDataType colDataType;
	private List columnSpecStrings;

	/**
	 * A list of strings of every word after the datatype of the column.<br>
	 * Example ("NOT", "NULL")
	 */
	public List getColumnSpecStrings() {
		return columnSpecStrings;
	}

	public void setColumnSpecStrings(final List list) {
		columnSpecStrings = list;
	}

	/**
	 * The {@link ColDataType} of this column definition
	 */
	public ColDataType getColDataType() {
		return colDataType;
	}

	public void setColDataType(final ColDataType type) {
		colDataType = type;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(final String string) {
		columnName = string;
	}

	@Override
	public String toString() {
		return columnName + " " + colDataType + " "
				+ PlainSelect.getStringList(columnSpecStrings, false, false);
	}

}
