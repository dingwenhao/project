// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BigDecimalDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class BigDecimalDeserializer
	implements ObjectDeserializer
{

	public static final BigDecimalDeserializer instance = new BigDecimalDeserializer();

	public BigDecimalDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		return deserialze(parser);
	}

	public static Object deserialze(DefaultJSONParser parser)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 2)
		{
			long val = lexer.longValue();
			lexer.nextToken(16);
			return new BigDecimal(val);
		}
		if (lexer.token() == 3)
		{
			BigDecimal val = lexer.decimalValue();
			lexer.nextToken(16);
			return val;
		}
		Object value = parser.parse();
		if (value == null)
			return null;
		else
			return TypeUtils.castToBigDecimal(value);
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
