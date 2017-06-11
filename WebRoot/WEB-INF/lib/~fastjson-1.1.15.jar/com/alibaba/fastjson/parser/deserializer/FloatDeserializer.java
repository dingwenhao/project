// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FloatDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class FloatDeserializer
	implements ObjectDeserializer
{

	public static final FloatDeserializer instance = new FloatDeserializer();

	public FloatDeserializer()
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
			String val = lexer.numberString();
			lexer.nextToken(16);
			return Float.valueOf(Float.parseFloat(val));
		}
		if (lexer.token() == 3)
		{
			float val = lexer.floatValue();
			lexer.nextToken(16);
			return Float.valueOf(val);
		}
		Object value = parser.parse();
		if (value == null)
			return null;
		else
			return TypeUtils.castToFloat(value);
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
