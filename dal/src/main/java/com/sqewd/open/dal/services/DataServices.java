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
 * @filename DataServices.java
 * @created Aug 23, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.OperationResponse;
import com.sqewd.open.dal.api.reflect.EntityDef;
import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.api.utils.Timer;
import com.sqewd.open.dal.core.persistence.DataManager;
import com.sqewd.open.dal.server.ServerConfig;
import com.sun.jersey.api.JResponse;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
@Path("/dal/services/")
public class DataServices {
	private static final Logger log = LoggerFactory
			.getLogger(DataServices.class);

	@Path("/schema/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JResponse<DALResponse> schema(
			@Context final HttpServletRequest req,
			@DefaultValue(ServerConfig._EMPTY_PATH_ELEMENT_) @PathParam("type") final String type)
			throws Exception {
		try {
			Timer timer = new Timer();

			log.debug("[SESSIONID:" + req.getSession().getId() + "]");

			List<EntityDef> types = null;
			if (type.compareTo(ServerConfig._EMPTY_PATH_ELEMENT_) == 0) {
				types = ReflectionUtils.get().getAllMetadata();
			} else {
				String[] names = type.split(",");
				if (names != null && names.length > 0) {
					types = new ArrayList<StructEntityReflect>();
					for (String name : names) {
						StructEntityReflect enref = ReflectionUtils.get()
								.getEntityMetadata(name);
						if (enref != null && !types.contains(enref)) {
							types.add(enref);
						}

					}
				}
			}
			DALResponse response = new DALResponse();
			response.setMessage("[TYPES:" + type + "]");
			if (types != null && types.size() > 0) {
				List<EntitySchema> schema = new ArrayList<EntitySchema>();
				List<String> packages = DataManager.get().getEntityPackages();

				for (StructEntityReflect enref : types) {
					if (!showSchema(enref, packages)) {
						continue;
					}

					EntitySchema es = EntitySchema.loadSchema(enref);
					schema.add(es);
				}
				response.setState(EnumResponseState.Success);
				response.setData(schema);
			} else {
				response.setState(EnumResponseState.NoData);
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

	private boolean showSchema(final StructEntityReflect enref,
			final List<String> packages) throws Exception {
		Class<?> type = Class.forName(enref.Class);
		String pname = type.getPackage().getName();
		if (packages.contains(pname))
			return true;
		return false;
	}

	@Path("/read/{type}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JResponse<DALResponse> read(@Context final HttpServletRequest req,
			@PathParam("type") final String type,
			@DefaultValue("") @QueryParam("q") final String query,
			@DefaultValue("1") @QueryParam("p") final String page,
			@DefaultValue("20") @QueryParam("s") final String size,
			@DefaultValue("off") @QueryParam("d") final String debugs)
			throws Exception {
		try {
			Timer timer = new Timer();

			log.debug("[ENTITY TYPE:" + type + "]");

			int pagec = Integer.parseInt(page);
			int limit = Integer.parseInt(size);
			int count = pagec * limit;
			boolean debug = false;
			if (debugs.compareToIgnoreCase("on") == 0) {
				debug = true;
			}

			DataManager dm = DataManager.get();

			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(type);
			if (enref == null)
				throw new Exception("No entity found for type [" + type + "]");
			Class<?> typec = Class.forName(enref.Class);

			List<AbstractEntity> data = dm.read(query, typec, count);
			DALResponse response = new DALResponse();
			String path = "/read/" + typec.getCanonicalName() + "?q=" + query;
			response.setRequest(path);
			if (data == null || data.size() <= 0) {
				response.setState(EnumResponseState.NoData);
			} else {
				response.setState(EnumResponseState.Success);
				int stindex = limit * (pagec - 1);
				if (stindex > 0) {
					if (stindex > data.size()) {
						response.setState(EnumResponseState.NoData);
					} else {
						List<AbstractEntity> subdata = data.subList(stindex,
								data.size());
						DALReadResponse rr = new DALReadResponse();
						rr.setData(subdata);
						if (debug) {
							EntitySchema schema = EntitySchema
									.loadSchema(typec);
							rr.setSchema(schema);
						}
						response.setData(rr);
					}
				} else {
					DALReadResponse rr = new DALReadResponse();
					rr.setData(data);
					if (debug) {
						EntitySchema schema = EntitySchema.loadSchema(typec);
						rr.setSchema(schema);
					}
					response.setData(rr);
				}
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

	@Path("/save/{type}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JResponse<DALResponse> save(@Context final HttpServletRequest req,
			@PathParam("type") final String type, final String data,
			@DefaultValue("off") @QueryParam("d") final String debugs)
			throws Exception {
		try {
			Timer timer = new Timer();

			log.debug("[ENTITY TYPE:" + type + "]");

			boolean debug = false;
			if (debugs.compareToIgnoreCase("on") == 0) {
				debug = true;
			}

			DataManager dm = DataManager.get();

			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(type);
			if (enref == null)
				throw new Exception("No entity found for type [" + type + "]");
			Class<?> typec = Class.forName(enref.Class);

			ObjectMapper mapper = new ObjectMapper();
			AbstractEntity entity = (AbstractEntity) mapper.readValue(data,
					typec);
			if (debug) {
				log.debug(entity.toString());
			}

			DALResponse response = new DALResponse();

			OperationResponse or = dm.save(entity);
			response.setMessage("SAVE");
			response.setData(or);

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
}
