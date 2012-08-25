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
 * @filename DALResponse.java
 * @created Aug 23, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.services;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Service response wrapper.
 * 
 * @author subhagho
 * 
 */
public class DALResponse {
	@JsonProperty(value = "state")
	private EnumResponseState state = EnumResponseState.Success;

	@JsonProperty(value = "message")
	private String message = null;

	@JsonProperty(value = "request")
	private String request = null;

	@JsonProperty(value = "data")
	private Object data = null;

	@JsonProperty(value = "time-taken")
	private long timetaken = 0;

	/**
	 * @return the state
	 */
	public EnumResponseState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(EnumResponseState state) {
		this.state = state;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the timetaken
	 */
	public long getTimetaken() {
		return timetaken;
	}

	/**
	 * @param timetaken
	 *            the timetaken to set
	 */
	public void setTimetaken(long timetaken) {
		this.timetaken = timetaken;
	}

}