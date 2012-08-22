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
 * @filename SQLUtils.java
 * @created Aug 21, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class SQLUtils {
	public static String quoteValue(String value, Class<?> type, String field)
			throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				type);
		if (enref != null) {
			StructAttributeReflect attr = enref.get(field);
			if (attr.Field.getType().equals(String.class)) {
				return "'" + value + "'";
			}
			if (attr.Field.getType().isEnum()) {
				return "'" + value + "'";
			}
		}
		return value;
	}
}
