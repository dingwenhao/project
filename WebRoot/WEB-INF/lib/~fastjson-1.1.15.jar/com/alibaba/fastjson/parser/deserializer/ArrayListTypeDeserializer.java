// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayListTypeDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class ArrayListTypeDeserializer
	implements ObjectDeserializer
{

	private Type itemType;
	private Class rawClass;

	public ArrayListTypeDeserializer(Class rawClass, Type itemType)
	{
		this.rawClass = rawClass;
		this.itemType = itemType;
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		java.util.Collection list = null;
		if (parser.getLexer().token() == 8)
		{
			parser.getLexer().nextToken();
		} else
		{
			if (rawClass.isAssignableFrom(java/util/LinkedHashSet))
				list = new LinkedHashSet();
			else
			if (rawClass.isAssignableFrom(java/util/HashSet))
				list = new HashSet();
			else
				list = new ArrayList();
			parser.parseArray(itemType, list, fieldName);
		}
		return list;
	}

	public int getFastMatchToken()
	{
		return 14;
	}
}
