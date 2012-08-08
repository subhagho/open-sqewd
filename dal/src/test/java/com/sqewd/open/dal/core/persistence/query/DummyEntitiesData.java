/**
 * 
 */
package com.sqewd.open.dal.core.persistence.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqewd.open.dal.core.persistence.AbstractEntity;
import com.sqewd.open.dal.core.persistence.Attribute;
import com.sqewd.open.dal.core.persistence.Entity;

/**
 * @author subhagho
 * 
 */
public class DummyEntitiesData {
	public static enum TENUM {
		Ok, Cancel;
	}

	@Entity(recordset = "EMBED2")
	public static class EntityMatchEmbed2 extends AbstractEntity {
		@Attribute(name = "FORint")
		private int forint = -99999;

		@Attribute(name = "FORInt")
		private int forInt = -99999;

		@Attribute(name = "FORString")
		private String forString = "";

		/**
		 * @return the forint
		 */
		public int getForint() {
			return forint;
		}

		/**
		 * @param forint
		 *            the forint to set
		 */
		public void setForint(int forint) {
			this.forint = forint;
		}

		/**
		 * @return the forInt
		 */
		public int getForInt() {
			return forInt;
		}

		/**
		 * @param forInt
		 *            the forInt to set
		 */
		public void setForInt(int forInt) {
			this.forInt = forInt;
		}

		/**
		 * @return the forString
		 */
		public String getForString() {
			return forString;
		}

		/**
		 * @param forString
		 *            the forString to set
		 */
		public void setForString(String forString) {
			this.forString = forString;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "{" + forint + ", " + forInt + ", " + forString + "}";
		}
	}

	@Entity(recordset = "EMBED")
	public static class EntityMatchEmbed extends AbstractEntity {
		@Attribute(name = "FORshort")
		private short forshort = 128;
		@Attribute(name = "FORReference")
		private List<EntityMatchEmbed2> forReference = new ArrayList<EntityMatchEmbed2>();

		public EntityMatchEmbed() {
			EntityMatchEmbed2 ee = new EntityMatchEmbed2();
			ee.setForint(99999);
			forReference.add(ee);
			ee = new EntityMatchEmbed2();
			ee.setForint(11111);
			forReference.add(ee);
			ee = new EntityMatchEmbed2();
			forReference.add(ee);
		}

		/**
		 * @return the forshort
		 */
		public short getForshort() {
			return forshort;
		}

		/**
		 * @param forshort
		 *            the forshort to set
		 */
		public void setForshort(short forshort) {
			this.forshort = forshort;
		}

		/**
		 * @return the forReference
		 */
		public List<EntityMatchEmbed2> getForReference() {
			return forReference;
		}

		/**
		 * @param forReference
		 *            the forReference to set
		 */
		public void setForReference(List<EntityMatchEmbed2> forReference) {
			this.forReference = forReference;
		}

	}

	@Entity(recordset = "ROOT")
	public static class EntityMatchRoot extends AbstractEntity {
		@Attribute(name = "FORshort")
		private short forshort = -1;
		@Attribute(name = "FORShort")
		private Short forShort = 1;
		@Attribute(name = "FORint")
		private int forint = -99999;
		@Attribute(name = "FORInt")
		private Integer forInt = 99999;
		@Attribute(name = "FORlong")
		private long forlong = -99999999;
		@Attribute(name = "FORLong")
		private Long forLong = 99999999L;
		@Attribute(name = "FORfloat")
		private float forfloat = -99.99f;
		@Attribute(name = "FORFloat")
		private Float forFloat = 99.99f;
		@Attribute(name = "FORdouble")
		private double fordouble = -9999.99;
		@Attribute(name = "FORDouble")
		private Double forDouble = 9999.99;
		@Attribute(name = "FORchar")
		private char forchar = 'A';
		@Attribute(name = "FORChar")
		private Character forChar = 'B';
		@Attribute(name = "FORString")
		private String forString = "77380-10632";
		@Attribute(name = "FORDate")
		private Date forDate = new Date(System.currentTimeMillis());
		@Attribute(name = "FORStringContains")
		private List<String> forStringContains = new ArrayList<String>();
		@Attribute(name = "FORIntContains")
		private Integer[] forIntConaints = { 1, 2, 3, 4, 5, 6, 7 };
		@Attribute(name = "FORDoubleContains")
		private double[] forDoubleContains = { 99.09, 99.01, 99.02 };
		@Attribute(name = "FORReference")
		private EntityMatchEmbed forReference = new EntityMatchEmbed();
		@Attribute(name = "FORArrayReference")
		private EntityMatchEmbed2[] forArrayReference = new EntityMatchEmbed2[2];
		@Attribute(name = "FOREnum")
		private TENUM forEnum = TENUM.Cancel;
		
		public EntityMatchRoot() {
			forStringContains.add("STRING_0");
			forStringContains.add("STRING_1");
			forStringContains.add("STRING_2");

			EntityMatchEmbed2 ee = new EntityMatchEmbed2();
			ee.setForint(77777);
			forArrayReference[0] = ee;
			ee = new EntityMatchEmbed2();
			ee.setForint(88888);
			forArrayReference[1] = ee;
		}

		/**
		 * @return the forshort
		 */
		public short getForshort() {
			return forshort;
		}

