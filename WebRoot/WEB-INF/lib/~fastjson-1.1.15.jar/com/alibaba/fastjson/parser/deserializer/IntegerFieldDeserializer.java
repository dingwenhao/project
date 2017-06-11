// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IntegerFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer

public class IntegerFieldDeserializer extends FieldDeserializer
{

	public IntegerFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 2)
		{
			int val = lexer.intValue();
			lexer.nextToken(16);
			if (object == null)
				fieldValues.put(fieldInfo.getName(), Integer.valueOf(val));
			else
				setValue(object, val);
			return;
		}
		Integer value;
		if (lexer.token() == 8)
		{
			value = null;
			lexer.nextToken(16);
		} else
		{
			Object obj = parser.parse();
			value = TypeUtils.castToInt(obj);
		}
		if (value == null && getFieldClass() == Integer.TYPE)
			return;
		if (object == null)
			fieldValues.put(fieldInfo.getName(), value);
		else
			setValue(object, value);
	}

	public int getFastMatchToken()
	{
		return 2;
	}
}
