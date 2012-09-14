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
 * @filename ExternalJoinGraph.java
 * @created Sep 3, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.EnumJoinType;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.core.persistence.DataManager;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class ExternalJoinGraph extends AbstractJoinGraph {
	private final HashMap<String, JoinMap> joins = new HashMap<String, JoinMap>();
	private String qjoin = null;

	public ExternalJoinGraph(final Class<?> type) throws Exception {
		this.type = type;
		this.parent = null;
		process();
	}

	private void process() throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		table = enref.Entity;
		alias = enref.Entity;

		if (!enref.IsJoin || enref.Join.Type != EnumJoinType.Virtual)
			throw new Exception("Invalid entity [" + enref.Class
					+ "], does not represent a native join.");
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
				Class<?> jt = Class.forName(attr.Reference.Class);
				InternalJoinGraph ig = new InternalJoinGraph(jt, this, name,
						attr.Column);
				AbstractPersister pers = DataManager.get().getPersister(jt);
				if (!joins.containsKey(pers.key())) {
					JoinMap map = new JoinMap();
					map.setPersisterKey(pers.key());
					joins.put(pers.key(), map);
				}
				JoinMap jm = joins.get(pers.key());
				jm.addGraph(ig.alias, ig);
				if (attr.IsKeyColumn) {
					jm.setKeyQuery(true);
				}
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
			for (String key : joins.keySet()) {
				JoinMap map = joins.get(key);
				if (map.containsKey(parts[0])) {
					AbstractJoinGraph ag = map.getGraph(parts[0]);
					column = column.substring(column.indexOf('.') + 1);
					return ag.getPath(column);
				}
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
			for (String jkey : joins.keySet()) {
				JoinMap map = joins.get(jkey);
				for (String key : map.keySet()) {
					InternalJoinGraph gr = map.getGraph(key);
					String ref = gr.getJoinCondition();
					if (ref != null && !ref.isEmpty()) {
						if (buff.length() > 0) {
							buff.append(" and ");
						}
						buff.append(ref);
					}
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

	public Set<String> getPersisters() {
		if (joins != null && joins.size() > 0)
			return joins.keySet();
		return null;
	}

	public JoinMap getPersisterQueryMap(final String key) {
		if (joins != null && joins.containsKey(key))
			return joins.get(key);
		return null;
	}

	public String getJoinCondition(final String persister) throws Exception {
		StringBuffer buff = new StringBuffer();
		if (joins != null && joins.size() > 0) {
			if (joins.containsKey(persister)) {
				JoinMap map = joins.get(persister);
				for (String key : map.keySet()) {
					InternalJoinGraph gr = map.getGraph(key);
					String ref = gr.getJoinCondition();
					if (ref != null && !ref.isEmpty()) {
						if (buff.length() > 0) {
							buff.append(" and ");
						}
						buff.append(ref);
					}
				}
			} else
				throw new Exception("Persister with ID [" + persister
						+ "] not present in the join table.");
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

	public List<JoinMap> getJoinKeys() {
		List<JoinMap> keys = new ArrayList<JoinMap>();
		for (String key : joins.keySet()) {
			JoinMap jm = joins.get(key);
			if (jm.isKeyQuery()) {
				keys.add(jm);
			}
		}
		return keys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#getAliasFor(
	 * java.util.Stack, java.lang.String, int)
	 */
	@Override
	public KeyValuePair<String> getAliasFor(
			final Stack<KeyValuePair<Class<?>>> path, final String column,
			final int index) throws Exception {
		KeyValuePair<Class<?>> cls = path.elementAt(index);
		if (cls.getValue().equals(type)) {
			if (index == path.size() - 1)
				return new KeyValuePair<String>(alias, table);
			for (String jkey : joins.keySet()) {
				JoinMap map = joins.get(jkey);
				for (String key : map.keySet()) {
					if (key.compareTo(cls.getKey()) == 0)
						return map.getGraph(key).getAliasFor(path, column,
								index + 1);
				}
			}
		}
		throw new Exception("Invalid Path : Root element isn't of type ["
				+ type.getCanonicalName() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph#resolveColumn
	 * (java.lang.String)
	 */
	@Override
	public boolean resolveColumn(final String column) throws Exception {
		throw new Exception("Should not be called.");
	}
}
