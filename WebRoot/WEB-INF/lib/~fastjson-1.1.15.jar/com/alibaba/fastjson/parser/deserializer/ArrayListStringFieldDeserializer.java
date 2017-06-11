// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayListStringFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ArrayListStringDeserializer

public class ArrayListStringFieldDeserializer extends FieldDeserializer
{

	public ArrayListStringFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
	}

	public int getFastMatchToken()
	{
		return 14;
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		JSONLexer lexer = parser.getLexer();
		ArrayList list;
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			list = null;
		} else
		{
			list = new ArrayList();
			ArrayListStringDeserializer.parseArray(parser, list);
		}
		if (object == null)
			fieldValues.put(fieldInfo.getName(), list);
		else
			setValue(object, list);
	}
}
