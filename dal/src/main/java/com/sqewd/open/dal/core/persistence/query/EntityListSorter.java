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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.utils.LogUtils;

/**
 * @author subhagho
 * 
 */
public class EntityListSorter implements Comparator<AbstractEntity> {
	private static final Logger log = LoggerFactory
			.getLogger(EntityListSorter.class);

	private List<SortColumn> columns = null;

	public EntityListSorter(List<SortColumn> columns) {
		this.columns = columns;
	}

	public <T extends AbstractEntity> void sort(List<T> entities)
			throws Exception {
		Collections.sort(entities, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(AbstractEntity o1, AbstractEntity o2) {
		int retval = 0;
		for (SortColumn column : columns) {
			try {
				retval = compare(o1, o2, column);
				if (retval != 0)
					return retval;
			} catch (Exception e) {
				log.debug("[" + column.getColumn() + "] : "
						+ e.getLocalizedMessage());
				LogUtils.stacktrace(log, e);
				return 0;
			}
		}
		return retval;
	}

	private int compare(AbstractEntity esrc, AbstractEntity etgt,
			SortColumn column) throws Exception {
		StructAttributeReflect attr = ReflectionUtils.get().getAttribute(
				esrc.getClass(), column.getColumn());

		Object vsrc = PropertyUtils.getProperty(esrc, attr.Field.getName());
		Object vtgt = PropertyUtils.getProperty(etgt, attr.Field.getName());

		int retval = 0;
		Class<?> ftype = attr.Field.getType();
		if (EnumPrimitives.isPrimitiveType(ftype)) {
			EnumPrimitives type = EnumPrimitives.type(ftype);
			switch (type) {
			case ECharacter:
				retval = (int) ((Character) vsrc - (Character) vtgt);
				break;
			case EShort:
				retval = (int) ((Short) vsrc - (Short) vtgt);
				break;
			case EInteger:
				retval = (int) ((Integer) vsrc - (Integer) vtgt);
				break;
			case ELong:
				retval = (int) ((Long) vsrc - (Long) vtgt);
				break;
			case EFloat:
				float fval = ((Float) vsrc - (Float) vtgt);
				if (fval < 0)
					retval = -1;
				else if (fval > 0)
					retval = 1;
				break;
			case EDouble:
				double dval = ((Double) vsrc - (Double) vtgt);
				if (dval < 0)
					retval = -1;
				else if (dval > 0)
					retval = 1;
				break;
			}
		} else {
			if (ftype.equals(String.class)) {
				retval = ((String) vsrc).compareTo((String) vtgt);
			} else if (ftype.equals(Date.class)) {
				retval = ((Date) vsrc).compareTo((Date) vtgt);
			}
		}
		if (column.getOrder() == EnumSortOrder.DSC) {
			retval = retval * -1;
		}
		return retval;
	}
}
