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
package com.sqewd.open.dal.api.utils;

import java.io.File;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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

	public static void validate(String xmlfile, String schemaf)
			throws Exception {
		Source xmlsrc = new StreamSource(new File(xmlfile));

		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(XMLUtils.class.getResource(schemaf));
		Validator validator = schema.newValidator();
		validator.validate(xmlsrc);
	}

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
