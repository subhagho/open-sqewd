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
 * @filename ConditionParser.java
 * @created Oct 6, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.sqewd.open.dal.core.persistence.query.conditions.AndCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.ArithmeticOperatorCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.Condition;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionAttribute;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionValue;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumConditionOperator;
import com.sqewd.open.dal.core.persistence.query.conditions.GroupCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.OperatorCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.OrCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition;

/**
 * Parse Query Conditions. This class implements a parser for processing query
 * condition strings.
 * 
 * @author subhagho
 * 
 */
public class ConditionParser {
	private String querystr;
	private QueryCondition query = null;
	private HashMap<String, QuotedStringToken> quoted = new HashMap<String, QuotedStringToken>();

	private Stack<QueryCondition> tstack = new Stack<QueryCondition>();
	private List<Token> tokens = null;

	/**
	 * Create a new instance of the Condition parser.
	 * 
	 * @param querystr
	 *            - Query String to process.
	 */
	public ConditionParser(final String querystr) {
		this.querystr = querystr.trim();
		this.querystr = "(" + this.querystr + ")";
	}

	private void addAndOrCondition(final QueryCondition qc) throws Exception {
		QueryCondition lc = tstack.peek();
		if (lc instanceof GroupCondition) {
			GroupCondition gc = (GroupCondition) lc;
			if (gc.getCondition() == null) {
				gc.setCondition(qc);
			} else if (qc instanceof AndCondition) {
				AndCondition ac = (AndCondition) qc;
				ac.setLeft(gc.getCondition());
			} else if (qc instanceof OrCondition) {
				OrCondition oc = (OrCondition) qc;
				oc.setLeft(gc.getCondition());
			} else
				throw new Exception(
						"Method should only be called with And/Or Conditions");
		} else
			throw new Exception(
					"Invalid Condition Type found on stack. Expecting Group Condition.");
	}

	private void addGroupCondition(final GroupCondition gc,
			final GroupCondition parent) throws Exception {
		QueryCondition leaf = getCurrentGroupCondition(parent);
		leaf = ((GroupCondition) leaf).getCondition();
		if (leaf == null)
			throw new Exception(
					"Invalid Stack state. No open conditions available.");
		if (leaf instanceof AndCondition) {
			AndCondition ac = (AndCondition) leaf;
			if (ac.getRight() == null) {
				ac.setRight(gc);
			} else
				throw new Exception("And Condition already complete.");
		} else if (leaf instanceof OrCondition) {
			OrCondition oc = (OrCondition) leaf;
			if (oc.getRight() == null) {
				oc.setRight(gc);
			} else
				throw new Exception("Or Condition already complete.");
		} else
			throw new Exception("Leaf condition expected to be And/Or.");
	}

