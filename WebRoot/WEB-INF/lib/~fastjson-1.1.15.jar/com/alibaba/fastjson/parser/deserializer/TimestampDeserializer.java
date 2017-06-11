// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TimestampDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class TimestampDeserializer
	implements ObjectDeserializer
{

	public static final TimestampDeserializer instance = new TimestampDeserializer();

	public TimestampDeserializer()
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
			return new Timestamp(((Date)val).getTime());
		if (val instanceof Number)
			return new Timestamp(((Number)val).longValue());
		if (!(val instanceof String))
			break MISSING_BLOCK_LABEL_133;
		strVal = (String)val;
		if (strVal.length() == 0)
			return null;
		dateFormat = parser.getDateFormat();
		Date date = dateFormat.parse(strVal);
		return new Timestamp(date.getTime());
		ParseException e;
		e;
		long longVal = Long.parseLong(strVal);
		return new Timestamp(longVal);
		throw new JSONException("parse error");
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
