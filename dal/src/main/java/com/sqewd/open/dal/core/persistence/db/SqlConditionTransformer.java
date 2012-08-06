/**
 * 
 */
package com.sqewd.open.dal.core.persistence.db;

import java.lang.reflect.Field;
import java.util.Date;

import com.sqewd.open.dal.core.persistence.AttributeReflection;
import com.sqewd.open.dal.core.persistence.Entity;
import com.sqewd.open.dal.core.persistence.EnumPrimitives;
import com.sqewd.open.dal.core.persistence.ReflectionUtils;
import com.sqewd.open.dal.core.persistence.query.AbstractCondition;
import com.sqewd.open.dal.core.persistence.query.AbstractConditionPredicate;
import com.sqewd.open.dal.core.persistence.query.ColumnConditionPredicate;
import com.sqewd.open.dal.core.persistence.query.ConditionTransformer;
import com.sqewd.open.dal.core.persistence.query.EnumConditionType;
import com.sqewd.open.dal.core.persistence.query.EnumOperator;
import com.sqewd.open.dal.core.persistence.query.FilterCondition;
import com.sqewd.open.dal.core.persistence.query.ValueConditionPredicate;
import com.sqewd.open.dal.utils.DateUtils;
import com.sqewd.open.dal.utils.KeyValuePair;

/**
 * @author subhagho
 * 
 */
