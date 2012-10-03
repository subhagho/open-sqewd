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
 * @filename PlanContext.java
 * @created Sep 29, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.HashMap;

/**
 * A Context Object that is shared by elements of a Execution Plan. All
 * shared/cache objects should be added to the context.
 * 
 * @author subhagho
 * 
 */
public class PlanContext {
	public static final String _ALIAS_SUFFIX_ = "_SQWD_";

	private HashMap<String, PlanNode> nodeindx = new HashMap<String, PlanNode>();

	private PlanNode root = null;

	/**
	 * Add the current Plan node to the Node index.
	 * 
	 * @param key
	 *            - Plan Node key (name or alias)
	 * @param node
	 *            - Plan node handle.
	 * @throws Exception
	 */
	public void addNode(final String key, final PlanNode node) throws Exception {
		if (nodeindx.containsKey(key))
			throw new Exception("Duplicate key, Plan node with key [" + key
					+ "] already exists.");
		nodeindx.put(key, node);
		if (node.parent == null) {
			root = node;
		}
	}

	/**
	 * Get the plan node referenced by the give key.
	 * 
	 * @param key
	 *            - Plan Node key (name or alias)
	 * @return
	 * @throws Exception
	 */
	public PlanNode getNode(final String key) throws Exception {
		if (nodeindx.containsKey(key))
			return nodeindx.get(key);
		throw new Exception("No Plan node found with key [" + key + "]");
	}

	/**
	 * Check if the key is already used.
	 * 
	 * @param key
	 *            - Plan Node key (name or alias)
	 * @return
	 */
	public boolean isNodeKeyUsed(final String key) {
		return nodeindx.containsKey(key);
	}

	/**
	 * Get the Root node of this Execution plan.
	 * 
	 * @return
	 */
	public PlanNode getRoot() {
		return root;
	}
}
