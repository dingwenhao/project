// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IntArraySerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class IntArraySerializer
	implements ObjectSerializer
{

	public static IntArraySerializer instance = new IntArraySerializer();

	public IntArraySerializer()
	{
	}

	public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
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
		} else
		{
			int array[] = (int[])(int[])object;
			out.writeIntArray(array);
			return;
		}
	}

}
