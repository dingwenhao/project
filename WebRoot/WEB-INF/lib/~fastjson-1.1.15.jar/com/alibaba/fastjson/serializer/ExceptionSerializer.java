// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ExceptionSerializer.java

package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			JavaBeanSerializer, JSONSerializer

public class ExceptionSerializer extends JavaBeanSerializer
{

	public ExceptionSerializer(Class clazz)
	{
		super(clazz);
	}

	protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object obj1)
	{
		return true;
	}
}
