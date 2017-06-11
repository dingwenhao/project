// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParseContext.java

package com.alibaba.fastjson.parser;


public class ParseContext
{

	private Object object;
	private final ParseContext parent;
	private final Object fieldName;

	public ParseContext(ParseContext parent, Object object, Object fieldName)
	{
		this.parent = parent;
		this.object = object;
		this.fieldName = fieldName;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}

	public ParseContext getParentContext()
	{
		return parent;
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
