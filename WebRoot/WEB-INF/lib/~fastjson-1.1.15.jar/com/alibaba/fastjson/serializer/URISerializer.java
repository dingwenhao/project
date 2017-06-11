// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   URISerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer

public class URISerializer
	implements ObjectSerializer
{

	public static final URISerializer instance = new URISerializer();

	public URISerializer()
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
			URI uri = (URI)object;
			serializer.write(uri.toString());
			return;
		}
	}

}
