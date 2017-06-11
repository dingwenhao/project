// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AtomicIntegerArrayDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicIntegerArray;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class AtomicIntegerArrayDeserializer
	implements ObjectDeserializer
{

	public static final AtomicIntegerArrayDeserializer instance = new AtomicIntegerArrayDeserializer();

	public AtomicIntegerArrayDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		if (parser.getLexer().token() == 8)
		{
			parser.getLexer().nextToken(16);
			return null;
		}
		JSONArray array = new JSONArray();
		parser.parseArray(array);
		AtomicIntegerArray atomicArray = new AtomicIntegerArray(array.size());
		for (int i = 0; i < array.size(); i++)
			atomicArray.set(i, array.getInteger(i).intValue());

		return atomicArray;
	}

	public int getFastMatchToken()
	{
		return 14;
	}

}
