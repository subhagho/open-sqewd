package com.sqewd.open.sqlparser.util.deparser;

import com.sqewd.open.sqlparser.expression.ExpressionVisitor;
import com.sqewd.open.sqlparser.statement.delete.Delete;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link net.sf.jsqlparser.statement.delete.Delete}
 */
public class DeleteDeParser {
	protected StringBuffer buffer;
	protected ExpressionVisitor expressionVisitor;

	public DeleteDeParser() {
	}

	/**
	 * @param expressionVisitor
	 *            a {@link ExpressionVisitor} to de-parse expressions. It has to
	 *            share the same<br>
	 *            StringBuffer (buffer parameter) as this object in order to
	 *            work
	 * @param buffer
	 *            the buffer that will be filled with the select
	 */
	public DeleteDeParser(final ExpressionVisitor expressionVisitor,
			final StringBuffer buffer) {
		this.buffer = buffer;
		this.expressionVisitor = expressionVisitor;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(final StringBuffer buffer) {
		this.buffer = buffer;
	}

	public void deParse(final Delete delete) {
		buffer.append("DELETE FROM " + delete.getTable().getWholeTableName());
		if (delete.getWhere() != null) {
			buffer.append(" WHERE ");
			delete.getWhere().accept(expressionVisitor);
		}

	}

	public ExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public void setExpressionVisitor(final ExpressionVisitor visitor) {
		expressionVisitor = visitor;
	}

}
