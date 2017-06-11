// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   URLDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class URLDeserializer
	implements ObjectDeserializer
{

	public static final URLDeserializer instance = new URLDeserializer();

	public URLDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String url;
		url = (String)parser.parse();
		if (url == null)
			return null;
		return new URL(url);
		MalformedURLException e;
		e;
		throw new JSONException("create url error", e);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
