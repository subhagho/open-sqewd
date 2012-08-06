/**
 * 
 */
package com.sqewd.open.dal.utils;

import org.w3c.dom.Element;


/**
 * @author subhagho
 * 
 */
public abstract class AbstractParam {
	protected String key;

	protected EnumParamType type = EnumParamType.Null;

	/**
	 * Get the parameter type.
	 * 
	 * @return
	 */
	public EnumParamType getType() {
		return type;
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
	 * Create an instance of this parameter based on the XML definition.
	 * 
	 * @param node
	 *            - XML element.
	 * @throws Exception
	 */
	public abstract void parse(Element node) throws Exception;
}
