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
