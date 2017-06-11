// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UUIDDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.util.UUID;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class UUIDDeserializer
	implements ObjectDeserializer
{

	public static final UUIDDeserializer instance = new UUIDDeserializer();

	public UUIDDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String name = (String)parser.parse();
		if (name == null)
			return null;
		else
			return UUID.fromString(name);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
