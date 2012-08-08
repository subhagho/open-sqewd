/**
 * 
 */
package com.sqewd.open.dal.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author subhagho
 * 
 */
public class InputWait {
	public static final boolean prompt() throws Exception {
		InputStreamReader inp = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(inp);

		System.out.println("Continue (Y/n) : ");

		String str = br.readLine();
		if (str.compareToIgnoreCase("n") == 0)
			return false;
		return true;
	}
}
