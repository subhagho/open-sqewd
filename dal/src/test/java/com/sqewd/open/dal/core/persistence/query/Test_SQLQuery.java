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
 * @filename Test_SQLQuery.java
 * @created Aug 21, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.core.persistence.db.JoinGraph;
import com.sqewd.open.dal.demo.entities.OrganizationView;
import com.sqewd.open.dal.demo.entities.TeamMember;
import com.sqewd.open.dal.test.EnvSetup;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class Test_SQLQuery {
	private static final Logger log = LoggerFactory
			.getLogger(Test_SQLQuery.class);

	/**
	 * TODO: <comments>
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EnvSetup.setup();
		// EnvSetup.doimport();
	}

	/**
	 * TODO: <comments>
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		EnvSetup.dispose();
	}

	@Test
	public void testParse() {
		try {
			JoinGraph gr = JoinGraph.lookup(TeamMember.class);

			List<String> nodes = gr.getPath("MEMBERSHIP.MEMBER.ID");
			if (nodes.size() > 0) {
				for (String column : nodes) {
					log.info("Found : " + column);
				}
			}
			String query = "(ORGANIZATION.EMPLOYEE.ID LIKE '10%';EMPLOYEE.ID LIKE '10%')";
			SQLQuery sq = new SQLQuery(OrganizationView.class);
			String sql = sq.parse(query, 30);
			log.info("SQL[" + sql + "]");

		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testLoadParse() {
		try {
			SQLQuery sq = new SQLQuery(OrganizationView.class);
			sq.parse(
					"(ORGANIZATION.EMPLOYEE.ID LIKE '10%';EMPLOYEE.ID LIKE '10%')",
					30);
			long stime = System.currentTimeMillis();
			int size = 10000;

			for (int ii = 0; ii < size; ii++) {
				sq = new SQLQuery(OrganizationView.class);
				sq.parse(
						"(ORGANIZATION.EMPLOYEE.ID LIKE '10%';EMPLOYEE.ID LIKE '10%')",
						30);
			}
			long etime = System.currentTimeMillis();

			log.info("Time to parse [" + size + "] queries : "
					+ (etime - stime) + " msec.");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
}
