package com.sqewd.open.sqlparser.util.deparser;

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;
import com.sqewd.open.sqlparser.schema.Column;
import com.sqewd.open.sqlparser.statement.update.Update;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link net.sf.jsqlparser.statement.update.Update}
 */
public class UpdateDeParser {
	protected StringBuffer buffer;
	protected ExpressionVisitor expressionVisitor;

	public UpdateDeParser() {
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
	public UpdateDeParser(final ExpressionVisitor expressionVisitor,
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

	public void deParse(final Update update) {
		buffer.append("UPDATE " + update.getTable().getWholeTableName()
				+ " SET ");
		for (int i = 0; i < update.getColumns().size(); i++) {
			Column column = (Column) update.getColumns().get(i);
			buffer.append(column.getWholeColumnName() + "=");

			Expression expression = (Expression) update.getExpressions().get(i);
			expression.accept(expressionVisitor);
			if (i < update.getColumns().size() - 1) {
				buffer.append(", ");
			}

		}

		if (update.getWhere() != null) {
			buffer.append(" WHERE ");
			update.getWhere().accept(expressionVisitor);
		}

	}

	public ExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public void setExpressionVisitor(final ExpressionVisitor visitor) {
		expressionVisitor = visitor;
	}

}
