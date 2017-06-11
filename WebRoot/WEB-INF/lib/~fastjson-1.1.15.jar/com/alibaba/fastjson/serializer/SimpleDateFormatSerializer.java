// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleDateFormatSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter

public class SimpleDateFormatSerializer
	implements ObjectSerializer
{

	private final String pattern;

	public SimpleDateFormatSerializer(String pattern)
	{
		this.pattern = pattern;
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		if (object == null)
		{
			serializer.getWriter().writeNull();
			return;
		} else
		{
			Date date = (Date)object;
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			String text = format.format(date);
			serializer.write(text);
			return;
		}
	}
}
