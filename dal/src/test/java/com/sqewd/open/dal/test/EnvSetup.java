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

import java.io.File;

import com.sqewd.open.dal.api.ReferenceCache;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.utils.FileUtils;
import com.sqewd.open.dal.core.Env;
import com.sqewd.open.dal.core.persistence.DataImport;
import com.sqewd.open.dal.core.persistence.DataManager;

/**
 * @author subhagho
 * 
 */
public class EnvSetup {
	public static final String _CONFIG_FILE_ = "src/test/java/com/sqewd/open/dal/demo/config/server-demo.xml";
	private static final String _SETUP_ROOTDIR_ = "src/test/java/com/sqewd/open/dal/demo/data";
	private static final String _SETUP_TEMP_ = "/tmp/sqewd/";
	private static final String _CACHE_SETUP_ = "/work/projects/open-sqewd/dal/src/test/java/com/sqewd/open/dal/demo/config/reference-cache.xml";

	private static void cleanup() throws Exception {
		File wdi = new File(_SETUP_ROOTDIR_ + "/run/");
		if (wdi.exists()) {
			FileUtils.delete(wdi);
		}
	}

	public static void dispose() {
		Env.dispose();
		try {
			cleanup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doimport() throws Exception {
		AbstractPersister persister = DataManager.get().getPersisterByName(
				"CSVPERSISTER");
		DataImport importer = new DataImport(persister);
		importer.load(new String[] { "ROLE", "EMPLOYEE", "TEAM", "MEMBERSHIP",
				"DEPARTMENT", "ORGANIZATION", "SALARY" });
	}

	public static void setup() throws Exception {
		Env.create(_CONFIG_FILE_);

		// Check the temp folders
		File tdi = new File(_SETUP_TEMP_);
		if (!tdi.exists()) {
			tdi.mkdirs();
		}
		cleanup();

		File wdi = new File(_SETUP_ROOTDIR_ + "/run/h2/");
		wdi.mkdirs();

		Env.get().setEntityLoader(Env.get().getClass().getClassLoader());

		DataManager.create(Env.get().getConfig());
	}

	public static void setupCacheOnly() throws Exception {
		ReferenceCache.get(_CACHE_SETUP_);
	}
}
