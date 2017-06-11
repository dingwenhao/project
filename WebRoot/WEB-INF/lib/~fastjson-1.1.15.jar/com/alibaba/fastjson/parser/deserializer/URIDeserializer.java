// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   URIDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.net.URI;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class URIDeserializer
	implements ObjectDeserializer
{

	public static final URIDeserializer instance = new URIDeserializer();

	public URIDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String uri = (String)parser.parse();
		if (uri == null)
			return null;
		else
			return URI.create(uri);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
