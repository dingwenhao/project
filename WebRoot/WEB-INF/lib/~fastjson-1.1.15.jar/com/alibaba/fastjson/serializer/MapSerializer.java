// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MapSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			PropertyFilter, NameFilter, ValueFilter, ObjectSerializer, 
//			JSONSerializer, SerializeWriter, SerializerFeature

public class MapSerializer
	implements ObjectSerializer
{

	public static MapSerializer instance = new MapSerializer();

	public MapSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out;
		Map map;
		SerialContext parent;
		out = serializer.getWriter();
		if (object == null)
		{
			out.writeNull();
			return;
		}
		map = (Map)object;
		if (out.isEnabled(SerializerFeature.SortField) && !(map instanceof SortedMap))
			try
			{
				map = new TreeMap(map);
			}
			catch (Exception ex) { }
		if (serializer.containsReference(object))
		{
			serializer.writeReference(object);
			return;
		}
		parent = serializer.getContext();
		serializer.setContext(parent, object, fieldName);
		out.write('{');
		Class preClazz = null;
		ObjectSerializer preWriter = null;
		boolean first = true;
		if (out.isEnabled(SerializerFeature.WriteClassName))
		{
			out.writeFieldName("@type");
			out.writeString(object.getClass().getName());
			first = false;
		}
		Iterator i$ = map.entrySet().iterator();
		do
		{
			if (!i$.hasNext())
				break;
			java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
			Object value = entry.getValue();
			Object entryKey = entry.getKey();
			if (entryKey == null || (entryKey instanceof String))
			{
				String key = (String)entryKey;
				List propertyFilters = serializer.getPropertyFiltersDirect();
				if (propertyFilters != null)
				{
					boolean apply = true;
					Iterator i$ = propertyFilters.iterator();
					do
					{
						if (!i$.hasNext())
							break;
						PropertyFilter propertyFilter = (PropertyFilter)i$.next();
						if (propertyFilter.apply(object, key, value))
							continue;
						apply = false;
						break;
					} while (true);
					if (!apply)
						continue;
				}
				List nameFilters = serializer.getNameFiltersDirect();
				if (nameFilters != null)
				{
					for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
					{
						NameFilter nameFilter = (NameFilter)i$.next();
						key = nameFilter.process(object, key, value);
					}

				}
				List valueFilters = serializer.getValueFiltersDirect();
				if (valueFilters != null)
				{
					for (Iterator i$ = valueFilters.iterator(); i$.hasNext();)
					{
						ValueFilter valueFilter = (ValueFilter)i$.next();
						value = valueFilter.process(object, key, value);
					}

				}
				if (value == null && !serializer.isEnabled(SerializerFeature.WriteMapNullValue))
					continue;
				if (!first)
					out.write(',');
				out.writeFieldName(key, true);
			} else
			{
				if (!first)
					out.write(',');
				serializer.write(entryKey);
				out.write(':');
			}
			first = false;
			if (value == null)
			{
				out.writeNull();
			} else
			{
				Class clazz = value.getClass();
				if (clazz == preClazz)
				{
					preWriter.write(serializer, value, entryKey, null);
				} else
				{
					preClazz = clazz;
					preWriter = serializer.getObjectWriter(clazz);
					preWriter.write(serializer, value, entryKey, null);
				}
			}
		} while (true);
		serializer.setContext(parent);
		break MISSING_BLOCK_LABEL_563;
		Exception exception;
		exception;
		serializer.setContext(parent);
		throw exception;
		out.write('}');
		return;
	}

}
