// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StackTraceElementDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class StackTraceElementDeserializer
	implements ObjectDeserializer
{

	public static final StackTraceElementDeserializer instance = new StackTraceElementDeserializer();

	public StackTraceElementDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			return null;
		}
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException((new StringBuilder()).append("syntax error: ").append(JSONToken.name(lexer.token())).toString());
		String declaringClass = null;
		String methodName = null;
		String fileName = null;
		int lineNumber = 0;
		do
		{
			String key = lexer.scanSymbol(parser.getSymbolTable());
			if (key == null)
			{
				if (lexer.token() == 13)
				{
					lexer.nextToken(16);
					break;
				}
				if (lexer.token() == 16 && lexer.isEnabled(Feature.AllowArbitraryCommas))
					continue;
			}
			lexer.nextTokenWithColon(4);
			if (key == "className")
			{
				if (lexer.token() == 8)
					declaringClass = null;
				else
				if (lexer.token() == 4)
					declaringClass = lexer.stringVal();
				else
					throw new JSONException("syntax error");
			} else
			if (key == "methodName")
			{
				if (lexer.token() == 8)
					methodName = null;
				else
				if (lexer.token() == 4)
					methodName = lexer.stringVal();
				else
					throw new JSONException("syntax error");
			} else
			if (key == "fileName")
			{
				if (lexer.token() == 8)
					fileName = null;
				else
				if (lexer.token() == 4)
					fileName = lexer.stringVal();
				else
					throw new JSONException("syntax error");
			} else
			if (key == "lineNumber")
			{
				if (lexer.token() == 8)
					lineNumber = 0;
				else
				if (lexer.token() == 2)
					lineNumber = lexer.intValue();
				else
					throw new JSONException("syntax error");
			} else
			if (key == "nativeMethod")
			{
				if (lexer.token() == 8)
					lexer.nextToken(16);
				else
				if (lexer.token() == 6)
					lexer.nextToken(16);
				else
				if (lexer.token() == 7)
					lexer.nextToken(16);
				else
					throw new JSONException("syntax error");
			} else
			if (key == "@type")
			{
				if (lexer.token() != 8)
					if (lexer.token() == 4)
					{
						String elementType = lexer.stringVal();
						if (!elementType.equals("java.lang.StackTraceElement"))
							throw new JSONException((new StringBuilder()).append("syntax error : ").append(elementType).toString());
					} else
					{
						throw new JSONException("syntax error");
					}
			} else
			{
				throw new JSONException((new StringBuilder()).append("syntax error : ").append(key).toString());
			}
			if (lexer.token() == 16 || lexer.token() != 13)
				continue;
			lexer.nextToken(16);
			break;
		} while (true);
		return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
	}

	public int getFastMatchToken()
	{
		return 12;
	}

}
