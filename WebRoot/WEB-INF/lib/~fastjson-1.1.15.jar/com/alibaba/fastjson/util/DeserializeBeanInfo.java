// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DeserializeBeanInfo.java

package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import java.lang.reflect.*;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.util:
//			FieldInfo

public class DeserializeBeanInfo
{

	private final Class clazz;
	private final Type type;
	private Constructor defaultConstructor;
	private Constructor creatorConstructor;
	private Method factoryMethod;
	private final List fieldList = new ArrayList();

	public DeserializeBeanInfo(Class clazz)
	{
		this.clazz = clazz;
		type = clazz;
	}

	public Constructor getDefaultConstructor()
	{
		return defaultConstructor;
	}

	public void setDefaultConstructor(Constructor defaultConstructor)
	{
		this.defaultConstructor = defaultConstructor;
	}

	public Constructor getCreatorConstructor()
	{
		return creatorConstructor;
	}

	public void setCreatorConstructor(Constructor createConstructor)
	{
		creatorConstructor = createConstructor;
	}

	public Method getFactoryMethod()
	{
		return factoryMethod;
	}

	public void setFactoryMethod(Method factoryMethod)
	{
		this.factoryMethod = factoryMethod;
	}

	public Class getClazz()
	{
		return clazz;
	}

	public Type getType()
	{
		return type;
	}

	public List getFieldList()
	{
		return fieldList;
	}

	public static DeserializeBeanInfo computeSetters(Class clazz)
	{
		DeserializeBeanInfo beanInfo = new DeserializeBeanInfo(clazz);
		Constructor defaultConstructor = getDefaultConstructor(clazz);
		if (defaultConstructor != null)
		{
			defaultConstructor.setAccessible(true);
			beanInfo.setDefaultConstructor(defaultConstructor);
		} else
		if (defaultConstructor == null && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()))
		{
			Constructor creatorConstructor = getCreatorConstructor(clazz);
			if (creatorConstructor != null)
			{
				creatorConstructor.setAccessible(true);
				beanInfo.setCreatorConstructor(creatorConstructor);
				for (int i = 0; i < creatorConstructor.getParameterTypes().length; i++)
				{
					java.lang.annotation.Annotation paramAnnotations[] = creatorConstructor.getParameterAnnotations()[i];
					JSONField fieldAnnotation = null;
					java.lang.annotation.Annotation arr$[] = paramAnnotations;
					int len$ = arr$.length;
					int i$ = 0;
					do
					{
						if (i$ >= len$)
							break;
						java.lang.annotation.Annotation paramAnnotation = arr$[i$];
						if (paramAnnotation instanceof JSONField)
						{
							fieldAnnotation = (JSONField)paramAnnotation;
							break;
						}
						i$++;
					} while (true);
					if (fieldAnnotation == null)
						throw new JSONException("illegal json creator");
					Class fieldClass = creatorConstructor.getParameterTypes()[i];
					Type fieldType = creatorConstructor.getGenericParameterTypes()[i];
					Field field = getField(clazz, fieldAnnotation.name());
					if (field != null)
						field.setAccessible(true);
					FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name(), clazz, fieldClass, fieldType, null, field);
					beanInfo.getFieldList().add(fieldInfo);
				}

				return beanInfo;
			}
			Method factoryMethod = getFactoryMethod(clazz);
			if (factoryMethod != null)
			{
				factoryMethod.setAccessible(true);
				beanInfo.setFactoryMethod(factoryMethod);
				for (int i = 0; i < factoryMethod.getParameterTypes().length; i++)
				{
					java.lang.annotation.Annotation paramAnnotations[] = factoryMethod.getParameterAnnotations()[i];
					JSONField fieldAnnotation = null;
					java.lang.annotation.Annotation arr$[] = paramAnnotations;
					int len$ = arr$.length;
					int i$ = 0;
					do
					{
						if (i$ >= len$)
							break;
						java.lang.annotation.Annotation paramAnnotation = arr$[i$];
						if (paramAnnotation instanceof JSONField)
						{
							fieldAnnotation = (JSONField)paramAnnotation;
							break;
						}
						i$++;
					} while (true);
					if (fieldAnnotation == null)
						throw new JSONException("illegal json creator");
					Class fieldClass = factoryMethod.getParameterTypes()[i];
					Type fieldType = factoryMethod.getGenericParameterTypes()[i];
					Field field = getField(clazz, fieldAnnotation.name());
					if (field != null)
						field.setAccessible(true);
					FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name(), clazz, fieldClass, fieldType, null, field);
					beanInfo.getFieldList().add(fieldInfo);
				}

