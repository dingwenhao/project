// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JavaBeanSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			FieldSerializer, NumberFieldSerializer, ObjectFieldSerializer, ObjectSerializer, 
//			JSONSerializer, SerializeWriter, SerializerFeature, FilterUtils

public class JavaBeanSerializer
	implements ObjectSerializer
{

	private final FieldSerializer getters[];

	public FieldSerializer[] getGetters()
	{
		return getters;
	}

	public JavaBeanSerializer(Class clazz)
	{
		this(clazz, (Map)null);
	}

	public transient JavaBeanSerializer(Class clazz, String aliasList[])
	{
		this(clazz, createAliasMap(aliasList));
	}

	static transient Map createAliasMap(String aliasList[])
	{
		Map aliasMap = new HashMap();
		String arr$[] = aliasList;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			String alias = arr$[i$];
			aliasMap.put(alias, alias);
		}

		return aliasMap;
	}

	public JavaBeanSerializer(Class clazz, Map aliasMap)
	{
		List getterList = new ArrayList();
		List fieldInfoList = TypeUtils.computeGetters(clazz, aliasMap);
		FieldInfo fieldInfo;
		for (Iterator i$ = fieldInfoList.iterator(); i$.hasNext(); getterList.add(createFieldSerializer(fieldInfo)))
			fieldInfo = (FieldInfo)i$.next();

		getters = (FieldSerializer[])getterList.toArray(new FieldSerializer[getterList.size()]);
	}

	protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object fieldName)
	{
		return serializer.isWriteClassName(fieldType, obj);
	}

	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType)
		throws IOException
	{
		Exception exception;
		SerializeWriter out = serializer.getWriter();
		if (object == null)
		{
			out.writeNull();
			return;
		}
		if (serializer.containsReference(object))
		{
			writeReference(serializer, object);
			return;
		}
		FieldSerializer getters[] = this.getters;
		if (out.isEnabled(SerializerFeature.SortField))
			Arrays.sort(getters);
		SerialContext parent = serializer.getContext();
		serializer.setContext(parent, object, fieldName);
		try
		{
			out.append('{');
			if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat))
			{
				serializer.incrementIndent();
				serializer.println();
			}
			boolean commaFlag = false;
			if (isWriteClassName(serializer, object, fieldType, fieldName))
			{
				Class objClass = object.getClass();
				if (objClass != fieldType)
				{
					out.writeFieldName("@type");
					serializer.write(object.getClass());
					commaFlag = true;
				}
			}
			for (int i = 0; i < getters.length; i++)
			{
				FieldSerializer fieldSerializer = getters[i];
				if (serializer.isEnabled(SerializerFeature.SkipTransientField))
				{
					Field field = fieldSerializer.getField();
					if (field != null && Modifier.isTransient(field.getModifiers()))
						continue;
				}
				Object propertyValue = fieldSerializer.getPropertyValue(object);
				if (!FilterUtils.apply(serializer, object, fieldSerializer.getName(), propertyValue))
					continue;
				String key = FilterUtils.processKey(serializer, object, fieldSerializer.getName(), propertyValue);
				Object originalValue = propertyValue;
				propertyValue = FilterUtils.processValue(serializer, object, fieldSerializer.getName(), propertyValue);
				if (propertyValue == null && !fieldSerializer.isWriteNull() && !serializer.isEnabled(SerializerFeature.WriteMapNullValue))
					continue;
				if (commaFlag)
				{
					out.append(',');
					if (out.isEnabled(SerializerFeature.PrettyFormat))
						serializer.println();
				}
				if (key != fieldSerializer.getName())
				{
					out.writeFieldName(key);
					serializer.write(propertyValue);
				} else
				if (originalValue != propertyValue)
				{
					fieldSerializer.writePrefix(serializer);
					serializer.write(propertyValue);
				} else
				{
					fieldSerializer.writeProperty(serializer, propertyValue);
				}
				commaFlag = true;
			}

			if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat))
			{
				serializer.decrementIdent();
				serializer.println();
			}
			out.append('}');
		}
		catch (Exception e)
		{
			throw new JSONException("write javaBean error", e);
		}
		finally
		{
			serializer.setContext(parent);
		}
		serializer.setContext(parent);
		break MISSING_BLOCK_LABEL_444;
		throw exception;
	}

	public void writeReference(JSONSerializer serializer, Object object)
	{
		serializer.writeReference(object);
	}

	public FieldSerializer createFieldSerializer(FieldInfo fieldInfo)
	{
		Class clazz = fieldInfo.getFieldClass();
		if (clazz == java/lang/Number)
			return new NumberFieldSerializer(fieldInfo);
		else
			return new ObjectFieldSerializer(fieldInfo);
	}
}
