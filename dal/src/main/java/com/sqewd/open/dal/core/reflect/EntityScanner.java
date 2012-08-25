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
 * @filename EntityScanner.java
 * @created Aug 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.reflect;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.reflections.vfs.Vfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.sqewd.open.dal.api.utils.LogUtils;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class EntityScanner {
	private static final Logger log = LoggerFactory
			.getLogger(EntityScanner.class);

	private ClassLoader loader = null;

	private List<Class<?>> classes = new ArrayList<Class<?>>();

	public EntityScanner() {
		loader = new EntityClassLoader(getClass().getClassLoader());
	}

	public void scan(String prefix) throws Exception {
		classes.clear();
		Set<URL> urls = forPackage(prefix, loader);
		for (URL url : urls) {
			for (final Vfs.File file : Vfs.fromURL(url).getFiles()) {
				scan(file, prefix);
			}
		}
	}

	private void scan(Vfs.File file, String prefix) throws Exception {
		String input = file.getRelativePath().replace('/', '.');
		if (input.endsWith(".class") && !input.endsWith("package-info.class")) {
			if (input.startsWith(prefix)) {
				log.debug("Scanning for [" + input + "]");
				String classname = getClassName(input);
				if (classname != null) {
					Class<?> cls = loader.loadClass(classname);
					if (cls != null)
						classes.add(cls);
				}
			}
		}
	}

	public List<Class<?>> getClasses() {
		return classes;
	}

	public static Set<URL> forPackage(String name, ClassLoader classLoader) {
		final Set<URL> result = Sets.newHashSet();

		final String resourceName = resourceName(name);

		try {
			final Enumeration<URL> urls = classLoader
					.getResources(resourceName);
			while (urls.hasMoreElements()) {
				final URL url = urls.nextElement();
				int index = url.toExternalForm().lastIndexOf(resourceName);
				if (index != -1) {
					result.add(new URL(url.toExternalForm().substring(0, index)));
				} else {
					result.add(url); // whatever
				}
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			LogUtils.stacktrace(log, e);
		}

		return result;
	}

	private static String resourceName(String name) {
		if (name != null) {
			String resourceName = name.replace(".", "/");
			resourceName = resourceName.replace("\\", "/");
			if (resourceName.startsWith("/")) {
				resourceName = resourceName.substring(1);
			}
			return resourceName;
		} else {
			return name;
		}
	}

	private static String getClassName(String input) {
		if (input.endsWith(".class")) {
			int index = input.indexOf(".class");
			return input.substring(0, index);
		}
		return null;
	}
}
