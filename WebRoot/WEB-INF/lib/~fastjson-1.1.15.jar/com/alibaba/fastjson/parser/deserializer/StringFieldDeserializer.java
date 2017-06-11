// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer

public class StringFieldDeserializer extends FieldDeserializer
{

	private final ObjectDeserializer fieldValueDeserilizer;

	public StringFieldDeserializer(ParserConfig config, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
		fieldValueDeserilizer = config.getDeserializer(fieldInfo);
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		JSONLexer lexer = parser.getLexer();
		String value;
		if (lexer.token() == 4)
		{
			value = lexer.stringVal();
			lexer.nextToken(16);
		} else
		{
			Object obj = parser.parse();
			if (obj == null)
				value = null;
			else
				value = obj.toString();
		}
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