	private Double checkNumeric(final String value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			// Not a number....
		}
		return null;
	}

	private int checkValueExpansion(final StringBuffer value, final int index)
			throws Exception {
		int offset = 0;
		if (index > 0) {
			Token nleft = tokens.get(index - 1);
			if (nleft.isSpecialToken()) {
				EnumConditionOperator oper = EnumConditionOperator.parse(nleft
						.getToken());
				if (oper != null) {
					if (oper == EnumConditionOperator.Add
							|| oper == EnumConditionOperator.Subtract) {
						if (index > 1) {
							Token opleft = tokens.get(index - 2);
							if (opleft.isSpecialToken()) {
								value.insert(0, nleft.getValue());
								offset = 1;
							} else if (!isQuotedString(opleft)) {
								String iv = opleft.getValue();
								if (isExponentialString(iv)) {
									value.insert(0, iv + nleft.getValue());
									offset = 2;
								}
							}
						} else {
							value.insert(0, nleft.getValue());
							offset = 1;
						}
					}
				}
			}
		}
		/*
		 * else if (index < tokens.size() - 1) { Token nright = tokens.get(index
		 * + 1); if (nright.isSpecialToken()) { EnumConditionOperator oper =
		 * EnumConditionOperator.parse(nright .getToken()); if (oper != null) {
		 * if (oper == EnumConditionOperator.Add || oper ==
		 * EnumConditionOperator.Subtract) { if (index < tokens.size() - 2) {
		 * Token opright = tokens.get(index + 2); if (opright.isSpecialToken())
		 * { value.insert(0, nright.getValue()); offset = 1; } else if
		 * (!isQuotedString(opright)) { String iv = opright.getValue(); if
		 * (isExponentialString(iv)) {
		 * value.append(nright.getValue()).append(iv); offset = 2; } } } else
		 * throw new Exception( "Invalid Expression : Trailing [" +
		 * nright.getToken() + "]"); } } } }
		 */
		return offset;
	}

	private StringBuffer extractQuotedTokens(final char[] stream)
			throws Exception {
		StringBuffer buff = new StringBuffer();

		QuotedStringToken qt = null;
		StringBuffer qs = null;
		for (int index = 0; index < stream.length; index++) {
			char cc = stream[index];
			if (qt == null) {
				if (cc == '"' || cc == '\'')
					if (stream[index - 1] != '\\') {
						qt = new QuotedStringToken(cc);
						qt.setStartIndex(index);
						quoted.put(qt.getKey(), qt);
						qs = new StringBuffer();
						continue;
					}
			} else if (qt != null) {
				if (cc == qt.getQuote()) {
					if (stream[index - 1] != '\\') {
						qt.setEndIndex(index);
						qt.setValue(qs.toString());
						buff.append(qt.getKey());
						qt = null;
						continue;
					}
				} else {
					qs.append(cc);
				}
				continue;
			}
			buff.append(cc);
		}

		// Check if all the quotes have been terminated properly
		if (qt != null)
			throw new Exception("Missing terminating quote [" + qt.getQuote()
					+ "]");
		return buff;
	}

	/**
	 * Get the parsed Query Condition. Ensure that parse has been invoked prior
	 * to calling get.
	 * 
	 * @return
	 * @throws Exception
	 */
	public QueryCondition get() throws Exception {
		if (query == null)
			throw new Exception(
					"Query hasn't been parsed or pasre raised an exception.");
		return query;
	}

	private GroupCondition getCurrentGroupCondition(final GroupCondition parent) {
		QueryCondition qc = parent;
		while (true) {
			if (qc instanceof GroupCondition) {
				if (((GroupCondition) qc).getCondition() != null) {
					if (((GroupCondition) qc).getCondition() instanceof GroupCondition) {
						qc = ((GroupCondition) qc).getCondition();
						continue;
					}
				}
			}
			break;
		}
		return (GroupCondition) qc;
	}

	/**
	 * Get the query string associated with this parser.
	 * 
	 * @return
	 */
	public String getQueryString() {
		return querystr;
	}

	private boolean isExponentialString(final String value) {
		char[] buff = value.toCharArray();
		boolean digitfound = false;
		for (int ii = 0; ii < buff.length; ii++) {
			if (!Character.isDigit(buff[ii]) && digitfound) {
				if (buff[ii] == 'e' || buff[ii] == 'E') {
					if (ii == buff.length - 1)
						return true;
				}
				return false;
			} else {
				digitfound = true;
			}
		}
		if (digitfound)
			return true;

		return false;
	}

	private boolean isQuotedString(final Token tk) throws Exception {
		String value = tk.getValue();
		if (value != null && !value.isEmpty())
			return quoted.containsKey(value);
		return false;
	}

	private boolean isValidArithmeticGroup(final GroupCondition gc) {
		GroupCondition curr = gc;
		while (true) {
			QueryCondition qc = curr.getCondition();
			if (qc == null) {
				break;
			}
			if (qc instanceof GroupCondition) {
				curr = (GroupCondition) qc;
				continue;
			} else if (!(qc instanceof OperatorCondition))
				return false;
			else {
				break;
			}
		}
		return true;
	}

	/**
	 * Parse the Query string and create a QueryCondition instance.
	 * 
	 * @return
	 * @throws Exception
	 */
	public QueryCondition parse() throws Exception {
		char[] carr = querystr.toCharArray();

		// Remove all the quoted strings from the stream
		StringBuffer qbuff = extractQuotedTokens(carr);
		carr = qbuff.toString().toCharArray();

		Tokenizer tokenizer = new Tokenizer(carr);
		tokenizer.tokenize();

		tokens = tokenizer.tokens();
		if (tokens == null || tokens.size() <= 0)
			throw new Exception("Error tokenizing query. No tokens generated.");

		processTokens();

		return query;
	}

	private void processAndOperator(final int index) throws Exception {

		QueryCondition qc = tstack.peek();
		AndCondition ac = new AndCondition();

		if (qc instanceof GroupCondition) {
			GroupCondition gc = (GroupCondition) qc;
			if (gc.getCondition() == null) {
				gc.setCondition(ac);
			} else {
				ac.setLeft(gc.getCondition());
				gc.setCondition(ac);
				tstack.push(ac);
			}
		} else if (qc instanceof OperatorCondition) {
			ac.setLeft(qc);
			tstack.pop();
			addAndOrCondition(ac);
		} else if (qc instanceof AndCondition || qc instanceof OrCondition) {
			if (qc.isComplete()) {
				if (qc.getParent() != null
						&& qc.getParent() instanceof GroupCondition) {
					((GroupCondition) qc.getParent()).setCondition(ac);
				}
				ac.setLeft(qc);
				tstack.pop();
				tstack.push(ac);
			} else
				throw new Exception(
						"Condition Parse Error : Incomplete And condition encountered.");
		}
	}

	private void processGroupCondition(final int index) throws Exception {
		GroupCondition gc = new GroupCondition();
		if (!tstack.isEmpty()) {
			QueryCondition qc = tstack.peek();
			if (qc instanceof GroupCondition) {
				GroupCondition pgc = (GroupCondition) qc;
				if (pgc.getCondition() == null) {
					pgc.setCondition(gc);
				} else {
					addGroupCondition(gc, pgc);
				}
			} else if (qc instanceof AndCondition) {
				AndCondition anc = (AndCondition) qc;
				if (anc.getRight() == null) {
					anc.setRight(gc);
				} else
					throw new Exception(
							"Condition Parse Error : Condition is already complete, cannot add right value.");
			} else if (qc instanceof OrCondition) {
				OrCondition orc = (OrCondition) qc;
				if (orc.getRight() == null) {
					orc.setRight(gc);
				} else
					throw new Exception(
							"Condition Parse Error : Condition is already complete, cannot add right value.");
			}
		}
		tstack.push(gc);
	}

	private int processList(int index) throws Exception {
		List<Token> parts = new ArrayList<Token>();
		while (index < tokens.size()) {
			Token tk = tokens.get(index);
			if (tk.isListStart()) {
				index++;
				continue;
			}
			if (tk.isListEnd()) {
				break;
			}
			if (tk.isOr()) {
				index++;
				continue;
			}
			if (isQuotedString(tk)) {
				QuotedStringToken qst = quoted.get(tk.getValue());
				tk = qst;
			}
			parts.add(tk);
			index++;
		}
		if (parts.size() <= 0)
			throw new Exception("No list elements found.");
		ConditionValue cv = new ConditionValue();
		cv.setValue(parts);

		QueryCondition qc = tstack.peek();
		OperatorCondition oc = null;
		if (qc instanceof OperatorCondition) {
			oc = (OperatorCondition) qc;

		} else {
			if (qc instanceof GroupCondition) {
				GroupCondition gc = getCurrentGroupCondition((GroupCondition) qc);
				qc = gc.getCondition();
			}
			if (qc instanceof AndCondition) {

				AndCondition ac = (AndCondition) qc;
				if (ac.getRight() != null) {
					if (ac.getRight() instanceof OperatorCondition) {
						oc = (OperatorCondition) ac.getRight();
					}
				}
			} else if (qc instanceof OrCondition) {
				OrCondition orc = (OrCondition) qc;
				if (orc.getRight() != null) {
					if (orc.getRight() instanceof OperatorCondition) {
						oc = (OperatorCondition) orc.getRight();
					}
				}
			}
		}
		if (oc != null) {
			if (oc.getRight() == null) {
				oc.setRight(cv);
			} else
				throw new Exception(
						"Condition Parse Error : Completed OperatorCondition on stack.");
		} else
			throw new Exception(
					"Condition Parse Error : No OperatorCondition on stack.");
		return index;
	}

	private void processOperator(final int index) throws Exception {
		Token tk = tokens.get(index);

		EnumConditionOperator oper = EnumConditionOperator.parse(tk.getToken());

		OperatorCondition oc = new OperatorCondition();
		QueryCondition qc = tstack.peek();
		if (qc instanceof GroupCondition) {
			GroupCondition gc = getCurrentGroupCondition((GroupCondition) qc);
			qc = gc.getCondition();
		}

		if (qc instanceof OrCondition) {
			OrCondition orc = (OrCondition) qc;
			if (orc.getRight() == null) {
				orc.setRight(oc);
			} else
				throw new Exception(
						"Invalid Operator Condition : Cannot be joined to condition on stack.");
			oc.setOperator(oper);
			setOperatorValue(oc, index, true);
		} else if (qc instanceof AndCondition) {
			AndCondition anc = (AndCondition) qc;
			if (anc.getRight() == null) {
				anc.setRight(oc);
			} else
				throw new Exception(
						"Invalid Operator Condition : Cannot be joined to condition on stack.");
			oc.setOperator(oper);
			setOperatorValue(oc, index, true);
		} else {
			oc.setOperator(oper);
			setOperatorValue(oc, index, true);
			tstack.push(oc);
		}

	}

	private void processOperatorArithmetic(final int index) throws Exception {
		Token tk = tokens.get(index);

		EnumConditionOperator oper = EnumConditionOperator.parse(tk.getToken());
		if (oper == EnumConditionOperator.Add
				|| oper == EnumConditionOperator.Subtract) {
			if (!shouldProcessSignOperator(index))
				return;
		}

		ArithmeticOperatorCondition oc = new ArithmeticOperatorCondition();
		oc.setOperator(oper);

		QueryCondition qc = tstack.peek();

	}

	private void processOrOperator(final int index) throws Exception {

		QueryCondition qc = tstack.peek();
		OrCondition oc = new OrCondition();

		if (qc instanceof GroupCondition) {
			GroupCondition gc = (GroupCondition) qc;
			if (gc.getCondition() == null) {
				gc.setCondition(oc);
			} else {
				oc.setLeft(gc.getCondition());
				gc.setCondition(oc);
			}
		} else if (qc instanceof OperatorCondition) {
			oc.setLeft(qc);
			tstack.pop();
			addAndOrCondition(oc);
		} else if (qc instanceof AndCondition || qc instanceof OrCondition) {
			if (qc.isComplete()) {
				if (qc.getParent() != null
						&& qc.getParent() instanceof GroupCondition) {
					((GroupCondition) qc.getParent()).setCondition(oc);
				}
				oc.setLeft(qc);
				tstack.pop();
				tstack.push(oc);
			} else
				throw new Exception(
						"Condition Parse Error : Incomplete And condition encountered.");
		}
	}

	private void processTokens() throws Exception {
		for (int index = 0; index < tokens.size(); index++) {
			Token tk = tokens.get(index);

			if (tk.isSpecialToken()) {
				// Open braces
				if (tk.isOpenBrace()) {
					processGroupCondition(index);
					if (query == null) {
						query = tstack.peek();
					}
				} else if (tk.isCloseBrace()) {
					QueryCondition qc = tstack.peek();
					if (qc instanceof GroupCondition) {
						((GroupCondition) qc).setComplete();
						if (tstack.size() == 1) {
							query = qc;
						}
						tstack.pop();
					} else {
						QueryCondition lastc = null;
						while (true) {
							if (tstack.size() > 0) {
								QueryCondition sqc = tstack.pop();
								if (sqc instanceof GroupCondition) {
									GroupCondition gqc = (GroupCondition) sqc;
									if (gqc.getCondition() == null) {
										if (lastc != null) {
											gqc.setCondition(lastc);
										} else
											throw new Exception(
													"Condition Parse Error : GroupCondition with null condition value.");
									}
									break;
								}
								lastc = sqc;
							} else
								throw new Exception(
										"Condition Parse Error : Exhausted stack. No closing brace found.");
						}
					}
				} else if (tk.isAnd()) {
					processAndOperator(index);
				} else if (tk.isOr()) {
					processOrOperator(index);
				} else if (tk.isListStart()) {
					index = processList(index);
				} else if (tk.isOperator()) {
					processOperator(index);
				} else if (tk.isArithmeticOperator()) {
					processOperatorArithmetic(index);
				}
			} else {
				String value = tk.getValue();
				if (value == null || value.isEmpty()) {
					continue;
				}
				index += processValue(index);
			}
		}
	}

	private int processValue(final int index) throws Exception {
		QueryCondition qc = tstack.peek();

		if (qc instanceof OperatorCondition) {
			OperatorCondition oc = (OperatorCondition) qc;
			return setOperatorValue(oc, index, false);
		} else {
			if (qc instanceof GroupCondition) {
				GroupCondition gc = getCurrentGroupCondition((GroupCondition) qc);
				qc = gc.getCondition();
			}
			if (qc != null) {
				if (qc instanceof AndCondition) {
					AndCondition anc = (AndCondition) qc;
					if (anc.getRight() != null) {
						if (anc.getRight() instanceof OperatorCondition) {
							OperatorCondition oc = (OperatorCondition) anc
									.getRight();
							return setOperatorValue(oc, index, false);
						} else
							throw new Exception(
									"Invalid Operator Condition : Condition on stack cannot absorb value.");
					}
				} else if (qc instanceof OrCondition) {
					OrCondition orc = (OrCondition) qc;
					if (orc.getRight() != null) {
						if (orc.getRight() instanceof OperatorCondition) {
							OperatorCondition oc = (OperatorCondition) orc
									.getRight();
							return setOperatorValue(oc, index, false);
						} else
							throw new Exception(
									"Invalid Operator Condition : Condition on stack cannot absorb value.");
					}
				}
			}
		}
		return 0;
	}

	private int setArithmeticValue(final ArithmeticOperatorCondition oc,
			int index, final boolean left) throws Exception {
		if (left) {
			index--;
		}
		int incr = 0;
		Token tk = tokens.get(index);
		if (tk.isSpecialToken())
			throw new Exception(
					"Condition Parse Error : Expected value/column token. Found operator token.");

		StringBuffer value = new StringBuffer(tk.getValue());
		Condition vcond = null;
		if (isQuotedString(tk)) {
			QuotedStringToken qst = quoted.get(value.toString());
			// Passing NULL works here as the quoted string is supposed to
			// contain the replaced value.
			vcond = new ConditionValue(qst.getValue());
		} else {
			incr = checkValueExpansion(value, index);
			Double dval = checkNumeric(value.toString());
			if (dval != null) {
				vcond = new ConditionValue(dval);
			} else {
				vcond = new ConditionAttribute();
				((ConditionAttribute) vcond).setRawvalue(value.toString());
			}
		}
		if (left) {
			if (oc.getLeft() != null)
				throw new Exception(
						"Condition Parse Error : Expression already has a left value.");
			oc.setLeft(vcond);
		} else {
			if (oc.getRight() != null) {

			} else {
				oc.setRight(vcond);
			}
		}
		return incr;
	}

	private int setOperatorValue(final OperatorCondition oc, int index,
			final boolean left) throws Exception {
		if (left) {
			index--;
		}
		int incr = 0;
		Token tk = tokens.get(index);
		if (tk.isSpecialToken())
			throw new Exception(
					"Condition Parse Error : Expected value/column token. Found operator token.");

		StringBuffer value = new StringBuffer(tk.getValue());
		Condition vcond = null;
		if (isQuotedString(tk)) {
			QuotedStringToken qst = quoted.get(value.toString());
			// Passing NULL works here as the quoted string is supposed to
			// contain the replaced value.
			vcond = new ConditionValue(qst.getValue());
		} else {
			incr = checkValueExpansion(value, index);
			Double dval = checkNumeric(value.toString());
			if (dval != null) {
				vcond = new ConditionValue(dval);
			} else {
				vcond = new ConditionAttribute();
				((ConditionAttribute) vcond).setRawvalue(value.toString());
			}
		}
		if (left) {
			if (oc.getLeft() != null)
				throw new Exception(
						"Condition Parse Error : Expression already has a left value.");
			oc.setLeft(vcond);
		} else {
			if (oc.getRight() != null) {

			} else {
				oc.setRight(vcond);
			}
		}
		return incr;
	}

	private boolean shouldProcessSignOperator(final int index) throws Exception {
		Token tk = tokens.get(index - 1);
		if (tk.isSpecialToken())
			return false;
		if (!isQuotedString(tk)) {
			Token nleft = tokens.get(index - 2);
			String value = nleft.getValue();
			if (isExponentialString(value))
				return false;
		}
		return true;
	}
}
