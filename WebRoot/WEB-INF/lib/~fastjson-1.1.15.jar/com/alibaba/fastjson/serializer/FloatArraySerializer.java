// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FloatArraySerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class FloatArraySerializer
	implements ObjectSerializer
{

	public static final FloatArraySerializer instance = new FloatArraySerializer();

	public FloatArraySerializer()
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
		}
		float array[] = (float[])(float[])object;
		int size = array.length;
		int end = size - 1;
		if (end == -1)
		{
			out.append("[]");
			return;
		}
		out.append('[');
		for (int i = 0; i < end; i++)
		{
			float item = array[i];
			if (Float.isNaN(item))
				out.writeNull();
			else
				out.append(Float.toString(item));
			out.append(',');
		}

		float item = array[end];
		if (Float.isNaN(item))
			out.writeNull();
		else
			out.append(Float.toString(item));
		out.append(']');
	}

}
