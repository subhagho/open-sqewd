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
 * @filename SQLConditionMatcher.java
 * @created Sep 11, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.gibello.zql.ZConstant;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.core.persistence.db.LocalResultSet;
import com.sqewd.open.dal.core.persistence.query.ConditionMatcher;
import com.sqewd.open.dal.core.persistence.query.EnumOperator;

/**
 * Processes Join conditions and matches records which satisfy the join
 * condition.
 * 
 * @author subhagho
 * 
 */
public class SQLConditionMatcher extends ConditionMatcher {
	private static final Logger log = LoggerFactory
			.getLogger(SQLConditionMatcher.class);
	private ZExpression expression = null;
	private SQLCondition condition;

	public SQLConditionMatcher(final ZExpression expression) throws Exception {
		this.expression = expression;
		convert();
	}

	private void convert() throws Exception {
		condition = convert(expression);
		log.debug("PARSED CONDITION : " + condition.toString()
				+ ", ORIGINAL : " + expression.toString());
	}

	private SQLCondition convert(final ZExp exp) throws Exception {
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			String op = ze.getOperator();
			if (op.compareToIgnoreCase("AND") == 0)
				return convertAnd(ze);
			else if (op.compareToIgnoreCase("OR") == 0)
				return convertOr(ze);
			else
				return convertExpression(ze);
		}
		return null;
	}

	private SQLCondition convertExpression(final ZExp exp) throws Exception {
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			String op = ze.getOperator();
			EnumOperator oper = EnumOperator.tryParse(op);
			if (oper != null) {
				switch (oper) {
				case Equal:
				case NotEqual:
				case GreaterThan:
				case GreaterThanEqual:
				case LessThan:
				case LessThanEqual:
				case Like:
					SQLConstant left = convertConstant(ze.getOperand(0));
					SQLConstant right = convertConstant(ze.getOperand(1));
					return new ExpressionCondition(left, right, oper);
				case Between:
				case In:
					SQLConstant col = convertConstant(ze.getOperand(0));
					SQLConstant vals = convertValues(ze.getOperands());
					return new ExpressionCondition(col, vals, oper);
				default:
					break;
				}
			}
		}
		throw new Exception("Error converting to expression. ["
				+ exp.toString() + "]");
	}

	private SQLConstant convertValues(final Vector<ZExp> nodes)
			throws Exception {
		List<Object> values = new ArrayList<Object>();
		for (int ii = 1; ii < nodes.size(); ii++) {
			ZExp exp = nodes.elementAt(ii);
			if (!(exp instanceof ZConstant))
				throw new Exception(
						"Invalid value type found. Vector element not of type ["
								+ ZConstant.class.getCanonicalName() + "]");
			ZConstant zc = (ZConstant) exp;
			if (zc.getType() == ZConstant.COLUMNNAME
					|| zc.getType() == ZConstant.UNKNOWN)
				throw new Exception("Vector element not a value type.");
			if (zc.getType() == ZConstant.NULL) {
				values.add(null);
			} else {
				values.add(zc.getValue());
			}
		}
		return new ValueConstant(values);
	}

	private SQLConstant convertConstant(final ZExp exp) throws Exception {
		if (exp instanceof ZConstant) {
			ZConstant zc = (ZConstant) exp;
			if (zc.getType() == ZConstant.COLUMNNAME)
				return new ColumnConstant(zc.getValue());
			else
				return new ValueConstant(zc.getValue());
		}
		throw new Exception("Cannot convert to SQLConstant ["
				+ exp.getClass().getCanonicalName() + "]");
	}

	private SQLCondition convertAnd(final ZExp exp) throws Exception {
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			if (ze.getOperator().compareToIgnoreCase("AND") == 0) {
				SQLCondition left = convert(ze.getOperands().get(0));
				SQLCondition right = null;
				if (ze.nbOperands() == 2) {
					right = convert(ze.getOperands().get(1));
				} else {
					right = convertAnd(ze.getOperands(), 1);
				}
				return new AndCondition(left, right);
			}
		}
		return null;
	}

	private SQLCondition convertAnd(final Vector<ZExp> exps, final int index)
			throws Exception {
		ZExp exp = exps.elementAt(index);
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			SQLCondition left = convert(ze);
			SQLCondition right = null;
			if (index == exps.size() - 2) {
				right = convert(exps.elementAt(index + 1));
			} else {
				right = convertAnd(exps, index + 1);
			}
			return new AndCondition(left, right);
		}
		return null;
	}

	private SQLCondition convertOr(final ZExp exp) throws Exception {
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			if (ze.getOperator().compareToIgnoreCase("OR") == 0) {
				SQLCondition left = convert(ze.getOperands().get(0));
				SQLCondition right = null;
				if (ze.nbOperands() == 2) {
					right = convert(ze.getOperands().get(1));
				} else {
					right = convertOr(ze.getOperands(), 1);
				}
				return new OrCondition(left, right);
			}
		}
		return null;
	}

	private SQLCondition convertOr(final Vector<ZExp> exps, final int index)
			throws Exception {
		ZExp exp = exps.elementAt(index);
		if (exp instanceof ZExpression) {
			ZExpression ze = (ZExpression) exp;
			SQLCondition left = convert(ze);
			SQLCondition right = null;
			if (index == exps.size() - 2) {
				right = convert(exps.elementAt(index + 1));
			} else {
				right = convertAnd(exps, index + 1);
			}
			return new OrCondition(left, right);
		}
		return null;
	}

	/**
	 * Check to see if the current cursor records satisfy the join condition.
	 * 
	 * @param src
	 * @param tgt
	 * @return
	 */
	public boolean match(final LocalResultSet src, final LocalResultSet tgt)
			throws Exception {
		return condition.match(src, tgt);
	}
}
