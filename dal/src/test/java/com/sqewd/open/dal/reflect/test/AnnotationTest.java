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
 * @filename AnnotationTest.java
 * @created Aug 24, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.reflect.test;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sqewd.open.dal.core.reflect.EntityClassLoader;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class AnnotationTest {

	public void run() {
		try {
			EntityClassLoader loader = new EntityClassLoader();
			loader.loadClass("com.sqewd.open.dal.reflect.test.TestEntity");

			Class.forName("com.sqewd.open.dal.reflect.test.TestEntity");
			TestEntity entity = new TestEntity();

			JsonRootName re = entity.getClass().getAnnotation(
					JsonRootName.class);
			if (re != null) {
				System.out.println("ROOT : " + re.value());
			}
			Field[] fields = entity.getClass().getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (Field fd : fields) {
					if (fd.isAnnotationPresent(JsonProperty.class)) {
						JsonProperty attr = fd
								.getAnnotation(JsonProperty.class);
						System.out.println("Field : " + fd.getName() + " --> "
								+ attr.value());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new AnnotationTest().run();
	}
}
