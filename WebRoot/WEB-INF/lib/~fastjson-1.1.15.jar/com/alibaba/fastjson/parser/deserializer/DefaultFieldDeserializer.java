// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer

public class DefaultFieldDeserializer extends FieldDeserializer
{

	private ObjectDeserializer fieldValueDeserilizer;

	public DefaultFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		if (fieldValueDeserilizer == null)
			fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldInfo);
		Object value = fieldValueDeserilizer.deserialze(parser, getFieldType(), fieldInfo.getName());
		if (parser.getResolveStatus() == 1)
		{
			com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask task = parser.getLastResolveTask();
			task.setFieldDeserializer(this);
			task.setOwnerContext(parser.getContext());
			parser.setResolveStatus(0);
		} else
		if (object == null)
			fieldValues.put(fieldInfo.getName(), value);
		else
			setValue(object, value);
	}

	public int getFastMatchToken()
	{
		if (fieldValueDeserilizer != null)
			return fieldValueDeserilizer.getFastMatchToken();
		else
			return 2;
	}
}
