package com.sqewd.open.sqlparser.statement.create.table;

import java.util.List;

import com.sqewd.open.sqlparser.statement.select.PlainSelect;

public class ColDataType {

	private String dataType;
	private List argumentsStringList;

	public List getArgumentsStringList() {
		return argumentsStringList;
	}

	public String getDataType() {
		return dataType;
	}

	public void setArgumentsStringList(final List list) {
		argumentsStringList = list;
	}

	public void setDataType(final String string) {
		dataType = string;
	}

	@Override
	public String toString() {
		return dataType
				+ (argumentsStringList != null ? " "
						+ PlainSelect.getStringList(argumentsStringList, true,
								true) : "");
	}
}