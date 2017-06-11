// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FontDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.awt.Font;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			AutowiredObjectDeserializer

public class FontDeserializer
	implements AutowiredObjectDeserializer
{

	public static final FontDeserializer instance = new FontDeserializer();

	public FontDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException("syntax error");
		lexer.nextToken();
		int size = 0;
		int style = 0;
		String name = null;
		do
		{
			if (lexer.token() == 13)
			{
				lexer.nextToken();
				break;
			}
			String key;
			if (lexer.token() == 4)
			{
				key = lexer.stringVal();
				lexer.nextTokenWithColon(2);
			} else
			{
				throw new JSONException("syntax error");
			}
			if (key.equalsIgnoreCase("name"))
			{
				if (lexer.token() == 4)
				{
					name = lexer.stringVal();
					lexer.nextToken();
				} else
				{
					throw new JSONException("syntax error");
				}
			} else
			if (key.equalsIgnoreCase("style"))
			{
				if (lexer.token() == 2)
				{
					style = lexer.intValue();
					lexer.nextToken();
				} else
				{
					throw new JSONException("syntax error");
				}
			} else
			if (key.equalsIgnoreCase("size"))
			{
				if (lexer.token() == 2)
				{
					size = lexer.intValue();
					lexer.nextToken();
				} else
				{
					throw new JSONException("syntax error");
				}
			} else
			{
				throw new JSONException((new StringBuilder()).append("syntax error, ").append(key).toString());
			}
			if (lexer.token() == 16)
				lexer.nextToken(4);
		} while (true);
		return new Font(name, style, size);
	}

	public int getFastMatchToken()
	{
		return 12;
	}

	public Set getAutowiredFor()
	{
		return Collections.singleton(java/awt/Font);
	}

}
