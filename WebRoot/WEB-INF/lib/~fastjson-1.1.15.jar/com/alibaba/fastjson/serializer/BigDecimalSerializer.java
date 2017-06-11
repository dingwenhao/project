// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BigDecimalSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializerFeature, SerializeWriter

public class BigDecimalSerializer
	implements ObjectSerializer
{

	public static final BigDecimalSerializer instance = new BigDecimalSerializer();

	public BigDecimalSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero))
				out.write('0');
			else
				out.writeNull();
			return;
		}
		BigDecimal val = (BigDecimal)object;
		out.write(val.toString());
		if (out.isEnabled(SerializerFeature.WriteClassName) && fieldType != java/math/BigDecimal && val.scale() == 0)
			out.write('.');
	}

}
