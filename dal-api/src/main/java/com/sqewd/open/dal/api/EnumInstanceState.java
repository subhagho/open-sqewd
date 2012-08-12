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
package com.sqewd.open.dal.api;
public enum EnumInstanceState {
	/**
	 * Instance has been initialized
	 */
	Initialized,
	/**
	 * Instance is running and functioning normally.
	 */
	Running,
	/**
	 * Instance raised an exception and terminated.
	 */
	Exception,
	/**
	 * Instance handle has been closed/disposed.
	 */
	Closed,
	/**
	 * State Unknown.
	 */
	Unknown
}
