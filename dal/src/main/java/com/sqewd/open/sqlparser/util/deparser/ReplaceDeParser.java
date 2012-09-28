package com.sqewd.open.sqlparser.util.deparser;

import java.util.Iterator;

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;
import com.sqewd.open.sqlparser.expression.operators.relational.ExpressionList;
import com.sqewd.open.sqlparser.expression.operators.relational.ItemsListVisitor;
import com.sqewd.open.sqlparser.schema.Column;
import com.sqewd.open.sqlparser.statement.replace.Replace;
import com.sqewd.open.sqlparser.statement.select.SelectVisitor;
import com.sqewd.open.sqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link net.sf.jsqlparser.statement.replace.Replace}
 */
public class ReplaceDeParser implements ItemsListVisitor {
	protected StringBuffer buffer;
	protected ExpressionVisitor expressionVisitor;
	protected SelectVisitor selectVisitor;

	public ReplaceDeParser() {
	}

	/**
	 * @param expressionVisitor
	 *            a {@link ExpressionVisitor} to de-parse expressions. It has to
	 *            share the same<br>
	 *            StringBuffer (buffer parameter) as this object in order to
	 *            work
	 * @param selectVisitor
	 *            a {@link SelectVisitor} to de-parse
	 *            {@link net.sf.jsqlparser.statement.select.Select}s. It has to
	 *            share the same<br>
	 *            StringBuffer (buffer parameter) as this object in order to
	 *            work
	 * @param buffer
	 *            the buffer that will be filled with the select
	 */
	public ReplaceDeParser(final ExpressionVisitor expressionVisitor,
			final SelectVisitor selectVisitor, final StringBuffer buffer) {
		this.buffer = buffer;
		this.expressionVisitor = expressionVisitor;
		this.selectVisitor = selectVisitor;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(final StringBuffer buffer) {
		this.buffer = buffer;
	}

	public void deParse(final Replace replace) {
		buffer.append("REPLACE " + replace.getTable().getWholeTableName());
		if (replace.getItemsList() != null) {
			if (replace.getColumns() != null) {
				buffer.append(" (");
				for (int i = 0; i < replace.getColumns().size(); i++) {
					Column column = (Column) replace.getColumns().get(i);
					buffer.append(column.getWholeColumnName());
					if (i < replace.getColumns().size() - 1) {
						buffer.append(", ");
					}
				}
				buffer.append(") ");
			} else {
				buffer.append(" ");
			}

		} else {
			buffer.append(" SET ");
			for (int i = 0; i < replace.getColumns().size(); i++) {
				Column column = (Column) replace.getColumns().get(i);
				buffer.append(column.getWholeColumnName() + "=");

				Expression expression = (Expression) replace.getExpressions()
						.get(i);
				expression.accept(expressionVisitor);
				if (i < replace.getColumns().size() - 1) {
					buffer.append(", ");
				}

			}
		}

	}

	public void visit(final ExpressionList expressionList) {
		buffer.append(" VALUES (");
		for (Iterator iter = expressionList.getExpressions().iterator(); iter
				.hasNext();) {
			Expression expression = (Expression) iter.next();
			expression.accept(expressionVisitor);
			if (iter.hasNext()) {
				buffer.append(", ");
			}
		}
		buffer.append(")");
	}

	public void visit(final SubSelect subSelect) {
		subSelect.getSelectBody().accept(selectVisitor);
	}

	public ExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public SelectVisitor getSelectVisitor() {
		return selectVisitor;
	}

	public void setExpressionVisitor(final ExpressionVisitor visitor) {
		expressionVisitor = visitor;
	}

	public void setSelectVisitor(final SelectVisitor visitor) {
		selectVisitor = visitor;
	}

}
