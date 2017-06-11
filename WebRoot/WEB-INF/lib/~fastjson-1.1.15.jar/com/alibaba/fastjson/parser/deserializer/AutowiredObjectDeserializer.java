// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AutowiredObjectDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ObjectDeserializer

public interface AutowiredObjectDeserializer
	extends ObjectDeserializer
{

	public abstract Set getAutowiredFor();
}
