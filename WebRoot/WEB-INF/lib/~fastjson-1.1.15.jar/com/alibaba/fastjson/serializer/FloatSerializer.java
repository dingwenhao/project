// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FloatSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class FloatSerializer
	implements ObjectSerializer
{

	public static FloatSerializer instance = new FloatSerializer();

	public FloatSerializer()
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
		float floatValue = ((Float)object).floatValue();
		if (Float.isNaN(floatValue))
			out.writeNull();
		else
		if (Float.isInfinite(floatValue))
		{
			out.writeNull();
		} else
		{
			String floatText = Float.toString(floatValue);
			if (floatText.endsWith(".0"))
				floatText = floatText.substring(0, floatText.length() - 2);
			out.write(floatText);
			if (serializer.isEnabled(SerializerFeature.WriteClassName))
				out.write('F');
		}
	}

}
