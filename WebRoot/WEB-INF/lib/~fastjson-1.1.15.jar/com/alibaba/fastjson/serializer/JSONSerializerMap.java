// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONSerializerMap.java

package com.alibaba.fastjson.serializer;


// Referenced classes of package com.alibaba.fastjson.serializer:
//			SerializeConfig, ObjectSerializer

/**
 * @deprecated Class JSONSerializerMap is deprecated
 */

public class JSONSerializerMap extends SerializeConfig
{

	public JSONSerializerMap()
	{
	}

	public final boolean put(Class clazz, ObjectSerializer serializer)
	{
		return super.put(clazz, serializer);
	}
}
