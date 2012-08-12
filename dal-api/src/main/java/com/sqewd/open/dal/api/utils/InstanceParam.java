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
import org.w3c.dom.Element;


/**
 * @author subhagho
 * 
 */
public class InstanceParam extends AbstractParam {
	public static final String _PARAM_ATTR_CLASS_ = "class";
	public static final String _PARAM_NODE_PARAMS_ = "./params/param";

	private String classname;
	private ListParam params;

	public InstanceParam() {
		type = EnumParamType.Instance;
	}

	public InstanceParam(String key, String classname) {
		type = EnumParamType.Instance;
		this.key = key;
		this.classname = classname;
	}

	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @param classname
	 *            the classname to set
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * @return the params
	 */
	public ListParam getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(ListParam params) {
		this.params = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wookler.utils.AbstractParam#parse(org.w3c.dom.Element)
	 */
	@Override
	public void parse(Element node) throws Exception {
		params = new ListParam("params", _PARAM_NODE_PARAMS_);
		params.parse(node);
		if (params.getSize() <= 0)
			params = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("{INSTANCE: class=" + classname + " key=" + key + "\n");
		if (params != null)
			buff.append(params.toString());
		buff.append("}\n");
		return buff.toString();
	}

}
