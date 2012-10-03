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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class DbRecord {
	private final ArrayList<Object> data = new ArrayList<Object>();
	private HashMap<String, Boolean> persisters = new HashMap<String, Boolean>();

	public DbRecord(final String persister, final int size) {
		initarray(persister, size);
	}

	private DbRecord() {

	}

	public void initarray(final String persister, final int size) {
		if (persister != null) {
			if (persisters.containsKey(persister))
				return;
			persisters.put(persister, false);
		}
		if (data.size() < size + 1) {
			for (int ii = data.size(); ii <= size + 1; ii++) {
				data.add(null);
			}
		}
	}

	public void add(final int index, final Object value) {
		data.set(index, value);
	}

	public Object get(final int index) throws SQLException {
		if (index < data.size() && index > 0)
			return data.get(index);
		else
			throw new SQLException("Cannot find column [" + index + "]");
	}

	public boolean hasData(final String persister) {
		boolean has = false;
		if (persisters.containsKey(persister)) {
			has = persisters.get(persister);
		}
		return has;
	}

	public boolean hasColumns(final String persister) {
		if (persisters.containsKey(persister))
			return true;
		return false;
	}

	public void dataSet(final String persister) throws Exception {
		if (!persisters.containsKey(persister))
			throw new Exception("Data set for type [" + persister
					+ "] not defined.");
		else {
			persisters.remove(persister);
		}
		persisters.put(persister, true);
	}

	public DbRecord copy() {
		DbRecord rs = new DbRecord();
		rs.initarray(null, this.data.size());
		rs.persisters = this.persisters;

		for (int ii = 0; ii < data.size(); ii++) {
			rs.add(ii, data.get(ii));
		}
		return rs;
	}
}
