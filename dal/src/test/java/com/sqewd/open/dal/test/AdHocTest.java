package com.sqewd.open.dal.test;

import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sqewd.open.dal.api.utils.XMLUtils;
import com.sqewd.open.dal.core.persistence.db.H2DbPersister;

/**
 * 
 */

/**
 * @author subhagho
 *
 */
public class AdHocTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			XMLConfiguration config = new XMLConfiguration(args[0]);
			
			NodeList nl = XMLUtils.search(H2DbPersister._CONFIG_SETUP_ENTITIES_, config
					.getDocument().getDocumentElement());
			if (nl != null && nl.getLength() > 0) {
				for (int ii = 0; ii < nl.getLength(); ii++) {
					Element elm = (Element) nl.item(ii);
					String eclass = elm.getNodeValue();
					if (eclass != null && !eclass.isEmpty()) {
						Class<?> cls = Class.forName(eclass);
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
