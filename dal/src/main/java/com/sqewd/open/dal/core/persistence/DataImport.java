/**
 * 
 */
package com.sqewd.open.dal.core.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.persistence.AbstractEntity;
import com.sqewd.open.dal.api.persistence.AbstractPersister;
import com.sqewd.open.dal.api.persistence.EnumEntityState;

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
			Class<?> cls = Class.forName(entity);
			List<AbstractEntity> data = source.read("", cls);
			if (data != null && data.size() > 0) {
				AbstractPersister dest = DataManager.get().getPersister(cls);
				for (AbstractEntity en : data) {
					if (en == null) {
						log.warn("Data Import : null record found. [PERSISTER:"
								+ source.getClass().getCanonicalName() + "]");
						continue;
					}
					en.setState(EnumEntityState.Overwrite);
					dest.save(en);
				}
			}
		}
	}
}
