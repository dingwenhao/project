// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileSerializer.java

package com.alibaba.fastjson.serializer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ObjectSerializer, JSONSerializer, SerializeWriter

public class FileSerializer
	implements ObjectSerializer
{

	public static FileSerializer instance = new FileSerializer();

	public FileSerializer()
	{
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			out.writeNull();
			return;
		} else
		{
			File file = (File)object;
			serializer.write(file.getPath());
			return;
		}
	}

}
