// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ASMJavaBeanDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.Map;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer, JavaBeanDeserializer

public abstract class ASMJavaBeanDeserializer
	implements ObjectDeserializer
{
	public final class InnerJavaBeanDeserializer extends JavaBeanDeserializer
	{

		final ASMJavaBeanDeserializer this$0;

		public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map fieldValues)
		{
			return ASMJavaBeanDeserializer.this.parseField(parser, key, object, objectType, fieldValues);
		}

		public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
		{
			return ASMJavaBeanDeserializer.this.createFieldDeserializer(mapping, clazz, fieldInfo);
		}

		private InnerJavaBeanDeserializer(ParserConfig mapping, Class clazz)
		{
			this$0 = ASMJavaBeanDeserializer.this;
			super(mapping, clazz);
		}

	}


	protected InnerJavaBeanDeserializer serializer;

	public ASMJavaBeanDeserializer(ParserConfig mapping, Class clazz)
	{
		serializer = new InnerJavaBeanDeserializer(mapping, clazz);
		serializer.getFieldDeserializerMap();
	}

	public abstract Object createInstance(DefaultJSONParser defaultjsonparser, Type type);

	public InnerJavaBeanDeserializer getInnterSerializer()
	{
		return serializer;
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		return serializer.deserialze(parser, type, fieldName);
	}

	public int getFastMatchToken()
	{
		return serializer.getFastMatchToken();
	}

	public Object createInstance(DefaultJSONParser parser)
	{
		return serializer.createInstance(parser, serializer.getClazz());
	}

	public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
	}

	public FieldDeserializer getFieldDeserializer(String name)
	{
		return (FieldDeserializer)serializer.getFieldDeserializerMap().get(name);
	}

	public Type getFieldType(String name)
	{
		return ((FieldDeserializer)serializer.getFieldDeserializerMap().get(name)).getFieldType();
	}

	public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map fieldValues)
	{
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		FieldDeserializer fieldDeserializer = (FieldDeserializer)serializer.getFieldDeserializerMap().get(key);
		if (fieldDeserializer == null)
		{
			if (!parser.isEnabled(Feature.IgnoreNotMatch))
			{
				throw new JSONException((new StringBuilder()).append("setter not found, class ").append(serializer.getClass()).append(", property ").append(key).toString());
			} else
			{
				lexer.nextTokenWithColon();
				parser.parse();
				return false;
			}
		} else
		{
			lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
			fieldDeserializer.parseField(parser, object, objectType, fieldValues);
			return true;
		}
	}
}
