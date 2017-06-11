// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListResolveFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer

public final class ListResolveFieldDeserializer extends FieldDeserializer
{

	private final int index;
	private final List list;

	public ListResolveFieldDeserializer(List list, int index)
	{
		super(null, null);
		this.index = index;
		this.list = list;
	}

	public void setValue(Object object, Object value)
	{
		list.set(index, value);
	}

	public void parseField(DefaultJSONParser defaultjsonparser, Object obj, Type type, Map map)
	{
	}

	public int getFastMatchToken()
	{
		return 0;
	}
}
