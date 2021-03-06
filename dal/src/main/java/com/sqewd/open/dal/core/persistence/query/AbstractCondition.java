/**
 * Copyright 2012 Subho Ghosh (subho dot ghosh at outlook dot com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sqewd.open.dal.core.persistence.query;

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
