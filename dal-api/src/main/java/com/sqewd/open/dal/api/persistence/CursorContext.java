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
 * @filename CursorContext.java
 * @created Oct 20, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.api.persistence;

/**
 * Class represents a cursor context for data fetch requests. Cursor Context is
 * used for distributed joins only.
 * 
 * @author subhagho
 * 
 */
public class CursorContext {
	private String id;

	private boolean keepopen;

	private int offset;

	private int batchsize; // -ve number indicates fetch all.

	private boolean debug;

	/**
	 * Create a new Cursor Context with the specified ID and batch size. Batch
	 * Size cannot be changed once set.
	 * 
	 * @param id
	 *            - Cursor ID
	 * @param batchsize
	 *            - Fetch Batch size.
	 */
	public CursorContext(final String id, final int batchsize) {
		this.id = id;
		this.batchsize = batchsize;
	}

	/**
	 * @return the batchsize
	 */
	public int getBatchsize() {
		return batchsize;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the keepopen
	 */
	public boolean isKeepopen() {
		return keepopen;
	}

	/**
	 * @param debug
	 *            the debug to set
	 */
	public void setDebug(final boolean debug) {
		this.debug = debug;
	}

	/**
	 * @param keepopen
	 *            the keepopen to set
	 */
	public void setKeepopen(final boolean keepopen) {
		this.keepopen = keepopen;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(final int offset) {
		this.offset = offset;
	}
}
