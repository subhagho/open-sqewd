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
 * @filename EntityHelper.java
 * @created Sep 15, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Attr;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.EnumRefereceType;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.reflect.AttributeDef;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.utils.KeyValuePair;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class EntityHelper {
	public static <T extends AbstractEntity> void addListValue(
			final AbstractEntity entity, final AbstractEntity parent,
			final AttributeDef attr) throws Exception {
		Object vo = createListInstance(parent, attr);
		MethodUtils.invokeMethod(vo, "add", new Object[] { entity });
	}

	public static <T extends AbstractEntity> void copyToList(final T source,
			final T dest, final AttributeDef attr) throws Exception {
		Object so = PropertyUtils.getSimpleProperty(source, attr.getField()
				.getName());
		Object to = PropertyUtils.getSimpleProperty(dest, attr.getField()
				.getName());
		if (so == null)
			return;
		if (to == null)
			throw new Exception(
					"Source List has not been intiialized for Field ["
							+ attr.Column + "]");
		if (!(so instanceof List<?>))
			throw new Exception("Source element [" + attr.Column
					+ "] is not a List");
		if (!(to instanceof List<?>))
			throw new Exception("Target element [" + attr.Column
					+ "] is not a List");
		MethodUtils.invokeMethod(to, "addAll", new Object[] { so });
	}

	public static <T extends AbstractEntity> Object createListInstance(
			final AbstractEntity entity, final AttributeDef attr)
			throws Exception {
		Object vo = PropertyUtils.getSimpleProperty(entity, attr.getField()
				.getName());
		if (vo == null) {
			vo = new ArrayList<T>();
			PropertyUtils.setSimpleProperty(entity, attr.getField().getName(),
					vo);
		}
		return vo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getColumnValue(final ResultSet rs,
			final StructAttributeReflect attr, final AbstractEntity entity,
			final AbstractJoinGraph gr, final Stack<KeyValuePair<Class<?>>> path)
			throws Exception {

		Object value = null;

		KeyValuePair<String> alias = gr.getAliasFor(path, attr.Column, 0);
		String tabprefix = alias.getKey();

		if (EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
			EnumPrimitives prim = EnumPrimitives.type(attr.Field.getType());
			switch (prim) {
			case ECharacter:
				String sv = rs.getString(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), sv.charAt(0));
				}
				break;
			case EShort:
				short shv = rs.getShort(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), shv);
				}
				break;
			case EInteger:
				int iv = rs.getInt(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), iv);
				}
				break;
			case ELong:
				long lv = rs.getLong(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), lv);
				}
				break;
			case EFloat:
				float fv = rs.getFloat(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), fv);
				}
				break;
			case EDouble:
				double dv = rs.getDouble(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), dv);
				}
				break;
			default:
				throw new Exception("Unsupported Data type [" + prim.name()
						+ "]");
			}
		} else if (attr.Convertor != null) {
			// TODO : Not supported at this time.
			value = rs.getString(tabprefix + "." + attr.Column);

		} else if (attr.Field.getType().equals(String.class)) {
			value = rs.getString(tabprefix + "." + attr.Column);
			if (rs.wasNull()) {
				value = null;
			}
		} else if (attr.Field.getType().equals(Date.class)) {
			long lvalue = rs.getLong(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				Date dt = new Date(lvalue);
				value = dt;
			}
		} else if (attr.Field.getType().isEnum()) {
			String svalue = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				Class ecls = attr.Field.getType();
				value = Enum.valueOf(ecls, svalue);
			}
		} else if (attr.Reference != null) {
			Class<?> rt = Class.forName(attr.Reference.Class);
			Object obj = rt.newInstance();
			if (!(obj instanceof AbstractEntity))
				throw new Exception("Unsupported Entity type ["
						+ rt.getCanonicalName() + "]");
			AbstractEntity rentity = (AbstractEntity) obj;
			if (path.size() > 0) {
				path.peek().setKey(attr.Column);
			}

			KeyValuePair<Class<?>> cls = new KeyValuePair<Class<?>>();
			cls.setValue(rentity.getClass());
			path.push(cls);
			setEntity(rentity, rs, gr, path);
			value = rentity;
			path.pop();
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setColumnValue(final ResultSet rs,
			final StructAttributeReflect attr, final AbstractEntity entity,
			final AbstractJoinGraph gr, final Stack<KeyValuePair<Class<?>>> path)
			throws Exception {

		KeyValuePair<String> alias = gr.getAliasFor(path, attr.Column, 0);
		String tabprefix = alias.getKey();

		if (EnumPrimitives.isPrimitiveType(attr.Field.getType())) {
			EnumPrimitives prim = EnumPrimitives.type(attr.Field.getType());
			switch (prim) {
			case ECharacter:
				String sv = rs.getString(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), sv.charAt(0));
				}
				break;
			case EShort:
				short shv = rs.getShort(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), shv);
				}
				break;
			case EInteger:
				int iv = rs.getInt(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), iv);
				}
				break;
			case ELong:
				long lv = rs.getLong(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), lv);
				}
				break;
			case EFloat:
				float fv = rs.getFloat(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), fv);
				}
				break;
			case EDouble:
				double dv = rs.getDouble(tabprefix + "." + attr.Column);
				if (!rs.wasNull()) {
					PropertyUtils.setSimpleProperty(entity,
							attr.Field.getName(), dv);
				}
				break;
			default:
				throw new Exception("Unsupported Data type [" + prim.name()
						+ "]");
			}
		} else if (attr.Convertor != null) {
			String value = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				attr.Convertor.load(entity, attr.Column, value);
			}
		} else if (attr.Field.getType().equals(String.class)) {
			String value = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
						value);
			}
		} else if (attr.Field.getType().equals(Date.class)) {
			long value = rs.getLong(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				Date dt = new Date(value);
				PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
						dt);
			}
		} else if (attr.Field.getType().isEnum()) {
			String value = rs.getString(tabprefix + "." + attr.Column);
			if (!rs.wasNull()) {
				Class ecls = attr.Field.getType();
				Object evalue = Enum.valueOf(ecls, value);
				PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
						evalue);
			}
		} else if (attr.Reference != null) {
			Class<?> rt = Class.forName(attr.Reference.Class);
			Object obj = rt.newInstance();
			if (!(obj instanceof AbstractEntity))
				throw new Exception("Unsupported Entity type ["
						+ rt.getCanonicalName() + "]");
			AbstractEntity rentity = (AbstractEntity) obj;
			if (path.size() > 0) {
				path.peek().setKey(attr.Column);
			}

			KeyValuePair<Class<?>> cls = new KeyValuePair<Class<?>>();
			cls.setValue(rentity.getClass());
			path.push(cls);
			setEntity(rentity, rs, gr, path);
			PropertyUtils.setSimpleProperty(entity, attr.Field.getName(),
					rentity);
			path.pop();
		}
	}

	public static void setEntity(final AbstractEntity entity,
			final ResultSet rs, final AbstractJoinGraph gr,
			final Stack<KeyValuePair<Class<?>>> path) throws Exception {
		StructEntityReflect enref = ReflectionUtils.get().getEntityMetadata(
				entity.getClass());

		for (StructAttributeReflect attr : enref.Attributes) {
			setColumnValue(rs, attr, entity, gr, path);
		}
	}

	public static void setEntity(final EntityDef enref,
			final HashMap<String, AbstractEntity> entities, final ResultSet rs)
			throws Exception {
		Class<?> type = enref.getClasstype();
		Object obj = type.newInstance();
		if (!(obj instanceof AbstractEntity))
			throw new Exception("Unsupported Entity type ["
					+ type.getCanonicalName() + "]");
		AbstractEntity entity = (AbstractEntity) obj;
		AbstractJoinGraph gr = AbstractJoinGraph.lookup(type);

		for (AttributeDef attr : enref.getAttributes()) {
			Stack<KeyValuePair<Class<?>>> path = new Stack<KeyValuePair<Class<?>>>();
			Class<?> at = attr.Field.getType();
			KeyValuePair<Class<?>> ak = new KeyValuePair<Class<?>>();
			ak.setValue(entity.getClass());
			ak.setKey(attr.Column);
			path.push(ak);

			if (!attr.isRefrenceAttr()
					|| attr. != EnumRefereceType.One2Many) {
				setColumnValue(rs, attr, entity, gr, path);
			} else if (attr.Reference != null) {
				// Object ao = createListInstance(entity, attr);
				Class<?> rt = Class.forName(attr.Reference.Class);
				Object ro = rt.newInstance();
				if (!(ro instanceof AbstractEntity))
					throw new Exception("Reference [" + attr.Column
							+ "] is of invalid type. [" + at.getCanonicalName()
							+ "] does not extend from ["
							+ AbstractEntity.class.getCanonicalName() + "]");
				AbstractEntity ae = (AbstractEntity) getColumnValue(rs, attr,
						entity, gr, path);
				addListValue(ae, entity, attr);
			}

		}
		String key = entity.getEntityKey();
		if (!entities.containsKey(key)) {
			entities.put(entity.getEntityKey(), entity);
		} else {
			AbstractEntity target = entities.get(key);
			for (StructAttributeReflect attr : enref.Attributes) {
				if (attr.Reference.Type == EnumRefereceType.One2Many) {
					copyToList(entity, target, attr);
				}
			}
		}
	}
}