		/**
		 * @param forshort
		 *            the forshort to set
		 */
		public void setForshort(short forshort) {
			this.forshort = forshort;
		}

		/**
		 * @return the forShort
		 */
		public Short getForShort() {
			return forShort;
		}

		/**
		 * @param forShort
		 *            the forShort to set
		 */
		public void setForShort(Short forShort) {
			this.forShort = forShort;
		}

		/**
		 * @return the forint
		 */
		public int getForint() {
			return forint;
		}

		/**
		 * @param forint
		 *            the forint to set
		 */
		public void setForint(int forint) {
			this.forint = forint;
		}

		/**
		 * @return the forInt
		 */
		public Integer getForInt() {
			return forInt;
		}

		/**
		 * @param forInt
		 *            the forInt to set
		 */
		public void setForInt(Integer forInt) {
			this.forInt = forInt;
		}

		/**
		 * @return the forlong
		 */
		public long getForlong() {
			return forlong;
		}

		/**
		 * @param forlong
		 *            the forlong to set
		 */
		public void setForlong(long forlong) {
			this.forlong = forlong;
		}

		/**
		 * @return the forLong
		 */
		public Long getForLong() {
			return forLong;
		}

		/**
		 * @param forLong
		 *            the forLong to set
		 */
		public void setForLong(Long forLong) {
			this.forLong = forLong;
		}

		/**
		 * @return the forfloat
		 */
		public float getForfloat() {
			return forfloat;
		}

		/**
		 * @param forfloat
		 *            the forfloat to set
		 */
		public void setForfloat(float forfloat) {
			this.forfloat = forfloat;
		}

		/**
		 * @return the forFloat
		 */
		public Float getForFloat() {
			return forFloat;
		}

		/**
		 * @param forFloat
		 *            the forFloat to set
		 */
		public void setForFloat(Float forFloat) {
			this.forFloat = forFloat;
		}

		/**
		 * @return the fordouble
		 */
		public double getFordouble() {
			return fordouble;
		}

		/**
		 * @param fordouble
		 *            the fordouble to set
		 */
		public void setFordouble(double fordouble) {
			this.fordouble = fordouble;
		}

		/**
		 * @return the forDouble
		 */
		public Double getForDouble() {
			return forDouble;
		}

		/**
		 * @param forDouble
		 *            the forDouble to set
		 */
		public void setForDouble(Double forDouble) {
			this.forDouble = forDouble;
		}

		/**
		 * @return the forchar
		 */
		public char getForchar() {
			return forchar;
		}

		/**
		 * @param forchar
		 *            the forchar to set
		 */
		public void setForchar(char forchar) {
			this.forchar = forchar;
		}

		/**
		 * @return the forChar
		 */
		public Character getForChar() {
			return forChar;
		}

		/**
		 * @param forChar
		 *            the forChar to set
		 */
		public void setForChar(Character forChar) {
			this.forChar = forChar;
		}

		/**
		 * @return the forString
		 */
		public String getForString() {
			return forString;
		}

		/**
		 * @param forString
		 *            the forString to set
		 */
		public void setForString(String forString) {
			this.forString = forString;
		}

		/**
		 * @return the forStringContains
		 */
		public List<String> getForStringContains() {
			return forStringContains;
		}

		/**
		 * @param forStringContains
		 *            the forStringContains to set
		 */
		public void setForStringContains(List<String> forStringContains) {
			this.forStringContains = forStringContains;
		}

		/**
		 * @return the forIntConaints
		 */
		public Integer[] getForIntConaints() {
			return forIntConaints;
		}

		/**
		 * @param forIntConaints
		 *            the forIntConaints to set
		 */
		public void setForIntConaints(Integer[] forIntConaints) {
			this.forIntConaints = forIntConaints;
		}

		/**
		 * @return the forDoubleContains
		 */
		public double[] getForDoubleContains() {
			return forDoubleContains;
		}

		/**
		 * @param forDoubleContains
		 *            the forDoubleContains to set
		 */
		public void setForDoubleContains(double[] forDoubleContains) {
			this.forDoubleContains = forDoubleContains;
		}

		/**
		 * @return the forReference
		 */
		public EntityMatchEmbed getForReference() {
			return forReference;
		}

		/**
		 * @param forReference
		 *            the forReference to set
		 */
		public void setForReference(EntityMatchEmbed forReference) {
			this.forReference = forReference;
		}

		/**
		 * @return the forDate
		 */
		public Date getForDate() {
			return forDate;
		}

		/**
		 * @param forDate
		 *            the forDate to set
		 */
		public void setForDate(Date forDate) {
			this.forDate = forDate;
		}

		/**
		 * @return the forArrayReference
		 */
		public EntityMatchEmbed2[] getForArrayReference() {
			return forArrayReference;
		}

		/**
		 * @param forArrayReference
		 *            the forArrayReference to set
		 */
		public void setForArrayReference(EntityMatchEmbed2[] forArrayReference) {
			this.forArrayReference = forArrayReference;
		}

		/**
		 * @return the forEnum
		 */
		public TENUM getForEnum() {
			return forEnum;
		}

		/**
		 * @param forEnum the forEnum to set
		 */
		public void setForEnum(TENUM forEnum) {
			this.forEnum = forEnum;
		}

	}

}
