/**
 * 
 */
package com.sqewd.open.dal.api.persistence;


/**
 * Structure encapsulates the Entity Reference parameters extracted from the
 * annotation using reflection.
 * 
 * @author subhagho
 * 
 */
public class ReferenceReflection {
	public String Class;
	public String Field;
	public EnumRefereceType Type = EnumRefereceType.One2One;
}
