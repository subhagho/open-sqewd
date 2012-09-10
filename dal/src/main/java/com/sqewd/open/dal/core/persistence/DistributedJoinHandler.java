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
import com.sqewd.open.dal.core.persistence.db.LocalResultSet;
import com.sqewd.open.dal.core.persistence.db.JoinMap;
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
	private int limit = -1;
	private ExternalJoinGraph graph = null;
	private String combinedQuery = null;

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

	public List<AbstractEntity> read() throws Exception {
		combinedQuery = query;
		if (combinedQuery != null && !combinedQuery.isEmpty()) {
			combinedQuery = combinedQuery + " AND " + graph.getJoinCondition();
		} else {
			combinedQuery = graph.getJoinCondition();
		}

		for (String persister : graph.getPersisters()) {
			JoinMap qmap = graph.getPersisterQueryMap(persister);

			InputStream is = new ByteArrayInputStream(query.getBytes());
			DalSqlParser parser = new DalSqlParser(is);

			ZQuery zq = parser.QueryClause();

			parseQuery(qmap, zq);

			ResultSet rs = DataManager.get().getPersisterByName(persister)
					.select(zq.getWhere().toString(), qmap.getType(), -1);
			if (!(rs instanceof LocalResultSet))
				throw new Exception("Invalid ResultSet returned. TYPE ["
						+ rs.getClass().getCanonicalName() + "]");

		}
		return null;
	}

	private void parseQuery(final JoinMap pmap, final ZQuery zq)
			throws Exception {
		splitQuery(pmap, zq.getWhere());

		zq.getWhere().clean();

		pmap.setQuery(zq);
	}

	private void splitQuery(final JoinMap pmap, final ZExp exp)
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
							if (!clearCondition(pmap, lc)) {
								if (!clearCondition(pmap, rc)) {
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

						if (clearCondition(pmap, zc)) {
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
							if (!pmap.search(ename)) {
								ze.clear();
								return;
							}
						}
					} else {
						splitQuery(pmap, ex);
					}
				}
			}
		}
	}

	private boolean clearCondition(final JoinMap map,
			final ZConstant col) throws Exception {
		boolean clear = true;

		String ename = getEntityName(col.getValue());
		if (map.search(ename)) {
			clear = false;
		}
		return clear;
	}

	private String getEntityName(final String column) {
		String[] parts = column.split("\\.");
		return parts[0];
	}
}
