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
 * @filename DemoService.java
 * @created Aug 24, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.demo.services;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;
import com.sqewd.open.dal.api.utils.ListParam;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.api.utils.Timer;
import com.sqewd.open.dal.api.utils.ValueParam;
import com.sqewd.open.dal.core.persistence.DataImport;
import com.sqewd.open.dal.core.persistence.csv.CSVPersister;
import com.sqewd.open.dal.core.persistence.csv.EnumImportFormat;
import com.sqewd.open.dal.services.DALResponse;
import com.sqewd.open.dal.services.EnumResponseState;
import com.sun.jersey.api.JResponse;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
@Path("/dal/demo/")
public class DemoService {
	private static final Logger log = LoggerFactory
			.getLogger(DemoService.class);
	private String rootpath = "./target/resources/demo/data/";
	private String[] ENTITIES = { "ROLE", "DEPARTMENT", "EMPLOYEE", "TEAM",
			"ORGANIZATION", "MEMBERSHIP" };

	@Path("/setup")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JResponse<DALResponse> setup(@Context HttpServletRequest req)
			throws Exception {
		try {
			Timer timer = new Timer();

			DALResponse response = new DALResponse();
			File path = new File(rootpath);
			response.setMessage(path.getAbsolutePath());

			for (String entity : ENTITIES) {
				File fi = new File(rootpath + "/" + entity + ".csv");
				if (!fi.exists())
					throw new Exception("File [" + fi.getAbsolutePath()
							+ "] not found...");
				log.debug("Looking for entity [" + entity + "]");

				StructEntityReflect enref = ReflectionUtils.get()
						.getEntityMetadata(entity);
				if (enref == null) {
					log.warn("Entity [" + entity + "] not found...");
					continue;
				}
				Class<?> enc = Class.forName(enref.Class);
				doImport(path.getAbsolutePath(), EnumImportFormat.CSV, enc);
				log.debug("Loaded entity [" + entity + "]");

			}
			response.setTimetaken(timer.stop());

			return JResponse.ok(response).build();
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			log.error(e.getLocalizedMessage());

			DALResponse response = new DALResponse();
			response.setState(EnumResponseState.Exception);
			response.setMessage(e.getLocalizedMessage());
			return JResponse.ok(response).build();
		}
	}

	private void doImport(String dirname, EnumImportFormat format, Class<?> type)
			throws Exception {
		CSVPersister source = new CSVPersister(format);

		// Setup CSV Persister
		ListParam params = new ListParam();

		ValueParam vp = new ValueParam();
		vp.setKey(CSVPersister._PARAM_KEY_);
		vp.setValue("CSVIMPORTSRC");
		params.add(vp);

		vp = new ValueParam();
		vp.setKey(CSVPersister._PARAM_DATADIR_);
		vp.setValue(dirname);
		params.add(vp);

		source.init(params);

		DataImport importer = new DataImport(source);
		importer.load(new String[] { type.getCanonicalName() });
	}
}
