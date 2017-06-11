// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Keywords.java

package com.alibaba.fastjson.parser;

import java.util.HashMap;
import java.util.Map;

public class Keywords
{

	private final Map keywords;
	public static Keywords DEFAULT_KEYWORDS;

	public Keywords(Map keywords)
	{
		this.keywords = keywords;
	}

	public Integer getKeyword(String key)
	{
		return (Integer)keywords.get(key);
	}

	static 
	{
		Map map = new HashMap();
		map.put("null", Integer.valueOf(8));
		map.put("new", Integer.valueOf(9));
		map.put("true", Integer.valueOf(6));
		map.put("false", Integer.valueOf(7));
		DEFAULT_KEYWORDS = new Keywords(map);
	}
}
