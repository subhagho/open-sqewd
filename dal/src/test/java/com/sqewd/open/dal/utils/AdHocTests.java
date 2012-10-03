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
package com.sqewd.open.dal.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.reflect.ReflectionHelper;

/**
 * @author subhagho
 * 
 */
public class AdHocTests {
	private ArrayList<AbstractEntity> entities = null;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		try {
			Class<?> ct = AdHocTests.class;
			List<Field> fields = ReflectionHelper.getFields(ct);
			for (Field fd : fields) {
				if (ReflectionHelper.isEntityListType(fd) != null) {
					System.out.println("Is List...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
