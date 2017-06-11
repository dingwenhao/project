// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LocaleDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.util.Locale;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class LocaleDeserializer
	implements ObjectDeserializer
{

	public static final LocaleDeserializer instance = new LocaleDeserializer();

	public LocaleDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String text = (String)parser.parse();
		if (text == null)
			return null;
		String items[] = text.split("_");
		if (items.length == 1)
			return new Locale(items[0]);
		if (items.length == 2)
			return new Locale(items[0], items[1]);
		else
			return new Locale(items[0], items[1], items[2]);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
