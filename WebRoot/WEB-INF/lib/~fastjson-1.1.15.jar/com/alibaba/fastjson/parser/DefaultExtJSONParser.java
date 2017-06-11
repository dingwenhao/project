// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultExtJSONParser.java

package com.alibaba.fastjson.parser;


// Referenced classes of package com.alibaba.fastjson.parser:
//			DefaultJSONParser, ParserConfig

/**
 * @deprecated Class DefaultExtJSONParser is deprecated
 */

public class DefaultExtJSONParser extends DefaultJSONParser
{

	public DefaultExtJSONParser(String input)
	{
		this(input, ParserConfig.getGlobalInstance());
	}

	public DefaultExtJSONParser(String input, ParserConfig mapping)
	{
		super(input, mapping);
	}

	public DefaultExtJSONParser(String input, ParserConfig mapping, int features)
	{
		super(input, mapping, features);
	}

	public DefaultExtJSONParser(char input[], int length, ParserConfig mapping, int features)
	{
		super(input, length, mapping, features);
	}
}
