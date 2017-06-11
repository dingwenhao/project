// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AtomicLongSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLong;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter

public class AtomicLongSerializer
	implements ObjectSerializer
{

	public static final AtomicLongSerializer instance = new AtomicLongSerializer();

	public AtomicLongSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		AtomicLong val = (AtomicLong)object;
		out.writeLong(val.get());
	}

}
