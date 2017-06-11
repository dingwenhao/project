// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArraySerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class ArraySerializer
	implements ObjectSerializer
{

	private final ObjectSerializer compObjectSerializer;

	public ArraySerializer(ObjectSerializer compObjectSerializer)
	{
		this.compObjectSerializer = compObjectSerializer;
	}

	public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out;
		Object array[];
		int end;
		SerialContext context;
		out = serializer.getWriter();
		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty))
				out.write("[]");
			else
				out.writeNull();
			return;
		}
		array = (Object[])(Object[])object;
		int size = array.length;
		end = size - 1;
		if (end == -1)
		{
			out.append("[]");
			return;
		}
		context = serializer.getContext();
		serializer.setContext(context, object, fieldName);
		out.append('[');
		for (int i = 0; i < end; i++)
		{
			Object item = array[i];
			if (item == null)
			{
				out.append("null,");
			} else
			{
				compObjectSerializer.write(serializer, item, null, null);
				out.append(',');
			}
		}

		Object item = array[end];
		if (item == null)
		{
			out.append("null]");
		} else
		{
			compObjectSerializer.write(serializer, item, null, null);
			out.append(']');
		}
		serializer.setContext(context);
		break MISSING_BLOCK_LABEL_220;
		Exception exception;
		exception;
		serializer.setContext(context);
		throw exception;
	}
}
