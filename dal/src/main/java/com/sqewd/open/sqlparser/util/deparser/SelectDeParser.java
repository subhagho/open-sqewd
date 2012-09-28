package com.sqewd.open.sqlparser.util.deparser;

import java.util.Iterator;
import java.util.List;

import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;
import com.sqewd.open.sqlparser.schema.Column;
import com.sqewd.open.sqlparser.schema.Table;
import com.sqewd.open.sqlparser.statement.select.AllColumns;
import com.sqewd.open.sqlparser.statement.select.AllTableColumns;
import com.sqewd.open.sqlparser.statement.select.FromItem;
import com.sqewd.open.sqlparser.statement.select.FromItemVisitor;
import com.sqewd.open.sqlparser.statement.select.Join;
import com.sqewd.open.sqlparser.statement.select.Limit;
import com.sqewd.open.sqlparser.statement.select.OrderByElement;
import com.sqewd.open.sqlparser.statement.select.OrderByVisitor;
import com.sqewd.open.sqlparser.statement.select.PlainSelect;
import com.sqewd.open.sqlparser.statement.select.SelectExpressionItem;
import com.sqewd.open.sqlparser.statement.select.SelectItem;
import com.sqewd.open.sqlparser.statement.select.SelectItemVisitor;
import com.sqewd.open.sqlparser.statement.select.SelectVisitor;
import com.sqewd.open.sqlparser.statement.select.SubJoin;
import com.sqewd.open.sqlparser.statement.select.SubSelect;
import com.sqewd.open.sqlparser.statement.select.Top;
import com.sqewd.open.sqlparser.statement.select.Union;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link net.sf.jsqlparser.statement.select.Select}
 */
