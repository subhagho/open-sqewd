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
 * @filename PersisterQueryMap.java
 * @created Sep 6, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.gibello.zql.ZQuery;

import com.sqewd.open.dal.api.utils.KeyValuePair;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class JoinMap {
	private String persisterKey;

	private HashMap<String, InternalJoinGraph> graphs = null;

	private ZQuery query;

	private boolean isKeyQuery = false;

	private boolean processed = false;

	/**
	 * @return the persisterKey
	 */
	public String getPersisterKey() {
		return persisterKey;
	}

	/**
	 * @param persisterKey
	 *            the persisterKey to set
	 */
	public void setPersisterKey(final String persisterKey) {
		this.persisterKey = persisterKey;
	}

	public List<KeyValuePair<Class<?>>> getTypes() {
		List<KeyValuePair<Class<?>>> types = new ArrayList<KeyValuePair<Class<?>>>();
		for (String key : graphs.keySet()) {
			InternalJoinGraph ig = graphs.get(key);
			KeyValuePair<Class<?>> kv = new KeyValuePair<Class<?>>();
			kv.setKey(key);
			kv.setValue(ig.getType());
			types.add(kv);
		}
		return types;
	}

	public void addGraph(final String alias, final InternalJoinGraph graph) {
		if (graphs == null) {
			graphs = new HashMap<String, InternalJoinGraph>();
		}
		graphs.put(alias, graph);
	}

	public InternalJoinGraph getGraph(final String entity) {
		if (graphs != null && graphs.containsKey(entity))
			return graphs.get(entity);
		return null;
	}

	public boolean containsKey(final String key) {
		if (graphs != null)
			return graphs.containsKey(key);
		return false;
	}

	public boolean search(final String key) {
		if (graphs != null) {
			if (graphs.containsKey(key))
				return true;
			for (String gk : graphs.keySet()) {
				InternalJoinGraph gr = graphs.get(gk);
				if (gr.searchAlias(key))
					return true;
			}
		}
		return false;
	}

	public Set<String> keySet() {
		if (graphs != null && graphs.size() > 0)
			return graphs.keySet();
		return null;
	}

	/**
	 * @return the query
	 */
	public ZQuery getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(final ZQuery query) {
		this.query = query;
	}

	/**
	 * @return the isKeyQuery
	 */
	public boolean isKeyQuery() {
		return isKeyQuery;
	}

	/**
	 * @param isKeyQuery
	 *            the isKeyQuery to set
	 */
	public void setKeyQuery(final boolean isKeyQuery) {
		this.isKeyQuery = isKeyQuery;
	}

	/**
	 * @return the processed
	 */
	public boolean isProcessed() {
		return processed;
	}

	/**
	 * @param processed
	 *            the processed to set
	 */
	public void setProcessed(final boolean processed) {
		this.processed = processed;
	}
}
