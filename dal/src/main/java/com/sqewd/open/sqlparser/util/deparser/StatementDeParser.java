package com.sqewd.open.sqlparser.util.deparser;

import java.util.Iterator;

import com.sqewd.open.sqlparser.statement.StatementVisitor;
import com.sqewd.open.sqlparser.statement.create.table.CreateTable;
import com.sqewd.open.sqlparser.statement.delete.Delete;
import com.sqewd.open.sqlparser.statement.drop.Drop;
import com.sqewd.open.sqlparser.statement.insert.Insert;
import com.sqewd.open.sqlparser.statement.replace.Replace;
import com.sqewd.open.sqlparser.statement.select.Select;
import com.sqewd.open.sqlparser.statement.select.WithItem;
import com.sqewd.open.sqlparser.statement.truncate.Truncate;
import com.sqewd.open.sqlparser.statement.update.Update;

public class StatementDeParser implements StatementVisitor {
	protected StringBuffer buffer;

	public StatementDeParser(final StringBuffer buffer) {
		this.buffer = buffer;
	}

	public void visit(final CreateTable createTable) {
		CreateTableDeParser createTableDeParser = new CreateTableDeParser(
				buffer);
		createTableDeParser.deParse(createTable);
	}

	public void visit(final Delete delete) {
		SelectDeParser selectDeParser = new SelectDeParser();
		selectDeParser.setBuffer(buffer);
		ExpressionDeParser expressionDeParser = new ExpressionDeParser(
				selectDeParser, buffer);
		selectDeParser.setExpressionVisitor(expressionDeParser);
		DeleteDeParser deleteDeParser = new DeleteDeParser(expressionDeParser,
				buffer);
		deleteDeParser.deParse(delete);
	}

	public void visit(final Drop drop) {
		// TODO Auto-generated method stub

	}

	public void visit(final Insert insert) {
		SelectDeParser selectDeParser = new SelectDeParser();
		selectDeParser.setBuffer(buffer);
		ExpressionDeParser expressionDeParser = new ExpressionDeParser(
				selectDeParser, buffer);
		selectDeParser.setExpressionVisitor(expressionDeParser);
		InsertDeParser insertDeParser = new InsertDeParser(expressionDeParser,
				selectDeParser, buffer);
		insertDeParser.deParse(insert);

	}

	public void visit(final Replace replace) {
		SelectDeParser selectDeParser = new SelectDeParser();
		selectDeParser.setBuffer(buffer);
		ExpressionDeParser expressionDeParser = new ExpressionDeParser(
				selectDeParser, buffer);
		selectDeParser.setExpressionVisitor(expressionDeParser);
		ReplaceDeParser replaceDeParser = new ReplaceDeParser(
				expressionDeParser, selectDeParser, buffer);
		replaceDeParser.deParse(replace);
	}

	public void visit(final Select select) {
		SelectDeParser selectDeParser = new SelectDeParser();
		selectDeParser.setBuffer(buffer);
		ExpressionDeParser expressionDeParser = new ExpressionDeParser(
				selectDeParser, buffer);
		selectDeParser.setExpressionVisitor(expressionDeParser);
		if (select.getWithItemsList() != null
				&& !select.getWithItemsList().isEmpty()) {
			buffer.append("WITH ");
			for (Iterator iter = select.getWithItemsList().iterator(); iter
					.hasNext();) {
				WithItem withItem = (WithItem) iter.next();
				buffer.append(withItem);
				if (iter.hasNext()) {
					buffer.append(",");
				}
				buffer.append(" ");
			}
		}
		select.getSelectBody().accept(selectDeParser);

	}

	public void visit(final Truncate truncate) {
		// TODO Auto-generated method stub

	}

	public void visit(final Update update) {
		SelectDeParser selectDeParser = new SelectDeParser();
		selectDeParser.setBuffer(buffer);
		ExpressionDeParser expressionDeParser = new ExpressionDeParser(
				selectDeParser, buffer);
		UpdateDeParser updateDeParser = new UpdateDeParser(expressionDeParser,
				buffer);
		selectDeParser.setExpressionVisitor(expressionDeParser);
		updateDeParser.deParse(update);

	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(final StringBuffer buffer) {
		this.buffer = buffer;
	}

}
