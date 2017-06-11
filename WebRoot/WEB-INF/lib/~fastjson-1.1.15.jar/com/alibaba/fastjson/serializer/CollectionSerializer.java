// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class CollectionSerializer
	implements ObjectSerializer
{

	public static final CollectionSerializer instance = new CollectionSerializer();

	public CollectionSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out;
		Type elementType;
		Collection collection;
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
		elementType = null;
		if (serializer.isEnabled(SerializerFeature.WriteClassName) && (fieldType instanceof ParameterizedType))
		{
			ParameterizedType param = (ParameterizedType)fieldType;
			elementType = param.getActualTypeArguments()[0];
		}
		collection = (Collection)object;
		context = serializer.getContext();
		serializer.setContext(context, object, fieldName);
		if (serializer.isEnabled(SerializerFeature.WriteClassName))
			if (java/util/HashSet == collection.getClass())
				out.append("Set");
			else
			if (java/util/TreeSet == collection.getClass())
				out.append("TreeSet");
		int i = 0;
		out.append('[');
		Iterator i$ = collection.iterator();
		do
		{
			if (!i$.hasNext())
				break;
			Object item = i$.next();
			if (i++ != 0)
				out.append(',');
			if (item == null)
			{
				out.writeNull();
			} else
			{
				Class clazz = item.getClass();
				if (clazz == java/lang/Integer)
					out.writeInt(((Integer)item).intValue());
				else
				if (clazz == java/lang/Long)
				{
					out.writeLong(((Long)item).longValue());
					if (out.isEnabled(SerializerFeature.WriteClassName))
						out.write('L');
				} else
				{
					ObjectSerializer itemSerializer = serializer.getObjectWriter(clazz);
					itemSerializer.write(serializer, item, Integer.valueOf(i - 1), elementType);
				}
			}
		} while (true);
		out.append(']');
		serializer.setContext(context);
		break MISSING_BLOCK_LABEL_346;
		Exception exception;
		exception;
		serializer.setContext(context);
		throw exception;
	}

}
