// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InetSocketAddressDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public class InetSocketAddressDeserializer
	implements ObjectDeserializer
{

	public static final InetSocketAddressDeserializer instance = new InetSocketAddressDeserializer();

	public InetSocketAddressDeserializer()
	{
	}

	public Object deserialze(DefaultJSONParser parser, Type clazz, Object fieldName)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			return null;
		}
		parser.accept(12);
		InetAddress address = null;
		int port = 0;
		do
		{
			String key = lexer.symbol(parser.getSymbolTable());
			lexer.nextToken();
			if (key.equals("address"))
			{
				parser.accept(17);
				address = (InetAddress)parser.parseObject(java/net/InetAddress);
			} else
			if (key.equals("port"))
			{
				parser.accept(17);
				if (lexer.token() != 2)
					throw new JSONException("port is not int");
				port = lexer.intValue();
				lexer.nextToken();
			} else
			{
				parser.accept(17);
				parser.parse();
			}
			if (lexer.token() == 16)
			{
				lexer.nextToken();
			} else
			{
				parser.accept(13);
				return new InetSocketAddress(address, port);
			}
		} while (true);
	}

	public int getFastMatchToken()
	{
		return 12;
	}

}
