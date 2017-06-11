// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EnumSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter, SerializerFeature

public class EnumSerializer
	implements ObjectSerializer
{

	public static final EnumSerializer instance = new EnumSerializer();

	public EnumSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			serializer.getWriter().writeNull();
			return;
		}
		if (serializer.isEnabled(SerializerFeature.WriteEnumUsingToString))
		{
			Enum e = (Enum)object;
			serializer.write(e.name());
		} else
		{
			Enum e = (Enum)object;
			out.writeInt(e.ordinal());
		}
	}

}
