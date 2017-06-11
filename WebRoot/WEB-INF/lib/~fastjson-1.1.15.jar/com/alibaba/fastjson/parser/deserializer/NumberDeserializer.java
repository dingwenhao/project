// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NumberDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigDecimal;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class NumberDeserializer
	implements ObjectDeserializer
{

	public static final NumberDeserializer instance = new NumberDeserializer();

	public NumberDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 2)
		{
			long val = lexer.longValue();
			lexer.nextToken(16);
			if (clazz == Double.TYPE || clazz == java/lang/Double)
				return Double.valueOf(val);
			if (clazz == Short.TYPE || clazz == java/lang/Short)
				return Short.valueOf((short)(int)val);
			if (clazz == Byte.TYPE || clazz == java/lang/Byte)
				return Byte.valueOf((byte)(int)val);
			if (val >= 0xffffffff80000000L && val <= 0x7fffffffL)
				return Integer.valueOf((int)val);
			else
				return Long.valueOf(val);
		}
		if (lexer.token() == 3)
		{
			BigDecimal val;
			if (clazz == Double.TYPE || clazz == java/lang/Double)
			{
				val = lexer.numberString();
				lexer.nextToken(16);
				return Double.valueOf(Double.parseDouble(val));
			}
			val = lexer.decimalValue();
			lexer.nextToken(16);
			if (clazz == Short.TYPE || clazz == java/lang/Short)
				return Short.valueOf(val.shortValue());
			if (clazz == Byte.TYPE || clazz == java/lang/Byte)
				return Byte.valueOf(val.byteValue());
			else
				return val;
		}
		Object value = parser.parse();
		if (value == null)
			return null;
		if (clazz == Double.TYPE || clazz == java/lang/Double)
			return TypeUtils.castToDouble(value);
		if (clazz == Short.TYPE || clazz == java/lang/Short)
			return TypeUtils.castToShort(value);
		if (clazz == Byte.TYPE || clazz == java/lang/Byte)
			return TypeUtils.castToByte(value);
		else
			return TypeUtils.castToBigDecimal(value);
	}

	public int getFastMatchToken()
	{
		return 2;
	}

}
