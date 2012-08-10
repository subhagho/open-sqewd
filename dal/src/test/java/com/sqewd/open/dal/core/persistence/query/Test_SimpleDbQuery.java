/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.core.persistence.query.test.ReferenceRoot;

/**
 * @author subhagho
 * 
 */
public class Test_SimpleDbQuery {
	private static final Logger log = LoggerFactory
			.getLogger(Test_SimpleDbQuery.class);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSelect() {
		try {
			SimpleDbQuery dbq = new SimpleDbQuery();

			dbq.parse(new Class<?>[] { ReferenceRoot.class },
					"XYZ.REF.STR like %STRING%");
			String sql = dbq.getSelectQuery(ReferenceRoot.class);
			log.info("SQL[" + sql + "]");

		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

}
