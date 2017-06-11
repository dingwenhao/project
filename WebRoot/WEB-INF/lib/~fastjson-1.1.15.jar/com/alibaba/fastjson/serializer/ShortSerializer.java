// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ShortSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class ShortSerializer
	implements ObjectSerializer
{

	public static ShortSerializer instance = new ShortSerializer();

	public ShortSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		Number numberValue = (Number)object;
		if (numberValue == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero))
				out.write('0');
			else
				out.writeNull();
			return;
		}
		short value = ((Number)object).shortValue();
		out.writeInt(value);
		if (serializer.isEnabled(SerializerFeature.WriteClassName))
			out.write('S');
	}

}
