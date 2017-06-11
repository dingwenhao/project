// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JavaBeanMapping.java

package com.alibaba.fastjson.parser;


// Referenced classes of package com.alibaba.fastjson.parser:
//			ParserConfig

/**
 * @deprecated Class JavaBeanMapping is deprecated
 */

public class JavaBeanMapping extends ParserConfig
{

	private static final JavaBeanMapping instance = new JavaBeanMapping();

	public JavaBeanMapping()
	{
	}

	public static JavaBeanMapping getGlobalInstance()
	{
		return instance;
	}

}
