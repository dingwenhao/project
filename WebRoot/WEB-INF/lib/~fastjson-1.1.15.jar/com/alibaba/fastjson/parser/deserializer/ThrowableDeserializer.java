// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ThrowableDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			JavaBeanDeserializer

public class ThrowableDeserializer extends JavaBeanDeserializer
{

	public ThrowableDeserializer(ParserConfig mapping, Class clazz)
	{
		super(mapping, clazz);
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		if (parser.getResolveStatus() == 2)
			parser.setResolveStatus(0);
		else
		if (lexer.token() != 12)
			throw new JSONException("syntax error");
		Throwable cause = null;
		Class exClass = null;
		if (type != null && (type instanceof Class))
		{
			Class clazz = (Class)type;
			if (java/lang/Throwable.isAssignableFrom(clazz))
				exClass = clazz;
		}
		String message = null;
		StackTraceElement stackTrace[] = null;
		Map otherValues = new HashMap();
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
			if ("@type".equals(key))
			{
				if (lexer.token() == 4)
				{
					String exClassName = lexer.stringVal();
					exClass = TypeUtils.loadClass(exClassName);
				} else
				{
					throw new JSONException("syntax error");
				}
				lexer.nextToken(16);
			} else
			if ("message".equals(key))
			{
				if (lexer.token() == 8)
					message = null;
				else
				if (lexer.token() == 4)
					message = lexer.stringVal();
				else
					throw new JSONException("syntax error");
				lexer.nextToken();
			} else
			if ("cause".equals(key))
				cause = (Throwable)deserialze(parser, null, "cause");
			else
			if ("stackTrace".equals(key))
				stackTrace = (StackTraceElement[])parser.parseObject([Ljava/lang/StackTraceElement;);
			else
				otherValues.put(key, parser.parse());
			if (lexer.token() == 16 || lexer.token() != 13)
				continue;
			lexer.nextToken(16);
			break;
		} while (true);
		Throwable ex = null;
		if (exClass == null)
			ex = new Exception(message, cause);
		else
			try
			{
				ex = createException(message, cause, exClass);
				if (ex == null)
					ex = new Exception(message, cause);
			}
			catch (Exception e)
			{
				throw new JSONException("create instance error", e);
			}
		if (stackTrace != null)
			ex.setStackTrace(stackTrace);
		return ex;
	}

	private Throwable createException(String message, Throwable cause, Class exClass)
		throws Exception
	{
		Constructor defaultConstructor = null;
		Constructor messageConstructor = null;
		Constructor causeConstructor = null;
		Constructor arr$[] = exClass.getConstructors();
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Constructor constructor = arr$[i$];
			if (constructor.getParameterTypes().length == 0)
			{
				defaultConstructor = constructor;
				continue;
			}
			if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == java/lang/String)
			{
				messageConstructor = constructor;
				continue;
			}
			if (constructor.getParameterTypes().length == 2 && constructor.getParameterTypes()[0] == java/lang/String && constructor.getParameterTypes()[1] == java/lang/Throwable)
				causeConstructor = constructor;
		}

		if (causeConstructor != null)
			return (Throwable)causeConstructor.newInstance(new Object[] {
				message, cause
			});
		if (messageConstructor != null)
			return (Throwable)messageConstructor.newInstance(new Object[] {
				message
			});
		if (defaultConstructor != null)
			return (Throwable)defaultConstructor.newInstance(new Object[0]);
		else
			return null;
	}

	public int getFastMatchToken()
	{
		return 12;
	}
}
