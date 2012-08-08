/**
 * 
 */
package com.sqewd.open.dal.api.utils;

import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author subhagho
 * 
 */
public class XMLUtils {
	public static final String _PARAM_NODE_PARAMS_ = "./params/param";
	public static final String _PARAM_ATTR_NAME_ = "name";
	public static final String _PARAM_ATTR_VALUE_ = "value";
	public static final String _PARAM_ATTR_TYPE_ = "type";

	private static XPath xpath = XPathFactory.newInstance().newXPath();

	/**
	 * Get the XML Nodes for the specified XPath.
	 * 
	 * @param path
	 *            - XPath String
	 * @param parent
	 *            - Parent Node.
	 * @return
	 * @throws Exception
	 */
	public static NodeList search(String path, Element parent) throws Exception {
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(path);
		return (NodeList) expr.evaluate(parent, XPathConstants.NODESET);
	}

	/**
	 * Extract the parameters specified in the XML document for the Element
	 * specified by parent.
	 * 
	 * @param parent
	 *            - Parent Node.
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Object> parseParams(Element parent)
			throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(_PARAM_NODE_PARAMS_);

		NodeList nodes = (NodeList) expr.evaluate(parent,
				XPathConstants.NODESET);
		if (nodes != null && nodes.getLength() > 0) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			for (int ii = 0; ii < nodes.getLength(); ii++) {
				Element elm = (Element) nodes.item(ii);
				String name = elm.getAttribute(_PARAM_ATTR_NAME_);
				String value = elm.getAttribute(_PARAM_ATTR_VALUE_);
				if (name != null && !name.isEmpty()) {
					params.put(name, value);
				}
			}
			return params;
		}
		return null;
	}
}
