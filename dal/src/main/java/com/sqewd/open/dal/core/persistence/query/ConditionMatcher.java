/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.StructAttributeReflect;
import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.DateUtils;

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
	public boolean match(AbstractEntity entity, String column,
			EnumOperator operator, Object value) throws Exception {
		if (column.indexOf('.') < 0) {
			StructAttributeReflect attr = ReflectionUtils.get().getAttribute(
					entity.getClass(), column);
			Object src = PropertyUtils
					.getProperty(entity, attr.Field.getName());
			if (src != null) {
				return compare(src, value, operator, attr.Field.getType());
			}
		} else {
			String[] vars = column.split("\\.");
			Object src = null;
			Class<?> type = null;
			StringBuffer coffset = new StringBuffer();
			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(entity.getClass());
			for (String var : vars) {
				if (src != null)
					coffset.append('.');
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
			if (src != null) {
				return compare(src, value, operator, type);
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractEntity> boolean matchArray(Object src,
			String column, EnumOperator operator, Object value)
			throws Exception {
		T[] entities = (T[]) src;
		for (T entity : entities) {
			if (match(entity, column, operator, value))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean isListEntityType(Object src) {
		Iterable<T> entities = (Iterable<T>) src;
		if (entities != null && entities.iterator().hasNext()) {
			T value = entities.iterator().next();
			if (value instanceof AbstractEntity) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean isArrayEntityType(Object src) {
		T[] entities = (T[]) src;
		if (entities != null && entities.length > 0) {
			T value = entities[0];
			if (value instanceof AbstractEntity) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractEntity> boolean matchList(Object src,
			String column, EnumOperator operator, Object value)
			throws Exception {
		Iterable<T> entities = (Iterable<T>) src;

		for (T entity : entities) {
			if (match(entity, column, operator, value))
				return true;
		}
		return false;
	}

	private boolean compare(Object src, Object tgt, EnumOperator operator,
			Class<?> type) throws Exception {
		if (EnumPrimitives.isPrimitiveType(type)) {
			EnumPrimitives etype = EnumPrimitives.type(type);
			switch (etype) {
			case EShort:
				return compareShort(src, tgt, operator);
			case EInteger:
				return compareInt(src, tgt, operator);
			case ELong:
				return compareLong(src, tgt, operator);
			case EFloat:
				return compareFloat(src, tgt, operator);
			case EDouble:
				return compareDouble(src, tgt, operator);
			case ECharacter:
				return compareChar(src, tgt, operator);
			}
			return src.equals(tgt);
		} else if (type == String.class) {
			return compareString(src, tgt, operator);
		} else if (type == Date.class) {
			return compareDate(src, tgt, operator);
		} else if (type.isArray()) {
			if (operator != EnumOperator.Contains)
				return false;
			Class<?> atype = type.getComponentType();
			if (EnumPrimitives.isPrimitiveType(atype)) {
				EnumPrimitives etype = EnumPrimitives.type(atype);
				switch (etype) {
				case EShort:
					return containsShortArray(src, tgt, atype);
				case EInteger:
					return containsIntArray(src, tgt, atype);
				case ELong:
					return containsLongArray(src, tgt, atype);
				case EFloat:
					return containsFloatArray(src, tgt, atype);
				case EDouble:
					return containsDoubleArray(src, tgt, atype);
				case ECharacter:
					return containsCharArray(src, tgt, atype);
				}
			} else {
				return containsObjectArray(src, tgt);
			}
		} else if (src instanceof Iterable<?>) {
			if (operator != EnumOperator.Contains)
				return false;
			return containsObjectList(src, tgt);

		} else if (src instanceof Enum) {
			return compareEnum(src, tgt);
		} else {
			throw new Exception("Unsupported value comparison. [CLASS:"
					+ type.getCanonicalName() + "]");
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T extends Enum> boolean compareEnum(Object src, Object tgt)
			throws Exception {
		String name = ((T) src).name();
		if (name.compareToIgnoreCase((String) tgt) == 0)
			return true;
		return false;
	}

	private boolean containsObjectList(Object src, Object tgt) throws Exception {
		Iterable<?> iterable = (Iterable<?>) src;
		Iterator<?> iter = iterable.iterator();

		Class<?> type = null;
		boolean primitive = false;
		EnumPrimitives etype = null;

		while (iter.hasNext()) {
			Object obj = iter.next();
			if (type == null) {
				type = obj.getClass();
				primitive = EnumPrimitives.isPrimitiveType(type);
				if (primitive)
					etype = EnumPrimitives.type(type);
			}
			boolean retval = false;
			if (primitive) {
				switch (etype) {
				case EShort:
					retval = compareShort(obj, tgt, EnumOperator.Equal);
				case EInteger:
					retval = compareInt(obj, tgt, EnumOperator.Equal);
				case ELong:
					retval = compareLong(obj, tgt, EnumOperator.Equal);
				case EFloat:
					retval = compareFloat(obj, tgt, EnumOperator.Equal);
				case EDouble:
					retval = compareDouble(obj, tgt, EnumOperator.Equal);
				case ECharacter:
					retval = compareChar(obj, tgt, EnumOperator.Equal);
				}
				retval = obj.equals(tgt);
			} else {
				retval = obj.equals(tgt);
			}
			if (retval)
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean containsObjectArray(Object src, Object tgt)
			throws Exception {
		T[] array = (T[]) src;
		for (T val : array) {
			if (val.equals((T) tgt))
				return true;
		}
		return false;
	}

	private boolean containsShortArray(Object src, Object tgt, Class<?> type)
			throws Exception {
		short value = Short.parseShort((String) tgt);
		if (type.isPrimitive()) {
			short[] array = (short[]) src;
			for (short val : array) {
				if (val == value)
					return true;
			}
		} else {
			Short[] array = (Short[]) src;
			for (short val : array) {
				if (val == value)
					return true;
			}
		}
		return false;
	}

	private boolean containsIntArray(Object src, Object tgt, Class<?> type)
			throws Exception {
		int value = Integer.parseInt((String) tgt);
		if (type.isPrimitive()) {
			int[] array = (int[]) src;
			for (int val : array) {
				if (val == value)
					return true;
			}
		} else {
			Integer[] array = (Integer[]) src;
			for (int val : array) {
				if (val == value)
					return true;
			}
		}
		return false;
	}

	private boolean containsLongArray(Object src, Object tgt, Class<?> type)
			throws Exception {
		long value = Long.parseLong((String) tgt);
		if (type.isPrimitive()) {
			long[] array = (long[]) src;
			for (long val : array) {
				if (val == value)
					return true;
			}
		} else {
			Long[] array = (Long[]) src;
			for (long val : array) {
				if (val == value)
					return true;
			}
		}
		return false;
	}

	private boolean containsFloatArray(Object src, Object tgt, Class<?> type)
			throws Exception {
		float value = Float.parseFloat((String) tgt);
		if (type.isPrimitive()) {
			float[] array = (float[]) src;
			for (float val : array) {
				if (val == value)
					return true;
			}
		} else {
			Float[] array = (Float[]) src;
			for (float val : array) {
				if (val == value)
					return true;
			}
		}
		return false;
	}

	private boolean containsDoubleArray(Object src, Object tgt, Class<?> type)
			throws Exception {
		double value = Double.parseDouble((String) tgt);
		if (type.isPrimitive()) {
			double[] array = (double[]) src;
			for (double val : array) {
				if (val == value)
					return true;
			}
		} else {
			Double[] array = (Double[]) src;
			for (double val : array) {
				if (val == value)
					return true;
			}
		}
		return false;
	}

	private boolean containsCharArray(Object src, Object tgt, Class<?> type)
			throws Exception {
		char value = ((String) tgt).charAt(0);
		if (type.isPrimitive()) {
			char[] array = (char[]) src;
			for (char val : array) {
				if (val == value)
					return true;
			}
		} else {
			Character[] array = (Character[]) src;
			for (char val : array) {
				if (val == value)
					return true;
			}
		}
		return false;
	}

	private boolean compareDate(Object src, Object tgt, EnumOperator oper)
			throws Exception {
		Date ds = (Date) src;
		long dsvalue = ds.getTime();
		Object dtgt = null;
		if (oper != EnumOperator.Between && oper != EnumOperator.In) {
			long dtvalue = -1;
			try {
				dtvalue = Long.parseLong((String) tgt);
			} catch (Exception e) {
				String sdt = (String) tgt;
				if (sdt.indexOf(';') > 0) {
					String[] parts = sdt.split(";");
					Date dt = DateUtils.parse(parts[0], parts[1]);
					dtvalue = dt.getTime();
					ds = DateUtils.parse(DateUtils.format(ds, parts[1]),
							parts[1]);
					dsvalue = ds.getTime();
				} else {
					Date dt = DateUtils.parse(sdt);
					dtvalue = dt.getTime();
					ds = DateUtils.parse(DateUtils.format(ds));
					dsvalue = ds.getTime();
				}
			}
			dtgt = String.valueOf(dtvalue);
		} else {
			String[] values = (String[]) tgt;
			String[] pvalue = new String[values.length];
			for (int ii = 0; ii < values.length; ii++) {
				String dts = values[ii];
				long dtvalue = -1;
				try {
					dtvalue = Long.parseLong(dts);
				} catch (Exception e) {
					if (dts.indexOf(';') > 0) {
						String[] parts = dts.split(";");
						Date dt = DateUtils.parse(parts[0], parts[1]);
						dtvalue = dt.getTime();
						ds = DateUtils.parse(DateUtils.format(ds, parts[1]),
								parts[1]);
						dsvalue = ds.getTime();
					} else {
						Date dt = DateUtils.parse(dts);
						dtvalue = dt.getTime();
						ds = DateUtils.parse(DateUtils.format(ds));
						dsvalue = ds.getTime();
					}
				}
				pvalue[ii] = String.valueOf(dtvalue);
			}
			dtgt = pvalue;
		}
		return compareLong(dsvalue, dtgt, oper);
	}

	private boolean compareString(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return (((String) src).compareTo((String) tgt) == 0);
		case GreaterThan:
			return (((String) src).compareTo((String) tgt) > 0);
		case GreaterThanEqual:
			return (((String) src).compareTo((String) tgt) >= 0);
		case LessThan:
			return (((String) src).compareTo((String) tgt) < 0);
		case LessThanEqual:
			return (((String) src).compareTo((String) tgt) <= 0);
		case NotEqual:
			return (((String) src).compareTo((String) tgt) != 0);
		case In:
			String[] values = (String[]) tgt;
			for (String value : values) {
				if (value.compareTo((String) src) == 0)
					return true;
			}
			return false;
		case Like:
			if (Pattern.matches((String) tgt, (String) src))
				return true;
			return false;
		default:
			return false;
		}
	}

	private boolean compareShort(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return ((Short) src == Short.parseShort((String) tgt));
		case GreaterThan:
			return ((Short) src > Short.parseShort((String) tgt));
		case GreaterThanEqual:
			return ((Short) src >= Short.parseShort((String) tgt));
		case LessThan:
			return ((Short) src < Short.parseShort((String) tgt));
		case LessThanEqual:
			return ((Short) src <= Short.parseShort((String) tgt));
		case NotEqual:
			return ((Short) src != Short.parseShort((String) tgt));
		case Between:
			String[] values = (String[]) tgt;
			short minv = Short.parseShort(values[0]);
			short maxv = Short.parseShort(values[1]);
			return ((Short) src >= minv && (Short) src <= maxv);
		case In:
			String[] svalues = (String[]) tgt;
			for (String value : svalues) {
				short shval = Short.parseShort(value);
				if (shval == (Short) src)
					return true;
			}
			return false;
		default:
			return false;
		}
	}

	private boolean compareInt(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return ((Integer) src == Integer.parseInt((String) tgt));
		case GreaterThan:
			return ((Integer) src > Integer.parseInt((String) tgt));
		case GreaterThanEqual:
			return ((Integer) src >= Integer.parseInt((String) tgt));
		case LessThan:
			return ((Integer) src < Integer.parseInt((String) tgt));
		case LessThanEqual:
			return ((Integer) src <= Integer.parseInt((String) tgt));
		case NotEqual:
			return ((Integer) src != Integer.parseInt((String) tgt));
		case Between:
			String[] values = (String[]) tgt;
			int minv = Integer.parseInt(values[0]);
			int maxv = Integer.parseInt(values[1]);
			return ((Integer) src >= minv && (Integer) src <= maxv);
		case In:
			String[] svalues = (String[]) tgt;
			for (String value : svalues) {
				int shval = Integer.parseInt(value);
				if (shval == (Integer) src)
					return true;
			}
			return false;
		default:
			return false;
		}
	}

	private boolean compareLong(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return ((Long) src == Long.parseLong((String) tgt));
		case GreaterThan:
			return ((Long) src > Long.parseLong((String) tgt));
		case GreaterThanEqual:
			return ((Long) src >= Long.parseLong((String) tgt));
		case LessThan:
			return ((Long) src < Long.parseLong((String) tgt));
		case LessThanEqual:
			return ((Long) src <= Long.parseLong((String) tgt));
		case NotEqual:
			return ((Long) src != Long.parseLong((String) tgt));
		case Between:
			String[] values = (String[]) tgt;
			long minv = Long.parseLong(values[0]);
			long maxv = Long.parseLong(values[1]);
			return ((Long) src >= minv && (Long) src <= maxv);
		case In:
			String[] svalues = (String[]) tgt;
			for (String value : svalues) {
				long shval = Long.parseLong(value);
				if (shval == (Long) src)
					return true;
			}
			return false;
		default:
			return false;
		}
	}

	private boolean compareFloat(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return ((Float) src == Float.parseFloat((String) tgt));
		case GreaterThan:
			return ((Float) src > Float.parseFloat((String) tgt));
		case GreaterThanEqual:
			return ((Float) src >= Float.parseFloat((String) tgt));
		case LessThan:
			return ((Float) src < Float.parseFloat((String) tgt));
		case LessThanEqual:
			return ((Float) src <= Float.parseFloat((String) tgt));
		case NotEqual:
			return ((Float) src != Float.parseFloat((String) tgt));
		case Between:
			String[] values = (String[]) tgt;
			float minv = Float.parseFloat(values[0]);
			float maxv = Float.parseFloat(values[1]);
			return ((Float) src >= minv && (Float) src <= maxv);
		case In:
			String[] svalues = (String[]) tgt;
			for (String value : svalues) {
				float shval = Float.parseFloat(value);
				if (shval == (Float) src)
					return true;
			}
			return false;
		default:
			return false;
		}
	}

	private boolean compareDouble(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return ((Double) src == Double.parseDouble((String) tgt));
		case GreaterThan:
			return ((Double) src > Double.parseDouble((String) tgt));
		case GreaterThanEqual:
			return ((Double) src >= Double.parseDouble((String) tgt));
		case LessThan:
			return ((Double) src < Double.parseDouble((String) tgt));
		case LessThanEqual:
			return ((Double) src <= Double.parseDouble((String) tgt));
		case NotEqual:
			return ((Double) src != Double.parseDouble((String) tgt));
		case Between:
			String[] values = (String[]) tgt;
			double minv = Double.parseDouble(values[0]);
			double maxv = Double.parseDouble(values[1]);
			return ((Double) src >= minv && (Double) src <= maxv);
		case In:
			String[] svalues = (String[]) tgt;
			for (String value : svalues) {
				double shval = Double.parseDouble(value);
				if (shval == (Double) src)
					return true;
			}
			return false;
		default:
			return false;
		}
	}

	private boolean compareChar(Object src, Object tgt, EnumOperator oper) {
		switch (oper) {
		case Equal:
			return ((Character) src == ((String) tgt).charAt(0));
		case GreaterThan:
			return ((Character) src > ((String) tgt).charAt(0));
		case GreaterThanEqual:
			return ((Character) src >= ((String) tgt).charAt(0));
		case LessThan:
			return ((Character) src < ((String) tgt).charAt(0));
		case LessThanEqual:
			return ((Character) src <= ((String) tgt).charAt(0));
		case NotEqual:
			return ((Character) src != ((String) tgt).charAt(0));
		case Between:
			String[] values = (String[]) tgt;
			char minv = values[0].charAt(0);
			char maxv = values[1].charAt(0);
			return ((Character) src >= minv && (Character) src <= maxv);
		case In:
			String[] svalues = (String[]) tgt;
			for (String value : svalues) {
				if (value.charAt(0) == (Character) src)
					return true;
			}
			return false;
		default:
			return false;
		}
	}
}
