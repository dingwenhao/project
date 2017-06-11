// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CharacterDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class CharacterDeserializer
	implements ObjectDeserializer
{

	public static final CharacterDeserializer instance = new CharacterDeserializer();

	public CharacterDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		Object value = parser.parse();
		if (value == null)
			return null;
		else
			return TypeUtils.castToChar(value);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
