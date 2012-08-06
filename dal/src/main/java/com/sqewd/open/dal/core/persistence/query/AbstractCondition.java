/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * Represents an abstract Query Condition.
 * 
 * @author subhagho
 * 
 */
public abstract class AbstractCondition {
	protected EnumConditionType conditiontype = EnumConditionType.Value;

	/**
	 * @return the conditiontype
	 */
	public EnumConditionType getConditionType() {
		return conditiontype;
	}

	/**
	 * @param conditiontype
	 *            the conditiontype to set
	 */
	public void setConditionType(EnumConditionType conditiontype) {
		this.conditiontype = conditiontype;
	}

}
