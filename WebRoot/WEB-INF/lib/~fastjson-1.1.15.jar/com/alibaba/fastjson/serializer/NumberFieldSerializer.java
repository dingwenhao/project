// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NumberFieldSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.util.FieldInfo;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			FieldSerializer, JSONSerializer, SerializerFeature, SerializeWriter

class NumberFieldSerializer extends FieldSerializer
{

	public NumberFieldSerializer(FieldInfo fieldInfo)
	{
		super(fieldInfo);
	}

	public void writeProperty(JSONSerializer serializer, Object propertyValue)
		throws Exception
	{
		SerializeWriter out = serializer.getWriter();
		writePrefix(serializer);
		Object value = propertyValue;
		if (value == null)
		{
			if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero))
				out.write('0');
			else
				out.writeNull();
			return;
		} else
		{
			out.append(value.toString());
			return;
		}
	}
}
