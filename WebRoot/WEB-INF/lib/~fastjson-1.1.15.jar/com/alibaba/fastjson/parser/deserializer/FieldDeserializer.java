// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.*;
import java.util.Map;

public abstract class FieldDeserializer
{

	protected final FieldInfo fieldInfo;
	protected final Class clazz;

	public FieldDeserializer(Class clazz, FieldInfo fieldInfo)
	{
		this.clazz = clazz;
		this.fieldInfo = fieldInfo;
	}

	public Method getMethod()
	{
		return fieldInfo.getMethod();
	}

	public Class getFieldClass()
	{
		return fieldInfo.getFieldClass();
	}

	public Type getFieldType()
	{
		return fieldInfo.getFieldType();
	}

	public abstract void parseField(DefaultJSONParser defaultjsonparser, Object obj, Type type, Map map);

	public abstract int getFastMatchToken();

	public void setValue(Object object, boolean value)
	{
		setValue(object, Boolean.valueOf(value));
	}

	public void setValue(Object object, int value)
	{
		setValue(object, Integer.valueOf(value));
	}

	public void setValue(Object object, long value)
	{
		setValue(object, Long.valueOf(value));
	}

	public void setValue(Object object, String value)
	{
		setValue(object, value);
	}

	public void setValue(Object object, Object value)
	{
		Method method = fieldInfo.getMethod();
		if (method != null)
			try
			{
				method.invoke(object, new Object[] {
					value
				});
			}
			catch (Exception e)
			{
				throw new JSONException((new StringBuilder()).append("set property error, ").append(fieldInfo.getName()).toString(), e);
			}
		else
		if (fieldInfo.getField() != null)
			try
			{
				fieldInfo.getField().set(object, value);
			}
			catch (Exception e)
			{
				throw new JSONException((new StringBuilder()).append("set property error, ").append(fieldInfo.getName()).toString(), e);
			}
	}
}
