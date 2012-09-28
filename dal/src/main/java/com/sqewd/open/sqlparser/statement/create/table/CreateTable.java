package com.sqewd.open.sqlparser.statement.create.table;

import java.util.List;

import com.sqewd.open.sqlparser.schema.Table;
import com.sqewd.open.sqlparser.statement.Statement;
import com.sqewd.open.sqlparser.statement.StatementVisitor;
import com.sqewd.open.sqlparser.statement.select.PlainSelect;

/**
 * A "CREATE TABLE" statement
 */
public class CreateTable implements Statement {

	private Table table;
	private List tableOptionsStrings;
	private List columnDefinitions;
	private List indexes;

	public void accept(final StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	/**
	 * The name of the table to be created
	 */
	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
	}

	/**
	 * A list of {@link ColumnDefinition}s of this table.
	 */
	public List getColumnDefinitions() {
		return columnDefinitions;
	}

	public void setColumnDefinitions(final List list) {
		columnDefinitions = list;
	}

	/**
	 * A list of options (as simple strings) of this table definition, as
	 * ("TYPE", "=", "MYISAM")
	 */
	public List getTableOptionsStrings() {
		return tableOptionsStrings;
	}

	public void setTableOptionsStrings(final List list) {
		tableOptionsStrings = list;
	}

	/**
	 * A list of {@link Index}es (for example "PRIMARY KEY") of this table.<br>
	 * Indexes created with column definitions (as in mycol INT PRIMARY KEY) are
	 * not inserted into this list.
	 */
	public List getIndexes() {
		return indexes;
	}

	public void setIndexes(final List list) {
		indexes = list;
	}

	@Override
	public String toString() {
		String sql = "";

		sql = "CREATE TABLE " + table + " (";

		sql += PlainSelect.getStringList(columnDefinitions, true, false);
		if (indexes != null && indexes.size() != 0) {
			sql += ", ";
			sql += PlainSelect.getStringList(indexes);
		}
		sql += ") ";
		sql += PlainSelect.getStringList(tableOptionsStrings, false, false);

		return sql;
	}
}