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
package com.sqewd.open.dal.utils;

import java.util.ArrayList;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition;
import com.sqewd.open.dal.core.persistence.query.parser.ConditionParser;

/**
 * @author subhagho
 * 
 */
public class AdHocTests {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		try {
			String query = "   (A.B ==    123123e-123 ; X >= +123),(THIS LIKE 'who' , THIS between[\"P\":\"X\"]);   NAME IN ['A':'B':'C']; NAME != 'whocares'";
			// String query = "sort ASASDSD asc";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<AbstractEntity> entities = null;

}
