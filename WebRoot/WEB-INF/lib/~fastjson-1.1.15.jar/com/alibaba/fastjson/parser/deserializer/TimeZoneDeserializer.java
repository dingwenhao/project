// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TimeZoneDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.util.TimeZone;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class TimeZoneDeserializer
	implements ObjectDeserializer
{

	public static final TimeZoneDeserializer instance = new TimeZoneDeserializer();

	public TimeZoneDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String id = (String)parser.parse();
		if (id == null)
			return null;
		else
			return TimeZone.getTimeZone(id);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
