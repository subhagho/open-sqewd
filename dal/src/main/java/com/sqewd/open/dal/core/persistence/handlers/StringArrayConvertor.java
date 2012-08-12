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
package com.sqewd.open.dal.core.persistence.handlers;
import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.CustomFieldDataHandler;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.utils.DateUtils;

/**
 * @author subhagho
 * 
 */
public class StringArrayConvertor implements CustomFieldDataHandler {
	public static final String _SPLIT_REGEX_ = ";";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.CustomFieldDataHandler#load(com.wookler.
	 * core.persistence.AbstractEntity, java.lang.String, java.lang.Object)
	 */
	public void load(AbstractEntity entity, String field, Object data)
			throws Exception {
		StructAttributeReflect attr = ReflectionUtils.get().getAttribute(
				entity.getClass(), field);
		String sdata = (String) data;
		String[] spdata = sdata.split(_SPLIT_REGEX_);
		if (attr.Field.getType().isArray()) {
			Object arrdata = getArrayData(spdata, attr.Field);
			PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
					arrdata);
		} else
			throw new Exception("Object field [" + attr.Field.getName()
					+ "] is not an array.");
	}

	private Object getArrayData(String[] data, Field field) throws Exception {
		Class<?> type = field.getType().getComponentType();
		if (type.equals(String.class)) {
			return data;
		} else if (EnumPrimitives.isPrimitiveType(type)) {
			EnumPrimitives et = EnumPrimitives.type(type);
			if (type.isPrimitive()) {
				switch (et) {
				case ECharacter:
					char[] carr = new char[data.length];
					for (int ii = 0; ii < carr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							carr[ii] = data[ii].charAt(0);
					}
					return carr;
				case EShort:
					short[] sarr = new short[data.length];
					for (int ii = 0; ii < sarr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							sarr[ii] = Short.parseShort(data[ii]);
					}
					return sarr;
				case EInteger:
					int[] iarr = new int[data.length];
					for (int ii = 0; ii < iarr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							iarr[ii] = Integer.parseInt(data[ii]);
					}
					return iarr;
				case ELong:
					long[] larr = new long[data.length];
					for (int ii = 0; ii < larr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							larr[ii] = Long.parseLong(data[ii]);
					}
					return larr;
				case EFloat:
					float[] farr = new float[data.length];
					for (int ii = 0; ii < farr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							farr[ii] = Float.parseFloat(data[ii]);
					}
					return farr;
				case EDouble:
					double[] darr = new double[data.length];
					for (int ii = 0; ii < darr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							darr[ii] = Double.parseDouble(data[ii]);
					}
					return darr;
				}
			} else {
				switch (et) {
				case ECharacter:
					Character[] carr = new Character[data.length];
					for (int ii = 0; ii < carr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							carr[ii] = data[ii].charAt(0);
					}
					return carr;
				case EShort:
					Short[] sarr = new Short[data.length];
					for (int ii = 0; ii < sarr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							sarr[ii] = Short.parseShort(data[ii]);
					}
					return sarr;
				case EInteger:
					Integer[] iarr = new Integer[data.length];
					for (int ii = 0; ii < iarr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							iarr[ii] = Integer.parseInt(data[ii]);
					}
					return iarr;
				case ELong:
					Long[] larr = new Long[data.length];
					for (int ii = 0; ii < larr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							larr[ii] = Long.parseLong(data[ii]);
					}
					return larr;
				case EFloat:
					Float[] farr = new Float[data.length];
					for (int ii = 0; ii < farr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							farr[ii] = Float.parseFloat(data[ii]);
					}
					return farr;
				case EDouble:
					Double[] darr = new Double[data.length];
					for (int ii = 0; ii < darr.length; ii++) {
						if (data[ii] != null && !data[ii].isEmpty())
							darr[ii] = Double.parseDouble(data[ii]);
					}
					return darr;
				}
			}
		} else if (type.equals(Date.class)) {
			Date[] dtarr = new Date[data.length];
			for (int ii = 0; ii < dtarr.length; ii++) {
				if (data[ii] != null && !data[ii].isEmpty()) {
					dtarr[ii] = DateUtils.fromString(data[ii]);
				}
			}
		}
		throw new Exception("Unsupported Array element type ["
				+ type.getCanonicalName() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.CustomFieldDataHandler#save(com.wookler.
	 * core.persistence.AbstractEntity, java.lang.String, java.lang.Object)
	 */
	public Object save(AbstractEntity entity, String field) throws Exception {
		StructAttributeReflect attr = ReflectionUtils.get().getAttribute(
				entity.getClass(), field);
		if (attr.Field.getType().isArray()) {
			Object data = PropertyUtils.getSimpleProperty(entity,
					attr.Field.getName());
			if (data == null)
				return null;
			return convertToString(data, attr.Field);
		}
		throw new Exception("Object field [" + attr.Field.getName()
				+ "] is not an array.");
	}

	private String convertToString(Object data, Field field) throws Exception {
		Class<?> type = field.getType().getComponentType();
		if (type.equals(String.class)) {
			String[] sarr = (String[]) data;
			StringBuffer buff = null;
			for (String str : sarr) {
				if (buff == null)
					buff = new StringBuffer();
				else {
					buff.append(_SPLIT_REGEX_);
				}
				buff.append(str);
			}
			return buff.toString();
		} else if (type.equals(Date.class)) {
			Date[] darr = (Date[]) data;
			StringBuffer buff = null;
			for (Date dt : darr) {
				if (buff == null)
					buff = new StringBuffer();
				else {
					buff.append(_SPLIT_REGEX_);
				}
				buff.append(dt.getTime());
			}
			return buff.toString();
		} else if (EnumPrimitives.isPrimitiveType(type)) {
			EnumPrimitives et = EnumPrimitives.type(type);
			StringBuffer buff = null;
			if (type.isPrimitive()) {
				switch (et) {
				case ECharacter:
					char[] carr = (char[]) data;
					for (int ii = 0; ii < carr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(carr[ii]);
					}
					return buff.toString();
				case EShort:
					short[] sarr = (short[]) data;
					for (int ii = 0; ii < sarr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(sarr[ii]);
					}
					return buff.toString();
				case EInteger:
					int[] iarr = (int[]) data;
					for (int ii = 0; ii < iarr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(iarr[ii]);
					}
					return buff.toString();
				case ELong:
					long[] larr = (long[]) data;
					for (int ii = 0; ii < larr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(larr[ii]);
					}
					return buff.toString();
				case EFloat:
					float[] farr = (float[]) data;
					for (int ii = 0; ii < farr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(farr[ii]);
					}
					return buff.toString();
				case EDouble:
					double[] darr = (double[]) data;
					for (int ii = 0; ii < darr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(darr[ii]);
					}
					return buff.toString();
				}
			} else {
				switch (et) {
				case ECharacter:
					Character[] carr = (Character[]) data;
					for (int ii = 0; ii < carr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(carr[ii]);
					}
					return buff.toString();
				case EShort:
					Short[] sarr = (Short[]) data;
					for (int ii = 0; ii < sarr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(sarr[ii]);
					}
					return buff.toString();
				case EInteger:
					Integer[] iarr = (Integer[]) data;
					for (int ii = 0; ii < iarr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(iarr[ii]);
					}
					return buff.toString();
				case ELong:
					Long[] larr = (Long[]) data;
					for (int ii = 0; ii < larr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(larr[ii]);
					}
					return buff.toString();
				case EFloat:
					Float[] farr = (Float[]) data;
					for (int ii = 0; ii < farr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(farr[ii]);
					}
					return buff.toString();
				case EDouble:
					Double[] darr = (Double[]) data;
					for (int ii = 0; ii < darr.length; ii++) {
						if (buff == null)
							buff = new StringBuffer();
						else
							buff.append(_SPLIT_REGEX_);
						buff.append(darr[ii]);
					}
					return buff.toString();
				}
			}
		}
		throw new Exception("Unsupported Array element type ["
				+ type.getCanonicalName() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.core.persistence.CustomFieldDataHandler#getDataType()
	 */
	public Class<?> getDataType() {
		return String.class;
	}

}
