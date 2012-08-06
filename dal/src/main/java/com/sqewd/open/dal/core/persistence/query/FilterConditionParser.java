/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sqewd.open.dal.core.persistence.Entity;
import com.sqewd.open.dal.core.persistence.ReflectionUtils;

/**
 * @author subhagho
 * 
 */
public class FilterConditionParser {
	private static final String _QUOTES_REGEX_ = "['|\"](.*?)['|\"]";

	private static final String _LIMIT_REGEX_ = "(?i)(limit)\\s+(\\d+)";

	private static final String _SORT_REGEX_ = "(?i)(sort)\\s+(.*)(,.*)?";

	private HashMap<String, String> quoted = new HashMap<String, String>();

	private List<SortColumn> sort = null;

	private List<Class<?>> tables = null;

	private int limit = -1;

	/**
	 * Parse the specified query string
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<FilterCondition> parse(List<Class<?>> tables, String query)
			throws Exception {
		quoted.clear();
		this.tables = tables;

		List<FilterCondition> conditions = new ArrayList<FilterCondition>();
		String filterstr = parseQuoted(query);
		String[] filters = filterstr.split(Query._QUERY_CONDITION_AND_);
		if (filters != null) {
			for (String filter : filters) {
				if (filter.trim().isEmpty())
					continue;
				if (parseLimit(filter))
					continue;
				if (parseSort(filter))
					continue;
				FilterCondition cond = parseCondition(filter);
				if (cond != null)
					conditions.add(cond);
			}
		}
		return conditions;
	}

	private boolean parseSort(String value) throws Exception {
		value = value.trim();
		Pattern pattern = Pattern.compile(_SORT_REGEX_);
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			String mts = matcher.group(2);
			String[] cols = mts.split(",");
			if (cols != null && cols.length > 0) {
				sort = new ArrayList<SortColumn>();
				for (String sc : cols) {
					String[] parts = sc.trim().split("\\s+");
					SortColumn column = new SortColumn();
					if (parts.length == 1) {
						column.setColumn(parts[0].trim());
						column.setOrder(EnumSortOrder.DSC);
					} else {
						column.setColumn(parts[0].trim());
						column.setOrder(EnumSortOrder.parse(parts[1]));
					}
					sort.add(column);
				}
			}
			return true;
		}
		return false;
	}

	private boolean parseLimit(String value) throws Exception {
		value = value.trim();
		Pattern pattern = Pattern.compile(_LIMIT_REGEX_);
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			limit = Integer.parseInt(matcher.group(2));
			return true;
		}
		return false;
	}

	private FilterCondition parseCondition(String filter) throws Exception {
		for (String oper : EnumOperator._OPERATOR_TOKENS_) {
			if (filter.indexOf(oper) >= 0) {
				String[] parts = filter.split(oper);
				if (parts.length < 2)
					throw new Exception("Error parsing filter condition ["
							+ filter + "]");
				if (parts[0] == null || parts[0].isEmpty())
					throw new Exception("Error parsing filter condition ["
							+ filter + "] : Missing condition field.");
				if (parts[1] == null || parts[1].isEmpty())
					throw new Exception("Error parsing filter condition ["
							+ filter + "] : Missing condition value.");
				parts[1] = parts[1].trim();
				if (quoted.containsKey(parts[1])) {
					parts[1] = quoted.get(parts[1]);
				}
				EnumOperator eoper = EnumOperator.parse(oper);
				FilterCondition cond = new FilterCondition(getTableType(
						tables.get(0), parts[0].trim()), parts[0].trim(),
						eoper, parts[1]);
				return cond;
			}
		}
		for (String oper : EnumOperator._OPERATOR_KEYWORDS_) {
			String regex = "(.*) (?i)" + oper + "(.*)";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(filter);
			while (matcher.find()) {
				String column = matcher.group(1).trim();
				String value = matcher.group(2).trim();
				if (quoted.containsKey(value)) {
					value = quoted.get(value);
				}
				EnumOperator eoper = EnumOperator.parse(oper);
				switch (eoper) {
				case Like:
				case Contains:
					FilterCondition cond = new FilterCondition(getTableType(
							tables.get(0), column), column, eoper, value);
					return cond;
				case Between:
					String vregx = "\\[(.*),(.*)\\]";
					Pattern vpattrn = Pattern.compile(vregx);
					Matcher vmatch = vpattrn.matcher(value);
					while (vmatch.find()) {
						String[] values = new String[2];
						values[0] = vmatch.group(1).trim();
						if (quoted.containsKey(values[0]))
							values[0] = quoted.get(values[0]);
						values[1] = vmatch.group(2).trim();
						if (quoted.containsKey(values[1]))
							values[1] = quoted.get(values[1]);

						FilterCondition bcond = new FilterCondition(
								getTableType(tables.get(0), column), column,
								eoper, values);
						return bcond;
					}
				case In:
					String iregx = "\\[(.*)(,.*)?\\]";
					Pattern ipattrn = Pattern.compile(iregx);
					Matcher imatch = ipattrn.matcher(value);
					while (imatch.find()) {
						String vs = imatch.group(1);
						String[] values = vs.split(",");
						if (values != null && values.length > 0) {
							for (int ii = 0; ii < values.length; ii++) {
								if (quoted.containsKey(values[ii])) {
									values[ii] = quoted.get(values[ii]);
								}
							}
						}
						FilterCondition bcond = new FilterCondition(
								getTableType(tables.get(0), column), column,
								eoper, values);
						return bcond;
					}
				}
			}
		}
		throw new Exception("Error parsing filter condition [" + filter + "]");
	}

	private Class<?> getTableType(Class<?> type, String column) {
		Entity eann = type.getAnnotation(Entity.class);
		String table = eann.recordset();
		if (column.indexOf('.') > 0) {
			String[] parts = column.split("\\.");
			table = parts[0];
		}
		return ReflectionUtils.get().getType(table);
	}

	private String parseQuoted(String condition) {
		Pattern pattern = Pattern.compile(_QUOTES_REGEX_);
		Matcher matcher = pattern.matcher(condition);
		int index = quoted.size();
		StringBuffer out = new StringBuffer();
		while (matcher.find()) {
			String part = matcher.group(1);
			String key = "QUOTED_STRING_" + index;
			matcher.appendReplacement(out, key);
			quoted.put(key, part.trim());
			index++;
		}
		return matcher.appendTail(out).toString();
	}

	/**
	 * @return the sort
	 */
	public List<SortColumn> getSort() {
		return sort;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
}
