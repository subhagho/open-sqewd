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
 * @filename CSVPlanNode.java
 * @created Oct 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import com.sqewd.open.dal.api.persistence.query.PlanContext;
import com.sqewd.open.dal.api.persistence.query.PlanNode;
import com.sqewd.open.dal.api.reflect.SchemaObject;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class CSVPlanNode extends PlanNode {

	/**
	 * Defualt Constructor.
	 * 
	 * @param parent
	 * @param ctx
	 * @throws Exception
	 */
	public CSVPlanNode(final PlanNode parent, final PlanContext ctx,
			final SchemaObject object) throws Exception {
		super(parent, ctx);
		this.object = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.query.PlanNode#addReference(com.sqewd
	 * .open.dal.api.reflect.SchemaObject)
	 */
	@Override
	public PlanNode addReference(final SchemaObject object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.query.PlanNode#getReference(java.lang
	 * .String)
	 */
	@Override
	public PlanNode getReference(final String key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.query.PlanNode#getReferenceObject(
	 * java.lang.String)
	 */
	@Override
	public SchemaObject getReferenceObject(final String key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
