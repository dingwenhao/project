// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LongFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer

public class LongFieldDeserializer extends FieldDeserializer
{

	private final ObjectDeserializer fieldValueDeserilizer;

	public LongFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
		fieldValueDeserilizer = mapping.getDeserializer(fieldInfo);
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 2)
		{
			long val = lexer.longValue();
			lexer.nextToken(16);
			if (object == null)
				fieldValues.put(fieldInfo.getName(), Long.valueOf(val));
			else
				setValue(object, val);
			return;
		}
		Long value;
		if (lexer.token() == 8)
		{
			value = null;
			lexer.nextToken(16);
		} else
		{
			Object obj = parser.parse();
			value = TypeUtils.castToLong(obj);
		}
		if (value == null && getFieldClass() == Long.TYPE)
			return;
		if (object == null)
			fieldValues.put(fieldInfo.getName(), value);
		else
			setValue(object, value);
	}

	public int getFastMatchToken()
	{
		return fieldValueDeserilizer.getFastMatchToken();
	}
}
