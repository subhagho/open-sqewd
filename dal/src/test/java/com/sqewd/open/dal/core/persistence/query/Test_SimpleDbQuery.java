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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.demo.entities.TeamMember;
import com.sqewd.open.dal.test.EnvSetup;

/**
 * @author subhagho
 * 
 */
public class Test_SimpleDbQuery {
	private static final Logger log = LoggerFactory
			.getLogger(Test_SimpleDbQuery.class);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EnvSetup.setup();
		EnvSetup.doimport();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		EnvSetup.dispose();
	}

	@Test
	public void testSelect() {
		try {
			String query = "MEMBERSHIP.MEMBER.ID = '1000'";

			SimpleDbQuery dbq = new SimpleDbQuery();

			dbq.parse(new Class<?>[] { TeamMember.class }, query);
			String sql = dbq.getSelectQuery(TeamMember.class);
			log.info("SQL[" + sql + "]");

			List<AbstractEntity> entities = DataManager.get().read(query,
					TeamMember.class);
			assertEquals((entities.size() == 1), true);
			TeamMember member = (TeamMember) entities.get(0);
			assertEquals(member.getMember().getFirstname(), "Subho");
			assertEquals(member.getTeam().getManager().getLastname(), "Doe");

			query = "MEMBERSHIP.TEAM.MANAGER.FIRSTNAME like '%on%'";
			entities = DataManager.get().read(query, TeamMember.class);
			assertEquals((entities.size() > 1), true);
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
