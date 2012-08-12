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
package com.sqewd.open.dal.core.data;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.core.Env;


/**
 * @author subhagho
 * 
 */
public class Test_Env {
	private static final Logger log = LoggerFactory.getLogger(Test_Env.class);

	public static final String configfile = "./src/test/java/com/sqewd/open/dal/demo/config/server-demo.xml";

	/**
	 * Test method for {@link com.wookler.core.Env#get()}.
	 */
	@Test
	public void testGet() {
		try {
			log.info("Current directory [" + new File(".").getAbsolutePath()
					+ "]");
			Env.create(configfile);
			log.info("Environment initialzied...");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

}
