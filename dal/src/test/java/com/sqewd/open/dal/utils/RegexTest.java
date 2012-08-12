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
