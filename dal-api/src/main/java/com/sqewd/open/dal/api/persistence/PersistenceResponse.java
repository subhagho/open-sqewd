/**
 * Copyright 2012 Subho Ghosh (subho.ghosh at outlook dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @filename PersistenceResponse.java
 * @created Aug 25, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.persistence;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Structure is used to record persistence response.
 * 
 * @author subhagho
 * 
 */
public class PersistenceResponse {
	@JsonProperty(value = "statuses")
	private HashMap<String, OperationResponse> responses = new HashMap<String, OperationResponse>();

	/**
	 * @return the responses
	 */
	public HashMap<String, OperationResponse> getResponses() {
		return responses;
	}

	/**
	 * @param responses
	 *            the responses to set
	 */
	public void setResponses(HashMap<String, OperationResponse> responses) {
		this.responses = responses;
	}

	public void add(OperationResponse response) {
		String key = response.getHashKey();
		if (responses.containsKey(key)) {
			responses.remove(key);
		}
		responses.put(key, response);
	}
}
