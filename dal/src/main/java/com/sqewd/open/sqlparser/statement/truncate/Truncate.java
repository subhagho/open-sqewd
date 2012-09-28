package com.sqewd.open.sqlparser.statement.truncate;

import com.sqewd.open.sqlparser.schema.Table;
import com.sqewd.open.sqlparser.statement.Statement;
import com.sqewd.open.sqlparser.statement.StatementVisitor;

/**
 * A TRUNCATE TABLE statement
 */
public class Truncate implements Statement {
	private Table table;

	public void accept(final StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return "TRUNCATE TABLE " + table;
	}
}
