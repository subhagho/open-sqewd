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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class ListParam extends AbstractParam {
	private String paramNodeValues = "./values/value";

	private HashMap<String, AbstractParam> values = new HashMap<String, AbstractParam>();

	public ListParam() {
		type = EnumParamType.List;
	}

	public ListParam(String key) {
		type = EnumParamType.List;
		this.key = key;
	}

	public ListParam(String key, String nodeparam) {
		type = EnumParamType.List;
		this.key = key;
		this.paramNodeValues = nodeparam;
	}

	/**
	 * Add a value to the the list.
	 * 
	 * @param value
	 */
	public void add(AbstractParam value) {
		values.put(value.key, value);
	}

	/**
	 * Get all the values as a List.
	 * 
	 * @return
	 */
	public List<AbstractParam> values() {
		List<AbstractParam> vs = new ArrayList<AbstractParam>();
		for (String ky : values.keySet()) {
			vs.add(values.get(ky));
		}
		return vs;
	}

	/**
	 * Get the size of the List.
	 * 
	 * @return
	 */
	public int getSize() {
		return values.size();
	}

	/**
	 * Get an element from the list specified by the name.
	 * 
	 * @param name
	 *            - Key to search for.
	 * @return
	 */
	public AbstractParam get(String name) {
		if (values.containsKey(name)) {
			return values.get(name);
		}
		return null;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(HashMap<String, AbstractParam> values) {
		this.values = values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.utils.AbstractParam#parse(org.w3c.dom.Element)
	 */
	@Override
	public void parse(Element node) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(paramNodeValues);

		NodeList nodes = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
		if (nodes != null && nodes.getLength() > 0) {
			for (int ii = 0; ii < nodes.getLength(); ii++) {
				Element elm = (Element) nodes.item(ii);
				String type = elm.getAttribute(XMLUtils._PARAM_ATTR_TYPE_);
				if (type == null || type.isEmpty())
					throw new Exception(
							"Invalid Parameter : Missing attribute ["
									+ XMLUtils._PARAM_ATTR_TYPE_ + "]");
				EnumParamType pt = EnumParamType.valueOf(type);
				if (pt == null)
					throw new Exception(
							"Invalid Parameter : Cannot find type [" + type
									+ "]");
				if (pt == EnumParamType.Value) {
					String vk = elm.getAttribute(XMLUtils._PARAM_ATTR_NAME_);
					if (vk == null || vk.isEmpty()) {
						vk = "param_k" + ii;
					}
					String vv = elm.getAttribute(XMLUtils._PARAM_ATTR_VALUE_);
					ValueParam vp = new ValueParam(vk, vv);
					values.put(vk, vp);
				} else if (pt == EnumParamType.List) {
					String vk = elm.getAttribute(XMLUtils._PARAM_ATTR_NAME_);
					if (vk == null || vk.isEmpty()) {
						vk = "param_k" + ii;
					}
					ListParam lp = new ListParam(vk);
					lp.parse(elm);
					values.put(vk, lp);
				} else if (pt == EnumParamType.Instance) {
					String vk = elm.getAttribute(XMLUtils._PARAM_ATTR_NAME_);
					if (vk == null || vk.isEmpty()) {
						vk = "param_k" + ii;
					}
					String cn = elm
							.getAttribute(InstanceParam._PARAM_ATTR_CLASS_);
					if (cn == null || cn.isEmpty()) {
						throw new Exception(
								"Invalid Parameter : Missing attribute ["
										+ InstanceParam._PARAM_ATTR_CLASS_
										+ "]");
					}
					InstanceParam ip = new InstanceParam(vk, cn);
					ip.parse(elm);
					values.put(vk, ip);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("{LIST: size=" + values.size() + " key=" + key + "\n");
		for (String ks : values.keySet()) {
			AbstractParam ap = values.get(ks);
			buff.append(ap.toString());
		}
		buff.append("}\n");
		return buff.toString();
	}
}