				return beanInfo;
			} else
			{
				throw new JSONException((new StringBuilder()).append("default constructor not found. ").append(clazz).toString());
			}
		}
		Method arr$[] = clazz.getMethods();
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Method method = arr$[i$];
			String methodName = method.getName();
			if (methodName.length() < 4 || Modifier.isStatic(method.getModifiers()) || !method.getReturnType().equals(Void.TYPE) || method.getParameterTypes().length != 1)
				continue;
			JSONField annotation = (JSONField)method.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
			String propertyName;
			if (annotation != null)
			{
				if (!annotation.deserialize())
					continue;
				if (annotation.name().length() != 0)
				{
					propertyName = annotation.name();
					beanInfo.getFieldList().add(new FieldInfo(propertyName, method, null));
					method.setAccessible(true);
					continue;
				}
			}
			if (!methodName.startsWith("set") || !Character.isUpperCase(methodName.charAt(3)))
				continue;
			propertyName = (new StringBuilder()).append(Character.toLowerCase(methodName.charAt(3))).append(methodName.substring(4)).toString();
			Field field = getField(clazz, propertyName);
			if (field != null)
			{
				JSONField fieldAnnotation = (JSONField)field.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
				if (fieldAnnotation != null && fieldAnnotation.name().length() != 0)
				{
					propertyName = fieldAnnotation.name();
					beanInfo.getFieldList().add(new FieldInfo(propertyName, method, field));
					continue;
				}
			}
			beanInfo.getFieldList().add(new FieldInfo(propertyName, method, null));
			method.setAccessible(true);
		}

		arr$ = clazz.getFields();
		len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Field field = arr$[i$];
			if (Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers()))
				continue;
			boolean contains = false;
			Iterator i$ = beanInfo.getFieldList().iterator();
			do
			{
				if (!i$.hasNext())
					break;
				FieldInfo item = (FieldInfo)i$.next();
				if (item.getName().equals(field.getName()))
					contains = true;
			} while (true);
			if (!contains)
				beanInfo.getFieldList().add(new FieldInfo(field.getName(), null, field));
		}

		return beanInfo;
	}

	public static Field getField(Class clazz, String fieldName)
	{
		return clazz.getDeclaredField(fieldName);
		Exception e;
		e;
		return null;
	}

	public static Constructor getDefaultConstructor(Class clazz)
	{
		if (Modifier.isAbstract(clazz.getModifiers()))
			return null;
		Constructor defaultConstructor = null;
		Constructor arr$[] = clazz.getDeclaredConstructors();
		int len$ = arr$.length;
		int i$ = 0;
		do
		{
			if (i$ >= len$)
				break;
			Constructor constructor = arr$[i$];
			if (constructor.getParameterTypes().length == 0)
			{
				defaultConstructor = constructor;
				break;
			}
			i$++;
		} while (true);
		if (defaultConstructor == null && clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()))
		{
			arr$ = clazz.getDeclaredConstructors();
			len$ = arr$.length;
			i$ = 0;
			do
			{
				if (i$ >= len$)
					break;
				Constructor constructor = arr$[i$];
				if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0].equals(clazz.getDeclaringClass()))
				{
					defaultConstructor = constructor;
					break;
				}
				i$++;
			} while (true);
		}
		return defaultConstructor;
	}

	public static Constructor getCreatorConstructor(Class clazz)
	{
		Constructor creatorConstructor = null;
		Constructor arr$[] = clazz.getDeclaredConstructors();
		int len$ = arr$.length;
		int i$ = 0;
		do
		{
			if (i$ >= len$)
				break;
			Constructor constructor = arr$[i$];
			JSONCreator annotation = (JSONCreator)constructor.getAnnotation(com/alibaba/fastjson/annotation/JSONCreator);
			if (annotation != null)
			{
				if (creatorConstructor != null)
					throw new JSONException("multi-json creator");
				creatorConstructor = constructor;
				break;
			}
			i$++;
		} while (true);
		return creatorConstructor;
	}

	public static Method getFactoryMethod(Class clazz)
	{
		Method factoryMethod = null;
		Method arr$[] = clazz.getDeclaredMethods();
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Method method = arr$[i$];
			if (!Modifier.isStatic(method.getModifiers()) || !clazz.isAssignableFrom(method.getReturnType()))
				continue;
			JSONCreator annotation = (JSONCreator)method.getAnnotation(com/alibaba/fastjson/annotation/JSONCreator);
			if (annotation == null)
				continue;
			if (factoryMethod != null)
				throw new JSONException("multi-json creator");
			factoryMethod = method;
			break;
		}

		return factoryMethod;
	}
}
