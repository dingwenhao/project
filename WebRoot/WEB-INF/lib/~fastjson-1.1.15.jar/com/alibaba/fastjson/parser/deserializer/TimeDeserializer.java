// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TimeDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.Calendar;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class TimeDeserializer
	implements ObjectDeserializer
{

	public static final TimeDeserializer instance = new TimeDeserializer();

	public TimeDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() == 16)
		{
			lexer.nextToken(4);
			if (lexer.token() != 4)
				throw new JSONException("syntax error");
			lexer.nextTokenWithColon(2);
			if (lexer.token() != 2)
				throw new JSONException("syntax error");
			long time = lexer.longValue();
			lexer.nextToken(13);
			if (lexer.token() != 13)
			{
				throw new JSONException("syntax error");
			} else
			{
				lexer.nextToken(16);
				return new Time(time);
			}
		}
		Object val = parser.parse();
		if (val == null)
			return null;
		if (val instanceof Time)
			return val;
		if (val instanceof Number)
			return new Time(((Number)val).longValue());
		if (val instanceof String)
		{
			String strVal = (String)val;
			if (strVal.length() == 0)
				return null;
			JSONScanner dateLexer = new JSONScanner(strVal);
			long longVal;
			if (dateLexer.scanISO8601DateIfMatch())
				longVal = dateLexer.getCalendar().getTimeInMillis();
			else
				longVal = Long.parseLong(strVal);
			return new Time(longVal);
		} else
		{
			throw new JSONException("parse error");
		}
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
