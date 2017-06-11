// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class ArrayDeserializer
	implements ObjectDeserializer
{

	public static final ArrayDeserializer instance = new ArrayDeserializer();

	public ArrayDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		if (lexer.token() == 4)
		{
			byte bytes[] = lexer.bytesValue();
			lexer.nextToken(16);
			return bytes;
		} else
		{
			JSONArray array = new JSONArray();
			parser.parseArray(array);
			return toObjectArray(parser, (Class)clazz, array);
		}
	}

	private Object toObjectArray(DefaultJSONParser parser, Class clazz, JSONArray array)
	{
		if (array == null)
			return null;
		int size = array.size();
		Class componentType = clazz.getComponentType();
		Object objArray = Array.newInstance(componentType, size);
		for (int i = 0; i < size; i++)
		{
			Object value = array.get(i);
			if (componentType.isArray())
			{
				Object element = toObjectArray(parser, componentType, (JSONArray)value);
				Array.set(objArray, i, element);
			} else
			{
				Object element = TypeUtils.cast(value, componentType, parser.getConfig());
				Array.set(objArray, i, element);
			}
		}

		return objArray;
	}

	public int getFastMatchToken()
	{
		return 14;
	}

}
