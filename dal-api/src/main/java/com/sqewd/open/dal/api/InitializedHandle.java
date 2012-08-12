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
import com.sqewd.open.dal.api.utils.ListParam;


/**
 * Interface represents a handle which can be pre-initialized and cached. Handle
 * instances must take care of synchronization if required as handles can be
 * shared across threads.
 * 
 * @author subhagho
 * 
 */
public interface InitializedHandle {
	/**
	 * Reference Key for the handle in the cache.
	 * 
	 * @return
	 */
	public String key();

	/**
	 * Initialize the handle using the specified configruation.
	 * 
	 * @param config
	 *            - Initialization configuration.
	 * @throws Exception
	 */
	public void init(ListParam param) throws Exception;

	/**
	 * Get the state of this instance handle.
	 * 
	 * @return
	 */
	public EnumInstanceState state();

	/**
	 * Dispose the Handle.
	 */
	public void dispose();
}
