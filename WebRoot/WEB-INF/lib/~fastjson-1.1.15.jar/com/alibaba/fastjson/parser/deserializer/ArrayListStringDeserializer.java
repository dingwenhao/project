// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayListStringDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class ArrayListStringDeserializer
	implements ObjectDeserializer
{

	public static final ArrayListStringDeserializer instance = new ArrayListStringDeserializer();

	public ArrayListStringDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		Collection array = null;
		if (type == java/util/Set || type == java/util/HashSet)
			array = new HashSet();
		else
		if (type instanceof ParameterizedType)
		{
			Type rawType = ((ParameterizedType)type).getRawType();
			if (rawType == java/util/Set || rawType == java/util/HashSet)
				array = new HashSet();
		}
		if (array == null)
			array = new ArrayList();
		parseArray(parser, array);
		return array;
	}

	public static void parseArray(DefaultJSONParser parser, Collection array)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return;
		}
		if (lexer.token() == 21)
			lexer.nextToken();
		if (lexer.token() != 14)
			throw new JSONException((new StringBuilder()).append("exepct '[', but ").append(lexer.token()).toString());
		lexer.nextToken(4);
		do
		{
			if (lexer.isEnabled(Feature.AllowArbitraryCommas))
				for (; lexer.token() == 16; lexer.nextToken());
			if (lexer.token() == 15)
				break;
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
			array.add(value);
			if (lexer.token() == 16)
				lexer.nextToken(4);
		} while (true);
		lexer.nextToken(16);
	}

	public int getFastMatchToken()
	{
		return 14;
	}

}
