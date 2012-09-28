package com.sqewd.open.sqlparser.util.deparser;

import java.util.Iterator;
import java.util.List;

import com.sqewd.open.sqlparser.expression.AllComparisonExpression;
import com.sqewd.open.sqlparser.expression.AnyComparisonExpression;
import com.sqewd.open.sqlparser.expression.BinaryExpression;
import com.sqewd.open.sqlparser.expression.CaseExpression;
import com.sqewd.open.sqlparser.expression.DateValue;
import com.sqewd.open.sqlparser.expression.DoubleValue;
import com.sqewd.open.sqlparser.expression.Expression;
import com.sqewd.open.sqlparser.expression.ExpressionVisitor;
import com.sqewd.open.sqlparser.expression.Function;
import com.sqewd.open.sqlparser.expression.InverseExpression;
import com.sqewd.open.sqlparser.expression.JdbcParameter;
import com.sqewd.open.sqlparser.expression.LongValue;
import com.sqewd.open.sqlparser.expression.NullValue;
import com.sqewd.open.sqlparser.expression.Parenthesis;
import com.sqewd.open.sqlparser.expression.TimeValue;
import com.sqewd.open.sqlparser.expression.TimestampValue;
import com.sqewd.open.sqlparser.expression.WhenClause;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.Addition;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.BitwiseAnd;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.BitwiseOr;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.BitwiseXor;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.Concat;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.Division;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.Multiplication;
import com.sqewd.open.sqlparser.expression.operators.arithmetic.Subtraction;
import com.sqewd.open.sqlparser.expression.operators.conditional.AndExpression;
import com.sqewd.open.sqlparser.expression.operators.conditional.OrExpression;
import com.sqewd.open.sqlparser.expression.operators.relational.Between;
import com.sqewd.open.sqlparser.expression.operators.relational.EqualsTo;
import com.sqewd.open.sqlparser.expression.operators.relational.ExistsExpression;
import com.sqewd.open.sqlparser.expression.operators.relational.ExpressionList;
import com.sqewd.open.sqlparser.expression.operators.relational.GreaterThan;
import com.sqewd.open.sqlparser.expression.operators.relational.GreaterThanEquals;
import com.sqewd.open.sqlparser.expression.operators.relational.InExpression;
import com.sqewd.open.sqlparser.expression.operators.relational.IsNullExpression;
import com.sqewd.open.sqlparser.expression.operators.relational.ItemsListVisitor;
import com.sqewd.open.sqlparser.expression.operators.relational.LikeExpression;
import com.sqewd.open.sqlparser.expression.operators.relational.Matches;
import com.sqewd.open.sqlparser.expression.operators.relational.MinorThan;
import com.sqewd.open.sqlparser.expression.operators.relational.MinorThanEquals;
import com.sqewd.open.sqlparser.expression.operators.relational.NotEqualsTo;
import com.sqewd.open.sqlparser.schema.Column;
import com.sqewd.open.sqlparser.statement.select.SelectVisitor;
import com.sqewd.open.sqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link net.sf.jsqlparser.expression.Expression}
 */
public class ExpressionDeParser implements ExpressionVisitor, ItemsListVisitor {

	protected StringBuffer buffer;
	protected SelectVisitor selectVisitor;
	protected boolean useBracketsInExprList = true;

	public ExpressionDeParser() {
	}

