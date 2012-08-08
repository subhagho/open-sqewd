/**
 * 
 */
package com.sqewd.open.dal.utils;

import java.util.Date;

/**
 * @author subhagho
 * 
 */
public class AdHocTests {
	static enum TestEnum {
		One, Two, Three
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			System.out.println("DATE:" + new Date().getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
