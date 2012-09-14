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

import org.apache.commons.beanutils.PropertyUtils;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.core.persistence.db.ConditionMatchHelper;

/**
 * Utility Class to match Entity attributes against the filter condition.
 * 
 * @author subhagho
 * 
 */
public class ConditionMatcher {
	/**
	 * Match the filter value with the Entity attribute of the specified entity
	 * record.
	 * 
	 * @param entity
	 *            - Entity record to check.
	 * @param column
	 *            - Attribute Column name
	 * @param operator
	 *            - Condition Operator
	 * @param value
	 *            - Value to match.
	 * @return
	 * @throws Exception
	 */
	public boolean match(final AbstractEntity entity, final String column,
			final EnumOperator operator, final Object value) throws Exception {
		if (column.indexOf('.') < 0) {
			StructAttributeReflect attr = ReflectionUtils.get().getAttribute(
					entity.getClass(), column);
			Object src = PropertyUtils
					.getProperty(entity, attr.Field.getName());
			if (src != null)
				return ConditionMatchHelper.compare(src, value, operator,
						attr.Field.getType());
		} else {
			String[] vars = column.split("\\.");
			Object src = null;
			Class<?> type = null;
			StringBuffer coffset = new StringBuffer();
			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(entity.getClass());
			for (String var : vars) {
				if (src != null) {
					coffset.append('.');
				}
				coffset.append(var);
				if (var.compareTo(enref.Entity) == 0) {
					coffset.append(".");
					continue;
				}
				if (src == null) {
					StructAttributeReflect attr = ReflectionUtils.get()
							.getAttribute(entity.getClass(), var);
					src = PropertyUtils.getProperty(entity,
							attr.Field.getName());
					type = attr.Field.getType();
				} else {
					StructAttributeReflect attr = ReflectionUtils.get()
							.getAttribute(src.getClass(), var);
					src = PropertyUtils.getProperty(src, attr.Field.getName());
					type = attr.Field.getType();
				}
				if (src.getClass().isArray()) {
					if (isArrayEntityType(src)) {
						String cpart = column.replaceFirst(
								"^" + coffset.toString() + ".", "");
						return matchArray(src, cpart, operator, value);
					}
				} else if (src instanceof Iterable) {
					if (isListEntityType(src)) {
						String cpart = column.replaceFirst(
								"^" + coffset.toString() + ".", "");
						return matchList(src, cpart, operator, value);
					}
				}
			}
			if (src != null)
				return ConditionMatchHelper.compare(src, value, operator, type);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected <T extends AbstractEntity> boolean matchArray(final Object src,
			final String column, final EnumOperator operator, final Object value)
			throws Exception {
		T[] entities = (T[]) src;
		for (T entity : entities) {
			if (match(entity, column, operator, value))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected <T> boolean isListEntityType(final Object src) {
		Iterable<T> entities = (Iterable<T>) src;
		if (entities != null && entities.iterator().hasNext()) {
			T value = entities.iterator().next();
			if (value instanceof AbstractEntity)
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected <T> boolean isArrayEntityType(final Object src) {
		T[] entities = (T[]) src;
		if (entities != null && entities.length > 0) {
			T value = entities[0];
			if (value instanceof AbstractEntity)
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected <T extends AbstractEntity> boolean matchList(final Object src,
			final String column, final EnumOperator operator, final Object value)
			throws Exception {
		Iterable<T> entities = (Iterable<T>) src;

		for (T entity : entities) {
			if (match(entity, column, operator, value))
				return true;
		}
		return false;
	}

}
