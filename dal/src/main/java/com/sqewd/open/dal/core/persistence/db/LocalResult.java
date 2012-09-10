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
 * @filename LocalResult.java
 * @created Sep 8, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.db;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class LocalResult {
	private final ArrayList<Object> data = new ArrayList<Object>();
	private HashMap<String, Integer> columns = null;

	public LocalResult(final HashMap<String, Integer> columns) {
		this.columns = columns;
		initarray();
	}

	public void initarray() {
		if (data.size() < columns.size() + 1) {
			for (int ii = data.size(); ii <= columns.size() + 1; ii++) {
				data.add(null);
			}
		}
	}

	public void add(final int index, final Object value) {
		data.set(index, value);
	}

	public void add(final String column, final Object value) throws Exception {
		if (columns.containsKey(column)) {
			int index = columns.get(column);
			add(index, value);
		} else
			throw new Exception("Cannot find column [" + column + "]");
	}

	public Object get(final String column) throws Exception {
		if (columns.containsKey(column)) {
			int index = columns.get(column);
			return get(index);
		} else
			throw new Exception("Cannot find column [" + column + "]");
	}

	public Object get(final int index) {
		return data.get(index);
	}

	protected HashMap<String, Integer> getColumns() {
		return columns;
	}
}
