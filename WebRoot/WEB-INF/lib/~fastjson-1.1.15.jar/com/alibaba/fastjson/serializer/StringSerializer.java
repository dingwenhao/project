// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class StringSerializer
	implements ObjectSerializer
{

	public static StringSerializer instance = new StringSerializer();

	public StringSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		write(serializer, (String)object);
	}

	public void write(JSONSerializer serializer, String value)
	{
		SerializeWriter out = serializer.getWriter();
		if (value == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty))
				out.writeString("");
			else
				out.writeNull();
			return;
		} else
		{
			out.writeString(value);
			return;
		}
	}

}
