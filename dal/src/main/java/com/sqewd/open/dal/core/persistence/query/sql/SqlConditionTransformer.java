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
 * @filename SqlConditionTransformer.java
 * @created Sep 30, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.sql;

import com.sqewd.open.dal.core.persistence.query.conditions.AndCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionAttribute;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionElement;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionTransformer;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionValue;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumConditionOperator;
import com.sqewd.open.dal.core.persistence.query.conditions.OperatorCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.OrCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition;

/**
 * Transforms the condition tree into a SQL where clause.
 * 
 * @author subhagho
 * 
 */
public class SqlConditionTransformer implements ConditionTransformer {
	private StringBuffer buffer = new StringBuffer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sqewd.open.dal.core.persistence.query.conditions.ConditionTransformer
	 * #
	 * transform(com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition
	 * )
	 */
	public void transform(final QueryCondition condition) throws Exception {
		buffer.append(visit(condition));
	}

	private String visit(final QueryCondition condition) throws Exception {
		if (condition instanceof AndCondition)
			return visit((AndCondition) condition);
		else if (condition instanceof OrCondition)
			return visit((OrCondition) condition);
		else if (condition instanceof OperatorCondition)
			return visit((OperatorCondition) condition);
		throw new Exception("Condition type ["
				+ condition.getClass().getCanonicalName()
				+ "] not implemented.");
	}

	private String visit(final AndCondition condition) throws Exception {
		StringBuffer bf = new StringBuffer();

		bf.append('(').append(visit(condition.getLeft())).append(')');
		bf.append(" AND ");
		bf.append('(').append(visit(condition.getRight())).append(')');

		return bf.toString();
	}

	private String visit(final OrCondition condition) throws Exception {
		StringBuffer bf = new StringBuffer();

		bf.append('(').append(visit(condition.getLeft())).append(')');
		bf.append(" OR ");
		bf.append('(').append(visit(condition.getRight())).append(')');

		return bf.toString();
	}

	private String visit(final OperatorCondition condition) throws Exception {
		StringBuffer bf = new StringBuffer();

		bf.append(visit(condition.getLeft()));
		bf.append(' ')
				.append(SQLUtils.convertOperator(condition.getOperator()))
				.append(')');
		bf.append(visit(condition.getRight()));

		return bf.toString();
	}

	private String visit(final ConditionElement element) throws Exception {
		if (element instanceof ConditionAttribute)
			return visit((ConditionAttribute) element);
		else if (element instanceof ConditionValue)
			return visit((ConditionValue) element);
		else if (element instanceof QueryCondition) {
			// Check if element is an arithmetic operation.
			if (element instanceof OperatorCondition) {
				OperatorCondition co = (OperatorCondition) element;
				if (co.getOperator() == EnumConditionOperator.Add
						|| co.getOperator() == EnumConditionOperator.Subtract
						|| co.getOperator() == EnumConditionOperator.Multiply
						|| co.getOperator() == EnumConditionOperator.Divide)
					return "(" + visit(co) + ")";
			}
		}
		throw new Exception("Condition Element ["
				+ element.getClass().getCanonicalName() + "] not supported.");
	}

	private String visit(final ConditionAttribute attr) throws Exception {
		if (attr.getAttribute() instanceof SqlColumn)
			return attr.getAttribute().toString();
		else
			throw new Exception("Unsupported SQL Attribute type ["
					+ attr.getAttribute().getType().getClass()
							.getCanonicalName() + "]");
	}

	private String visit(final ConditionValue value) throws Exception {
		if (value.getType() == null)
			return value.getValue().toString();
		else
			return value.getType().toString(value.getValue());
	}

	/**
	 * Get the condition tree as a where clause.
	 * 
	 * @return
	 */
	public String getConditionClause() {
		return buffer.toString();
	}
}
