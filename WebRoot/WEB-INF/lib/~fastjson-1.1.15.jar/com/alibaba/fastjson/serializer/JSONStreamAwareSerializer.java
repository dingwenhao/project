// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONStreamAwareSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONStreamAware;
import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer

public class JSONStreamAwareSerializer
	implements ObjectSerializer
{

	public static JSONStreamAwareSerializer instance = new JSONStreamAwareSerializer();

	public JSONStreamAwareSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		JSONStreamAware aware = (JSONStreamAware)object;
		aware.writeJSONString(out);
	}

}
