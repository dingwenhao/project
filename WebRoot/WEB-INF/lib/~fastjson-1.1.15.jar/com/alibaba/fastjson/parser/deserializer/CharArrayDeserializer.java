// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CharArrayDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class CharArrayDeserializer
	implements ObjectDeserializer
{

	public static final CharArrayDeserializer instance = new CharArrayDeserializer();

	public CharArrayDeserializer()
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
			return val.toCharArray();
		}
		if (lexer.token() == 2)
		{
			Number val = lexer.integerValue();
			lexer.nextToken(16);
			return val.toString().toCharArray();
		}
		Object value = parser.parse();
		if (value == null)
			return null;
		else
			return JSON.toJSONString(value).toCharArray();
	}

	public int getFastMatchToken()
	{
		return 4;
	}

}
