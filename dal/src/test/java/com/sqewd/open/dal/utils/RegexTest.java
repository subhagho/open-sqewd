/**
 * 
 */
package com.sqewd.open.dal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author subhagho
 * 
 */
public class RegexTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String condition = "Sort   32 asc, Col1, Col2 dsc";

			String qr = "(?i)(sort)\\s+(.*)(,.*)?";
			Pattern pattern = Pattern.compile(qr);
			Matcher matcher = pattern.matcher(condition);
			while (matcher.find()) {

				// for (int i = 0; i <= matcher.groupCount(); i++) {
				// System.out.println(matcher.group(i));
				// }
				String[] cols = matcher.group(2).split(",");
				for (String col : cols) {
					String[] parts = col.trim().split("\\s+");
					for (String pp : parts)
						System.out.println(pp);
				}
			}
			// System.out.println(matcher.appendTail(out).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
