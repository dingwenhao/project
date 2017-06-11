// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BooleanSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class BooleanSerializer
	implements ObjectSerializer
{

	public static final BooleanSerializer instance = new BooleanSerializer();

	public BooleanSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		Boolean value = (Boolean)object;
		if (value == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullBooleanAsFalse))
				out.write("false");
			else
				out.writeNull();
			return;
		}
		if (value.booleanValue())
			out.write("true");
		else
			out.write("false");
	}

}
