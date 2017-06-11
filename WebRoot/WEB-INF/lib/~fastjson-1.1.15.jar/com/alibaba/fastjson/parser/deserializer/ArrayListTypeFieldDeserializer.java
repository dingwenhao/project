// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayListTypeFieldDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.*;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer

public class ArrayListTypeFieldDeserializer extends FieldDeserializer
{

	private final Type itemType;
	private int itemFastMatchToken;
	private ObjectDeserializer deserializer;

	public ArrayListTypeFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		super(clazz, fieldInfo);
		Type fieldType = getFieldType();
		if (fieldType instanceof ParameterizedType)
			itemType = ((ParameterizedType)getFieldType()).getActualTypeArguments()[0];
		else
			itemType = java/lang/Object;
	}

	public int getFastMatchToken()
	{
		return 14;
	}

	public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map fieldValues)
	{
		if (parser.getLexer().token() == 8)
		{
			setValue(object, null);
			return;
		}
		ArrayList list = new ArrayList();
		com.alibaba.fastjson.parser.ParseContext context = parser.getContext();
		parser.setContext(context, object, fieldInfo.getName());
		parseArray(parser, objectType, list);
		parser.setContext(context);
		if (object == null)
			fieldValues.put(fieldInfo.getName(), list);
		else
			setValue(object, list);
	}

	public final void parseArray(DefaultJSONParser parser, Type objectType, Collection array)
	{
		Type itemType = this.itemType;
		if ((itemType instanceof TypeVariable) && (objectType instanceof ParameterizedType))
		{
			TypeVariable typeVar = (TypeVariable)itemType;
			ParameterizedType paramType = (ParameterizedType)objectType;
			Class objectClass = null;
			if (paramType.getRawType() instanceof Class)
				objectClass = (Class)paramType.getRawType();
			int paramIndex = -1;
			if (objectClass != null)
			{
				int i = 0;
				int size = objectClass.getTypeParameters().length;
				do
				{
					if (i >= size)
						break;
					TypeVariable item = objectClass.getTypeParameters()[i];
					if (item.getName().equals(typeVar.getName()))
					{
						paramIndex = i;
						break;
					}
					i++;
				} while (true);
			}
			if (paramIndex != -1)
				itemType = paramType.getActualTypeArguments()[paramIndex];
		}
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() != 14)
			throw new JSONException((new StringBuilder()).append("exepct '[', but ").append(JSONToken.name(lexer.token())).toString());
		if (deserializer == null)
		{
			deserializer = parser.getConfig().getDeserializer(itemType);
			itemFastMatchToken = deserializer.getFastMatchToken();
		}
		lexer.nextToken(itemFastMatchToken);
		int i = 0;
		do
		{
			if (lexer.isEnabled(Feature.AllowArbitraryCommas))
				for (; lexer.token() == 16; lexer.nextToken());
			if (lexer.token() != 15)
			{
				Object val = deserializer.deserialze(parser, itemType, Integer.valueOf(i));
				array.add(val);
				parser.checkListResolve(array);
				if (lexer.token() == 16)
					lexer.nextToken(itemFastMatchToken);
				i++;
			} else
			{
				lexer.nextToken(16);
				return;
			}
		} while (true);
	}
}
