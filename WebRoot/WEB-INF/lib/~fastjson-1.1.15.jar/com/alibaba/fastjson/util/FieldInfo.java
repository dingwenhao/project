// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldInfo.java

package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class FieldInfo
	implements Comparable
{

	private final String name;
	private final Method method;
	private final Field field;
	private final Class fieldClass;
	private final Type fieldType;
	private final Class declaringClass;

	public FieldInfo(String name, Class declaringClass, Class fieldClass, Type fieldType, Method method, Field field)
	{
		this.name = name;
		this.declaringClass = declaringClass;
		this.fieldClass = fieldClass;
		this.fieldType = fieldType;
		this.method = method;
		this.field = field;
		if (method != null)
			method.setAccessible(true);
		if (field != null)
			field.setAccessible(true);
	}

	public FieldInfo(String name, Method method, Field field)
	{
		this.name = name;
		this.method = method;
		this.field = field;
		if (method != null)
			method.setAccessible(true);
		if (field != null)
			field.setAccessible(true);
		if (method != null)
		{
			if (method.getParameterTypes().length == 1)
			{
				fieldClass = method.getParameterTypes()[0];
				fieldType = method.getGenericParameterTypes()[0];
			} else
			{
				fieldClass = method.getReturnType();
				fieldType = method.getGenericReturnType();
			}
			declaringClass = method.getDeclaringClass();
		} else
		{
			fieldClass = field.getType();
			fieldType = field.getGenericType();
			declaringClass = field.getDeclaringClass();
		}
	}

	public String toString()
	{
		return name;
	}

	public Class getDeclaringClass()
	{
		return declaringClass;
	}

	public Class getFieldClass()
	{
		return fieldClass;
	}

	public Type getFieldType()
	{
		return fieldType;
	}

	public String getName()
	{
		return name;
	}

	public Method getMethod()
	{
		return method;
	}

	public Field getField()
	{
		return field;
	}

	public int compareTo(FieldInfo o)
	{
		return name.compareTo(o.name);
	}

	public Annotation getAnnotation(Class annotationClass)
	{
		Annotation annotation = null;
		if (method != null)
			annotation = method.getAnnotation(annotationClass);
		if (annotation == null && field != null)
			annotation = field.getAnnotation(annotationClass);
		return annotation;
	}

	public Object get(Object javaObject)
		throws IllegalAccessException, InvocationTargetException
	{
		if (method != null)
		{
			Object value = method.invoke(javaObject, new Object[0]);
			return value;
		} else
		{
			return field.get(javaObject);
		}
	}

	public void set(Object javaObject, Object value)
		throws IllegalAccessException, InvocationTargetException
	{
		if (method != null)
		{
			method.invoke(javaObject, new Object[] {
				value
			});
			return;
		} else
		{
			field.set(javaObject, value);
			return;
		}
	}

	public void setAccessible(boolean flag)
		throws SecurityException
	{
		if (method != null)
		{
			method.setAccessible(flag);
			return;
		} else
		{
			field.setAccessible(flag);
			return;
		}
	}

	public volatile int compareTo(Object x0)
	{
		return compareTo((FieldInfo)x0);
	}
}
