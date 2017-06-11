// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SqlDateDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class SqlDateDeserializer
	implements ObjectDeserializer
{

	public static final SqlDateDeserializer instance = new SqlDateDeserializer();

	public SqlDateDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		Object val;
		String strVal;
		long longVal;
		DateFormat dateFormat;
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		if (lexer.token() == 16)
		{
			String key = lexer.scanSymbol(parser.getSymbolTable());
			if ("val" != key)
				throw new JSONException("syntax error");
			lexer.nextTokenWithColon(2);
			if (lexer.token() != 2)
				throw new JSONException("syntax error");
			long val = lexer.longValue();
			lexer.nextToken(13);
			if (lexer.token() != 13)
			{
				throw new JSONException("syntax error");
			} else
			{
				lexer.nextToken(16);
				return new Date(val);
			}
		}
		val = parser.parse();
		if (val == null)
			return null;
		if (val instanceof java.util.Date)
		{
			val = new Date(((java.util.Date)val).getTime());
			break MISSING_BLOCK_LABEL_343;
		}
		if (val instanceof Number)
		{
			val = new Date(((Number)val).longValue());
			break MISSING_BLOCK_LABEL_343;
		}
		if (!(val instanceof String))
			break MISSING_BLOCK_LABEL_315;
		strVal = (String)val;
		if (strVal.length() == 0)
			return null;
		JSONScanner dateLexer = new JSONScanner(strVal);
		if (dateLexer.scanISO8601DateIfMatch())
		{
			longVal = dateLexer.getCalendar().getTimeInMillis();
			break MISSING_BLOCK_LABEL_305;
		}
		dateFormat = parser.getDateFormat();
		java.util.Date date = dateFormat.parse(strVal);
		return new Date(date.getTime());
		ParseException e;
		e;
		longVal = Long.parseLong(strVal);
		return new Date(longVal);
		throw new JSONException((new StringBuilder()).append("parse error : ").append(val).toString());
		return val;
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
