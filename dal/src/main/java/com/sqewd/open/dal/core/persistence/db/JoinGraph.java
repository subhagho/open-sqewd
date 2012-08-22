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
import java.util.Collection;
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
public class JoinGraph {
	private JoinGraph parent;

	private Class<?> type;

	private String alias;

	private String table;

	private HashMap<String, KeyValuePair<JoinGraph>> joins = null;

	private HashMap<String, String> usedaliases = null;

	private HashMap<String, String> columns = new HashMap<String, String>();

	public JoinGraph(Class<?> type, JoinGraph parent) throws Exception {
		this.type = type;
		this.parent = parent;

		process(null);
	}

	public JoinGraph(Class<?> type, JoinGraph parent, String name)
			throws Exception {
		this.type = type;
		this.parent = parent;

		process(name);
	}

	private boolean hasAlias(String alias) {
		if (parent == null)
			if (usedaliases.containsKey(alias)) {
				return true;
			} else
				return false;
		else
			return parent.hasAlias(alias);
	}

	private void addalias(String alias, String table) {
		if (parent == null) {
			if (usedaliases == null)
				usedaliases = new HashMap<String, String>();
			usedaliases.put(alias, table);
		} else
			parent.addalias(alias, table);
	}

	private void addcolumn(String column) {
		if (parent != null)
			parent.addcolumn(column);
		else {
			if (columns == null) {
				columns = new HashMap<String, String>();
			}
			columns.put(column, column);
		}
	}

	private void process(String name) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
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
			alias = alias + "_" + ii;
			ii++;
		}
		addalias(alias, enref.Entity);

		for (String column : enref.ColumnMaps.keySet()) {
			StructAttributeReflect attr = enref.get(column);
			addcolumn(alias + "." + attr.Column);
			if (parent != null) {
				columns.put(alias + "." + attr.Column, alias + "."
						+ attr.Column);
			}
			if (attr.Reference == null)
				continue;
			Class<?> rt = Class.forName(attr.Reference.Class);
			JoinGraph graph = new JoinGraph(rt, this, attr.Column);
			KeyValuePair<JoinGraph> kvp = new KeyValuePair<JoinGraph>(
					attr.Reference.Field, graph);
			if (joins == null)
				joins = new HashMap<String, KeyValuePair<JoinGraph>>();
			joins.put(column, kvp);
		}
	}

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
					JoinGraph child = joins.get(parts[index + 1]).getValue();
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
					JoinGraph child = joins.get(parts[index]).getValue();
					child.getPath(parts, index + 1, list);
				} else {
					list.add(column);
				}
			}
		}
	}

	private String getPathString(Stack<KeyValuePair<Class<?>>> path) {
		StringBuffer buff = new StringBuffer();

		for (KeyValuePair<Class<?>> cls : path) {
			if (buff.length() != 0)
				buff.append("-->");
			buff.append(cls.getValue().getCanonicalName() + "[" + cls.getKey()
					+ "]");
		}
		return buff.toString();
	}

	/**
	 * Check if this Entity has embedded entities.
	 * 
	 * @return
	 */
	public boolean hasJoins() {
		if (joins != null)
			return true;
		return false;
	}

	/**
	 * Get the Table Aliases used in the Join Condition.
	 * 
	 * @return
	 */
	public HashMap<String, String> getTableAliases() {
		return usedaliases;
	}

	/**
	 * Get the SQL Join Condition represented buy this Graph.
	 * 
	 * @return
	 */
	public String getJoinCondition() {
		StringBuffer buff = new StringBuffer();
		if (joins != null && joins.size() > 0) {
			for (String key : joins.keySet()) {
				KeyValuePair<JoinGraph> gr = joins.get(key);
				if (buff.length() > 0)
					buff.append(" and ");

				buff.append(alias).append('.').append(key).append(" = ")
						.append(gr.getValue().alias).append('.')
						.append(gr.getKey());

				String ref = gr.getValue().getJoinCondition();
				if (ref != null && !ref.isEmpty()) {
					buff.append(" and ").append(ref);
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

	/**
	 * @return the columns
	 */
	public Collection<String> getColumns() {
		return columns.keySet();
	}

	/**
	 * Get the table alias for this Node.
	 * 
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	private static final HashMap<String, JoinGraph> graphs = new HashMap<String, JoinGraph>();

	/**
	 * Get the join graph for the specified type.
	 * 
	 * @param type
	 *            - AbstractEntity type.
	 * @return
	 * @throws Exception
	 */
	public static JoinGraph lookup(Class<?> type) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		String key = enref.Entity;
		if (!graphs.containsKey(key)) {
			JoinGraph graph = new JoinGraph(type, null);
			graphs.put(key, graph);
		}
		return graphs.get(key);
	}

}
