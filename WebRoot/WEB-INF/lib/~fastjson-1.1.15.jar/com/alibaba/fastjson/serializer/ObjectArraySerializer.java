// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectArraySerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class ObjectArraySerializer
	implements ObjectSerializer
{

	public static final ObjectArraySerializer instance = new ObjectArraySerializer();

	public ObjectArraySerializer()
	{
	}

	public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out;
		Object array[];
		int size;
		int end;
		SerialContext context;
		out = serializer.getWriter();
		array = (Object[])(Object[])object;
		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty))
				out.write("[]");
			else
				out.writeNull();
			return;
		}
		size = array.length;
		end = size - 1;
		if (end == -1)
		{
			out.append("[]");
			return;
		}
		context = serializer.getContext();
		serializer.setContext(context, object, fieldName);
		Class preClazz;
		ObjectSerializer preWriter;
		preClazz = null;
		preWriter = null;
		out.append('[');
		if (!out.isEnabled(SerializerFeature.PrettyFormat))
			break MISSING_BLOCK_LABEL_182;
		serializer.incrementIndent();
		serializer.println();
		for (int i = 0; i < size; i++)
		{
			if (i != 0)
			{
				out.write(',');
				serializer.println();
			}
			serializer.write(array[i]);
		}

		serializer.decrementIdent();
		serializer.println();
		out.write(']');
		serializer.setContext(context);
		return;
		for (int i = 0; i < end; i++)
		{
			Object item = array[i];
			if (item == null)
			{
				out.append("null,");
				continue;
			}
			Class clazz = item.getClass();
			if (clazz == preClazz)
			{
				preWriter.write(serializer, item, null, null);
			} else
			{
				preClazz = clazz;
				preWriter = serializer.getObjectWriter(clazz);
				preWriter.write(serializer, item, null, null);
			}
			out.append(',');
		}

		Object item = array[end];
		if (item == null)
		{
			out.append("null]");
		} else
		{
			serializer.write(item);
			out.append(']');
		}
		serializer.setContext(context);
		break MISSING_BLOCK_LABEL_339;
		Exception exception;
		exception;
		serializer.setContext(context);
		throw exception;
	}

}
