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
 * @filename Test_ExternalJoins.java
 * @created Sep 3, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.demo.ext.entities.SalaryView;
import com.sqewd.open.dal.test.EnvSetup;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class Test_ExternalJoins {

	private static final Logger log = LoggerFactory
			.getLogger(Test_ExternalJoins.class);

	/**
	 * TODO: <comments>
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		EnvSetup.setup();
		EnvSetup.doimport();
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
	public void test() {
		try {
			String query = "EMPLOYEEKEY.ID IN ('1000', '1001', '1002');SALARY.SALARY > 50500.00";
			List<AbstractEntity> entities = DataManager.get().read(query,
					SalaryView.class, -1);
			assertEquals((entities.size() > 1), true);
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
