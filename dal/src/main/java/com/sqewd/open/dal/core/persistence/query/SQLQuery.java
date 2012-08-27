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
 * @filename SQLQuery.java
 * @created Aug 21, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.gibello.zql.ZConstant;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZQuery;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZqlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.Entity;
import com.sqewd.open.dal.api.persistence.EnumJoinType;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.core.persistence.db.AbstractJoinGraph;
import com.sqewd.open.dal.core.persistence.query.parser.DalSqlParser;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class SQLQuery {
	private static final Logger log = LoggerFactory.getLogger(SQLQuery.class);

	private static HashMap<String, ZQuery> querycache = new HashMap<String, ZQuery>();

	private ZQuery getCachedQuery(Class<?> type) throws Exception {
		if (!type.isAnnotationPresent(Entity.class))
			throw new Exception("Class [" + type.getCanonicalName()
					+ "] has not been annotated as an Entity.");
		String selectq = null;

		try {
			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(type);
			if (!querycache.containsKey(enref.Entity)) {
				if (enref.IsView) {
					if (!enref.IsJoin)
						selectq = enref.Query;
					else {
						if (enref.Join.Type == EnumJoinType.Native) {
							selectq = getBasicSelect(graph);
						} else
							throw new Exception(
									"This method should not be called for Joined Entities.");
					}
				} else {
					selectq = getBasicSelect(graph);
				}

				selectq = selectq + ";";

				// convert String into InputStream
				InputStream is = new ByteArrayInputStream(selectq.getBytes());
				ZqlParser parser = new ZqlParser(is);

				ZQuery zq = null;
				ZStatement zst = parser.readStatement();
				if (zst instanceof ZQuery) {
					zq = (ZQuery) zst;
					querycache.put(enref.Entity, zq);
				} else {
					throw new Exception(
							"Invalid parsed SQL : Statement of type ["
									+ zst.getClass().getCanonicalName() + "]");
				}
			}

			return querycache.get(enref.Entity);
		} catch (Exception e) {
			log.debug("[QUERY : " + selectq + "]");
			throw e;
		}
	}

	private static String getBasicSelect(AbstractJoinGraph graph) throws Exception {
		StringBuffer where = new StringBuffer();

		Collection<String> columns = graph.getColumns();

		HashMap<String, String> tables = graph.getTableAliases();

		boolean first = true;
		StringBuffer cbuff = new StringBuffer();
		for (String column : columns) {
			if (first)
				first = false;
			else
				cbuff.append(',');
			cbuff.append(' ').append(column).append(" as \"").append(column)
					.append("\"");
		}
		String columnstr = cbuff.toString();

		// Get Where Clause
		first = true;
		if (graph.hasJoins()) {
			where.append(graph.getJoinCondition());
			first = false;
		}

		// Create query
		StringBuffer qbuff = new StringBuffer("select ");
		qbuff.append(columnstr);

		first = true;
		for (String tab : tables.keySet()) {
			String table = tables.get(tab);
			if (first) {
				first = false;
				qbuff.append(" from ");
			} else
				qbuff.append(", ");
			qbuff.append(table).append(' ').append(tab);
		}
		if (where.length() > 0) {
			qbuff.append(" where ").append(where);
		}
		return qbuff.toString();
	}

	private Class<?> type;

	private String query;

	private AbstractJoinGraph graph = null;

	public SQLQuery(Class<?> type) throws Exception {
		this.type = type;
		graph = AbstractJoinGraph.lookup(type);
	}

	public String parse(String query, int limit) throws Exception {
		this.query = query;

		ZQuery zq = getCachedQuery(type);
		ZQuery aq = null;
		if (query != null && !query.isEmpty()) {
			aq = parseQuery(zq);
		}

		return zq.getSql(aq, limit);
	}

	private ZQuery parseQuery(ZQuery qr) throws Exception {
		if (query == null || query.isEmpty())
			return null;

		InputStream is = new ByteArrayInputStream(query.getBytes());
		DalSqlParser parser = new DalSqlParser(is);

		ZQuery zq = parser.QueryClause();

		resolveColumn(zq.getWhere(), qr);

		return zq;
	}

	private void resolveColumn(ZExp exp, ZQuery qr) throws Exception {
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			Vector<ZExp> ve = ze.getOperands();
			if (ve != null && ve.size() > 0) {
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
						}
					} else {
						resolveColumn(ex, qr);
					}
				}
			}
		}
	}

}
