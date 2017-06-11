// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MapDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer, DefaultObjectDeserializer

public class MapDeserializer
	implements ObjectDeserializer
{

	public static final MapDeserializer instance = new MapDeserializer();

	public MapDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONLexer lexer;
		Map map;
		com.alibaba.fastjson.parser.ParseContext context;
		lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		map = createMap(type);
		context = parser.getContext();
		Object obj;
		parser.setContext(context, map, fieldName);
		if (lexer.token() != 13)
			break MISSING_BLOCK_LABEL_86;
		lexer.nextToken(16);
		obj = map;
		parser.setContext(context);
		return obj;
		obj = deserialze(parser, type, fieldName, map);
		parser.setContext(context);
		return obj;
		Exception exception;
		exception;
		parser.setContext(context);
		throw exception;
	}

	protected Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map)
	{
		if (type instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType)type;
			Type keyType = parameterizedType.getActualTypeArguments()[0];
			Type valueType = parameterizedType.getActualTypeArguments()[1];
			if (java/lang/String == keyType)
				return DefaultObjectDeserializer.instance.parseMap(parser, map, valueType, fieldName);
			else
				return DefaultObjectDeserializer.instance.parseMap(parser, map, keyType, valueType, fieldName);
		} else
		{
			return parser.parseObject(map, fieldName);
		}
	}

	protected Map createMap(Type type)
	{
		Class clazz;
		if (type == java/util/Properties)
			return new Properties();
		if (type == java/util/Hashtable)
			return new Hashtable();
		if (type == java/util/IdentityHashMap)
			return new IdentityHashMap();
		if (type == java/util/SortedMap || type == java/util/TreeMap)
			return new TreeMap();
		if (type == java/util/concurrent/ConcurrentMap || type == java/util/concurrent/ConcurrentHashMap)
			return new ConcurrentHashMap();
		if (type == java/util/Map || type == java/util/HashMap)
			return new HashMap();
		if (type == java/util/LinkedHashMap)
			return new LinkedHashMap();
		if (type instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType)type;
			return createMap(parameterizedType.getRawType());
		}
		if (!(type instanceof Class))
			break MISSING_BLOCK_LABEL_232;
		clazz = (Class)type;
		if (clazz.isInterface())
			throw new JSONException((new StringBuilder()).append("unsupport type ").append(type).toString());
		return (Map)clazz.newInstance();
		Exception e;
		e;
		throw new JSONException((new StringBuilder()).append("unsupport type ").append(type).toString(), e);
		throw new JSONException((new StringBuilder()).append("unsupport type ").append(type).toString());
	}

	public int getFastMatchToken()
	{
		return 12;
	}

}
