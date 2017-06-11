// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BooleanFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer

public class BooleanFieldDeserializer extends FieldDeserializer
{

	public BooleanFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 6)
		{
			lexer.nextToken(16);
			if (object == null)
				fieldValues.put(fieldInfo.getName(), Boolean.TRUE);
			else
				setValue(object, true);
			return;
		}
		if (lexer.token() == 2)
		{
			int val = lexer.intValue();
			lexer.nextToken(16);
			boolean booleanValue = val == 1;
			if (object == null)
				fieldValues.put(fieldInfo.getName(), Boolean.valueOf(booleanValue));
			else
				setValue(object, booleanValue);
			return;
		}
		Boolean value;
		if (lexer.token() == 8)
		{
			value = null;
			lexer.nextToken(16);
			if (getFieldClass() == Boolean.TYPE)
			{
				return;
			} else
			{
				setValue(object, null);
				return;
			}
		}
		if (lexer.token() == 7)
		{
			lexer.nextToken(16);
			if (object == null)
				fieldValues.put(fieldInfo.getName(), Boolean.FALSE);
			else
				setValue(object, false);
			return;
		}
		Object obj = parser.parse();
		value = TypeUtils.castToBoolean(obj);
		if (value == null && getFieldClass() == Boolean.TYPE)
			return;
		if (object == null)
			fieldValues.put(fieldInfo.getName(), value);
		else
			setValue(object, value);
	}

	public int getFastMatchToken()
	{
		return 6;
	}
}
