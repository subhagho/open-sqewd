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
 * @filename NativeJoinGraph.java
 * @created Aug 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.sqewd.open.dal.api.persistence.EnumJoinType;
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
public class NativeJoinGraph extends AbstractJoinGraph {
	private HashMap<String, InternalJoinGraph> joins = new HashMap<String, InternalJoinGraph>();
	private String qjoin = null;

	public NativeJoinGraph(Class<?> type) throws Exception {
		this.type = type;
		this.parent = null;
		process();
	}

	private void process() throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		table = enref.Entity;
		alias = enref.Entity;

		if (!enref.IsJoin || enref.Join.Type != EnumJoinType.Native) {
			throw new Exception("Invalid entity [" + enref.Class
					+ "], does not represent a native join.");
		}
		qjoin = enref.Join.Join;

		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr.Reference != null) {
				int count = 0;
				String name = attr.Column;
				while (true) {
					if (!hasAlias(name)) {
						break;
					}
					name = name + _ALIAS_SUFFIX_ + count;
					count++;
				}
				InternalJoinGraph ig = new InternalJoinGraph(
						Class.forName(attr.Reference.Class), this, name,
						attr.Column);
				joins.put(ig.alias, ig);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#hasJoins()
	 */
	@Override
	public boolean hasJoins() {
		if (joins != null && joins.size() > 0)
			return true;
		return false;
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
		List<String> path = new ArrayList<String>();
		if (column.indexOf('.') > 0) {
			String[] parts = column.split("\\.");
			if (joins.containsKey(parts[0])) {
				AbstractJoinGraph ag = joins.get(parts[0]);
				column = column.substring(column.indexOf('.') + 1);
				return ag.getPath(column);
			}
		} else {
			if (joins.containsKey(column)) {
				path.add(column);
			}
		}
		return path;
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
				InternalJoinGraph gr = joins.get(key);
				String ref = gr.getJoinCondition();
				if (ref != null && !ref.isEmpty()) {
					if (buff.length() > 0)
						buff.append(" and ");
					buff.append(ref);
				}
			}
		}

		if (buff.length() > 0) {
			buff.insert(0, "(").append(")");
			if (qjoin != null && qjoin.length() > 0) {
				buff.append(" and ");
			}
		}
		if (qjoin != null && qjoin.length() > 0) {
			buff.append(" ").append(qjoin);
		}
		return buff.toString();
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
			}
			for (String key : joins.keySet()) {
				if (key.compareTo(cls.getKey()) == 0) {
					return joins.get(key).getAliasFor(path, column, index + 1);
				}
			}
		}
		throw new Exception("Invalid Path : Root element isn't of type ["
				+ type.getCanonicalName() + "]");
	}

	public AbstractJoinGraph getElementGraph(String column) throws Exception {
		if (joins.containsKey(column)) {
			return joins.get(column);
		}
		throw new Exception("No joins specified for column [" + column + "]");
	}
}
