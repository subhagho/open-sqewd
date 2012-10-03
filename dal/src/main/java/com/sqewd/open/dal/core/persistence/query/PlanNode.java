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
 * @filename PlanNode.java
 * @created Sep 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqewd.open.dal.api.reflect.SchemaObject;
import com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition;

/**
 * Plan node represents individual persistence elements that is used to generate
 * the Execution plan.
 * 
 * @author subhagho
 * 
 */
public abstract class PlanNode {
	protected SchemaObject object;

	protected PlanNode parent = null;

	protected List<PlanNode> references = null;

	protected HashMap<String, Integer> nameindx = null;

	protected PlanContext ctx = null;

	protected EnumJoinType jointype = EnumJoinType.None;

	protected QueryCondition join = null;

	protected PlanNode(final SchemaObject object, final PlanNode parent,
			final PlanContext ctx) throws Exception {
		this.object = object;
		this.parent = parent;
		this.ctx = ctx;
		this.ctx.addNode(object.getKey(), this);
	}

	/**
	 * @return the object
	 */
	public SchemaObject getObject() {
		return object;
	}

	/**
	 * @return the parent
	 */
	public PlanNode getParent() {
		return parent;
	}

	/**
	 * Add a reference node to this instance.
	 * 
	 * @param node
	 * @throws Exception
	 */
	public void addExternalReference(final PlanNode node) throws Exception {
		String key = node.object.getKey();
		if (nameindx == null) {
			nameindx = new HashMap<String, Integer>();
		} else {
			if (nameindx.containsKey(key))
				throw new Exception("PlanNode reference with name [" + key
						+ "] already added.");
		}
		if (references == null) {
			references = new ArrayList<PlanNode>();
		}
		node.parent = this;
		references.add(node);
		nameindx.put(key, references.size() - 1);

		node.jointype = EnumJoinType.External;
	}

	/**
	 * Get all the reference nodes.
	 * 
	 * @return
	 */
	public List<PlanNode> getChildren() {
		return references;
	}

	/**
	 * Validate the Plan Tree. Should be invoke post setup.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		if (parent == null) {
			HashMap<String, String> aliases = new HashMap<String, String>();
			for (String key : nameindx.keySet()) {
				aliases.put(key, key);
			}
			validate(aliases);
		} else {
			parent.validate();
		}
	}

	/**
	 * Validate that there aren't any duplicate aliases.
	 * 
	 * @param aliases
	 * @throws Exception
	 */
	protected void validate(final HashMap<String, String> aliases)
			throws Exception {
		if (nameindx != null && nameindx.size() > 0) {
			for (String key : nameindx.keySet()) {
				if (aliases.containsKey(key))
					throw new Exception("Duplicate Object name found. [" + key
							+ "]");
				else {
					aliases.put(key, key);
				}
			}
			for (PlanNode ref : references) {
				ref.validate(aliases);
			}
		}
	}

	/**
	 * Get a child node for the specified name.
	 * 
	 * @param key
	 *            - Reference Object name.
	 * @return
	 * @throws Exception
	 */
	public abstract PlanNode getReference(String key) throws Exception;

	/**
	 * Create a new Plan Node for the specified Schema object.
	 * 
	 * @param object
	 *            - Schema Object to create plan node for.
	 * @return
	 * @throws Exception
	 */
	public abstract PlanNode addReference(SchemaObject object) throws Exception;

	/**
	 * Get the handle to a schema object based on the reference key specified.
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public abstract SchemaObject getReferenceObject(String key)
			throws Exception;

	/**
	 * @return the join
	 */
	public QueryCondition getJoin() {
		return join;
	}

	/**
	 * @param join
	 *            the join to set
	 */
	public void setJoin(final QueryCondition join) {
		this.join = join;
	}
}
