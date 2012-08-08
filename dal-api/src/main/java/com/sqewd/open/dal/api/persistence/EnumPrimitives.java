/**
 * 
 */
package com.sqewd.open.dal.api.persistence;


/**
 * Enumerates the java primitive types.
 * 
 * @author subhagho
 * 
 */
public enum EnumPrimitives {
	EShort, EInteger, ELong, EFloat, EDouble, EByte, ECharacter;

	/**
	 * Check is type is a Primitive datatype. Note: This is required because the
	 * standard Class.isPrimitive() method considers class instances of
	 * primitive types as Class types.
	 * 
	 * @param type
	 *            - Class type.
	 * @return
	 */
	public static boolean isPrimitiveType(Class<?> type) {
		if (type.equals(Short.class) || type.equals(Integer.class)
				|| type.equals(Long.class) || type.equals(Float.class)
				|| type.equals(Double.class) || type.equals(Boolean.class)
				|| type.equals(Byte.class) || type.equals(Character.class))
			return true;
		return type.isPrimitive();
	}

	/**
	 * Get the enum for the specified type, if type is primitive.
	 * 
	 * @param type
	 * @return
	 */
	public static EnumPrimitives type(Class<?> type) {
		if (type.equals(short.class) || type.equals(Short.class))
			return EShort;
		if (type.equals(int.class) || type.equals(Integer.class))
			return EInteger;
		if (type.equals(long.class) || type.equals(Long.class))
			return ELong;
		if (type.equals(float.class) || type.equals(Float.class))
			return EFloat;
		if (type.equals(double.class) || type.equals(Double.class))
			return EDouble;
		if (type.equals(char.class) || type.equals(Character.class))
			return ECharacter;
		if (type.equals(byte.class) || type.equals(Byte.class))
			return EByte;

		return null;
	}
}
