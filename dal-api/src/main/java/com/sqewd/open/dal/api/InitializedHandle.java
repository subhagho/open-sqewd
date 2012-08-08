/**
 * 
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
