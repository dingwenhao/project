// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EnumDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class EnumDeserializer
	implements ObjectDeserializer
{

	private final Class enumClass;
	private final Map ordinalMap = new HashMap();
	private final Map nameMap = new HashMap();

	public EnumDeserializer(Class enumClass)
	{
		this.enumClass = enumClass;
		try
		{
			Method valueMethod = enumClass.getMethod("values", new Class[0]);
			Object values[] = (Object[])(Object[])valueMethod.invoke(null, new Object[0]);
			Object arr$[] = values;
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				Object value = arr$[i$];
				Enum e = (Enum)value;
				ordinalMap.put(Integer.valueOf(e.ordinal()), e);
				nameMap.put(e.name(), e);
			}

		}
		catch (Exception ex)
		{
			throw new JSONException((new StringBuilder()).append("init enum values error, ").append(enumClass.getName()).toString());
		}
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		Object value;
		JSONLexer lexer;
		Object e;
		lexer = parser.getLexer();
		if (lexer.token() != 2)
			break MISSING_BLOCK_LABEL_102;
		value = Integer.valueOf(lexer.intValue());
		lexer.nextToken(16);
		e = ordinalMap.get(value);
		if (e == null)
			throw new JSONException((new StringBuilder()).append("parse enum ").append(enumClass.getName()).append(" error, value : ").append(value).toString());
		return e;
		String strVal;
		if (lexer.token() != 4)
			break MISSING_BLOCK_LABEL_167;
		strVal = lexer.stringVal();
		lexer.nextToken(16);
		if (strVal.length() == 0)
			return (Object)null;
		value = nameMap.get(strVal);
		return Enum.valueOf(enumClass, strVal);
		if (lexer.token() != 8)
			break MISSING_BLOCK_LABEL_193;
		value = null;
		lexer.nextToken(16);
		return null;
		try
		{
			value = parser.parse();
			throw new JSONException((new StringBuilder()).append("parse enum ").append(enumClass.getName()).append(" error, value : ").append(value).toString());
		}
		catch (JSONException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JSONException(e.getMessage(), e);
		}
	}

	public int getFastMatchToken()
	{
		return 2;
	}
}
