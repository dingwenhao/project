// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			SerialContext, ObjectSerializer, SerializerFeature, JSONSerializer, 
//			SerializeWriter

public final class ListSerializer
	implements ObjectSerializer
{

	public static final ListSerializer instance = new ListSerializer();

	public ListSerializer()
	{
	}

	public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		boolean writeClassName;
		SerializeWriter out;
		Type elementType;
		List list;
		int size;
		int end;
		SerialContext context;
		writeClassName = serializer.isEnabled(SerializerFeature.WriteClassName);
		out = serializer.getWriter();
		elementType = null;
		if (writeClassName && (fieldType instanceof ParameterizedType))
		{
			ParameterizedType param = (ParameterizedType)fieldType;
			elementType = param.getActualTypeArguments()[0];
		}
		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty))
				out.write("[]");
			else
				out.writeNull();
			return;
		}
		list = (List)object;
		size = list.size();
		end = size - 1;
		if (end == -1)
		{
			out.append("[]");
			return;
		}
		context = serializer.getContext();
		serializer.setContext(context, object, fieldName);
		ObjectSerializer itemSerializer = null;
		if (size <= 1 || !out.isEnabled(SerializerFeature.PrettyFormat))
			break MISSING_BLOCK_LABEL_309;
		out.append('[');
		serializer.incrementIndent();
		for (int i = 0; i < size; i++)
		{
			if (i != 0)
				out.append(',');
			serializer.println();
			Object item = list.get(i);
			if (item != null)
			{
				if (serializer.containsReference(item))
				{
					serializer.writeReference(item);
				} else
				{
					ObjectSerializer itemSerializer = serializer.getObjectWriter(item.getClass());
					SerialContext itemContext = new SerialContext(context, object, fieldName);
					serializer.setContext(itemContext);
					itemSerializer.write(serializer, item, Integer.valueOf(i), elementType);
				}
			} else
			{
				serializer.getWriter().writeNull();
			}
		}

		serializer.decrementIdent();
		serializer.println();
		out.append(']');
		serializer.setContext(context);
		return;
		out.append('[');
		for (int i = 0; i < end; i++)
		{
			Object item = list.get(i);
			if (item == null)
			{
				out.append("null,");
				continue;
			}
			Class clazz = item.getClass();
			if (clazz == java/lang/Integer)
			{
				out.writeIntAndChar(((Integer)item).intValue(), ',');
				continue;
			}
			if (clazz == java/lang/Long)
			{
				long val = ((Long)item).longValue();
				if (writeClassName)
				{
					out.writeLongAndChar(val, 'L');
					out.write(',');
				} else
				{
					out.writeLongAndChar(val, ',');
				}
				continue;
			}
			SerialContext itemContext = new SerialContext(context, object, fieldName);
			serializer.setContext(itemContext);
			if (serializer.containsReference(item))
			{
				serializer.writeReference(item);
			} else
			{
				ObjectSerializer itemSerializer = serializer.getObjectWriter(item.getClass());
				itemSerializer.write(serializer, item, Integer.valueOf(end), elementType);
			}
			out.append(',');
		}

		Object item = list.get(end);
		if (item == null)
		{
			out.append("null]");
		} else
		{
			Class clazz = item.getClass();
			if (clazz == java/lang/Integer)
				out.writeIntAndChar(((Integer)item).intValue(), ']');
			else
			if (clazz == java/lang/Long)
			{
				if (writeClassName)
				{
					out.writeLongAndChar(((Long)item).longValue(), 'L');
					out.write(']');
				} else
				{
					out.writeLongAndChar(((Long)item).longValue(), ']');
				}
			} else
			{
				SerialContext itemContext = new SerialContext(context, object, fieldName);
				serializer.setContext(itemContext);
				if (serializer.containsReference(item))
				{
					serializer.writeReference(item);
				} else
				{
					ObjectSerializer itemSerializer = serializer.getObjectWriter(item.getClass());
					itemSerializer.write(serializer, item, Integer.valueOf(end), elementType);
				}
				out.append(']');
			}
		}
		serializer.setContext(context);
		break MISSING_BLOCK_LABEL_729;
		Exception exception;
		exception;
		serializer.setContext(context);
		throw exception;
	}

}
