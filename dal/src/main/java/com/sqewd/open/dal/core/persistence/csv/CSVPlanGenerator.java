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
 * @filename CSVPlanGenerator.java
 * @created Oct 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.csv;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.query.PlanContext;
import com.sqewd.open.dal.api.persistence.query.PlanNode;
import com.sqewd.open.dal.core.persistence.query.BasePlanGenerator;

/**
 * Execution plan generator for CSV data source.
 * 
 * @author subhagho
 * 
 */
public class CSVPlanGenerator extends BasePlanGenerator {
	private AbstractPersister persister = null;

	/**
	 * Default Constructor.
	 * 
	 * @throws Exception
	 */
	public CSVPlanGenerator(final AbstractPersister persister) throws Exception {
		super();
		this.persister = persister;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.api.persistence.query.PlanGenerator#addContext(com
	 * .sqewd.open.dal.api.persistence.AbstractEntity,
	 * com.sqewd.open.dal.api.persistence.query.PlanNode)
	 */
	public void addContext(final AbstractEntity entity, final PlanNode parent)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.BasePlanGenerator#process(com
	 * .sqewd.open.dal.api.persistence.query.PlanContext,
	 * com.sqewd.open.dal.api.persistence.AbstractEntity)
	 */
	@Override
	protected PlanNode process(final PlanContext ctx,
			final AbstractEntity entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