public class SqlConditionTransformer implements ConditionTransformer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wookler.core.persistence.query.ConditionTransformer#transform(com
	 * .wookler.core.persistence.query.AbstractCondition)
	 */
	public String transform(AbstractCondition condition) throws Exception {

		if (condition instanceof FilterCondition) {
			FilterCondition fc = (FilterCondition) condition;
			AbstractConditionPredicate left = fc.getLeft();
			AbstractConditionPredicate right = fc.getRight();
			if (left instanceof ColumnConditionPredicate) {
				ColumnConditionPredicate ccp = (ColumnConditionPredicate) left;
				if (right instanceof ValueConditionPredicate) {
					ValueConditionPredicate vcp = (ValueConditionPredicate) right;
					return transformValueCondition(fc, ccp, fc.getComparator(),
							vcp);
				} else if (right instanceof ColumnConditionPredicate) {
					ColumnConditionPredicate rcp = (ColumnConditionPredicate) right;
					return transformJoinCondition(fc, ccp, fc.getComparator(),
							rcp);
				} else {
					throw new Exception("Unsupported Right Predicate : ["
							+ right.getClass().getCanonicalName() + "]");
				}
			} else {
				throw new Exception(
						"Invalid Filter Condition : Expecting type ["
								+ ColumnConditionPredicate.class
										.getCanonicalName() + "]");
			}
		}
		return null;
	}

	private String transformJoinCondition(FilterCondition fc,
			ColumnConditionPredicate lcp, EnumOperator compatator,
			ColumnConditionPredicate rcp) throws Exception {
		String cleft = getColumnReference(lcp);
		String cright = getColumnReference(rcp);

		StringBuffer buff = new StringBuffer(cleft);
		switch (compatator) {
		case Equal:
			buff.append(" = ");
			break;
		case NotEqual:
			buff.append(" <> ");
			break;
		case GreaterThan:
			buff.append(" > ");
			break;
		case GreaterThanEqual:
			buff.append(" >= ");
			break;
		case LessThan:
			buff.append(" < ");
			break;
		case LessThanEqual:
			buff.append(" <= ");
			break;
		case Between:
			buff.append(" between ");
			break;
		case In:
			buff.append(" in ");
			break;
		case Like:
			buff.append(" like ");
			break;
		default:
			throw new Exception("Operator [" + compatator.name()
					+ "] not supported");
		}
		buff.append(cright);

		return buff.toString();
	}

	private String getColumnReference(ColumnConditionPredicate ccp)
			throws Exception {
		Class<?> type = ccp.getType();

		String column = null;
		if (type != null) {
			if (ccp.getColumn().indexOf('.') > 0) {
				String[] refpath = ccp.getColumn().split("\\.");
				KeyValuePair<Class<?>> kvp = getJoinCondition(refpath, 1, type);
				column = kvp.getKey();
				type = kvp.getValue();
			} else {
				String table = null;

				// Get table name
				Entity eann = (Entity) type.getAnnotation(Entity.class);
				table = eann.recordset();

				column = table + "." + ccp.getColumn();
			}
		} else {
			column = ccp.getColumn();
		}
		return column;
	}

	private String transformValueCondition(FilterCondition fc,
			ColumnConditionPredicate ccp, EnumOperator compatator,
			ValueConditionPredicate vcp) throws Exception {
		Class<?> type = ccp.getType();

		String cleft = getColumnReference(ccp);

		StringBuffer buff = new StringBuffer(cleft);
		switch (compatator) {
		case Equal:
			buff.append(" = ");
			break;
		case NotEqual:
			buff.append(" <> ");
			break;
		case GreaterThan:
			buff.append(" > ");
			break;
		case GreaterThanEqual:
			buff.append(" >= ");
			break;
		case LessThan:
			buff.append(" < ");
			break;
		case LessThanEqual:
			buff.append(" <= ");
			break;
		case Between:
			buff.append(" between ");
			break;
		case In:
			buff.append(" in ");
			break;
		case Like:
			buff.append(" like ");
			break;
		case Contains:
			buff.append(" like ");
			vcp.setValue("'%" + (String) vcp.getValue() + "%'");
			break;
		default:
			throw new Exception("Operator [" + compatator.name()
					+ "] not supported");
		}
		if (type != null) {
			String value = getValue(fc, type);
			buff.append(value);
		} else {
			buff.append(vcp.getValue());
		}

		return buff.toString();
	}

	private KeyValuePair<Class<?>> getJoinCondition(String[] reference,
			int offset, Class<?> type) throws Exception {
		String column = reference[offset];
		AttributeReflection attr = ReflectionUtils.get().getAttribute(type,
				column);
		if (attr == null)
			throw new Exception("No attribute found for column [" + column
					+ "] for type [" + type.getCanonicalName() + "]");
		if (offset == reference.length - 1) {
			Entity eann = (Entity) type.getAnnotation(Entity.class);
			String table = eann.recordset();
			String cleft = table.concat(".").concat(attr.Column);
			;
			return new KeyValuePair<Class<?>>(cleft, type);
		} else {
			if (attr.Reference == null)
				throw new Exception(
						"Invalid Condition : Cannot resolve column [" + column
								+ "] for type [" + type.getCanonicalName()
								+ "]");
			Class<?> rtype = Class.forName(attr.Reference.Class);
			return getJoinCondition(reference, offset + 1, rtype);
		}
	}

	private String getValue(FilterCondition fc, Class<?> type) throws Exception {
		AbstractConditionPredicate acp = fc.getLeft();
		if (!(acp instanceof ColumnConditionPredicate))
			throw new Exception("Invalid Filter Condition : Expecting type ["
					+ ColumnConditionPredicate.class.getCanonicalName() + "]");
		ColumnConditionPredicate ccp = (ColumnConditionPredicate) acp;

		acp = fc.getRight();
		if (!(acp instanceof ValueConditionPredicate))
			throw new Exception("Invalid Filter Condition : Expecting type ["
					+ ValueConditionPredicate.class.getCanonicalName() + "]");
		ValueConditionPredicate vcp = (ValueConditionPredicate) acp;

		String column = ccp.getColumn();
		if (column.indexOf('.') > 0) {
			String[] parts = column.split("\\.");
			column = parts[parts.length - 1];
		}
		AttributeReflection attr = ReflectionUtils.get().getAttribute(type,
				column);
		if (vcp.getValue() instanceof String) {
			String value = (String) vcp.getValue();
			if (fc.getConditionType() == EnumConditionType.Value)
				return quoteValue(value, attr.Field);
			else
				return value;
		} else if (vcp.getValue().getClass().isArray()) {
			String[] values = (String[]) vcp.getValue();
			for (int ii = 0; ii < values.length; ii++) {
				values[ii] = quoteValue(values[ii], attr.Field);
			}
			if (fc.getComparator() == EnumOperator.Between) {
				return values[0] + " and " + values[1];
			} else if (fc.getComparator() == EnumOperator.In) {
				StringBuffer buff = new StringBuffer();
				buff.append("(");
				boolean first = false;
				for (String value : values) {
					if (first)
						first = false;
					else
						buff.append(',');
					buff.append(value);
				}
				buff.append(")");
			}
		}
		return null;
	}

	private String quoteValue(String value, Field field) throws Exception {
		if (value.startsWith("'") || value.startsWith("\""))
			return value;

		if (EnumPrimitives.isPrimitiveType(field.getType())) {
			EnumPrimitives prim = EnumPrimitives.type(field.getType());
			if (prim == EnumPrimitives.ECharacter) {
				return "'" + value.charAt(0) + "'";
			} else {
				return value;
			}
		} else {
			if (field.getType().equals(String.class)) {
				return "'" + value + "'";
			} else if (field.getType().equals(Date.class)) {
				Date dt = DateUtils.fromString(value);
				return String.valueOf(dt.getTime());
			} else if (field.getType().isEnum()) {
				return "'" + value + "'";
			}
		}
		return value;
	}
}
