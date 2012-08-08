/**
 * 
 */
package com.sqewd.open.dal.api.utils;

/**
 * Simple Key/Value pair.
 * 
 * Key is always a String. Value can be anything.
 * 
 * @author subhagho
 * 
 */
public class KeyValuePair<T> {
	private String key;
	private T value;

	public KeyValuePair() {
	}

	public KeyValuePair(String key, T value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}
}
