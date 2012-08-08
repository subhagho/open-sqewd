/**
 * 
 */
package com.sqewd.open.dal.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.utils.KeyValuePair;
import com.sqewd.open.dal.core.Env;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * @author subhagho
 * 
 */
public class JettyServer {
	private static final Logger log = LoggerFactory
			.getLogger(JettyServer.class);

	@Option(name = "-port", usage = "Specify the Jetty Server Port")
	private String port;

	@Option(name = "-webroot", usage = "Web Content Root directory")
	private String webroot;

	@Option(name = "-config", usage = "System Configuration file path.", required = true)
	private String config;

	@Option(name = "-cmd", usage = "Command to run.", required = true)
	private String cmd;

	private ServerConfig serverConfig = new ServerConfig();

	private void start(String[] args) throws Exception {
		try {
			CmdLineParser parser = new CmdLineParser(this);

			// if you have a wider console, you could increase the value;
			// here 80 is also the default
			parser.setUsageWidth(80);

			try {
				// parse the arguments.
				parser.parseArgument(args);
				Env.create(config);

				serverConfig.init(Env.get().getConfig());
				if (port != null && !port.isEmpty()) {
					serverConfig.setPort(Integer.parseInt(port));
				}
				if (webroot != null && !webroot.isEmpty()) {
					serverConfig.setWebRoot(webroot);
				}

			} catch (CmdLineException e) {
				System.err.println("Usage : "
						+ this.getClass().getCanonicalName() + " "
						+ parser.printExample(ExampleMode.ALL));
				throw e;
			}
			if (cmd == null || cmd.isEmpty()
					|| cmd.compareToIgnoreCase("start") == 0) {
				Server server = new Server(serverConfig.getPort());
				Map<String, Object> initMap = new HashMap<String, Object>();

				log.info("Starting Jetty Server:");
				log.info("\tPort : " + serverConfig.getPort());
				log.info("\tThreads : " + serverConfig.getNumThreads());
				log.info("\tWeb Root : " + serverConfig.getWebRoot());

				String jettyhome = serverConfig.getJettyHome();
				if (jettyhome != null && !jettyhome.isEmpty()) {
					System.setProperty("jetty.home", jettyhome);
				}

				ContextHandlerCollection ctxs = new ContextHandlerCollection();
				server.setHandler(ctxs);

				List<Handler> handlers = new ArrayList<Handler>();

				String serpack = serverConfig.getServicesPackage();
				if (serpack != null && !serpack.isEmpty()) {
					initMap.put("com.sun.jersey.api.json.POJOMappingFeature",
							"true");
					initMap.put("com.sun.jersey.config.property.packages",
							serpack);

					initMap.put(
							"com.sun.jersey.config.property.resourceConfigClass",
							"com.sun.jersey.api.core.PackagesResourceConfig");

					ServletHolder sh = new ServletHolder(new ServletContainer(
							new PackagesResourceConfig(initMap)));

					// un-comment these to enable tracing of requests and
					// responses

					// sh.setInitParameter("com.sun.jersey.config.feature.Debug",
					// "true");
					// sh.setInitParameter("com.sun.jersey.config.feature.Trace",
					// "true");
					//
					// sh.setInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters",
					// "com.sun.jersey.api.container.filter.LoggingFilter");
					// sh.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters",
					// "com.sun.jersey.api.container.filter.LoggingFilter");

					ServletContextHandler restctx = new ServletContextHandler(
							ServletContextHandler.SESSIONS);
					restctx.setContextPath("/rest");
					restctx.addServlet(sh, "/*");
					handlers.add(restctx);
				}

				if (serverConfig.getWebapps() != null) {
					for (KeyValuePair<String> webapp : serverConfig
							.getWebapps()) {
						String warfile = serverConfig.getWebRoot() + "/"
								+ webapp.getValue();
						File fi = new File(warfile);
						if (!fi.exists())
							throw new Exception("Cannot find WAR file ["
									+ fi.getAbsolutePath() + "]");

						WebAppContext webctx = new WebAppContext();
						webctx.setContextPath("/web" + webapp.getKey());
						webctx.setWar(warfile);
						webctx.setParentLoaderPriority(true);

						handlers.add(webctx);
					}
				}

				Handler[] harray = new Handler[handlers.size()];
				for (int ii = 0; ii < handlers.size(); ii++) {
					harray[ii] = handlers.get(ii);
				}

				ctxs.setHandlers(harray);

				QueuedThreadPool qtp = new QueuedThreadPool(
						serverConfig.getNumThreads());
				qtp.setName("JettyServer");
				server.setThreadPool(qtp);

				server.start();
				MonitorThread thread = new MonitorThread(server, serverConfig);
				thread.start();

				log.info("Jetty Server running...");
				if (serpack != null)
					log.info("Loaded services from package [" + serpack + "]");

				log.info("Root directory [" + new File(".").getAbsolutePath()
						+ "]");
				server.join();
			} else if (cmd.compareToIgnoreCase("stop") == 0) {
				Socket s = new Socket(InetAddress.getByName("127.0.0.1"),
						serverConfig.getMonitorPort());
				OutputStream out = s.getOutputStream();
				log.info("*** sending jetty stop request");
				out.write(("\r\n").getBytes());
				out.flush();
				s.close();
			}

		} finally {
			Env.dispose();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new JettyServer().start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class MonitorThread extends Thread {
		private ServerSocket socket;
		private Server server = null;

		public MonitorThread(Server server, ServerConfig config) {
			this.server = server;
			setDaemon(true);
			setName("JettyStopMonitor");
			try {

				socket = new ServerSocket(config.getMonitorPort(), 1,
						InetAddress.getByName("127.0.0.1"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			log.info("*** running jetty 'stop' thread");
			Socket accept;
			try {
				accept = socket.accept();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(accept.getInputStream()));
				reader.readLine();
				log.info("*** stopping jetty embedded server");
				server.stop();
				accept.close();
				socket.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
