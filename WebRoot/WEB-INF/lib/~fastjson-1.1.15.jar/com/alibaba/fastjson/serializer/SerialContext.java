// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerialContext.java

package com.alibaba.fastjson.serializer;


public class SerialContext
{

	private final SerialContext parent;
	private final Object object;
	private final Object fieldName;
	private int features;

	public SerialContext(SerialContext parent, Object object, Object fieldName)
	{
		features = 0;
		this.parent = parent;
		this.object = object;
		this.fieldName = fieldName;
	}

	public int getFeatures()
	{
		return features;
	}

	public void setFeatures(int features)
	{
		this.features = features;
	}

	public SerialContext getParent()
	{
		return parent;
	}

	public Object getObject()
	{
		return object;
	}

	public Object getFieldName()
	{
		return fieldName;
	}

	public String getPath()
	{
		if (parent == null)
			return "$";
		if (fieldName instanceof Integer)
			return (new StringBuilder()).append(parent.getPath()).append("[").append(fieldName).append("]").toString();
		else
			return (new StringBuilder()).append(parent.getPath()).append(".").append(fieldName).toString();
	}

	public String toString()
	{
		return getPath();
	}
}
