// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PointSerializer.java

package com.alibaba.fastjson.serializer;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			AutowiredObjectSerializer, JSONSerializer, SerializeWriter, SerializerFeature

public class PointSerializer
	implements AutowiredObjectSerializer
{

	public static final PointSerializer instance = new PointSerializer();

	public PointSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		Point font = (Point)object;
		if (font == null)
		{
			out.writeNull();
			return;
		}
		char sep = '{';
		if (out.isEnabled(SerializerFeature.WriteClassName))
		{
			out.write('{');
			out.writeFieldName("@type");
			out.writeString(java/awt/Point.getName());
			sep = ',';
		}
		out.writeFieldValue(sep, "x", font.getX());
		out.writeFieldValue(',', "y", font.getY());
		out.write('}');
	}

	public Set getAutowiredFor()
	{
		return Collections.singleton(java/awt/Point);
	}

}
