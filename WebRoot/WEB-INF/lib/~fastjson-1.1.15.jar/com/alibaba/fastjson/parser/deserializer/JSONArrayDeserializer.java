// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONArrayDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class JSONArrayDeserializer
	implements ObjectDeserializer
{

	public static final JSONArrayDeserializer instance = new JSONArrayDeserializer();

	public JSONArrayDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		JSONArray array = new JSONArray();
		parser.parseArray(array);
		return array;
	}

	public int getFastMatchToken()
	{
		return 14;
	}

}
