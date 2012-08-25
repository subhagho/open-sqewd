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
package com.sqewd.open.dal.core.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.EnumEntityState;
import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.api.persistence.StructEntityReflect;

/**
 * Utility class to import data from one source to another.
 * 
 * @author subhagho
 * 
 */
public class DataImport {
	private static final Logger log = LoggerFactory.getLogger(DataImport.class);
	private AbstractPersister source;

	/**
	 * Create an instance of the DataImport class with the specified source.
	 * 
	 * @param source
	 *            - Data import source.
	 */
	public DataImport(AbstractPersister source) {
		this.source = source;
	}

	/**
	 * Import the data from the source to the service database.
	 * 
	 * @param entities
	 *            - Class name(s) of entities to load.
	 * @throws Exception
	 */
	public void load(String[] entities) throws Exception {
		for (String entity : entities) {
			StructEntityReflect enref = ReflectionUtils.get()
					.getEntityMetadata(entity);
			Class<?> cls = Class.forName(enref.Key);
			List<AbstractEntity> data = source.read("", cls, -1);
			if (data != null && data.size() > 0) {
				AbstractPersister dest = DataManager.get().getPersister(cls);
				for (AbstractEntity en : data) {
					if (en == null) {
						log.warn("Data Import : null record found. [PERSISTER:"
								+ source.getClass().getCanonicalName() + "]");
						continue;
					}
					en.setState(EnumEntityState.Overwrite);
					dest.save(en, true);
				}
			}
		}
	}
}
