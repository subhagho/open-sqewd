/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqewd.open.dal.core.persistence.query.DummyEntitiesData.EntityMatchEmbed2;
import com.sqewd.open.dal.utils.LogUtils;

/**
 * @author subhagho
 * 
 */
public class Test_EntityListSorter {
	private static final Logger log = LoggerFactory
			.getLogger(Test_EntityListSorter.class);

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.EntityListSorter#sort(java.util.List)}
	 * .
	 */
	@Test
	public void testSort() {
		try {
			List<SortColumn> columns = new ArrayList<SortColumn>();

			SortColumn column = new SortColumn();
			column.setColumn("FORint");
			columns.add(column);

			column = new SortColumn();
			column.setColumn("FORInt");
			column.setOrder(EnumSortOrder.ASC);
			columns.add(column);

			column = new SortColumn();
			column.setColumn("FORString");
			column.setOrder(EnumSortOrder.ASC);
			columns.add(column);

			EntityListSorter sorter = new EntityListSorter(columns);

			List<EntityMatchEmbed2> entities = new ArrayList<DummyEntitiesData.EntityMatchEmbed2>();
			EntityMatchEmbed2 ent = new EntityMatchEmbed2();
			ent.setForint(0);
			ent.setForInt(3);
			entities.add(ent);

			ent = new EntityMatchEmbed2();
			ent.setForint(0);
			ent.setForInt(5);
			entities.add(ent);

			ent = new EntityMatchEmbed2();
			ent.setForint(1);
			ent.setForInt(1);
			entities.add(ent);

			ent = new EntityMatchEmbed2();
			ent.setForint(1);
			ent.setForInt(2);
			ent.setForString("AAA");
			entities.add(ent);

			ent = new EntityMatchEmbed2();
			ent.setForint(2);
			ent.setForInt(1);
			ent.setForString("ZZZ");
			entities.add(ent);

			ent = new EntityMatchEmbed2();
			ent.setForint(2);
			ent.setForInt(1);
			ent.setForString("ABA");
			entities.add(ent);

			ent = new EntityMatchEmbed2();
			ent.setForint(2);
			ent.setForInt(1);
			ent.setForString("AAA");
			entities.add(ent);

			sorter.sort(entities);

			for (EntityMatchEmbed2 en : entities) {
				System.out.println(en.toString());
			}
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.wookler.core.persistence.query.EntityListSorter#compare(com.wookler.core.persistence.AbstractEntity, com.wookler.core.persistence.AbstractEntity)}
	 * .
	 */
	@Test
	public void testCompareAbstractEntityAbstractEntity() {
		try {
			EntityMatchEmbed2 esrc = new EntityMatchEmbed2();
			esrc.setForint(0);
			esrc.setForInt(3);

			EntityMatchEmbed2 etgt = new EntityMatchEmbed2();
			etgt.setForint(1);
			etgt.setForInt(2);

			SortColumn column = new SortColumn();
			column.setColumn("FORint");
			List<SortColumn> columns = new ArrayList<SortColumn>();
			columns.add(column);

			EntityListSorter sorter = new EntityListSorter(columns);
			int retval = sorter.compare(esrc, etgt);
			assertEquals(true, (retval > 0));

			column = new SortColumn();
			column.setColumn("FORInt");
			columns = new ArrayList<SortColumn>();
			columns.add(column);

			sorter = new EntityListSorter(columns);
			retval = sorter.compare(esrc, etgt);
			assertEquals(false, (retval > 0));

			retval = sorter.compare(esrc, esrc);
			assertEquals(true, (retval == 0));
		} catch (Exception e) {
			LogUtils.stacktrace(log, e);
			fail(e.getLocalizedMessage());
		}
	}

}
