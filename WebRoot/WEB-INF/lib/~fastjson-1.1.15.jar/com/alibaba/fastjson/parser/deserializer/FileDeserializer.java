// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.io.File;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class FileDeserializer
	implements ObjectDeserializer
{

	public static final FileDeserializer instance = new FileDeserializer();

	public FileDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		Object value = parser.parse();
		if (value == null)
		{
			return null;
		} else
		{
			String path = (String)value;
			return new File(path);
		}
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
