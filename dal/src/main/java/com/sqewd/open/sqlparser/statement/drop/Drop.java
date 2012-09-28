package com.sqewd.open.sqlparser.statement.drop;

import java.util.List;

import com.sqewd.open.sqlparser.statement.Statement;
import com.sqewd.open.sqlparser.statement.StatementVisitor;
import com.sqewd.open.sqlparser.statement.select.PlainSelect;

public class Drop implements Statement {
	private String type;
	private String name;
	private List parameters;

	public void accept(final StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public List getParameters() {
		return parameters;
	}

	public String getType() {
		return type;
	}

	public void setName(final String string) {
		name = string;
	}

	public void setParameters(final List list) {
		parameters = list;
	}

	public void setType(final String string) {
		type = string;
	}

	@Override
	public String toString() {
		String sql = "DROP " + type + " " + name;

		if (parameters != null && parameters.size() > 0) {
			sql += " " + PlainSelect.getStringList(parameters);
		}

		return sql;
	}
}
