// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CharacterSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter

public class CharacterSerializer
	implements ObjectSerializer
{

	public static final CharacterSerializer instance = new CharacterSerializer();

	public CharacterSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		Character value = (Character)object;
		if (value == null)
		{
			out.writeString("");
			return;
		}
		char c = value.charValue();
		if (c == 0)
			out.writeString("\0");
		else
			out.writeString(value.toString());
	}

}
