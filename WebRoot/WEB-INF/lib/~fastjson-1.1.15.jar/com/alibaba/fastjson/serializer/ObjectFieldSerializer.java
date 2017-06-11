// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectFieldSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Method;
import java.util.Collection;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			FieldSerializer, SerializerFeature, JSONSerializer, SerializeWriter, 
//			ObjectSerializer

public class ObjectFieldSerializer extends FieldSerializer
{

	private ObjectSerializer fieldSerializer;
	private Class runtimeFieldClass;
	private String format;
	private boolean writeNumberAsZero;
	boolean writeNullStringAsEmpty;
	boolean writeNullBooleanAsFalse;
	boolean writeNullListAsEmpty;
	boolean writeEnumUsingToString;

	public ObjectFieldSerializer(FieldInfo fieldInfo)
	{
		super(fieldInfo);
		writeNumberAsZero = false;
		writeNullStringAsEmpty = false;
		writeNullBooleanAsFalse = false;
		writeNullListAsEmpty = false;
		writeEnumUsingToString = false;
		JSONField annotation = (JSONField)fieldInfo.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
		if (annotation != null)
		{
			format = annotation.format();
			if (format.trim().length() == 0)
				format = null;
			SerializerFeature arr$[] = annotation.serialzeFeatures();
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				SerializerFeature feature = arr$[i$];
				if (feature == SerializerFeature.WriteNullNumberAsZero)
				{
					writeNumberAsZero = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullStringAsEmpty)
				{
					writeNullStringAsEmpty = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullBooleanAsFalse)
				{
					writeNullBooleanAsFalse = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullListAsEmpty)
				{
					writeNullListAsEmpty = true;
					continue;
				}
				if (feature == SerializerFeature.WriteEnumUsingToString)
					writeEnumUsingToString = true;
			}

		}
	}

	public void writeProperty(JSONSerializer serializer, Object propertyValue)
		throws Exception
	{
		writePrefix(serializer);
		if (format != null)
		{
			serializer.writeWithFormat(propertyValue, format);
			return;
		}
		if (fieldSerializer == null)
		{
			if (propertyValue == null)
				runtimeFieldClass = getMethod().getReturnType();
			else
				runtimeFieldClass = propertyValue.getClass();
			fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
		}
		if (propertyValue == null)
		{
			if (writeNumberAsZero && java/lang/Number.isAssignableFrom(runtimeFieldClass))
			{
				serializer.getWriter().write('0');
				return;
			}
			if (writeNullStringAsEmpty && java/lang/String == runtimeFieldClass)
			{
				serializer.getWriter().write("\"\"");
				return;
			}
			if (writeNullBooleanAsFalse && java/lang/Boolean == runtimeFieldClass)
			{
				serializer.getWriter().write("false");
				return;
			}
			if (writeNullListAsEmpty && java/util/Collection.isAssignableFrom(runtimeFieldClass))
			{
				serializer.getWriter().write("[]");
				return;
			} else
			{
				fieldSerializer.write(serializer, null, null, null);
				return;
			}
		}
		if (writeEnumUsingToString && runtimeFieldClass.isEnum())
		{
			serializer.getWriter().writeString(((Enum)propertyValue).name());
			return;
		}
		Class valueClass = propertyValue.getClass();
		if (valueClass == runtimeFieldClass)
		{
			fieldSerializer.write(serializer, propertyValue, fieldInfo.getName(), fieldInfo.getFieldType());
			return;
		} else
		{
			ObjectSerializer valueSerializer = serializer.getObjectWriter(valueClass);
			valueSerializer.write(serializer, propertyValue, fieldInfo.getName(), null);
			return;
		}
	}
}
