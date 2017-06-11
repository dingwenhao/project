// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			SerializerFeature, JSONSerializer, SerializeWriter

public abstract class FieldSerializer
	implements Comparable
{

	protected final FieldInfo fieldInfo;
	private final String double_quoted_fieldPrefix;
	private final String single_quoted_fieldPrefix;
	private final String un_quoted_fieldPrefix;
	private boolean writeNull;

	public FieldSerializer(FieldInfo fieldInfo)
	{
		writeNull = false;
		this.fieldInfo = fieldInfo;
		fieldInfo.setAccessible(true);
		double_quoted_fieldPrefix = (new StringBuilder()).append('"').append(fieldInfo.getName()).append("\":").toString();
		single_quoted_fieldPrefix = (new StringBuilder()).append('\'').append(fieldInfo.getName()).append("':").toString();
		un_quoted_fieldPrefix = (new StringBuilder()).append(fieldInfo.getName()).append(":").toString();
		JSONField annotation = (JSONField)fieldInfo.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
		if (annotation != null)
		{
			SerializerFeature arr$[] = annotation.serialzeFeatures();
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				SerializerFeature feature = arr$[i$];
				if (feature == SerializerFeature.WriteMapNullValue)
					writeNull = true;
			}

		}
	}

	public boolean isWriteNull()
	{
		return writeNull;
	}

	public Field getField()
	{
		return fieldInfo.getField();
	}

	public String getName()
	{
		return fieldInfo.getName();
	}

	public Method getMethod()
	{
		return fieldInfo.getMethod();
	}

	public void writePrefix(JSONSerializer serializer)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (serializer.isEnabled(SerializerFeature.QuoteFieldNames))
		{
			if (serializer.isEnabled(SerializerFeature.UseSingleQuotes))
				out.write(single_quoted_fieldPrefix);
			else
				out.write(double_quoted_fieldPrefix);
		} else
		{
			out.write(un_quoted_fieldPrefix);
		}
	}

	public int compareTo(FieldSerializer o)
	{
		return getName().compareTo(o.getName());
	}

	public Object getPropertyValue(Object object)
		throws Exception
	{
		return fieldInfo.get(object);
	}

	public abstract void writeProperty(JSONSerializer jsonserializer, Object obj)
		throws Exception;

	public volatile int compareTo(Object x0)
	{
		return compareTo((FieldSerializer)x0);
	}
}
