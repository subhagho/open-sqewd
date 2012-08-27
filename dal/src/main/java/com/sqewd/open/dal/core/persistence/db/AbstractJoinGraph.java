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
 * @filename AbstractJoinGraph.java
 * @created Aug 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.sqewd.open.dal.api.persistence.EnumJoinType;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.KeyValuePair;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public abstract class AbstractJoinGraph {
	protected AbstractJoinGraph parent;

	protected Class<?> type;

	protected String alias;

	protected String table;

	protected HashMap<String, String> usedaliases = new HashMap<String, String>();

	protected HashMap<String, String> columns = new HashMap<String, String>();

	protected boolean hasAlias(String alias) {
		if (parent == null)
			if (usedaliases.containsKey(alias)) {
				return true;
			} else
				return false;
		else
			return parent.hasAlias(alias);
	}

	protected void addalias(String alias, String table) {
		if (parent == null) {
			if (usedaliases == null)
				usedaliases = new HashMap<String, String>();
			usedaliases.put(alias, table);
		} else
			parent.addalias(alias, table);
	}

	protected void addcolumn(String column) {
		if (parent != null)
			parent.addcolumn(column);
		else {
			if (columns == null) {
				columns = new HashMap<String, String>();
			}
			columns.put(column, column);
		}
	}

	/**
	 * Check if this Entity has embedded entities.
	 * 
	 * @return
	 */
	public abstract boolean hasJoins();

	/**
	 * Get the Table Aliases used in the Join Condition.
	 * 
	 * @return
	 */
	public HashMap<String, String> getTableAliases() {
		return usedaliases;
	}

	protected String getPathString(Stack<KeyValuePair<Class<?>>> path) {
		StringBuffer buff = new StringBuffer();

		for (KeyValuePair<Class<?>> cls : path) {
			if (buff.length() != 0)
				buff.append("-->");
			buff.append(cls.getValue().getCanonicalName() + "[" + cls.getKey()
					+ "]");
		}
		return buff.toString();
	}

	public abstract List<String> getPath(String column) throws Exception;

	/**
	 * Get the SQL Join Condition represented buy this Graph.
	 * 
	 * @return
	 */
	public abstract String getJoinCondition();

	public abstract KeyValuePair<String> getAliasFor(
			Stack<KeyValuePair<Class<?>>> path, String column, int index)
			throws Exception;

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

	private static final HashMap<String, AbstractJoinGraph> graphs = new HashMap<String, AbstractJoinGraph>();

	/**
	 * Get the join graph for the specified type.
	 * 
	 * @param type
	 *            - AbstractEntity type.
	 * @return
	 * @throws Exception
	 */
	public static AbstractJoinGraph lookup(Class<?> type) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		String key = enref.Entity;
		if (!graphs.containsKey(key)) {
			if (!enref.IsJoin) {
				InternalJoinGraph ig = new InternalJoinGraph(type, null, null);
				graphs.put(key, ig);
			} else if (enref.Join.Type == EnumJoinType.Native) {
				NativeJoinGraph ng = new NativeJoinGraph(type);
				graphs.put(key, ng);
			} else {
				throw new Exception("Join Type [" + enref.Join.Type.name()
						+ "] not implemented.");
			}
		}
		return graphs.get(key);
	}

}
