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
package com.sqewd.open.dal.server;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.api.utils.XMLUtils;
import com.sqewd.open.dal.core.Env;

/**
 * @author subhagho
 * 
 */
public class ServerConfig {
	public static final String _EMPTY_PATH_ELEMENT_ = "-";

	public static final String _SERVER_CONFIG_KEY_ = "SERVER-CONFIG";

	public static final String _CONFIG_SERVER_WEBROOT_ = "server.web[@directory]";
	public static final String _CONFIG_SERVER_WEBAPPS_ = Env._CONFIG_XPATH_ROOT_
			+ "/server/web/webapps/app";

	public static final String _CONFIG_SERVER_PORT_ = "server[@port]";
	public static final String _CONFIG_SERVER_STOPPORT_ = "server[@monitorport]";
	public static final String _CONFIG_SERVER_NTHREADS_ = "server[@numthreads]";
	public static final String _CONFIG_JETTY_HOME_ = "server.home";
	public static final String _CONFIG_SERVICES_PACKAGE_ = "server.services[@package]";

	private int port = 8080;
	private int monitorPort = 8099;

	private String webRoot;
	private int numThreads = 10;
	private List<KeyValuePair<String>> webapps = null;

	private String jettyHome = null;

	private String servicesPackage = null;

	public String getServicesPackage() {
		return servicesPackage;
	}

	/**
	 * Initialize the Server Configuration.
	 * 
	 * @param config
	 *            - XML Configuration.
	 * 
	 * @throws Exception
	 */
	public void init(XMLConfiguration config) throws Exception {
		String value = config.getString(_CONFIG_SERVER_PORT_);
		if (value != null && !value.isEmpty())
			port = Integer.parseInt(value);

		value = config.getString(_CONFIG_SERVER_NTHREADS_);
		if (value != null && !value.isEmpty())
			numThreads = Integer.parseInt(value);

		value = config.getString(_CONFIG_SERVER_WEBROOT_);
		if (value != null && !value.isEmpty())
			webRoot = value;
		else
			webRoot = Env.get().getWorkdir();

		value = config.getString(_CONFIG_JETTY_HOME_);
		if (value != null && !value.isEmpty())
			jettyHome = value;

		value = config.getString(_CONFIG_SERVER_STOPPORT_);
		if (value != null && !value.isEmpty())
			monitorPort = Integer.parseInt(value);

		value = config.getString(_CONFIG_SERVICES_PACKAGE_);
		if (value != null && !value.isEmpty()) {
			servicesPackage = value;
		}

		loadWebApps(config.getDocument());

		Env.get().register(_SERVER_CONFIG_KEY_, this, true);
	}

	private void loadWebApps(Document doc) throws Exception {
		NodeList nl = XMLUtils.search(_CONFIG_SERVER_WEBAPPS_,
				doc.getDocumentElement());
		if (nl != null && nl.getLength() > 0) {
			webapps = new ArrayList<KeyValuePair<String>>();
			for (int ii = 0; ii < nl.getLength(); ii++) {
				Element elm = (Element) nl.item(ii);
				String ctx = elm.getAttribute("context");
				if (ctx == null || ctx.isEmpty()) {
					throw new Exception(
							"Invalid Configuration : Missing WEBAPP context attribute [context]");
				}
				String war = elm.getAttribute("war");
				if (war == null || war.isEmpty()) {
					throw new Exception(
							"Invalid Configuration : Missing WEBAPP warfile attribute [war]");
				}
				if (!ctx.startsWith("/")) {
					ctx = "/" + ctx;
				}
				KeyValuePair<String> kvp = new KeyValuePair<String>(ctx, war);
				webapps.add(kvp);
			}
		}
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the webRoot
	 */
	public String getWebRoot() {
		return webRoot;
	}

	/**
	 * @param webRoot
	 *            the webRoot to set
	 */
	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	/**
	 * @return the numThreads
	 */
	public int getNumThreads() {
		return numThreads;
	}

	/**
	 * @param numThreads
	 *            the numThreads to set
	 */
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}

	/**
	 * @return the webapps
	 */
	public List<KeyValuePair<String>> getWebapps() {
		return webapps;
	}

	/**
	 * @return the jettyHome
	 */
	public String getJettyHome() {
		return jettyHome;
	}

	/**
	 * @return the monitorPort
	 */
	public int getMonitorPort() {
		return monitorPort;
	}

}
