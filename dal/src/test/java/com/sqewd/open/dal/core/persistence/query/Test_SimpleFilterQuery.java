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
package com.sqewd.open.dal.core.persistence.query;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.sqewd.open.dal.api.persistence.ReflectionUtils;
import com.sqewd.open.dal.core.persistence.query.DummyEntitiesData.EntityMatchRoot;

/**
 * @author subhagho
 * 
 */
public class Test_SimpleFilterQuery {

	@Test
	public void testDoSelect() {
		try {
			ReflectionUtils.get().getEntityMetadata(EntityMatchRoot.class);

			String qstring = "FORReference.FORReference.FORint=99999;FORDate > "
					+ new Date(0).getTime();
			EntityMatchRoot entity = new EntityMatchRoot();
			SimpleFilterQuery query = new SimpleFilterQuery();
			query.parse(new Class<?>[] { EntityMatchRoot.class }, qstring);
			boolean retval = query.doSelect(entity);
			assertEquals(true, retval);

			qstring = "ROOT.FORReference.FORReference.FORint=987666;FORDate > "
					+ new Date(0).getTime();
			query.parse(new Class<?>[] { EntityMatchRoot.class }, qstring);
			retval = query.doSelect(entity);
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

}
