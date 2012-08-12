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
package com.sqewd.open.dal.api.persistence;
public enum EnumEntityState {
	/**
	 * Newly created entity, hasn't yet been persisted.
	 */
	New,
	/**
	 * Memory instance of the entity has been updated. Not yet persisted.
	 */
	Updated,
	/**
	 * Entity has been deleted. Delete pending commit.
	 */
	Deleted,
	/**
	 * Entity loaded from the persistence store.
	 */
	Loaded,
	/**
	 * Force overwrite of data if timestamp conflict exists.
	 */
	Overwrite,
	/**
	 * Entity state unknown.
	 */
	Unknown;
}
