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
 * @filename EnumJoinType.java
 * @created Aug 26, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.persistence;

/**
 * Enum defines the Join types for Entities declared as Joined.
 * 
 * @author subhagho
 * 
 */
public enum EnumJoinType {
	/**
	 * All reference entities are provisioned by the same data source driver.
	 */
	Native,
	/**
	 * Entity(s) referenced across data source drivers.
	 */
	Virtual,
	/**
	 * Join type not parsed yet.
	 */
	Unknown
}
