// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RectangleDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.awt.Rectangle;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			AutowiredObjectDeserializer

public class RectangleDeserializer
	implements AutowiredObjectDeserializer
{

	public static final RectangleDeserializer instance = new RectangleDeserializer();

	public RectangleDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			return null;
		}
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException("syntax error");
		lexer.nextToken();
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
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
			int val;
			if (lexer.token() == 2)
			{
				val = lexer.intValue();
				lexer.nextToken();
			} else
			{
				throw new JSONException("syntax error");
			}
			if (key.equalsIgnoreCase("x"))
				x = val;
			else
			if (key.equalsIgnoreCase("y"))
				y = val;
			else
			if (key.equalsIgnoreCase("width"))
				width = val;
			else
			if (key.equalsIgnoreCase("height"))
				height = val;
			else
				throw new JSONException((new StringBuilder()).append("syntax error, ").append(key).toString());
			if (lexer.token() == 16)
				lexer.nextToken(4);
		} while (true);
		return new Rectangle(x, y, width, height);
	}

	public int getFastMatchToken()
	{
		return 12;
	}

	public Set getAutowiredFor()
	{
		return Collections.singleton(java/awt/Rectangle);
	}

}
