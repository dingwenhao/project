// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeReference.java

package com.alibaba.fastjson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeReference
{

	private final Type type;
	public static final Type LIST_STRING = (new TypeReference() {

	}).getType();

	protected TypeReference()
	{
		Type superClass = getClass().getGenericSuperclass();
		type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
	}

	public Type getType()
	{
		return type;
	}

}
