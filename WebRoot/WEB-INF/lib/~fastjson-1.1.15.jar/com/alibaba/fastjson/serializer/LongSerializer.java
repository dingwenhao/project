// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LongSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class LongSerializer
	implements ObjectSerializer
{

	public static LongSerializer instance = new LongSerializer();

	public LongSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero))
				out.write('0');
			else
				out.writeNull();
			return;
		}
		long value = ((Long)object).longValue();
		out.writeLong(value);
		if (serializer.isEnabled(SerializerFeature.WriteClassName) && value <= 0x7fffffffL && value >= 0xffffffff80000000L && fieldType != java/lang/Long)
			out.write('L');
	}

}