public class SelectDeParser implements SelectVisitor, OrderByVisitor,
		SelectItemVisitor, FromItemVisitor {
	protected StringBuffer buffer;
	protected ExpressionVisitor expressionVisitor;

	public SelectDeParser() {
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
	public SelectDeParser(final ExpressionVisitor expressionVisitor,
			final StringBuffer buffer) {
		this.buffer = buffer;
		this.expressionVisitor = expressionVisitor;
	}

	public void visit(final PlainSelect plainSelect) {
		buffer.append("SELECT ");
		Top top = plainSelect.getTop();
		if (top != null) {
			top.toString();
		}
		if (plainSelect.getDistinct() != null) {
			buffer.append("DISTINCT ");
			if (plainSelect.getDistinct().getOnSelectItems() != null) {
				buffer.append("ON (");
				for (Iterator iter = plainSelect.getDistinct()
						.getOnSelectItems().iterator(); iter.hasNext();) {
					SelectItem selectItem = (SelectItem) iter.next();
					selectItem.accept(this);
					if (iter.hasNext()) {
						buffer.append(", ");
					}
				}
				buffer.append(") ");
			}

		}

		for (Iterator iter = plainSelect.getSelectItems().iterator(); iter
				.hasNext();) {
			SelectItem selectItem = (SelectItem) iter.next();
			selectItem.accept(this);
			if (iter.hasNext()) {
				buffer.append(", ");
			}
		}

		buffer.append(" ");

		if (plainSelect.getFromItem() != null) {
			buffer.append("FROM ");
			plainSelect.getFromItem().accept(this);
		}

		if (plainSelect.getJoins() != null) {
			for (Iterator iter = plainSelect.getJoins().iterator(); iter
					.hasNext();) {
				Join join = (Join) iter.next();
				deparseJoin(join);
			}
		}

		if (plainSelect.getWhere() != null) {
			buffer.append(" WHERE ");
			plainSelect.getWhere().accept(expressionVisitor);
		}

		if (plainSelect.getGroupByColumnReferences() != null) {
			buffer.append(" GROUP BY ");
			for (Iterator iter = plainSelect.getGroupByColumnReferences()
					.iterator(); iter.hasNext();) {
				Expression columnReference = (Expression) iter.next();
				columnReference.accept(expressionVisitor);
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
		}

		if (plainSelect.getHaving() != null) {
			buffer.append(" HAVING ");
			plainSelect.getHaving().accept(expressionVisitor);
		}

		if (plainSelect.getOrderByElements() != null) {
			deparseOrderBy(plainSelect.getOrderByElements());
		}

		if (plainSelect.getLimit() != null) {
			deparseLimit(plainSelect.getLimit());
		}

	}

	public void visit(final Union union) {
		for (Iterator iter = union.getPlainSelects().iterator(); iter.hasNext();) {
			buffer.append("(");
			PlainSelect plainSelect = (PlainSelect) iter.next();
			plainSelect.accept(this);
			buffer.append(")");
			if (iter.hasNext()) {
				buffer.append(" UNION ");
			}

		}

		if (union.getOrderByElements() != null) {
			deparseOrderBy(union.getOrderByElements());
		}

		if (union.getLimit() != null) {
			deparseLimit(union.getLimit());
		}

	}

	public void visit(final OrderByElement orderBy) {
		orderBy.getExpression().accept(expressionVisitor);
		if (orderBy.isAsc()) {
			buffer.append(" ASC");
		} else {
			buffer.append(" DESC");
		}
	}

	public void visit(final Column column) {
		buffer.append(column.getWholeColumnName());
	}

	public void visit(final AllColumns allColumns) {
		buffer.append("*");
	}

	public void visit(final AllTableColumns allTableColumns) {
		buffer.append(allTableColumns.getTable().getWholeTableName() + ".*");
	}

	public void visit(final SelectExpressionItem selectExpressionItem) {
		selectExpressionItem.getExpression().accept(expressionVisitor);
		if (selectExpressionItem.getAlias() != null) {
			buffer.append(" AS " + selectExpressionItem.getAlias());
		}

	}

	public void visit(final SubSelect subSelect) {
		buffer.append("(");
		subSelect.getSelectBody().accept(this);
		buffer.append(")");
	}

	public void visit(final Table tableName) {
		buffer.append(tableName.getWholeTableName());
		String alias = tableName.getAlias();
		if (alias != null && !alias.isEmpty()) {
			buffer.append(" AS " + alias);
		}
	}

	public void deparseOrderBy(final List orderByElements) {
		buffer.append(" ORDER BY ");
		for (Iterator iter = orderByElements.iterator(); iter.hasNext();) {
			OrderByElement orderByElement = (OrderByElement) iter.next();
			orderByElement.accept(this);
			if (iter.hasNext()) {
				buffer.append(", ");
			}
		}
	}

	public void deparseLimit(final Limit limit) {
		// LIMIT n OFFSET skip
		buffer.append(" LIMIT ");
		if (limit.isRowCountJdbcParameter()) {
			buffer.append("?");
		} else if (limit.getRowCount() != 0) {
			buffer.append(limit.getRowCount());
		} else {
			/*
			 * from mysql docs: For compatibility with PostgreSQL, MySQL also
			 * supports the LIMIT row_count OFFSET offset syntax. To retrieve
			 * all rows from a certain offset up to the end of the result set,
			 * you can use some large number for the second parameter.
			 */
			buffer.append("18446744073709551615");
		}

		if (limit.isOffsetJdbcParameter()) {
			buffer.append(" OFFSET ?");
		} else if (limit.getOffset() != 0) {
			buffer.append(" OFFSET " + limit.getOffset());
		}

	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(final StringBuffer buffer) {
		this.buffer = buffer;
	}

	public ExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public void setExpressionVisitor(final ExpressionVisitor visitor) {
		expressionVisitor = visitor;
	}

	public void visit(final SubJoin subjoin) {
		buffer.append("(");
		subjoin.getLeft().accept(this);
		buffer.append(" ");
		deparseJoin(subjoin.getJoin());
		buffer.append(")");
	}

	public void deparseJoin(final Join join) {
		if (join.isSimple()) {
			buffer.append(", ");
		} else {

			if (join.isRight()) {
				buffer.append("RIGHT ");
			} else if (join.isNatural()) {
				buffer.append("NATURAL ");
			} else if (join.isFull()) {
				buffer.append("FULL ");
			} else if (join.isLeft()) {
				buffer.append("LEFT ");
			}

			if (join.isOuter()) {
				buffer.append("OUTER ");
			} else if (join.isInner()) {
				buffer.append("INNER ");
			}

			buffer.append("JOIN ");

		}

		FromItem fromItem = join.getRightItem();
		fromItem.accept(this);
		if (join.getOnExpression() != null) {
			buffer.append(" ON ");
			join.getOnExpression().accept(expressionVisitor);
		}
		if (join.getUsingColumns() != null) {
			buffer.append(" USING ( ");
			for (Iterator iterator = join.getUsingColumns().iterator(); iterator
					.hasNext();) {
				Column column = (Column) iterator.next();
				buffer.append(column.getWholeColumnName());
				if (iterator.hasNext()) {
					buffer.append(" ,");
				}
			}
			buffer.append(")");
		}

	}

}
