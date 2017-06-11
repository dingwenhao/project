// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONObject.java

package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson:
//			JSON, JSONArray, JSONException, JSONAware

public class JSONObject extends JSON
	implements Map, JSONAware, Cloneable, Serializable, InvocationHandler
{

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	private final Map map;

	public JSONObject()
	{
		this(16, false);
	}

	public JSONObject(Map map)
	{
		this.map = map;
	}

	public JSONObject(boolean ordered)
	{
		this(16, ordered);
	}

	public JSONObject(int initialCapacity)
	{
		this(initialCapacity, false);
	}

	public JSONObject(int initialCapacity, boolean ordered)
	{
		if (ordered)
			map = new LinkedHashMap(initialCapacity);
		else
			map = new HashMap(initialCapacity);
	}

	public int size()
	{
		return map.size();
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}

	public Object get(Object key)
	{
		return map.get(key);
	}

	public JSONObject getJSONObject(String key)
	{
		Object value = map.get(key);
		if (value instanceof JSONObject)
			return (JSONObject)value;
		else
			return (JSONObject)toJSON(value);
	}

	public JSONArray getJSONArray(String key)
	{
		Object value = map.get(key);
		if (value instanceof JSONArray)
			return (JSONArray)value;
		else
			return (JSONArray)toJSON(value);
	}

	public Object getObject(String key, Class clazz)
	{
		Object obj = map.get(key);
		return TypeUtils.castToJavaBean(obj, clazz);
	}

	public Boolean getBoolean(String key)
	{
		Object value = get(key);
		if (value == null)
			return null;
		else
			return TypeUtils.castToBoolean(value);
	}

	public byte[] getBytes(String key)
	{
		Object value = get(key);
		if (value == null)
			return null;
		else
			return TypeUtils.castToBytes(value);
	}

	public boolean getBooleanValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return false;
		else
			return TypeUtils.castToBoolean(value).booleanValue();
	}

	public Byte getByte(String key)
	{
		Object value = get(key);
		return TypeUtils.castToByte(value);
	}

	public byte getByteValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return 0;
		else
			return TypeUtils.castToByte(value).byteValue();
	}

	public Short getShort(String key)
	{
		Object value = get(key);
		return TypeUtils.castToShort(value);
	}

	public short getShortValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return 0;
		else
			return TypeUtils.castToShort(value).shortValue();
	}

	public Integer getInteger(String key)
	{
		Object value = get(key);
		return TypeUtils.castToInt(value);
	}

	public int getIntValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return 0;
		else
			return TypeUtils.castToInt(value).intValue();
	}

	public Long getLong(String key)
	{
		Object value = get(key);
		return TypeUtils.castToLong(value);
	}

	public long getLongValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return 0L;
		else
			return TypeUtils.castToLong(value).longValue();
	}

	public Float getFloat(String key)
	{
		Object value = get(key);
		return TypeUtils.castToFloat(value);
	}

	public float getFloatValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return 0.0F;
		else
			return TypeUtils.castToFloat(value).floatValue();
	}

	public Double getDouble(String key)
	{
		Object value = get(key);
		return TypeUtils.castToDouble(value);
	}

	public double getDoubleValue(String key)
	{
		Object value = get(key);
		if (value == null)
			return 0.0D;
		else
			return (double)TypeUtils.castToDouble(value).floatValue();
	}

	public BigDecimal getBigDecimal(String key)
	{
		Object value = get(key);
		return TypeUtils.castToBigDecimal(value);
	}

	public BigInteger getBigInteger(String key)
	{
		Object value = get(key);
		return TypeUtils.castToBigInteger(value);
	}

	public String getString(String key)
	{
		Object value = get(key);
		if (value == null)
			return null;
		else
			return value.toString();
	}

	public Date getDate(String key)
	{
		Object value = get(key);
		return TypeUtils.castToDate(value);
	}

	public java.sql.Date getSqlDate(String key)
	{
		Object value = get(key);
		return TypeUtils.castToSqlDate(value);
	}

	public Timestamp getTimestamp(String key)
	{
		Object value = get(key);
		return TypeUtils.castToTimestamp(value);
	}

	public Object put(String key, Object value)
	{
		return map.put(key, value);
	}

	public void putAll(Map m)
	{
		map.putAll(m);
	}

	public void clear()
	{
		map.clear();
	}

	public Object remove(Object key)
	{
		return map.remove(key);
	}

	public Set keySet()
	{
		return map.keySet();
	}

	public Collection values()
	{
		return map.values();
	}

	public Set entrySet()
	{
		return map.entrySet();
	}

	public Object clone()
	{
		return new JSONObject(new HashMap(map));
	}

	public boolean equals(Object obj)
	{
		return map.equals(obj);
	}

	public int hashCode()
	{
		return map.hashCode();
	}

	public Object invoke(Object proxy, Method method, Object args[])
		throws Throwable
	{
		Class parameterTypes[] = method.getParameterTypes();
		if (parameterTypes.length == 1)
		{
			Class returnType = method.getReturnType();
			if (returnType != Void.TYPE)
				throw new JSONException("illegal setter");
			String name = null;
			JSONField annotation = (JSONField)method.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
			if (annotation != null && annotation.name().length() != 0)
				name = annotation.name();
			if (name == null)
			{
				name = method.getName();
				if (!name.startsWith("set"))
					throw new JSONException("illegal setter");
				name = name.substring(3);
				if (name.length() == 0)
					throw new JSONException("illegal setter");
				name = (new StringBuilder()).append(Character.toLowerCase(name.charAt(0))).append(name.substring(1)).toString();
			}
			map.put(name, args[0]);
			return null;
		}
		if (parameterTypes.length == 0)
		{
			Class returnType = method.getReturnType();
			if (returnType == Void.TYPE)
				throw new JSONException("illegal getter");
			String name = null;
			JSONField annotation = (JSONField)method.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
			if (annotation != null && annotation.name().length() != 0)
				name = annotation.name();
			if (name == null)
			{
				name = method.getName();
				if (name.startsWith("get"))
				{
					name = name.substring(3);
					if (name.length() == 0)
						throw new JSONException("illegal getter");
					name = (new StringBuilder()).append(Character.toLowerCase(name.charAt(0))).append(name.substring(1)).toString();
				} else
				if (name.startsWith("is"))
				{
					name = name.substring(2);
					if (name.length() == 0)
						throw new JSONException("illegal getter");
					name = (new StringBuilder()).append(Character.toLowerCase(name.charAt(0))).append(name.substring(1)).toString();
				} else
				{
					throw new JSONException("illegal getter");
				}
			}
			Object value = map.get(name);
			return TypeUtils.cast(value, method.getGenericReturnType(), ParserConfig.getGlobalInstance());
		} else
		{
			throw new UnsupportedOperationException(method.toGenericString());
		}
	}

	public volatile Object put(Object x0, Object x1)
	{
		return put((String)x0, x1);
	}
}
