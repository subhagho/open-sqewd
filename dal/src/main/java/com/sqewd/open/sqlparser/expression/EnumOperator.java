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
 * @filename EnumOperator.java
 * @created Sep 27, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.sqlparser.expression;

/**
 * Enum represents the operator in a Binary Expression.
 * 
 * @author subhagho
 * 
 */
public enum EnumOperator {
	Between, EqualsTo, Exists, GreaterThan, GreaterThanEquals, In, IsNull, Like, MinorThan, MinorThanEquals, NotEquals, And, Or, Plus, Minus, BitAnd, BitOr, BitXor, Concat, Divide, Multiply;
}
