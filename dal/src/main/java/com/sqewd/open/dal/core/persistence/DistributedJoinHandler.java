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
 * @filename DistributedJoinHandler.java
 * @created Sep 3, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.gibello.zql.ZConstant;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZQuery;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.EnumJoinType;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph;
import com.sqewd.open.dal.core.persistence.db.ExternalJoinGraph;
import com.sqewd.open.dal.core.persistence.db.JoinMap;
import com.sqewd.open.dal.core.persistence.db.LocalResultSet;
import com.sqewd.open.dal.core.persistence.query.matcher.SQLConditionMatcher;
import com.sqewd.open.dal.core.persistence.query.parser.DalSqlParser;

/**
 * TODO <comment>
 * 
 * @author subhagho
 * 
 */
public class DistributedJoinHandler {
	private final HashMap<String, Class<?>> classmap = new HashMap<String, Class<?>>();
	private final List<StructEntityReflect> keys = new ArrayList<StructEntityReflect>();
	private StructEntityReflect enref = null;
	private String query = null;
	private ExternalJoinGraph graph = null;
	private ZQuery combinedQuery = null;
	private HashMap<String, String> refkeys = new HashMap<String, String>();
	private LocalResultSet results = null;
	private int limit = -1;

	public DistributedJoinHandler(final Class<?> type, final String query,
			final int limit) throws Exception {
		enref = ReflectionUtils.get().getEntityMetadata(type);
		if (!enref.IsJoin)
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] does not represent a join.");
		if (enref.Join.Type != EnumJoinType.Virtual)
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] does not represent a virtual join.");
		for (StructAttributeReflect attr : enref.Attributes) {
			if (attr.Reference == null) {
				continue;
			}
			Class<?> jt = Class.forName(attr.Reference.Class);
			StructEntityReflect subref = ReflectionUtils.get()
					.getEntityMetadata(jt);
			if (subref == null)
				throw new Exception("No entity defined for name ["
						+ attr.Column + "]");
			if (attr.IsKeyColumn) {
				keys.add(subref);
			}
			Class<?> jtype = Class.forName(subref.Class);
			classmap.put(attr.Column, jtype);
		}
		this.query = query;
		this.limit = limit;

		graph = (ExternalJoinGraph) AbstractJoinGraph.lookup(type);
	}

	private ZQuery getJoinClause(final JoinMap jm) throws Exception {
		if (refkeys == null || refkeys.size() <= 0)
			return null;
		JoinMap[] maps = new JoinMap[refkeys.size() + 1];
		maps[0] = jm;
		int index = 1;
		for (String perkey : refkeys.keySet()) {
			maps[index] = graph.getPersisterQueryMap(perkey);
			index++;
		}

		ZQuery zq = (ZQuery) combinedQuery.copy();
		splitQuery(maps, zq.getWhere());
		zq.getWhere().clean();

		return zq;
	}

	public List<AbstractEntity> read() throws Exception {
		String cQuery = query;
		if (cQuery != null && !cQuery.isEmpty()) {
			cQuery = cQuery + " AND " + graph.getJoinCondition();
		} else {
			cQuery = graph.getJoinCondition();
		}
		InputStream cis = new ByteArrayInputStream(cQuery.getBytes());
		DalSqlParser cparser = new DalSqlParser(cis);

		combinedQuery = cparser.QueryClause();

		List<JoinMap> keys = graph.getJoinKeys();
		if (keys != null && !keys.isEmpty()) {
			for (JoinMap qmap : keys) {
				InputStream is = new ByteArrayInputStream(query.getBytes());
				DalSqlParser parser = new DalSqlParser(is);

				ZQuery zq = parser.QueryClause();

				parseQuery(qmap, zq);

				ResultSet rs = DataManager.get()
						.getPersisterByName(qmap.getPersisterKey())
						.select(zq.getWhere().toString(), qmap.getTypes(), -1);
				if (!(rs instanceof LocalResultSet))
					throw new Exception("Invalid ResultSet returned. TYPE ["
							+ rs.getClass().getCanonicalName() + "]");
				addResult((LocalResultSet) rs, qmap);

				qmap.setProcessed(true);
			}
		}
		for (String persister : graph.getPersisters()) {
			JoinMap qmap = graph.getPersisterQueryMap(persister);
			if (qmap.isProcessed()) {
				continue;
			}
			InputStream is = new ByteArrayInputStream(query.getBytes());
			DalSqlParser parser = new DalSqlParser(is);

			ZQuery zq = parser.QueryClause();

			parseQuery(qmap, zq);

			ResultSet rs = DataManager.get().getPersisterByName(persister)
					.select(zq.getWhere().toString(), qmap.getTypes(), -1);
			if (!(rs instanceof LocalResultSet))
				throw new Exception("Invalid ResultSet returned. TYPE ["
						+ rs.getClass().getCanonicalName() + "]");
			addResult((LocalResultSet) rs, qmap);
		}
		if (!results.first())
			throw new Exception("Error resetting cursor index.");
		return null;
	}

	private void addResult(final LocalResultSet rs, final JoinMap jm)
			throws Exception {
		if (refkeys.isEmpty()) {
			results = rs;
			refkeys.put(jm.getPersisterKey(), jm.getPersisterKey());
			return;
		}
		ZQuery zq = getJoinClause(jm);
		if (zq != null) {
			ZExpression cond = (ZExpression) zq.getWhere();
			SQLConditionMatcher matcher = new SQLConditionMatcher(cond);

			if (!results.first())
				throw new Exception("Error resetting cursor index.");

			while (results.next()) {
				if (!rs.first())
					throw new Exception("Error resetting cursor index.");
				while (rs.next()) {
					if (matcher.match(results, rs)) {
						results.append(jm.getPersisterKey(), rs);
					}
				}
			}
		}
	}

	private void parseQuery(final JoinMap pmap, final ZQuery zq)
			throws Exception {
		splitQuery(new JoinMap[] { pmap }, zq.getWhere());

		zq.getWhere().clean();

		pmap.setQuery(zq);
	}

	private void splitQuery(final JoinMap[] maps, final ZExp exp)
			throws Exception {
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			Vector<ZExp> ve = ze.getOperands();
			if (ve != null && ve.size() > 0) {
				if (ve.size() == 2) {
					ZExp le = ve.elementAt(0);
					ZExp re = ve.elementAt(1);
					if ((le instanceof ZConstant) && (re instanceof ZConstant)) {
						ZConstant lc = (ZConstant) le;
						ZConstant rc = (ZConstant) re;
						ZConstant zc = null;
						if (lc.getType() == ZConstant.COLUMNNAME
								&& rc.getType() == ZConstant.COLUMNNAME) {
							boolean clear = true;
							if (!clearCondition(maps, lc)) {
								if (!clearCondition(maps, rc)) {
									clear = false;
								}
							}
							if (clear) {
								ze.clear();
							}
							return;
						} else if (lc.getType() == ZConstant.COLUMNNAME) {
							zc = lc;

						} else if (rc.getType() == ZConstant.COLUMNNAME) {
							zc = rc;

						}

						if (clearCondition(maps, zc)) {
							ze.clear();
						}
						return;
					}
				}
				for (ZExp ex : ve) {
					if (ex instanceof ZConstant) {
						ZConstant zc = (ZConstant) ex;
						if (zc.getType() == ZConstant.COLUMNNAME) {
							String column = zc.getValue();
							List<String> resolved = graph.getPath(column);
							if (resolved.size() <= 0)
								throw new Exception("Cannot resolve column ["
										+ column + "]");
							else if (resolved.size() > 1)
								throw new Exception("Ambiguous column ["
										+ column + "]");
							else {
								zc.setValue(resolved.get(0));
							}
							String ename = getEntityName(zc.getValue());
							boolean found = false;
							for (JoinMap map : maps) {
								if (map.search(ename)) {
									found = true;
									break;
								}
							}
							if (!found) {
								ze.clear();
								return;
							}
						}
					} else {
						splitQuery(maps, ex);
					}
				}
			}
		}
	}

	private boolean clearCondition(final JoinMap[] maps, final ZConstant col)
			throws Exception {
		boolean clear = true;

		String ename = getEntityName(col.getValue());
		for (JoinMap map : maps) {
			if (map.search(ename)) {
				clear = false;
			}
		}
		return clear;
	}

	private String getEntityName(final String column) {
		String[] parts = column.split("\\.");
		return parts[0];
	}
}
