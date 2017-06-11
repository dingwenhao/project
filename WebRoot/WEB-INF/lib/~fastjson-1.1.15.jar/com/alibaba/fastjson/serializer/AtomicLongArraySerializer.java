// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AtomicLongArraySerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLongArray;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class AtomicLongArraySerializer
	implements ObjectSerializer
{

	public static final AtomicLongArraySerializer instance = new AtomicLongArraySerializer();

	public AtomicLongArraySerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty))
				out.write("[]");
			else
				out.writeNull();
			return;
		}
		AtomicLongArray array = (AtomicLongArray)object;
		int len = array.length();
		out.append('[');
		for (int i = 0; i < len; i++)
		{
			long val = array.get(i);
			if (i != 0)
				out.write(',');
			out.writeLong(val);
		}

		out.append(']');
	}

}
