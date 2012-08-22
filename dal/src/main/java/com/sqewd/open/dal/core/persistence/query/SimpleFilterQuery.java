/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.ArrayList;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractEntity;

/**
 * Simple Object Query. All specified conditions are used to filter the input
 * result-set.
 * 
 * @author subhagho
 * 
 */
public class SimpleFilterQuery extends Query {
	protected List<FilterCondition> conditions = null;

	protected ConditionMatcher matcher = new ConditionMatcher();

	protected FilterConditionParser parser = null;

	/**
	 * Get the parsed filter conditions.
	 * 
	 * @return
	 */
	public List<FilterCondition> filters() {
		return conditions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.query.Query#parse(java.lang.String)
	 */
	@Override
	public void parse(List<Class<?>> tables, String query) throws Exception {
		if (query == null || query.isEmpty())
			return;

		parser = new FilterConditionParser();
		conditions = parser.parse(tables, query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.query.Query#parse(java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public void parse(Class<?>[] tables, String query) throws Exception {
		List<Class<?>> tabs = new ArrayList<Class<?>>();
		for (Class<?> table : tables) {
			tabs.add(table);
		}
		parse(tabs, query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.query.Query#doSelect(com.wookler.core.
	 * persistence.AbstractEntity)
	 */
	@Override
	public boolean doSelect(AbstractEntity entity) throws Exception {
		if (conditions != null && conditions.size() > 0) {
			for (FilterCondition condition : conditions) {

				AbstractConditionPredicate acp = condition.getLeft();
				if (!(acp instanceof ColumnConditionPredicate))
					throw new Exception(
							"Invalid Filter Condition : Expecting type ["
									+ ColumnConditionPredicate.class
											.getCanonicalName() + "]");
				ColumnConditionPredicate ccp = (ColumnConditionPredicate) acp;

				acp = condition.getRight();
				if (!(acp instanceof ValueConditionPredicate))
					throw new Exception(
							"Invalid Filter Condition : Expecting type ["
									+ ValueConditionPredicate.class
											.getCanonicalName() + "]");
				ValueConditionPredicate vcp = (ValueConditionPredicate) acp;

				if (!matcher.match(entity, ccp.getColumn(),
						condition.getComparator(), vcp.getValue())) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.query.Query#select(java.util.List)
	 */
	@Override
	public List<AbstractEntity> select(List<AbstractEntity> entities)
			throws Exception {
		List<AbstractEntity> results = doSelect(entities);
		if (parser.getSort() != null) {
			EntityListSorter sorter = new EntityListSorter(parser.getSort());
			sorter.sort(results);
		}
		if (parser.getLimit() > 0 && results.size() > parser.getLimit()) {
			results = results.subList(0, parser.getLimit());
		}
		return results;
	}

	public List<AbstractEntity> doSelect(List<AbstractEntity> entities)
			throws Exception {
		if (conditions != null && conditions.size() > 0) {
			List<AbstractEntity> filtered = new ArrayList<AbstractEntity>();
			for (AbstractEntity entity : entities) {
				boolean select = true;
				for (FilterCondition condition : conditions) {
					AbstractConditionPredicate acp = condition.getLeft();
					if (!(acp instanceof ColumnConditionPredicate))
						throw new Exception(
								"Invalid Filter Condition : Expecting type ["
										+ ColumnConditionPredicate.class
												.getCanonicalName() + "]");
					ColumnConditionPredicate ccp = (ColumnConditionPredicate) acp;

					acp = condition.getRight();
					if (!(acp instanceof ValueConditionPredicate))
						throw new Exception(
								"Invalid Filter Condition : Expecting type ["
										+ ValueConditionPredicate.class
												.getCanonicalName() + "]");
					ValueConditionPredicate vcp = (ValueConditionPredicate) acp;

					if (!matcher.match(entity, ccp.getColumn(),
							condition.getComparator(), vcp.getValue())) {
						select = false;
						break;
					}
				}
				if (select)
					filtered.add(entity);
			}
			return filtered;
		}
		return entities;
	}

}
