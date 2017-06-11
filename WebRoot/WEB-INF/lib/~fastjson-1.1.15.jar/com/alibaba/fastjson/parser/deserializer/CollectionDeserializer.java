// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class CollectionDeserializer
	implements ObjectDeserializer
{

	public static final CollectionDeserializer instance = new CollectionDeserializer();

	public CollectionDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		if (parser.getLexer().token() == 8)
		{
			parser.getLexer().nextToken(16);
			return null;
		}
		Class rawClass = getRawClass(type);
		Collection list;
		if (rawClass == java/util/AbstractCollection)
			list = new ArrayList();
		else
		if (rawClass.isAssignableFrom(java/util/HashSet))
			list = new HashSet();
		else
		if (rawClass.isAssignableFrom(java/util/LinkedHashSet))
			list = new LinkedHashSet();
		else
		if (rawClass.isAssignableFrom(java/util/ArrayList))
			list = new ArrayList();
		else
			try
			{
				list = (Collection)rawClass.newInstance();
			}
			catch (Exception e)
			{
				throw new JSONException((new StringBuilder()).append("create instane error, class ").append(rawClass.getName()).toString());
			}
		Type itemType;
		if (type instanceof ParameterizedType)
			itemType = ((ParameterizedType)type).getActualTypeArguments()[0];
		else
			itemType = java/lang/Object;
		parser.parseArray(itemType, list, fieldName);
		return list;
	}

	public Class getRawClass(Type type)
	{
		if (type instanceof Class)
			return (Class)type;
		if (type instanceof ParameterizedType)
			return getRawClass(((ParameterizedType)type).getRawType());
		else
			throw new JSONException("TODO");
	}

	public int getFastMatchToken()
	{
		return 14;
	}

}
