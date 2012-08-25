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
 * @filename Timer.java
 * @created Aug 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.utils;

/**
 * @author subhagho
 * 
 * 
 */
public class Timer {
	private long startime = System.currentTimeMillis();

	public long start() {
		startime = System.currentTimeMillis();
		return startime;
	}

	public long stop() {
		long endtime = System.currentTimeMillis();
		return endtime - startime;
	}
}