	/**
	 * @param selectVisitor
	 *            a SelectVisitor to de-parse SubSelects. It has to share the
	 *            same<br>
	 *            StringBuffer as this object in order to work, as:
	 * 
	 *            <pre>
	 * <code>
	 * StringBuffer myBuf = new StringBuffer();
	 * MySelectDeparser selectDeparser = new  MySelectDeparser();
	 * selectDeparser.setBuffer(myBuf);
	 * ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeparser, myBuf);
	 * </code>
	 * </pre>
	 * @param buffer
	 *            the buffer that will be filled with the expression
	 */
	public ExpressionDeParser(final SelectVisitor selectVisitor,
			final StringBuffer buffer) {
		this.selectVisitor = selectVisitor;
		this.buffer = buffer;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(final StringBuffer buffer) {
		this.buffer = buffer;
	}

	public void visit(final Addition addition) {
		visitBinaryExpression(addition, " + ");
	}

	public void visit(final AndExpression andExpression) {
		visitBinaryExpression(andExpression, " AND ");
	}

	public void visit(final Between between) {
		between.getLeftExpression().accept(this);
		if (between.isNot()) {
			buffer.append(" NOT");
		}

		buffer.append(" BETWEEN ");
		between.getBetweenExpressionStart().accept(this);
		buffer.append(" AND ");
		between.getBetweenExpressionEnd().accept(this);

	}

	public void visit(final Division division) {
		visitBinaryExpression(division, " / ");

	}

	public void visit(final DoubleValue doubleValue) {
		buffer.append(doubleValue.getValue());

	}

	public void visit(final EqualsTo equalsTo) {
		visitBinaryExpression(equalsTo, " = ");
	}

	public void visit(final GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan, " > ");
	}

