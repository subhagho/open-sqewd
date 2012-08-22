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
package com.sqewd.open.dal.test;

import com.sqewd.open.dal.core.persistence.query.SQLQuery;
import com.sqewd.open.dal.demo.entities.OrganizationView;

/**
 * 
 */

/**
 * @author subhagho
 * 
 */
public class AdHocTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String dtstr = "23-07-2012";
			//
			// SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			// Date dt = format.parse(dtstr);
			//
			// System.out.println("TIMESTAMP : " + dt.getTime());

			SQLQuery sq = new SQLQuery(OrganizationView.class);
			String sql = sq.parse("", -1);
			System.out.println("[" + sql + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
