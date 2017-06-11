// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BigIntegerSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class BigIntegerSerializer
	implements ObjectSerializer
{

	public static final BigIntegerSerializer instance = new BigIntegerSerializer();

	public BigIntegerSerializer()
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
		} else
		{
			BigInteger val = (BigInteger)object;
			out.write(val.toString());
			return;
		}
	}

}
