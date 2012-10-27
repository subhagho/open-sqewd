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
 * @filename SqlPlanNode.java
 * @created Sep 29, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import com.sqewd.open.dal.api.persistence.query.EnumJoinType;
import com.sqewd.open.dal.api.persistence.query.PlanContext;
import com.sqewd.open.dal.api.persistence.query.PlanNode;
import com.sqewd.open.dal.api.reflect.SchemaObject;

/**
 * Class represents a SQL Plan Node for the Execution plan.
 * 
 * @author subhagho
 * 
 */
public class SqlPlanNode extends PlanNode {

	/**
	 * @param object
	 * @param parent
	 * @param ctx
	 * @throws Exception
	 */
	public SqlPlanNode(final SchemaObject object, final PlanNode parent,
			final PlanContext ctx) throws Exception {
		super(parent, ctx);
		if (!(object instanceof SqlTable))
			throw new Exception(
					"Invalid Schema Object : Expected Schema Object of type ["
							+ SqlTable.class.getCanonicalName() + "]");
		SqlTable table = (SqlTable) object;

		String key = table.getKey();
		if (ctx.isNodeKeyUsed(key)) {
			key = ctx.createNewAlias(key);
			table.setAlias(key);
		}
		setObject(table);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.PlanNode#addChild(com.sqewd
	 * .open.dal.core.persistence.query.SchemaObject)
	 */
	@Override
	public PlanNode addReference(final SchemaObject object) throws Exception {
		if (object instanceof SqlTable) {
			String key = object.getKey();
			int count = 0;
			while (true) {
				if (ctx.isNodeKeyUsed(key)) {
					key = key + PlanContext._ALIAS_SUFFIX_ + count;
				} else {
					((SqlTable) object).setAlias(key);
					break;
				}
				count++;
			}
			// Create a new SQL Plan node.
			SqlPlanNode node = new SqlPlanNode(object, this, ctx);
			node.validate();
			if (!this.object.getPersister().equals(object.getPersister())) {
				node.jointype = EnumJoinType.External;
			} else {
				node.jointype = EnumJoinType.Internal;
			}
			return node;
		}
		// Children of different types should be added using the parent function
		// addExternalReference.
		throw new Exception("Cannot create PlanNode with type ["
				+ object.getClass().getCanonicalName() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.PlanNode#getChild(java.lang
	 * .String)
	 */
	@Override
	public PlanNode getReference(final String key) throws Exception {
		if (nameindx.containsKey(key))
			return references.get(nameindx.get(key));
		throw new Exception("No reference node found for key [" + key + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.PlanNode#getReferenceObject
	 * (java.lang.String)
	 */
	@Override
	public SchemaObject getReferenceObject(final String key) throws Exception {
		if (key.indexOf('.') > 0) {
			String[] parts = key.split("\\.");
			if (object.getKey().compareTo(parts[0]) == 0) {
				String offkey = key.substring(key.indexOf('.') + 1);
				for (String hk : nameindx.keySet()) {
					SchemaObject so = references.get(nameindx.get(hk))
							.getReferenceObject(offkey);
					if (so != null)
						return so;
				}
			}
		} else if (key.compareTo(object.getKey()) == 0)
			return object;

		return null;
	}

}
