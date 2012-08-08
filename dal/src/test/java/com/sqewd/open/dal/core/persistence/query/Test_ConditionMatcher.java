/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.sqewd.open.dal.api.utils.DateUtils;
import com.sqewd.open.dal.core.persistence.query.DummyEntitiesData.EntityMatchRoot;

/**
 * @author subhagho
 * 
 */
public class Test_ConditionMatcher {

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchArray() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORDoubleContains",
					EnumOperator.Contains, ""
							+ entity.getForDoubleContains()[1]);
			assertEquals(true, retval);

			retval = matcher.match(entity, "FORIntContains",
					EnumOperator.Contains, "" + entity.getForIntConaints()[2]);
			assertEquals(true, retval);

			retval = matcher
					.match(entity, "FORStringContains", EnumOperator.Contains,
							entity.getForStringContains().get(0));
			assertEquals(true, retval);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchReference() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORReference.FORshort",
					EnumOperator.Equal, ""
							+ entity.getForReference().getForshort());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORReference.FORReference.FORint",
					EnumOperator.Equal, ""
							+ entity.getForReference().getForReference().get(0)
									.getForint());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORReference.FORReference.FORint",
					EnumOperator.Equal, "" + 77777);
			assertEquals(false, retval);
			retval = matcher.match(entity, "FORArrayReference.FORint",
					EnumOperator.Equal,
					"" + entity.getForArrayReference()[0].getForint());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORArrayReference.FORint",
					EnumOperator.Equal, "" + 99999);
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchDate() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORDate", EnumOperator.Equal,
					DateUtils.format(entity.getForDate()));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDate", EnumOperator.NotEqual, ""
					+ new Date(0).getTime());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDate",
					EnumOperator.GreaterThanEqual,
					DateUtils.format(entity.getForDate(), "MM/dd/yyyy")
							+ ";MM/dd/yyyy");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDate", EnumOperator.GreaterThan,
					"" + new Date(0).getTime());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDate", EnumOperator.LessThan, ""
					+ DateUtils.parse("12-12-2222", "MM-dd-yyyy").getTime());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDate",
					EnumOperator.LessThanEqual,
					"" + DateUtils.parse("12-12-2222", "MM-dd-yyyy").getTime());
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORDate",
					EnumOperator.Between,
					new String[] {
							""
									+ DateUtils.parse("12-12-2000",
											"MM-dd-yyyy").getTime(),
							""
									+ DateUtils.parse("12-12-2222",
											"MM-dd-yyyy").getTime() });
			assertEquals(true, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchString() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORString", EnumOperator.Equal,
					entity.getForString());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORString", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORString",
					EnumOperator.GreaterThan, "*" + entity.getForString());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORString",
					EnumOperator.GreaterThanEqual, entity.getForString());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORString", EnumOperator.LessThan,
					"Z" + entity.getForString());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORString",
					EnumOperator.LessThanEqual, entity.getForString());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORString", EnumOperator.Like,
					"(.*)-(.*)");
			assertEquals(true, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchShort() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORshort", EnumOperator.Equal, ""
					+ entity.getForshort());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORshort", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORshort",
					EnumOperator.GreaterThan, "" + (entity.getForshort() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORshort",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForshort() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORshort", EnumOperator.LessThan,
					"" + (entity.getForshort() + 1));
			assertEquals(true, retval);
			retval = matcher
					.match(entity, "FORshort", EnumOperator.LessThanEqual, ""
							+ (entity.getForshort() + 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORshort", EnumOperator.Between,
					new String[] { "" + (entity.getForshort() - 1),
							"" + (entity.getForshort() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORshort", EnumOperator.Between,
					new String[] { "" + (entity.getForshort() + 1),
							"" + (entity.getForshort() + 2) });
			assertEquals(false, retval);

			retval = matcher.match(entity, "FORShort", EnumOperator.Equal, ""
					+ entity.getForShort());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORShort", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORShort",
					EnumOperator.GreaterThan, "" + (entity.getForShort() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORShort",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForShort() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORShort", EnumOperator.LessThan,
					"" + (entity.getForShort() + 1));
			assertEquals(true, retval);
			retval = matcher
					.match(entity, "FORShort", EnumOperator.LessThanEqual, ""
							+ (entity.getForShort() + 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORShort", EnumOperator.Between,
					new String[] { "" + (entity.getForShort() - 1),
							"" + (entity.getForShort() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORShort", EnumOperator.Between,
					new String[] { "" + (entity.getForShort() + 1),
							"" + (entity.getForShort() + 2) });
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchEnum() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FOREnum", EnumOperator.Equal,
					"cancel");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FOREnum", EnumOperator.Equal,
					"none");
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchInt() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORint", EnumOperator.Equal, ""
					+ entity.getForint());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORint", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORint", EnumOperator.GreaterThan,
					"" + (entity.getForint() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORint",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForint() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORint", EnumOperator.LessThan, ""
					+ (entity.getForint() + 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORint",
					EnumOperator.LessThanEqual, "" + (entity.getForint() + 2));
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORint",
					EnumOperator.Between,
					new String[] { "" + (entity.getForint() - 1),
							"" + (entity.getForint() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORint",
					EnumOperator.Between,
					new String[] { "" + (entity.getForint() + 1),
							"" + (entity.getForint() + 2) });
			assertEquals(false, retval);

			retval = matcher.match(entity, "FORInt", EnumOperator.Equal, ""
					+ entity.getForInt());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORInt", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORInt", EnumOperator.GreaterThan,
					"" + (entity.getForInt() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORInt",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForInt() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORInt", EnumOperator.LessThan, ""
					+ (entity.getForInt() + 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORInt",
					EnumOperator.LessThanEqual, "" + (entity.getForInt() + 2));
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORInt",
					EnumOperator.Between,
					new String[] { "" + (entity.getForInt() - 1),
							"" + (entity.getForInt() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORInt",
					EnumOperator.Between,
					new String[] { "" + (entity.getForInt() + 1),
							"" + (entity.getForInt() + 2) });
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchLong() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORlong", EnumOperator.Equal, ""
					+ entity.getForlong());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORlong", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORlong", EnumOperator.GreaterThan,
					"" + (entity.getForlong() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORlong",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForlong() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORlong", EnumOperator.LessThan, ""
					+ (entity.getForlong() + 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORlong",
					EnumOperator.LessThanEqual, "" + (entity.getForlong() + 2));
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORlong",
					EnumOperator.Between,
					new String[] { "" + (entity.getForlong() - 1),
							"" + (entity.getForlong() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORlong",
					EnumOperator.Between,
					new String[] { "" + (entity.getForlong() + 1),
							"" + (entity.getForlong() + 2) });
			assertEquals(false, retval);

			retval = matcher.match(entity, "FORLong", EnumOperator.Equal, ""
					+ entity.getForLong());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORLong", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORLong", EnumOperator.GreaterThan,
					"" + (entity.getForLong() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORLong",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForLong() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORLong", EnumOperator.LessThan, ""
					+ (entity.getForLong() + 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORLong",
					EnumOperator.LessThanEqual, "" + (entity.getForLong() + 2));
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORLong",
					EnumOperator.Between,
					new String[] { "" + (entity.getForLong() - 1),
							"" + (entity.getForLong() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(
					entity,
					"FORLong",
					EnumOperator.Between,
					new String[] { "" + (entity.getForLong() + 1),
							"" + (entity.getForLong() + 2) });
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchFloat() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORfloat", EnumOperator.Equal, ""
					+ entity.getForfloat());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORfloat", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORfloat",
					EnumOperator.GreaterThan, "" + (entity.getForfloat() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORfloat",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForfloat() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORfloat", EnumOperator.LessThan,
					"" + (entity.getForfloat() + 1));
			assertEquals(true, retval);
			retval = matcher
					.match(entity, "FORfloat", EnumOperator.LessThanEqual, ""
							+ (entity.getForfloat() + 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORfloat", EnumOperator.Between,
					new String[] { "" + (entity.getForfloat() - 1),
							"" + (entity.getForfloat() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORfloat", EnumOperator.Between,
					new String[] { "" + (entity.getForfloat() + 1),
							"" + (entity.getForlong() + 2) });
			assertEquals(false, retval);

			retval = matcher.match(entity, "FORFloat", EnumOperator.Equal, ""
					+ entity.getForFloat());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORFloat", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORFloat",
					EnumOperator.GreaterThan, "" + (entity.getForFloat() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORFloat",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForFloat() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORFloat", EnumOperator.LessThan,
					"" + (entity.getForFloat() + 1));
			assertEquals(true, retval);
			retval = matcher
					.match(entity, "FORFloat", EnumOperator.LessThanEqual, ""
							+ (entity.getForFloat() + 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORFloat", EnumOperator.Between,
					new String[] { "" + (entity.getForFloat() - 1),
							"" + (entity.getForFloat() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORFloat", EnumOperator.Between,
					new String[] { "" + (entity.getForFloat() + 1),
							"" + (entity.getForFloat() + 2) });
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchDouble() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORdouble", EnumOperator.Equal, ""
					+ entity.getFordouble());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble",
					EnumOperator.GreaterThan, "" + (entity.getFordouble() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getFordouble() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble", EnumOperator.LessThan,
					"" + (entity.getFordouble() + 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble",
					EnumOperator.LessThanEqual, ""
							+ (entity.getFordouble() + 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble", EnumOperator.Between,
					new String[] { "" + (entity.getFordouble() - 1),
							"" + (entity.getFordouble() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORdouble", EnumOperator.Between,
					new String[] { "" + (entity.getFordouble() + 1),
							"" + (entity.getFordouble() + 2) });
			assertEquals(false, retval);

			retval = matcher.match(entity, "FORDouble", EnumOperator.Equal, ""
					+ entity.getForDouble());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble",
					EnumOperator.GreaterThan, "" + (entity.getForDouble() - 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble",
					EnumOperator.GreaterThanEqual, ""
							+ (entity.getForDouble() - 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble", EnumOperator.LessThan,
					"" + (entity.getForDouble() + 1));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble",
					EnumOperator.LessThanEqual, ""
							+ (entity.getForDouble() + 2));
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble", EnumOperator.Between,
					new String[] { "" + (entity.getForDouble() - 1),
							"" + (entity.getForDouble() + 1) });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORDouble", EnumOperator.Between,
					new String[] { "" + (entity.getForDouble() + 1),
							"" + (entity.getForDouble() + 2) });
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.ConditionMatcher#match(com.wookler.core.persistence.AbstractEntity, java.lang.String, com.wookler.core.persistence.query.EnumOperator, java.lang.Object)}
	 * .
	 */
	@Test
	public void testMatchChar() {
		try {
			EntityMatchRoot entity = new EntityMatchRoot();
			ConditionMatcher matcher = new ConditionMatcher();
			boolean retval = false;

			retval = matcher.match(entity, "FORchar", EnumOperator.Equal, ""
					+ entity.getForchar());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar", EnumOperator.NotEqual,
					"X");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar", EnumOperator.GreaterThan,
					"*");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar",
					EnumOperator.GreaterThanEqual, " ");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar", EnumOperator.LessThan,
					"Z");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar",
					EnumOperator.LessThanEqual, "B");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar", EnumOperator.Between,
					new String[] { " ", "B" });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORchar", EnumOperator.Between,
					new String[] { "a", "b" });
			assertEquals(false, retval);

			retval = matcher.match(entity, "FORChar", EnumOperator.Equal, ""
					+ entity.getForChar());
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar", EnumOperator.NotEqual,
					"" + 0);
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar", EnumOperator.GreaterThan,
					"A");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar",
					EnumOperator.GreaterThanEqual, " ");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar", EnumOperator.LessThan,
					"Z");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar",
					EnumOperator.LessThanEqual, "B");
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar", EnumOperator.Between,
					new String[] { "A", "Z" });
			assertEquals(true, retval);
			retval = matcher.match(entity, "FORChar", EnumOperator.Between,
					new String[] { "a", "n" });
			assertEquals(false, retval);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
}