	public void visit(final GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals, " >= ");

	}

	public void visit(final InExpression inExpression) {

		inExpression.getLeftExpression().accept(this);
		if (inExpression.isNot()) {
			buffer.append(" NOT");
		}
		buffer.append(" IN ");

		inExpression.getItemsList().accept(this);
	}

	public void visit(final InverseExpression inverseExpression) {
		buffer.append("-");
		inverseExpression.getExpression().accept(this);
	}

	public void visit(final IsNullExpression isNullExpression) {
		isNullExpression.getLeftExpression().accept(this);
		if (isNullExpression.isNot()) {
			buffer.append(" IS NOT NULL");
		} else {
			buffer.append(" IS NULL");
		}
	}

	public void visit(final JdbcParameter jdbcParameter) {
		buffer.append("?");

	}

	public void visit(final LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression, " LIKE ");

	}

	public void visit(final ExistsExpression existsExpression) {
		if (existsExpression.isNot()) {
			buffer.append(" NOT EXISTS ");
		} else {
			buffer.append(" EXISTS ");
		}
		existsExpression.getRightExpression().accept(this);
	}

	public void visit(final LongValue longValue) {
		buffer.append(longValue.getStringValue());

	}

	public void visit(final MinorThan minorThan) {
		visitBinaryExpression(minorThan, " < ");

	}

	public void visit(final MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals, " <= ");

	}

	public void visit(final Multiplication multiplication) {
		visitBinaryExpression(multiplication, " * ");

	}

	public void visit(final NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo, " <> ");

	}

	public void visit(final NullValue nullValue) {
		buffer.append("NULL");

	}

	public void visit(final OrExpression orExpression) {
		visitBinaryExpression(orExpression, " OR ");

	}

	public void visit(final Parenthesis parenthesis) {
		if (parenthesis.isNot()) {
			buffer.append(" NOT ");
		}

		buffer.append("(");
		parenthesis.getExpression().accept(this);
		buffer.append(")");

	}

	public void visit(final Subtraction subtraction) {
		visitBinaryExpression(subtraction, "-");

	}

	private void visitBinaryExpression(final BinaryExpression binaryExpression,
			final String operator) {
		if (binaryExpression.isNot()) {
			buffer.append(" NOT ");
		}
		binaryExpression.getLeftExpression().accept(this);
		buffer.append(operator);
		binaryExpression.getRightExpression().accept(this);

	}

	public void visit(final SubSelect subSelect) {
		buffer.append("(");
		subSelect.getSelectBody().accept(selectVisitor);
		buffer.append(")");
	}

	public void visit(final Column tableColumn) {
		String tableName = tableColumn.getTable().getWholeTableName();
		if (tableName != null) {
			buffer.append(tableName + ".");
		}

		buffer.append(tableColumn.getColumnName());
	}

	public void visit(final Function function) {
		if (function.isEscaped()) {
			buffer.append("{fn ");
		}

		buffer.append(function.getName());
		if (function.isAllColumns()) {
			buffer.append("(*)");
		} else if (function.getParameters() == null) {
			buffer.append("()");
		} else {
			boolean oldUseBracketsInExprList = useBracketsInExprList;
			if (function.isDistinct()) {
				useBracketsInExprList = false;
				buffer.append("(DISTINCT ");
			}
			visit(function.getParameters());
			useBracketsInExprList = oldUseBracketsInExprList;
			if (function.isDistinct()) {
				buffer.append(")");
			}
		}

		if (function.isEscaped()) {
			buffer.append("}");
		}

	}

	public void visit(final ExpressionList expressionList) {
		if (useBracketsInExprList) {
			buffer.append("(");
		}
		for (Iterator iter = expressionList.getExpressions().iterator(); iter
				.hasNext();) {
			Expression expression = (Expression) iter.next();
			expression.accept(this);
			if (iter.hasNext()) {
				buffer.append(", ");
			}
		}
		if (useBracketsInExprList) {
			buffer.append(")");
		}
	}

	public SelectVisitor getSelectVisitor() {
		return selectVisitor;
	}

	public void setSelectVisitor(final SelectVisitor visitor) {
		selectVisitor = visitor;
	}

	public void visit(final DateValue dateValue) {
		buffer.append("{d '" + dateValue.getValue().toString() + "'}");
	}

	public void visit(final TimestampValue timestampValue) {
		buffer.append("{ts '" + timestampValue.getValue().toString() + "'}");
	}

	public void visit(final TimeValue timeValue) {
		buffer.append("{t '" + timeValue.getValue().toString() + "'}");
	}

	public void visit(final CaseExpression caseExpression) {
		buffer.append("CASE ");
		Expression switchExp = caseExpression.getSwitchExpression();
		if (switchExp != null) {
			switchExp.accept(this);
		}

		List clauses = caseExpression.getWhenClauses();
		for (Iterator iter = clauses.iterator(); iter.hasNext();) {
			Expression exp = (Expression) iter.next();
			exp.accept(this);
		}

		Expression elseExp = caseExpression.getElseExpression();
		if (elseExp != null) {
			elseExp.accept(this);
		}

		buffer.append(" END");
	}

	public void visit(final WhenClause whenClause) {
		buffer.append(" WHEN ");
		whenClause.getWhenExpression().accept(this);
		buffer.append(" THEN ");
		whenClause.getThenExpression().accept(this);
	}

	public void visit(final AllComparisonExpression allComparisonExpression) {
		buffer.append(" ALL ");
		allComparisonExpression.GetSubSelect().accept((ExpressionVisitor) this);
	}

	public void visit(final AnyComparisonExpression anyComparisonExpression) {
		buffer.append(" ANY ");
		anyComparisonExpression.GetSubSelect().accept((ExpressionVisitor) this);
	}

	public void visit(final Concat concat) {
		visitBinaryExpression(concat, " || ");
	}

	public void visit(final Matches matches) {
		visitBinaryExpression(matches, " @@ ");
	}

	public void visit(final BitwiseAnd bitwiseAnd) {
		visitBinaryExpression(bitwiseAnd, " & ");
	}

	public void visit(final BitwiseOr bitwiseOr) {
		visitBinaryExpression(bitwiseOr, " | ");
	}

	public void visit(final BitwiseXor bitwiseXor) {
		visitBinaryExpression(bitwiseXor, " ^ ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.sqlparser.expression.ExpressionVisitor#visit(com.sqewd
	 * .open.sqlparser.expression.StringValue)
	 */
	public void visit(
			final com.sqewd.open.sqlparser.expression.StringValue stringValue) {
		// TODO Auto-generated method stub

	}

}