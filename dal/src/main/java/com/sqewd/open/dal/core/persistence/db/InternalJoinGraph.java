/**
 * Copyright 2012 Subho Ghosh (subho.ghosh at outlook dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @filename JoinGraph.java
 * @created Aug 14, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.KeyValuePair;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class InternalJoinGraph extends AbstractJoinGraph {
	private HashMap<String, KeyValuePair<InternalJoinGraph>> joins = null;

	public InternalJoinGraph(Class<?> type, AbstractJoinGraph parent,
			String name, String columname) throws Exception {
		this.type = type;
		this.parent = parent;
		this.columname = columname;

		process(name);
	}

	private void process(String name) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		if (enref.IsJoin)
			throw new Exception("Joined entities should not be included here.");

		table = enref.Entity;
		if (name != null) {
			alias = name;
		} else
			alias = table;
		int ii = 0;
		while (true) {
			if (parent == null || !parent.hasAlias(alias)) {
				break;
			}
			alias = alias + _ALIAS_SUFFIX_ + ii;
			ii++;
		}
		addalias(alias, enref.Entity, true);
		for (String column : enref.ColumnMaps.keySet()) {
			StructAttributeReflect attr = enref.get(column);
			addcolumn(alias + "." + attr.Column, true);
			if (parent != null) {
				columns.put(alias + "." + attr.Column, alias + "."
						+ attr.Column);
			}
			if (attr.Reference == null)
				continue;
			Class<?> rt = Class.forName(attr.Reference.Class);
			InternalJoinGraph graph = new InternalJoinGraph(rt, this,
					attr.Column, attr.Column);
			KeyValuePair<InternalJoinGraph> kvp = new KeyValuePair<InternalJoinGraph>(
					attr.Reference.Field, graph);
			if (joins == null)
				joins = new HashMap<String, KeyValuePair<InternalJoinGraph>>();
			joins.put(column, kvp);

		}
	}

	public boolean searchAlias(String alias) {
		if (usedaliases != null && usedaliases.containsKey(alias)) {
			return true;
		}
		if (joins != null) {
			for (String key : joins.keySet()) {
				InternalJoinGraph ig = joins.get(key).getValue();
				if (ig.searchAlias(alias))
					return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#getAliasFor(
	 * java.util.Stack, java.lang.String, int)
	 */
	@Override
	public KeyValuePair<String> getAliasFor(Stack<KeyValuePair<Class<?>>> path,
			String column, int index) throws Exception {
		KeyValuePair<Class<?>> cls = path.elementAt(index);
		if (cls.getValue().equals(type)) {
			if (index == path.size() - 1) {
				return new KeyValuePair<String>(alias, table);
			} else {
				for (String key : joins.keySet()) {
					if (key.compareTo(cls.getKey()) == 0) {
						return joins.get(key).getValue()
								.getAliasFor(path, cls.getKey(), index + 1);
					}
				}
			}
		}
		throw new Exception("Cannot find alias for PATH[" + getPathString(path)
				+ "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#getPath(java
	 * .lang.String)
	 */
	@Override
	public List<String> getPath(String column) throws Exception {
		List<String> list = new ArrayList<String>();
		if (column.indexOf('.') > 0) {
			String[] parts = column.split("\\.");
			getPath(parts, 0, list);
		} else {
			String cname = alias + "." + column;
			if (columns.containsKey(cname)) {
				list.add(cname);
			}
		}
		return list;
	}

	private void getPath(String[] parts, int index, List<String> list)
			throws Exception {
		if (parts[index].compareTo(alias) == 0 && index + 1 < parts.length) {
			String column = alias + "." + parts[index + 1];
			if (columns.containsKey(column)) {
				if (joins != null && joins.containsKey(parts[index + 1])) {
					InternalJoinGraph child = joins.get(parts[index + 1])
							.getValue();
					child.getPath(parts, index + 1, list);
				} else {
					if (index + 1 == parts.length - 1) {
						list.add(column);
					}
				}
			}
		} else {
			String column = alias + "." + parts[index];
			if (columns.containsKey(column)) {
				if (joins != null && joins.containsKey(parts[index])) {
					InternalJoinGraph child = joins.get(parts[index])
							.getValue();
					child.getPath(parts, index + 1, list);
				} else {
					list.add(column);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#getJoinCondition
	 * ()
	 */
	@Override
	public String getJoinCondition() {
		StringBuffer buff = new StringBuffer();
		if (joins != null && joins.size() > 0) {
			for (String key : joins.keySet()) {
				KeyValuePair<InternalJoinGraph> gr = joins.get(key);
				if (buff.length() > 0)
					buff.append(" and ");

				buff.append(alias).append('.').append(key).append(" = ")
						.append(gr.getValue().alias).append('.')
						.append(gr.getKey());
				String ref = gr.getValue().getJoinCondition();
				if (ref != null && !ref.isEmpty()) {
					if (buff.length() > 0)
						buff.append(" and ");
					buff.append(ref);
				}
			}
		}

		return buff.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		if (columns != null) {
			buff.append("[COLUMNS:");
			boolean first = true;
			for (String column : columns.keySet()) {
				if (first)
					first = false;
				else
					buff.append(", ");
				buff.append(column);
			}
			buff.append("]\n");
		}
		if (usedaliases != null) {
			buff.append("[TABLES:");
			boolean first = true;
			for (String key : usedaliases.keySet()) {
				if (first)
					first = false;
				else
					buff.append(", ");
				buff.append(usedaliases.get(key)).append(" ").append(key);
			}
			buff.append("]\n");
		}
		buff.append("[CONDITION:").append(getJoinCondition()).append("]\n");
		return buff.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#hasJoins()
	 */
	@Override
	public boolean hasJoins() {
		if (joins != null && joins.size() > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#resolveColumn
	 * (java.lang.String)
	 */
	@Override
	public boolean resolveColumn(String column) throws Exception {
		String[] parts = column.split("\\.");
		String talias = parts[0];
		if (hasAlias(talias)) {
			if (alias.compareTo(talias) == 0) {
				if (parts.length == 2) {
					if (columns != null && columns.containsKey(column)) {
						return true;
					} else
						return false;
				} else {
					if (joins.containsKey(talias)) {
						AbstractJoinGraph ag = joins.get(talias).getValue();
						column = column.substring(column.indexOf('.') + 1);
						return ag.resolveColumn(column);
					}
				}
			} else {
				boolean retval = false;
				for (String key : joins.keySet()) {
					AbstractJoinGraph ag = joins.get(key).getValue();
					retval = ag.resolveColumn(column);
					if (retval)
						return true;
				}
			}
		}
		return false;
	}
}
