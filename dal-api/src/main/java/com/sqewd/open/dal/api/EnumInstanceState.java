/**
 * 
 */
package com.sqewd.open.dal.api;

/**
 * Enum listing the states of an instance handle.
 * 
 * @author subhagho
 *
 */
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
