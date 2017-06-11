// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IntegerDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class IntegerDeserializer
	implements ObjectDeserializer
{

	public static final IntegerDeserializer instance = new IntegerDeserializer();

	public IntegerDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		return deserialze(parser);
	}

	public static Object deserialze(DefaultJSONParser parser)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		if (lexer.token() == 2)
		{
			int val = lexer.intValue();
			lexer.nextToken(16);
			return Integer.valueOf(val);
		}
		if (lexer.token() == 3)
		{
			BigDecimal decimalValue = lexer.decimalValue();
			lexer.nextToken(16);
			return Integer.valueOf(decimalValue.intValue());
		} else
		{
			Object value = parser.parse();
			return TypeUtils.castToInt(value);
		}
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
