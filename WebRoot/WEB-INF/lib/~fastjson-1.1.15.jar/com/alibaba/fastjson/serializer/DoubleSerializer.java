// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DoubleSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class DoubleSerializer
	implements ObjectSerializer
{

	public static final DoubleSerializer instance = new DoubleSerializer();

	public DoubleSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero))
				out.write('0');
			else
				out.writeNull();
			return;
		}
		double doubleValue = ((Double)object).doubleValue();
		if (Double.isNaN(doubleValue))
			out.writeNull();
		else
		if (Double.isInfinite(doubleValue))
		{
			out.writeNull();
		} else
		{
			String doubleText = Double.toString(doubleValue);
			if (doubleText.endsWith(".0"))
				doubleText = doubleText.substring(0, doubleText.length() - 2);
			out.append(doubleText);
			if (serializer.isEnabled(SerializerFeature.WriteClassName))
				out.write('D');
		}
	}

}
