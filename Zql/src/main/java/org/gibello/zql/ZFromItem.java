/*
 * This file is part of Zql.
 *
 * Zql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zql.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gibello.zql;

/**
 * ZFromItem: an SQL FROM clause (example: the FROM part of a SELECT...FROM).
 */
@SuppressWarnings("serial")
public class ZFromItem extends ZAliasedName {

	/**
	 * Create a new FROM clause. See the ZAliasedName constructor for more
	 * information.
	 */
	public ZFromItem() {
		super();
	}

	/**
	 * Create a new FROM clause on a given table. See the ZAliasedName
	 * constructor for more information.
	 * 
	 * @param fullname
	 *            the table name.
	 */
	public ZFromItem(String fullname) {
		super(fullname, ZAliasedName.FORM_TABLE);
	}

	public ZFromItem copy() {
		ZFromItem fi = new ZFromItem();
		if (alias_ != null)
			fi.alias_ = new String(alias_);
		if (column_ != null)
			fi.column_ = new String(column_);
		fi.form_ = form_;
		if (schema_ != null)
			fi.schema_ = new String(schema_);
		if (strform_ != null)
			fi.strform_ = new String(strform_);
		if (table_ != null)
			fi.table_ = new String(table_);
		return fi;
	}
};
