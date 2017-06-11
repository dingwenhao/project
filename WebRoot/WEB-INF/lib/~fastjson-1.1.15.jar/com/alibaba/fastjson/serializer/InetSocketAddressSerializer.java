// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InetSocketAddressSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter

public class InetSocketAddressSerializer
	implements ObjectSerializer
{

	public static InetSocketAddressSerializer instance = new InetSocketAddressSerializer();

	public InetSocketAddressSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		if (object == null)
		{
			serializer.writeNull();
			return;
		}
		SerializeWriter out = serializer.getWriter();
		InetSocketAddress address = (InetSocketAddress)object;
		java.net.InetAddress inetAddress = address.getAddress();
		out.write('{');
		if (inetAddress != null)
		{
			out.writeFieldName("address");
			serializer.write(inetAddress);
			out.write(',');
		}
		out.writeFieldName("port");
		out.writeInt(address.getPort());
		out.write('}');
	}

}
