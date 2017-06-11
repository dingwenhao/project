// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RectangleSerializer.java

package com.alibaba.fastjson.serializer;

import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			AutowiredObjectSerializer, JSONSerializer, SerializeWriter, SerializerFeature

public class RectangleSerializer
	implements AutowiredObjectSerializer
{

	public static final RectangleSerializer instance = new RectangleSerializer();

	public RectangleSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		Rectangle rectangle = (Rectangle)object;
		if (rectangle == null)
		{
			out.writeNull();
			return;
		}
		char sep = '{';
		if (out.isEnabled(SerializerFeature.WriteClassName))
		{
			out.write('{');
			out.writeFieldName("@type");
			out.writeString(java/awt/Rectangle.getName());
			sep = ',';
		}
		out.writeFieldValue(sep, "x", rectangle.getX());
		out.writeFieldValue(',', "y", rectangle.getY());
		out.writeFieldValue(',', "width", rectangle.getWidth());
		out.writeFieldValue(',', "height", rectangle.getHeight());
		out.write('}');
	}

	public Set getAutowiredFor()
	{
		return Collections.singleton(java/awt/Rectangle);
	}

}
