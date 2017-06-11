// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ColorSerializer.java

package com.alibaba.fastjson.serializer;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			AutowiredObjectSerializer, JSONSerializer, SerializeWriter, SerializerFeature

public class ColorSerializer
	implements AutowiredObjectSerializer
{

	public ColorSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		Color color = (Color)object;
		if (color == null)
		{
			out.writeNull();
			return;
		}
		char sep = '{';
		if (out.isEnabled(SerializerFeature.WriteClassName))
		{
			out.write('{');
			out.writeFieldName("@type");
			out.writeString(java/awt/Color.getName());
			sep = ',';
		}
		out.writeFieldValue(sep, "r", color.getRed());
		out.writeFieldValue(',', "g", color.getGreen());
		out.writeFieldValue(',', "b", color.getBlue());
		if (color.getAlpha() > 0)
			out.writeFieldValue(',', "alpha", color.getAlpha());
		out.write('}');
	}

	public Set getAutowiredFor()
	{
		return Collections.singleton(java/awt/Color);
	}
}
