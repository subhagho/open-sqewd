package com.sqewd.open.sqlparser.util.deparser;

import java.util.Iterator;

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;
import com.sqewd.open.sqlparser.expression.operators.relational.ExpressionList;
import com.sqewd.open.sqlparser.expression.operators.relational.ItemsListVisitor;
import com.sqewd.open.sqlparser.schema.Column;
import com.sqewd.open.sqlparser.statement.insert.Insert;
import com.sqewd.open.sqlparser.statement.select.SelectVisitor;
import com.sqewd.open.sqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link net.sf.jsqlparser.statement.insert.Insert}
 */
public class InsertDeParser implements ItemsListVisitor {
	protected StringBuffer buffer;
	protected ExpressionVisitor expressionVisitor;
	protected SelectVisitor selectVisitor;

	public InsertDeParser() {
	}

	/**
	 * @param expressionVisitor
	 *            a {@link ExpressionVisitor} to de-parse
	 *            {@link net.sf.jsqlparser.expression.Expression}s. It has to
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
	 *            the buffer that will be filled with the insert
	 */
	public InsertDeParser(final ExpressionVisitor expressionVisitor,
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

	public void deParse(final Insert insert) {
		buffer.append("INSERT INTO ");
		buffer.append(insert.getTable().getWholeTableName());
		if (insert.getColumns() != null) {
			buffer.append("(");
			for (Iterator iter = insert.getColumns().iterator(); iter.hasNext();) {
				Column column = (Column) iter.next();
				buffer.append(column.getColumnName());
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
			buffer.append(")");
		}

		insert.getItemsList().accept(this);

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
