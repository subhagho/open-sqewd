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
 * @filename EntityClassLoader.java
 * @created Aug 24, 2012
 * @author subhagho
 *
 */
package com.sqewd.open.dal.core.reflect;

import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sqewd.open.dal.api.persistence.Attribute;
import com.sqewd.open.dal.api.persistence.Entity;

/**
 * @author subhagho
 * 
 *         TODO: <comment>
 * 
 */
public class EntityClassLoader extends ClassLoader {
	private ClassPool classpool = ClassPool.getDefault();

	public EntityClassLoader(ClassLoader parent) {
		super(parent);
	}

	public EntityClassLoader() {
		super(EntityClassLoader.class.getClassLoader());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String arg0) throws ClassNotFoundException {
		Class<?> cls = findLoadedClass(arg0);
		if (cls != null)
			return cls;

		try {
			cls = process(arg0);
			if (cls != null)
				return cls;
		} catch (Exception e) {
			// throw new ClassNotFoundException("Failed to load class...", e);
		}

		return null;
	}

	public Class<?> process(String classname) throws Exception {
		CtClass cc = classpool.getCtClass(classname);
		if (cc != null) {
			Object[] annots = cc.getAnnotations();
			if (annots != null && annots.length > 0) {
				Entity entity = null;
				for (Object annot : annots) {
					if (annot instanceof Entity) {
						entity = (Entity) annot;
					} else if (annot instanceof JsonRootName) {
						entity = null;
					}
				}
				if (entity != null) {
					System.out.println("Adding annotations to [" + classname
							+ "]");
					addClassAnnotation(cc, entity);
					// byte[] b = cc.toBytecode();
					// return defineClass(classname, b, 0, b.length);
					return cc.toClass();
				}
			}
		}
		return null;
	}

	private void addClassAnnotation(CtClass cc, Entity entity) throws Exception {
		String name = entity.recordset();
		ClassFile ccf = cc.getClassFile();
		ConstPool constp = ccf.getConstPool();

		AnnotationsAttribute attr = null;

		@SuppressWarnings("unchecked")
		List<AttributeInfo> attrs = ccf.getAttributes();
		if (attrs != null && attrs.size() > 0) {
			for (AttributeInfo ai : attrs) {
				if (ai instanceof AnnotationsAttribute) {
					attr = (AnnotationsAttribute) ai;
					break;
				}
			}
		}
		if (attr == null)
			attr = new AnnotationsAttribute(constp,
					AnnotationsAttribute.visibleTag);

		// JsonProperty
		Annotation attroot = new Annotation(
				JsonRootName.class.getCanonicalName(), constp);
		attroot.addMemberValue("value", new StringMemberValue(name, constp));
		attr.addAnnotation(attroot);

		cc.getClassFile().addAttribute(attr);

		// Annotate members
		CtField[] fields = cc.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (CtField fd : fields) {
				Object[] annots = fd.getAnnotations();
				if (annots != null && annots.length > 0) {
					Attribute attran = null;
					boolean ignore = true;
					for (Object annot : annots) {
						if (annot instanceof Attribute) {
							attran = (Attribute) annot;
							ignore = false;
						} else if (annot instanceof JsonProperty) {
							attran = null;
						}
					}
					if (attran != null) {
						addFieldIncludeAnnotation(cc, fd, attran, constp);
					} else {
						if (ignore) {
							addFieldIgnoreAnnotation(cc, fd, constp);
						}
					}
				}
			}
		}
	}

	private static void addFieldIgnoreAnnotation(CtClass cc, CtField fd,
			ConstPool constp) throws Exception {
		AnnotationsAttribute attran = null;

		@SuppressWarnings("unchecked")
		List<AttributeInfo> attrs = fd.getFieldInfo().getAttributes();
		if (attrs != null && attrs.size() > 0) {
			for (AttributeInfo ai : attrs) {
				if (ai instanceof AnnotationsAttribute) {
					attran = (AnnotationsAttribute) ai;
					break;
				}
			}
		}
		if (attran == null)
			attran = new AnnotationsAttribute(constp,
					AnnotationsAttribute.visibleTag);

		// JsonProperty
		Annotation attroot = new Annotation(
				JsonIgnore.class.getCanonicalName(), constp);
		attran.addAnnotation(attroot);

		fd.getFieldInfo().addAttribute(attran);
	}

	private static void addFieldIncludeAnnotation(CtClass cc, CtField fd,
			Attribute attr, ConstPool constp) throws Exception {
		AnnotationsAttribute attran = null;

		@SuppressWarnings("unchecked")
		List<AttributeInfo> attrs = fd.getFieldInfo().getAttributes();
		if (attrs != null && attrs.size() > 0) {
			for (AttributeInfo ai : attrs) {
				if (ai instanceof AnnotationsAttribute) {
					attran = (AnnotationsAttribute) ai;
					break;
				}
			}
		}
		if (attran == null)
			attran = new AnnotationsAttribute(constp,
					AnnotationsAttribute.visibleTag);

		String name = attr.name();

		// JsonProperty
		Annotation attroot = new Annotation(
				JsonProperty.class.getCanonicalName(), constp);
		attroot.addMemberValue("value", new StringMemberValue(name, constp));
		attran.addAnnotation(attroot);

		fd.getFieldInfo().addAttribute(attran);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> cls = findClass(name);
		if (cls != null)
			return cls;
		return super.loadClass(name);
	}
}
