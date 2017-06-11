// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InetAddressSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer

public class InetAddressSerializer
	implements ObjectSerializer
{

	public static InetAddressSerializer instance = new InetAddressSerializer();

	public InetAddressSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		} else
		{
			InetAddress address = (InetAddress)object;
			serializer.write(address.getHostAddress());
			return;
		}
	}

}
