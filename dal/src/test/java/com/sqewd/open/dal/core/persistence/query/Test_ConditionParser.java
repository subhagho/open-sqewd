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
 * @filename Test_ConditionParser.java
 * @created Oct 16, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.persistence.query;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.api.utils.LogUtils;
import com.sqewd.open.dal.core.persistence.query.conditions.QueryCondition;
import com.sqewd.open.dal.core.persistence.query.parser.ConditionParser;

/**
 * TODO: <comment>
 * 
 * @author subhagho
 * 
 */
public class Test_ConditionParser {
	private static final Logger log = LoggerFactory
			.getLogger(Test_ConditionParser.class);

	/**
	 * TODO: <comment>
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * TODO: <comment>
	 * 
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Test parsing of a basic AND condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseAnd() {
		try {
			String query = " TABLE.COLUMN >= '12344' ; TABLE.FIELD == 8888.343 ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic BETWEEN condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseBetween() {
		try {
			String query = " NAME BETWEEN ['as', 'ab'] ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic OR condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseComplexQuery() {
		try {
			String query = " NAME in ['as', 'ab', 'sc'] ;  ( A.C + ((1823 / 737646 + (ABC.X + 4555f)) *  123123e-64) != A.B.C, (X.A == 'YYYY' ; (X.B == 12837 , (X.C == '123' ; x.P > 999))) ; X >= +123),(THIS LIKE 'who' , THIS between[\"P\",\"X\"]);  NAME IN ['A','B','C'], (NAME != 'whocares'), X.V.B == 9887f";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic Grouped condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseGroup() {
		try {
			String query = " (TABLE.COLUMN >= '12344' , TABLE.FIELD == 8888.343) ; (TABLE.VALUE IN ['A', 'B', 'C']) ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic IN condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseIn() {
		try {
			String query = " NAME in ['as', 'ab', 'sc'] ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic OR condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseLeftValue() {
		try {
			String query = " ( A.C + ((1823 / 737646 + (ABC.X + 4555f)) *  123123e-64) ) == A.B.C , 88324.99 == TABLE.FIELD ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic OR condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseNestedArithmetic() {
		try {
			String query = " (TABLE.COLUMN * (PI + -188.6678e+34 / 8882389f + (TABLE.COLUMN * PI - (PI + 128736/847593)))) <= TABLE.COLUMN , 88324.99 == TABLE.FIELD ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a complex nested Grouped condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseNestedGroup() {
		try {
			String query = " (TABLE.COLUMN >= '12344' , (TABLE.FIELD == 8888.343 ; TABLE.ENTITY.NAME == 'whocares' , (COLUMN.VALUE != 1234; COLUMN.VALUE <= 334243))) ; (TABLE.VALUE IN ['A', 'B', 'C']) ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test parsing of a basic OR condition.
	 * 
	 * Test method for
	 * {@link com.sqewd.open.dal.core.persistence.query.parser.ConditionParser#parse()}
	 * .
	 */
	@Test
	public void testParseOr() {
		try {
			String query = " TABLE.COLUMN >= '12344' , TABLE.FIELD == 8888.343 ";

			ConditionParser parser = new ConditionParser(query);
			parser.parse();

			QueryCondition qc = parser.get();
			log.info("ORIGINAL QUERY [" + query + "]");
			log.info("PARSED QUERY   [\n" + qc.prettyPrint(0) + "\n]");
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}
}
