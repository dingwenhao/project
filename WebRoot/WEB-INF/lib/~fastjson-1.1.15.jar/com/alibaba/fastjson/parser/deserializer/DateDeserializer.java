// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DateDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class DateDeserializer
	implements ObjectDeserializer
{

	public static final DateDeserializer instance = new DateDeserializer();

	public DateDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		String strVal;
		DateFormat dateFormat;
		Object val = parser.parse();
		if (val == null)
			return null;
		if (val instanceof Date)
			return val;
		if (val instanceof Number)
			return new Date(((Number)val).longValue());
		if (!(val instanceof String))
			break MISSING_BLOCK_LABEL_134;
		strVal = (String)val;
		if (strVal.length() == 0)
			return null;
		JSONScanner dateLexer = new JSONScanner(strVal);
		if (dateLexer.scanISO8601DateIfMatch())
			return dateLexer.getCalendar().getTime();
		dateFormat = parser.getDateFormat();
		return dateFormat.parse(strVal);
		ParseException e;
		e;
		long longVal = Long.parseLong(strVal);
		return new Date(longVal);
		throw new JSONException("parse error");
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
