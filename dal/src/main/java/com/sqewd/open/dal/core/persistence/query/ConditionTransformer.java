/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

/**
 * @author subhagho
 * 
 */
public interface ConditionTransformer {
	/**
	 * Transform a Condition to a target query format.
	 * 
	 * @param condition
	 *            - Filter Condition to transform.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String transform(AbstractCondition condition) throws Exception;
}
