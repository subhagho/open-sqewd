package com.sqewd.open.sqlparser.statement.create.table;

import java.util.List;

import com.sqewd.open.sqlparser.statement.select.PlainSelect;

/**
 * An index (unique, primary etc.) in a CREATE TABLE statement
 */
public class Index {

	private String type;
	private List columnsNames;
	private String name;

	/**
	 * A list of strings of all the columns regarding this index
	 */
	public List getColumnsNames() {
		return columnsNames;
	}

	public String getName() {
		return name;
	}

	/**
	 * The type of this index: "PRIMARY KEY", "UNIQUE", "INDEX"
	 */
	public String getType() {
		return type;
	}

	public void setColumnsNames(final List list) {
		columnsNames = list;
	}

	public void setName(final String string) {
		name = string;
	}

	public void setType(final String string) {
		type = string;
	}

	@Override
	public String toString() {
		return type + " " + PlainSelect.getStringList(columnsNames, true, true)
				+ (name != null ? " " + name : "");
	}
}