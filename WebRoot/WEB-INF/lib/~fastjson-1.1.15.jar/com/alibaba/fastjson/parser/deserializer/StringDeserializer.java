// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class StringDeserializer
	implements ObjectDeserializer
{

	public static final StringDeserializer instance = new StringDeserializer();

	public StringDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		return deserialze(parser);
	}

	public static Object deserialze(DefaultJSONParser parser)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 4)
		{
			String val = lexer.stringVal();
			lexer.nextToken(16);
			return val;
		}
		if (lexer.token() == 2)
		{
			String val = lexer.numberString();
			lexer.nextToken(16);
			return val;
		}
		Object value = parser.parse();
		if (value == null)
			return null;
		else
			return value.toString();
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
