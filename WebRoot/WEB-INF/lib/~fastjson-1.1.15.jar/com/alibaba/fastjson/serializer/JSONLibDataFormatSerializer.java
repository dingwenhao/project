// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONLibDataFormatSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter

public class JSONLibDataFormatSerializer
	implements ObjectSerializer
{

	public JSONLibDataFormatSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		if (object == null)
		{
			serializer.getWriter().writeNull();
			return;
		} else
		{
			Date date = (Date)object;
			JSONObject json = new JSONObject();
			json.put("date", Integer.valueOf(date.getDate()));
			json.put("day", Integer.valueOf(date.getDay()));
			json.put("hours", Integer.valueOf(date.getHours()));
			json.put("minutes", Integer.valueOf(date.getMinutes()));
			json.put("month", Integer.valueOf(date.getMonth()));
			json.put("seconds", Integer.valueOf(date.getSeconds()));
			json.put("time", Long.valueOf(date.getTime()));
			json.put("timezoneOffset", Integer.valueOf(date.getTimezoneOffset()));
			json.put("year", Integer.valueOf(date.getYear()));
			serializer.write(json);
			return;
		}
	}
}
