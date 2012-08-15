/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.core.persistence.query;

import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;

/**
 * @author subhagho
 * 
 */
public class ColumnConditionPredicate extends AbstractConditionPredicate {
	private Class<?> type;

	private String column;

	private String alias;

	public ColumnConditionPredicate(Class<?> type, String column) {
		this.type = type;
		setColumn(column);
	}

	public ColumnConditionPredicate(String alias, Class<?> type, String column) {
		this.type = type;
		setColumn(column);
		this.alias = alias;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(String column) {
		if (type != null) {
			try {
				if (column.indexOf('.') > 0) {
					StructEntityReflect enref = ReflectionUtils.get()
							.getEntityMetadata(type);
					String[] parts = column.split("\\.");
					if (parts[0].compareTo(enref.Entity) == 0) {
						StructAttributeReflect attr = enref.get(parts[0]);
						boolean tablename = true;
						if (attr != null) {
							if (attr.Reference != null) {
								Class<?> rt = Class
										.forName(attr.Reference.Class);
								StructEntityReflect renref = ReflectionUtils
										.get().getEntityMetadata(rt);
								StructAttributeReflect rattr = renref
										.get(parts[1]);
								if (rattr != null)
									tablename = false;
							}
						}
						if (tablename) {
							StringBuffer buff = new StringBuffer();
							for (int ii = 1; ii < parts.length; ii++) {
								if (ii != 1)
									buff.append(".");
								buff.append(parts[ii]);
							}
							this.column = buff.toString();
						} else {
							this.column = column;
						}
					} else {
						this.column = column;
					}
				} else
					this.column = column;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (alias == null) {
			Entity eann = type.getAnnotation(Entity.class);
			return eann.recordset() + "." + column;
		} else {
			return alias + "." + column;
		}
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
