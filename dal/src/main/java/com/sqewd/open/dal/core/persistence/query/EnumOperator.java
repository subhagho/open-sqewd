/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * Enum defines comparison operators for Query conditions.
 * 
 * @author subhagho
 * 
 */
public enum EnumOperator {
	/**
	 * Value equals (=)
	 */
	Equal,
	/**
	 * Value not equal to (!=)
	 */
	NotEqual,
	/**
	 * Value less than (<)
	 */
	LessThan,
	/**
	 * Value less than equal to (<=)
	 */
	LessThanEqual,
	/**
	 * Value greater than (>)
	 */
	GreaterThan,
	/**
	 * Value greater than equal to (>=)
	 */
	GreaterThanEqual,
	/**
	 * Value like, condition can be a regular expression (if data store supports
	 * regular expressions).
	 * 
	 * (like)
	 */
	Like,
	/**
	 * Value is contained in List. (contains)
	 */
	Contains,
	/**
	 * Value between (between)
	 */
	Between,
	/**
	 * Value in List (in)
	 */
	In;

	public static final String[] _OPERATOR_TOKENS_ = { "<=", ">=", "!=", "=",
			"<", ">" };
	public static final String[] _OPERATOR_KEYWORDS_ = { "like", "between",
			"contains", "in" };

	public static final int _INDEX_LEQ_ = 0;
	public static final int _INDEX_GEQ_ = 1;
	public static final int _INDEX_NEQ_ = 2;
	public static final int _INDEX_EQ_ = 3;
	public static final int _INDEX_LT_ = 4;
	public static final int _INDEX_GT_ = 5;
	public static final int _INDEX_LK_ = 0;
	public static final int _INDEX_BT_ = 1;
	public static final int _INDEX_CON_ = 2;
	public static final int _INDEX_IN_ = 3;

	/**
	 * Convert the specified query operator to the Operator Enum.
	 * 
	 * @param oper
	 *            - Operator string.
	 * @return
	 * @throws Exception
	 */
	public static final EnumOperator parse(String oper) throws Exception {
		for (int ii = 0; ii < _OPERATOR_TOKENS_.length; ii++) {
			if (oper.compareTo(_OPERATOR_TOKENS_[ii]) == 0) {
				switch (ii) {
				case _INDEX_LEQ_:
					return LessThanEqual;
				case _INDEX_GEQ_:
					return GreaterThanEqual;
				case _INDEX_NEQ_:
					return NotEqual;
				case _INDEX_EQ_:
					return Equal;
				case _INDEX_LT_:
					return LessThan;
				case _INDEX_GT_:
					return GreaterThan;
				}
			}
		}
		for (int ii = 0; ii < _OPERATOR_KEYWORDS_.length; ii++) {
			if (oper.compareTo(_OPERATOR_KEYWORDS_[ii]) == 0) {
				switch (ii) {
				case _INDEX_LK_:
					return Like;
				case _INDEX_BT_:
					return Between;
				case _INDEX_CON_:
					return Contains;
				case _INDEX_IN_:
					return In;
				}
			}
		}
		throw new Exception("Unsupported operator [" + oper + "]");
	}
}
