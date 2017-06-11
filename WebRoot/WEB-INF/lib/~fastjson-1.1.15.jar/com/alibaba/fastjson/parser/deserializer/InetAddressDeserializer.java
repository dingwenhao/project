// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InetAddressDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class InetAddressDeserializer
	implements ObjectDeserializer
{

	public static final InetAddressDeserializer instance = new InetAddressDeserializer();

	public InetAddressDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String host;
		host = (String)parser.parse();
		if (host == null)
			return null;
		if (host.length() == 0)
			return null;
		return InetAddress.getByName(host);
		UnknownHostException e;
		e;
		throw new JSONException("deserialize error", e);
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
