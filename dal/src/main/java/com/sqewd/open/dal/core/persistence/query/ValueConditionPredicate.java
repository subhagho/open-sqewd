/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * Value Condition predicate. Holds a condition value.
 * 
 * @author subhagho
 * 
 */
public class ValueConditionPredicate extends AbstractConditionPredicate {
	private Object value;

	public ValueConditionPredicate(Object value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value.toString();
	}
}
