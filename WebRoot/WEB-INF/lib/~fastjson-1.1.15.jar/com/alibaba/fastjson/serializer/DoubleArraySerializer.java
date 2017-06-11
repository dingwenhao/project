// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DoubleArraySerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class DoubleArraySerializer
	implements ObjectSerializer
{

	public static final DoubleArraySerializer instance = new DoubleArraySerializer();

	public DoubleArraySerializer()
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
		double array[] = (double[])(double[])object;
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
			double item = array[i];
			if (Double.isNaN(item))
				out.writeNull();
			else
				out.append(Double.toString(item));
			out.append(',');
		}

		double item = array[end];
		if (Double.isNaN(item))
			out.writeNull();
		else
			out.append(Double.toString(item));
		out.append(']');
	}

}
