/* ================================================================
 * JSQLParser : java based sql parser 
 * ================================================================
 *
 * Project Info:  http://jsqlparser.sourceforge.net
 * Project Lead:  Leonardo Francalanci (leoonardoo@yahoo.it);
 *
 * (C) Copyright 2004, by Leonardo Francalanci
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package com.sqewd.open.sqlparser.schema;

import com.sqewd.open.sqlparser.statement.select.FromItem;
import com.sqewd.open.sqlparser.statement.select.FromItemVisitor;
import com.sqewd.open.sqlparser.statement.select.IntoTableVisitor;

/**
 * A table. It can have an alias and the schema name it belongs to.
 */
public class Table implements FromItem {
	private String schemaName;
	private String name;
	private String alias;

	public Table() {
	}

	public Table(final String schemaName, final String name) {
		this.schemaName = schemaName;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setName(final String string) {
		name = string;
	}

	public void setSchemaName(final String string) {
		schemaName = string;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(final String string) {
		alias = string;
	}

	public String getWholeTableName() {

		String tableWholeName = null;
		if (name == null)
			return null;
		if (schemaName != null) {
			tableWholeName = schemaName + "." + name;
		} else {
			tableWholeName = name;
		}

		return tableWholeName;

	}

	public void accept(final FromItemVisitor fromItemVisitor) {
		fromItemVisitor.visit(this);
	}

	public void accept(final IntoTableVisitor intoTableVisitor) {
		intoTableVisitor.visit(this);
	}

	@Override
	public String toString() {
		return getWholeTableName() + ((alias != null) ? " AS " + alias : "");
	}
}
