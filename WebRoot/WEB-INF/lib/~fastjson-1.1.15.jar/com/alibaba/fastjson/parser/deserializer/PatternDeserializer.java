// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PatternDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class PatternDeserializer
	implements ObjectDeserializer
{

	public static final PatternDeserializer instance = new PatternDeserializer();

	public PatternDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		Object value = parser.parse();
		if (value == null)
		{
			return null;
		} else
		{
			String pattern = (String)value;
			return Pattern.compile(pattern);
		}
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
