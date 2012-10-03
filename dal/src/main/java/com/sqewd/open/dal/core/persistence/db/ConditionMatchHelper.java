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
 * @filename ConditionMatchHelper.java
 * @created Sep 13, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.sqewd.open.dal.api.persistence.EnumPrimitives;
import com.sqewd.open.dal.api.utils.DateUtils;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumConditionOperator;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class ConditionMatchHelper {
	public static boolean compare(final Object src, final Object tgt,
			final EnumConditionOperator operator, final Class<?> type)
			throws Exception {
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
			default:
				break;
			}
			return src.equals(tgt);
		} else if (type == String.class)
			return compareString(src, tgt, operator);
		else if (type == Date.class)
			return compareDate(src, tgt, operator);

		else if (src instanceof Iterable<?>) {

		} else if (src instanceof Enum)
			return compareEnum(src, tgt);
		else
			throw new Exception("Unsupported value comparison. [CLASS:"
					+ type.getCanonicalName() + "]");
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Enum> boolean compareEnum(final Object src,
			final Object tgt) throws Exception {
		String name = ((T) src).name();
		if (name.compareToIgnoreCase((String) tgt) == 0)
			return true;
		return false;
	}

	public static boolean containsObjectList(final Object src, final Object tgt)
			throws Exception {
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
				if (primitive) {
					etype = EnumPrimitives.type(type);
				}
			}
			boolean retval = false;
			if (primitive) {
				switch (etype) {
				case EShort:
					retval = compareShort(obj, tgt,
							EnumConditionOperator.Equals);
				case EInteger:
					retval = compareInt(obj, tgt, EnumConditionOperator.Equals);
				case ELong:
					retval = compareLong(obj, tgt, EnumConditionOperator.Equals);
				case EFloat:
					retval = compareFloat(obj, tgt,
							EnumConditionOperator.Equals);
				case EDouble:
					retval = compareDouble(obj, tgt,
							EnumConditionOperator.Equals);
				case ECharacter:
					retval = compareChar(obj, tgt, EnumConditionOperator.Equals);
				default:
					break;
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
	public static <T> boolean containsObjectArray(final Object src,
			final Object tgt) throws Exception {
		T[] array = (T[]) src;
		for (T val : array) {
			if (val.equals(tgt))
				return true;
		}
		return false;
	}

	public static boolean containsShortArray(final Object src,
			final Object tgt, final Class<?> type) throws Exception {
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

	public static boolean containsIntArray(final Object src, final Object tgt,
			final Class<?> type) throws Exception {
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

	public static boolean containsLongArray(final Object src, final Object tgt,
			final Class<?> type) throws Exception {
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

	public static boolean containsFloatArray(final Object src,
			final Object tgt, final Class<?> type) throws Exception {
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

	public static boolean containsDoubleArray(final Object src,
			final Object tgt, final Class<?> type) throws Exception {
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

	public static boolean containsCharArray(final Object src, final Object tgt,
			final Class<?> type) throws Exception {
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

	public static boolean compareDate(final Object src, final Object tgt,
			final EnumConditionOperator oper) throws Exception {
		Date ds = (Date) src;
		long dsvalue = ds.getTime();
		Object dtgt = null;
		if (oper != EnumConditionOperator.Between
				&& oper != EnumConditionOperator.In) {
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

	@SuppressWarnings("unchecked")
	public static boolean compareString(final Object src, final Object tgt,
			final EnumConditionOperator oper) {
		switch (oper) {
		case Equals:
			return (((String) src).compareTo((String) tgt) == 0);
		case MoreThan:
			return (((String) src).compareTo((String) tgt) > 0);
		case MoreThanEquals:
			return (((String) src).compareTo((String) tgt) >= 0);
		case LessThan:
			return (((String) src).compareTo((String) tgt) < 0);
		case LessThanEquals:
			return (((String) src).compareTo((String) tgt) <= 0);
		case NotEqualTo:
			return (((String) src).compareTo((String) tgt) != 0);
		case In:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				for (String value : values) {
					if (value.compareTo((String) src) == 0)
						return true;
				}
			} else if (tgt instanceof Collection<?>) {
				Collection<String> values = (Collection<String>) tgt;
				for (String value : values) {
					if (value.compareTo((String) src) == 0)
						return true;
				}
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

	@SuppressWarnings("unchecked")
	public static boolean compareShort(final Object src, final Object tgt,
			final EnumOperator oper) {
		switch (oper) {
		case EqualsTo:
			return ((Short) src == Short.parseShort((String) tgt));
		case GreaterThan:
			return ((Short) src > Short.parseShort((String) tgt));
		case GreaterThanEquals:
			return ((Short) src >= Short.parseShort((String) tgt));
		case MinorThan:
			return ((Short) src < Short.parseShort((String) tgt));
		case MinorThanEquals:
			return ((Short) src <= Short.parseShort((String) tgt));
		case NotEquals:
			return ((Short) src != Short.parseShort((String) tgt));
		case Between:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				short minv = Short.parseShort(values[0]);
				short maxv = Short.parseShort(values[1]);
				return ((Short) src >= minv && (Short) src <= maxv);
			} else if (tgt instanceof Collection<?>) {
				Short[] values = (Short[]) ((Collection<Short>) tgt).toArray();
				short minv = values[0];
				short maxv = values[1];
				return ((Short) src >= minv && (Short) src <= maxv);
			}
		case In:
			if (tgt.getClass().isArray()) {
				String[] svalues = (String[]) tgt;
				for (String value : svalues) {
					short shval = Short.parseShort(value);
					if (shval == (Short) src)
						return true;
				}
				return false;
			} else if (tgt instanceof Collection<?>) {
				Collection<Short> values = (Collection<Short>) tgt;
				for (Short value : values) {
					if (value == (Short) src)
						return true;
				}
			}
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean compareInt(final Object src, final Object tgt,
			final EnumOperator oper) {
		switch (oper) {
		case EqualsTo:
			return ((Integer) src == Integer.parseInt((String) tgt));
		case GreaterThan:
			return ((Integer) src > Integer.parseInt((String) tgt));
		case GreaterThanEquals:
			return ((Integer) src >= Integer.parseInt((String) tgt));
		case MinorThan:
			return ((Integer) src < Integer.parseInt((String) tgt));
		case MinorThanEquals:
			return ((Integer) src <= Integer.parseInt((String) tgt));
		case NotEquals:
			return ((Integer) src != Integer.parseInt((String) tgt));
		case Between:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				int minv = Integer.parseInt(values[0]);
				int maxv = Integer.parseInt(values[1]);
				return ((Integer) src >= minv && (Integer) src <= maxv);
			} else if (tgt instanceof Collection<?>) {
				Integer[] values = (Integer[]) ((Collection<Integer>) tgt)
						.toArray();
				int minv = values[0];
				int maxv = values[1];
				return ((Integer) src >= minv && (Integer) src <= maxv);
			}
		case In:
			if (tgt.getClass().isArray()) {
				String[] svalues = (String[]) tgt;
				for (String value : svalues) {
					int shval = Integer.parseInt(value);
					if (shval == (Integer) src)
						return true;
				}
				return false;
			} else if (tgt instanceof Collection<?>) {
				Collection<Integer> values = (Collection<Integer>) tgt;
				for (Integer value : values) {
					if (value == (Integer) src)
						return true;
				}
			}
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean compareLong(final Object src, final Object tgt,
			final EnumOperator oper) {
		switch (oper) {
		case EqualsTo:
			return ((Long) src == Long.parseLong((String) tgt));
		case GreaterThan:
			return ((Long) src > Long.parseLong((String) tgt));
		case GreaterThanEquals:
			return ((Long) src >= Long.parseLong((String) tgt));
		case MinorThan:
			return ((Long) src < Long.parseLong((String) tgt));
		case MinorThanEquals:
			return ((Long) src <= Long.parseLong((String) tgt));
		case NotEquals:
			return ((Long) src != Long.parseLong((String) tgt));
		case Between:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				long minv = Long.parseLong(values[0]);
				long maxv = Long.parseLong(values[1]);
				return ((Long) src >= minv && (Long) src <= maxv);
			} else if (tgt instanceof Collection<?>) {
				Long[] values = (Long[]) ((Collection<Long>) tgt).toArray();
				long minv = values[0];
				long maxv = values[1];
				return ((Long) src >= minv && (Long) src <= maxv);
			}
		case In:
			if (tgt.getClass().isArray()) {
				String[] svalues = (String[]) tgt;
				for (String value : svalues) {
					long shval = Long.parseLong(value);
					if (shval == (Long) src)
						return true;
				}
				return false;
			} else if (tgt instanceof Collection<?>) {
				Collection<Long> values = (Collection<Long>) tgt;
				for (Long value : values) {
					if (value == (Long) src)
						return true;
				}
			}
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean compareFloat(final Object src, final Object tgt,
			final EnumOperator oper) {
		switch (oper) {
		case EqualsTo:
			return ((Float) src == Float.parseFloat((String) tgt));
		case GreaterThan:
			return ((Float) src > Float.parseFloat((String) tgt));
		case GreaterThanEquals:
			return ((Float) src >= Float.parseFloat((String) tgt));
		case MinorThan:
			return ((Float) src < Float.parseFloat((String) tgt));
		case MinorThanEquals:
			return ((Float) src <= Float.parseFloat((String) tgt));
		case NotEquals:
			return ((Float) src != Float.parseFloat((String) tgt));
		case Between:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				float minv = Float.parseFloat(values[0]);
				float maxv = Float.parseFloat(values[1]);
				return ((Float) src >= minv && (Float) src <= maxv);
			} else if (tgt instanceof Collection<?>) {
				Float[] values = (Float[]) ((Collection<Float>) tgt).toArray();
				float minv = values[0];
				float maxv = values[1];
				return ((Float) src >= minv && (Float) src <= maxv);
			}
		case In:
			if (tgt.getClass().isArray()) {
				String[] svalues = (String[]) tgt;
				for (String value : svalues) {
					float shval = Float.parseFloat(value);
					if (shval == (Float) src)
						return true;
				}
				return false;
			} else if (tgt instanceof Collection<?>) {
				Collection<Float> values = (Collection<Float>) tgt;
				for (Float value : values) {
					if (value == (Float) src)
						return true;
				}
			}
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean compareDouble(final Object src, final Object tgt,
			final EnumOperator oper) {
		switch (oper) {
		case EqualsTo:
			return ((Double) src == Double.parseDouble((String) tgt));
		case GreaterThan:
			return ((Double) src > Double.parseDouble((String) tgt));
		case GreaterThanEquals:
			return ((Double) src >= Double.parseDouble((String) tgt));
		case MinorThan:
			return ((Double) src < Double.parseDouble((String) tgt));
		case MinorThanEquals:
			return ((Double) src <= Double.parseDouble((String) tgt));
		case NotEquals:
			return ((Double) src != Double.parseDouble((String) tgt));
		case Between:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				double minv = Double.parseDouble(values[0]);
				double maxv = Double.parseDouble(values[1]);
				return ((Double) src >= minv && (Double) src <= maxv);
			} else if (tgt instanceof Collection<?>) {
				Double[] values = (Double[]) ((Collection<Double>) tgt)
						.toArray();
				double minv = values[0];
				double maxv = values[1];
				return ((Double) src >= minv && (Double) src <= maxv);
			}
		case In:
			if (tgt.getClass().isArray()) {
				String[] svalues = (String[]) tgt;
				for (String value : svalues) {
					double shval = Double.parseDouble(value);
					if (shval == (Double) src)
						return true;
				}
				return false;
			} else if (tgt instanceof Collection<?>) {
				Collection<Double> values = (Collection<Double>) tgt;
				for (Double value : values) {
					if (value == (Double) src)
						return true;
				}
			}
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean compareChar(final Object src, final Object tgt,
			final EnumOperator oper) {
		switch (oper) {
		case EqualsTo:
			return ((Character) src == ((String) tgt).charAt(0));
		case GreaterThan:
			return ((Character) src > ((String) tgt).charAt(0));
		case GreaterThanEquals:
			return ((Character) src >= ((String) tgt).charAt(0));
		case MinorThan:
			return ((Character) src < ((String) tgt).charAt(0));
		case MinorThanEquals:
			return ((Character) src <= ((String) tgt).charAt(0));
		case NotEquals:
			return ((Character) src != ((String) tgt).charAt(0));
		case Between:
			if (tgt.getClass().isArray()) {
				String[] values = (String[]) tgt;
				char minv = values[0].charAt(0);
				char maxv = values[1].charAt(0);
				return ((Character) src >= minv && (Character) src <= maxv);
			} else if (tgt instanceof Collection<?>) {
				Character[] values = (Character[]) ((Collection<Character>) tgt)
						.toArray();
				char minv = values[0];
				char maxv = values[1];
				return ((Character) src >= minv && (Character) src <= maxv);
			}
		case In:
			if (tgt.getClass().isArray()) {
				String[] svalues = (String[]) tgt;
				for (String value : svalues) {
					if (value.charAt(0) == (Character) src)
						return true;
				}
				return false;
			} else if (tgt instanceof Collection<?>) {
				Collection<Character> values = (Collection<Character>) tgt;
				for (Character value : values) {
					if (value == (Character) src)
						return true;
				}
			}
		default:
			return false;
		}
	}
}
