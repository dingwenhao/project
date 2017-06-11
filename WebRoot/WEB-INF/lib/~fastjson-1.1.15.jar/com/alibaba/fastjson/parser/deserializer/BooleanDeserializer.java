// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BooleanDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class BooleanDeserializer
	implements ObjectDeserializer
{

	public static final BooleanDeserializer instance = new BooleanDeserializer();

	public BooleanDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		return deserialze(parser);
	}

	public static Object deserialze(DefaultJSONParser parser)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 6)
		{
			lexer.nextToken(16);
			return Boolean.TRUE;
		}
		if (lexer.token() == 7)
		{
			lexer.nextToken(16);
			return Boolean.FALSE;
		}
		if (lexer.token() == 2)
		{
			int intValue = lexer.intValue();
			lexer.nextToken(16);
			if (intValue == 1)
				return Boolean.TRUE;
			else
				return Boolean.FALSE;
		}
		Object value = parser.parse();
		if (value == null)
			return null;
		else
			return TypeUtils.castToBoolean(value);
	}

	public int getFastMatchToken()
	{
		return 6;
	}

}
