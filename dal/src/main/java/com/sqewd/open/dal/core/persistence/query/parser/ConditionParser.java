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

import com.sqewd.open.dal.core.persistence.query.QueryColumn;
import com.sqewd.open.dal.core.persistence.query.conditions.AndCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.ArithmeticOperatorCondition;
import com.sqewd.open.dal.core.persistence.query.conditions.Condition;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionAttribute;
import com.sqewd.open.dal.core.persistence.query.conditions.ConditionValue;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumConditionOperator;
import com.sqewd.open.dal.core.persistence.query.conditions.EnumSortDirection;
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
	private List<QueryColumn> sortColumns = null;

	// Local Variables
	private HashMap<String, QuotedStringToken> quoted = new HashMap<String, QuotedStringToken>();

	private Stack<QueryCondition> tstack = new Stack<QueryCondition>();

	private List<Token> tokens = null;
	private Condition valueToken = null;

	/**
	 * Create a new instance of the Condition parser.
	 * 
	 * @param querystr
	 *            - Query String to process.
	 */
	public ConditionParser(final String querystr) {
		this.querystr = querystr.trim();
	}

	private Double checkNumeric(final String value) {
		if (Character.isDigit(value.charAt(0)) || value.charAt(0) == '+'
				|| value.charAt(0) == '-') {
			try {
				return Double.parseDouble(value);
			} catch (Exception e) {
				// Not a number....
			}
		}
		return null;
	}

	private int checkValueExpansion(final StringBuffer output, final int index)
			throws Exception {
		int offset = 0;

		if (index > 0) {
			// Check if number of type +/-<number>
			Token ltk = tokens.get(index - 1);
			if (ltk.isArithmeticOperator()) {
				EnumConditionOperator oper = EnumConditionOperator.parse(ltk
						.getToken());
				if (oper != null) {
					if (oper == EnumConditionOperator.Add
							|| oper == EnumConditionOperator.Subtract) {
						// Starts with +<number>
						if (index == 1) {
							output.insert(0, ltk.getToken());
						} else if (index > 1) {
							// Check exponential value.
							if (isExponentialString(index - 1)) {
								Token lltk = tokens.get(index - 2);
								output.insert(0, ltk.getToken());
								output.insert(0, lltk.getValue());
							}
						}
					}
				}
			}
		}
		// Check if right is an exponent.
		if (index < tokens.size() - 2) {
			Token rtk = tokens.get(index + 1);
			if (rtk.isArithmeticOperator()) {
				EnumConditionOperator oper = EnumConditionOperator.parse(rtk
						.getToken());
				if (oper != null) {
					if (oper == EnumConditionOperator.Add
							|| oper == EnumConditionOperator.Subtract) {
						if (isExponentialString(index + 1)) {
							Token rrtk = tokens.get(index + 2);
							output.append(rtk.getToken()).append(
									rrtk.getValue());
							offset += 2;
						}
					}
				}
			}
		}

		return offset;
	}

	private boolean doProcessSignOperator(final int index) throws Exception {
		if (index > 0) {
			Token tk = tokens.get(index - 1);
			// Check <special token> +/- <value>
			if (tk.isSpecialToken())
				return false;
			// Check <number>e/E+/-<number>
			if (isExponentialString(index))
				return false;
		} else
			return false;
		return true;
	}

	private StringBuffer extractQuotedTokens(final char[] stream)
			throws Exception {
		StringBuffer buff = new StringBuffer();

		QuotedStringToken qt = null;
		StringBuffer qs = null;
		for (int index = 0; index < stream.length; index++) {
			char cc = stream[index];
			if (qt == null) {
				if (cc == '"' || cc == '\'') {
					if (index == 0 || stream[index - 1] != '\\') {
						qt = new QuotedStringToken(cc);
						qt.setStartIndex(index);
						quoted.put(qt.getKey(), qt);
						qs = new StringBuffer();
						continue;
					}
				}
			} else if (qt != null) {
				if (cc == qt.getQuote()) {
					if (index == 0 || stream[index - 1] != '\\') {
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

	/**
	 * @return the sortColumns
	 */
	public List<QueryColumn> getSortColumns() {
		return sortColumns;
	}

	private QueryCondition getStackCondition() throws Exception {
		QueryCondition qc = null;
		while (true) {
			qc = tstack.peek();
			if (qc instanceof ArithmeticOperatorCondition) {
				if (qc.getParent() != null) {
					tstack.pop();
				} else
					throw new Exception(
							"Orphan Arithmetic condition found on stack.");
				continue;
			} else if (qc instanceof GroupCondition) {
				if (qc.isComplete()) {
					if (((GroupCondition) qc).getCondition() instanceof ArithmeticOperatorCondition) {
						tstack.pop();
						continue;
					}
				}
			}
			break;
		}
		// Check if condition is the Root condition.
		if (qc instanceof GroupCondition) {
			if (qc.getParent() == null) {
				if (((GroupCondition) qc).getCondition() != null) {
					qc = ((GroupCondition) qc).getCondition();
				}
			}
		}
		return qc;
	}

	private boolean isExponentialString(final int index) throws Exception {
		// Check String is <number>e/E+/-<number>
		Token ltk = tokens.get(index - 1);
		if (!isQuotedString(ltk)) {
			char[] buff = ltk.getValue().toCharArray();
			if (buff[buff.length - 1] == 'e' || buff[buff.length - 1] == 'E') {
				boolean digitfound = false;
				for (int ii = buff.length - 2; ii >= 0; ii--) {
					if (!Character.isDigit(buff[ii]) && buff[ii] != '.')
						return false;
					else if (Character.isDigit(buff[ii])) {
						digitfound = true;
					}
				}
				if (digitfound) {
					Token rtk = tokens.get(index + 1);
					if (!isQuotedString(rtk)) {
						Double db = checkNumeric(rtk.getValue());
						if (db != null)
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isQuotedString(final Token tk) throws Exception {
		String value = tk.getValue();
		if (value != null && !value.isEmpty())
			return quoted.containsKey(value);
		return false;
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

		QueryCondition qc = getStackCondition();
		// Check if Condition is already consumed.
		while (true) {
			if (qc instanceof OperatorCondition) {
				if (((OperatorCondition) qc).isConsumed()) {
					tstack.pop();
					qc = tstack.peek();
				} else {
					break;
				}
			} else {
				break;
			}
		}
		AndCondition ac = new AndCondition();

		if (qc instanceof GroupCondition) {
			if (((GroupCondition) qc).getCondition() == null) {
				((GroupCondition) qc).setCondition(ac);
			} else if (qc.isComplete()) {
				if (qc.getParent() != null) {
					switchParent(ac, qc);
				}
				ac.setLeft(qc);
				tstack.pop();
			} else {
				ac.setLeft(((GroupCondition) qc).getCondition());
				((GroupCondition) qc).setCondition(ac);
			}
		} else if (qc instanceof AndCondition) {
			if (qc.isComplete()) {
				// Switch Parents.
				switchParent(ac, qc);
				ac.setLeft(qc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Incomplete And Condition found on stack.");
		} else if (qc instanceof OrCondition) {
			if (qc.isComplete()) {
				// Switch Parents.
				switchParent(ac, qc);
				ac.setLeft(qc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Incomplete Or Condition found on stack.");
		} else if (qc instanceof OperatorCondition) {
			if (qc.isComplete()) {
				// Switch Parents.
				if (qc.getParent() != null) {
					switchParent(ac, qc);
				}
				ac.setLeft(qc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Incomplete Operator Condition found on stack.");
		} else if (qc instanceof ArithmeticOperatorCondition)
			throw new ParseStackException(
					"Arithmetic Operator Condition found on stack.");
		tstack.push(ac);
	}

	private void processEndGroupCondtion(final int index) throws Exception {
		boolean closed = false;
		while (true) {
			QueryCondition qc = tstack.peek();
			if (qc instanceof GroupCondition) {
				if (!qc.isComplete()) {
					((GroupCondition) qc).setComplete();
					closed = true;
					break;
				}
			}
			if (qc.getParent() == null)
				throw new ParseStackException(
						"Orphan Condition found on stack.");
			tstack.pop();
		}
		if (!closed)
			throw new ParseStackException("No Group left to close.");
	}

	private void processGroupCondition(final int index) throws Exception {
		GroupCondition gc = new GroupCondition();
		QueryCondition qc = tstack.peek();
		if (qc instanceof GroupCondition) {
			// Newly created parent group condition.
			if (((GroupCondition) qc).getCondition() == null) {
				((GroupCondition) qc).setCondition(gc);
			} else
				throw new ParseStackException("Group condition already set.");
		} else if (qc instanceof AndCondition) {
			// If right is empty this condition is the right.
			if (((AndCondition) qc).getRight() == null) {
				((AndCondition) qc).setRight(gc);
			}
		} else if (qc instanceof OrCondition) {
			// If right is empty this condition is the right.
			if (((OrCondition) qc).getRight() == null) {
				((OrCondition) qc).setRight(gc);
			}
		} else if (qc instanceof ArithmeticOperatorCondition) {
			// If right is empty this condition is the right.
			if (((ArithmeticOperatorCondition) qc).getRight() == null) {
				((ArithmeticOperatorCondition) qc).setRight(gc);
			} else
				throw new ParseStackException(
						"Arithmetic condtion should have an empty right.");
		} else if (qc instanceof OperatorCondition) {
			// If right is empty this condition is the right.
			if (((OperatorCondition) qc).getRight() == null) {
				((OperatorCondition) qc).setRight(gc);
			} else
				throw new ParseStackException(
						"Operator condtion should have an empty right.");
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
		QueryCondition qc = getStackCondition();
		oc.setOperator(oper);

		if (qc instanceof AndCondition) {
			if (((AndCondition) qc).getRight() == null) {
				((AndCondition) qc).setRight(oc);
				oc.setConsumed(true);
			} else
				throw new ParseStackException("And Condition already complete.");
		} else if (qc instanceof OrCondition) {
			if (((OrCondition) qc).getRight() == null) {
				((OrCondition) qc).setRight(oc);
				oc.setConsumed(true);
			} else
				throw new ParseStackException("Or Condition already complete.");
		} else if (qc instanceof GroupCondition) {
			if (((GroupCondition) qc).getCondition() == null) {
				((GroupCondition) qc).setCondition(oc);
			} else if (((GroupCondition) qc).isArithmeticGroup()) {
				oc.setLeft(((GroupCondition) qc).getCondition());
				((GroupCondition) qc).setCondition(oc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Group condition already occupied");
		} else if (qc instanceof ArithmeticOperatorCondition) {
			if (qc.getParent() != null) {
				switchParent(oc, qc);
			}
			oc.setLeft(qc);
			tstack.pop();
		} else
			throw new Exception("Operator Condition not compatible with stack.");
		if (valueToken != null) {
			oc.setLeft(valueToken);
			valueToken = null;
		}
		tstack.push(oc);
	}

	private void processOperatorArithmetic(final int index) throws Exception {
		Token tk = tokens.get(index);

		EnumConditionOperator oper = EnumConditionOperator.parse(tk.getToken());
		if (oper == EnumConditionOperator.Add
				|| oper == EnumConditionOperator.Subtract) {
			if (!doProcessSignOperator(index))
				return;
		}

		ArithmeticOperatorCondition arc = new ArithmeticOperatorCondition();
		arc.setOperator(oper);

		QueryCondition qc = tstack.peek();
		if (qc instanceof OperatorCondition) {
			if (((OperatorCondition) qc).getRight() == null) {
				((OperatorCondition) qc).setRight(arc);
			} else {
				arc.setLeft(((OperatorCondition) qc).getRight());
				((OperatorCondition) qc).setRight(arc);
			}
			arc.setConsumed(true);
		} else if (qc instanceof ArithmeticOperatorCondition) {
			if (qc.getParent() != null) {
				switchParent(arc, qc);
				arc.setConsumed(true);
			}
			arc.setLeft(qc);
			((ArithmeticOperatorCondition) qc).setConsumed(true);
		} else if (qc instanceof GroupCondition) {
			if (((GroupCondition) qc).getCondition() == null) {
				((GroupCondition) qc).setCondition(arc);
				arc.setConsumed(true);
			} else if (qc.isComplete()) {
				if (qc.getParent() != null) {
					switchParent(arc, qc);
					arc.setConsumed(true);
				}
				arc.setLeft(qc);
				tstack.pop();
			}
		}
		if (valueToken != null) {
			arc.setLeft(valueToken);
			valueToken = null;
		}
		tstack.push(arc);
	}

	private void processOrOperator(final int index) throws Exception {

		QueryCondition qc = getStackCondition();
		// Check if Condition is already consumed.
		while (true) {
			if (qc instanceof OperatorCondition) {
				if (((OperatorCondition) qc).isConsumed()) {
					tstack.pop();
					qc = tstack.peek();
				} else {
					break;
				}
			} else {
				break;
			}
		}
		OrCondition oc = new OrCondition();

		if (qc instanceof GroupCondition) {
			if (((GroupCondition) qc).getCondition() == null) {
				((GroupCondition) qc).setCondition(oc);
			} else if (qc.isComplete()) {
				if (qc.getParent() != null) {
					switchParent(oc, qc);
				}
				oc.setLeft(qc);
				tstack.pop();
			} else {
				oc.setLeft(((GroupCondition) qc).getCondition());
				((GroupCondition) qc).setCondition(oc);
			}
		} else if (qc instanceof AndCondition) {
			if (qc.isComplete()) {
				// Switch Parents.
				switchParent(oc, qc);
				oc.setLeft(qc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Incomplete And Condition found on stack.");
		} else if (qc instanceof OrCondition) {
			if (qc.isComplete()) {
				oc.setLeft(qc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Incomplete Or Condition found on stack.");
		} else if (qc instanceof OperatorCondition) {
			if (qc.isComplete()) {
				if (qc.getParent() != null) {
					switchParent(oc, qc);
				}
				oc.setLeft(qc);
				tstack.pop();
			} else
				throw new ParseStackException(
						"Incomplete Operator Condition found on stack.");
		} else if (qc instanceof ArithmeticOperatorCondition)
			throw new ParseStackException(
					"Arithmetic Operator Condition found on stack.");
		tstack.push(oc);
	}

	private void processSort(final int index) throws Exception {
		Token tk = tokens.get(index);
		if (!tk.isSort())
			throw new Exception("Invalid Token, expected a SORT token.");
		sortColumns = new ArrayList<QueryColumn>();

		QueryColumn column = null;
		for (int ii = index; ii < tokens.size(); ii++) {
			tk = tokens.get(ii);
			if (tk.isSort() || tk.isOr()) {
				column = new QueryColumn();
				sortColumns.add(column);
				continue;
			}
			if (column == null)
				throw new Exception(
						"Invalid State: Column object not initialized.");
			if (tk.getToken() != null) {
				if (tk.getToken().compareToIgnoreCase("asc") == 0) {
					column.setDirection(EnumSortDirection.Asc);
				} else if (tk.getToken().compareToIgnoreCase("desc") == 0) {
					column.setDirection(EnumSortDirection.Desc);
				} else
					throw new Exception("Invalid Token : Did not expect ["
							+ tk.getToken() + "]");
			} else {
				if (column.getName() == null) {
					column.setName(tk.getValue());
				} else
					throw new Exception(
							"Invalid Token, column name already set.");
			}
		}
	}

	private void processTokens() throws Exception {
		if (tokens.size() > 0) {
			tstack.clear();
			// Create a dummy root GroupCondition.
			query = new GroupCondition();
			tstack.push(query);
			for (int index = 0; index < tokens.size(); index++) {
				Token tk = tokens.get(index);

				if (tk.isSpecialToken()) {
					if (tk.isOpenBrace()) {
						// Group condition start.
						processGroupCondition(index);
					} else if (tk.isCloseBrace()) {
						// Group condition end.
						processEndGroupCondtion(index);
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
					} else if (tk.isSort()) {
						// Sort is expected to be the last token set.
						processSort(index);
						break;
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
	}

	private int processValue(final int index) throws Exception {
		Token tk = tokens.get(index);
		if (tk.isSpecialToken())
			throw new Exception(
					"Condition Parse Error : Expected value/column token. Found operator token.");
		int incr = 0;

		StringBuffer value = new StringBuffer(tk.getValue());
		if (isQuotedString(tk)) {
			QuotedStringToken qst = quoted.get(value.toString());
			valueToken = new ConditionValue(qst.getValue());
		} else {
			incr = checkValueExpansion(value, index);
			Double dval = checkNumeric(value.toString());
			if (dval != null) {
				valueToken = new ConditionValue(dval);
			} else {
				valueToken = new ConditionAttribute();
				((ConditionAttribute) valueToken).setRawvalue(value.toString());
			}
		}
		QueryCondition qc = tstack.peek();
		if (qc instanceof OperatorCondition) {
			((OperatorCondition) qc).setRight(valueToken);
			valueToken = null;
		} else if (qc instanceof ArithmeticOperatorCondition) {
			((ArithmeticOperatorCondition) qc).setRight(valueToken);
			valueToken = null;
		}
		return incr;
	}

	private void switchParent(final QueryCondition to, final QueryCondition from)
			throws Exception {
		if (from.getParent() != null) {
			if (from.getParent() instanceof QueryCondition) {
				QueryCondition parent = (QueryCondition) from.getParent();
				if (parent instanceof GroupCondition) {
					((GroupCondition) parent).setCondition(to);
				} else if (parent instanceof AndCondition) {
					if (((AndCondition) parent).getLeft().equals(from)) {
						((AndCondition) parent).setLeft(to);
					} else if (((AndCondition) parent).getRight().equals(from)) {
						((AndCondition) parent).setRight(to);
					} else
						throw new Exception(
								"Invalid Parent set : Nither Left nor Right condition matches the FROM node.");
				} else if (parent instanceof OrCondition) {
					if (((OrCondition) parent).getLeft().equals(from)) {
						((OrCondition) parent).setLeft(to);
					} else if (((OrCondition) parent).getRight().equals(from)) {
						((OrCondition) parent).setRight(to);
					} else
						throw new Exception(
								"Invalid Parent set : Nither Left nor Right condition matches the FROM node.");
				} else if (parent instanceof OperatorCondition) {
					if (((OperatorCondition) parent).getLeft().equals(from)) {
						((OperatorCondition) parent).setLeft(to);
					} else if (((OperatorCondition) parent).getRight().equals(
							from)) {
						((OperatorCondition) parent).setRight(to);
					} else
						throw new Exception(
								"Invalid Parent set : Nither Left nor Right condition matches the FROM node.");
				} else if (parent instanceof ArithmeticOperatorCondition) {
					Condition pparent = parent.getParent();
					if (pparent instanceof ArithmeticException) {
						switchParent(to, (QueryCondition) pparent);
						return;
					} else if (pparent instanceof GroupCondition) {
						if (((GroupCondition) pparent).isComplete()) {
							switchParent(to, (QueryCondition) pparent);
							return;
						}
					}
					if (((ArithmeticOperatorCondition) parent).getLeft()
							.equals(from)) {
						((ArithmeticOperatorCondition) parent).setLeft(to);
					} else if (((ArithmeticOperatorCondition) parent)
							.getRight().equals(from)) {
						((ArithmeticOperatorCondition) parent).setRight(to);
					} else
						throw new Exception(
								"Invalid Parent set : Nither Left nor Right condition matches the FROM node.");
				}
				to.setParent(parent);
			} else
				throw new Exception(
						"Invalid Parent : Cannot set object of type ["
								+ from.getParent().getClass()
										.getCanonicalName() + "] as parent.");
		} else
			throw new Exception("Parent of source is NULL.");
	}
}
