// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONArray.java

package com.alibaba.fastjson;

import com.alibaba.fastjson.util.TypeUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

// Referenced classes of package com.alibaba.fastjson:
//			JSON, JSONObject, JSONAware

public class JSONArray extends JSON
	implements List, JSONAware, Cloneable, RandomAccess, Serializable
{

	private static final long serialVersionUID = 1L;
	private final List list;

	public JSONArray()
	{
		list = new ArrayList(10);
	}

	public JSONArray(List list)
	{
		this.list = list;
	}

	public JSONArray(int initialCapacity)
	{
		list = new ArrayList(initialCapacity);
	}

	public int size()
	{
		return list.size();
	}

	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	public boolean contains(Object o)
	{
		return list.contains(o);
	}

	public Iterator iterator()
	{
		return list.iterator();
	}

	public Object[] toArray()
	{
		return list.toArray();
	}

	public Object[] toArray(Object a[])
	{
		return list.toArray(a);
	}

	public boolean add(Object e)
	{
		return list.add(e);
	}

	public boolean remove(Object o)
	{
		return list.remove(o);
	}

	public boolean containsAll(Collection c)
	{
		return list.containsAll(c);
	}

	public boolean addAll(Collection c)
	{
		return list.addAll(c);
	}

	public boolean addAll(int index, Collection c)
	{
		return list.addAll(index, c);
	}

	public boolean removeAll(Collection c)
	{
		return list.removeAll(c);
	}

	public boolean retainAll(Collection c)
	{
		return list.retainAll(c);
	}

	public void clear()
	{
		list.clear();
	}

	public Object set(int index, Object element)
	{
		return list.set(index, element);
	}

	public void add(int index, Object element)
	{
		list.add(index, element);
	}

	public Object remove(int index)
	{
		return list.remove(index);
	}

	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}

	public ListIterator listIterator()
	{
		return list.listIterator();
	}

	public ListIterator listIterator(int index)
	{
		return list.listIterator(index);
	}

	public List subList(int fromIndex, int toIndex)
	{
		return list.subList(fromIndex, toIndex);
	}

	public Object get(int index)
	{
		return list.get(index);
	}

	public JSONObject getJSONObject(int index)
	{
		Object value = list.get(index);
		if (value instanceof JSONObject)
			return (JSONObject)value;
		else
			return (JSONObject)toJSON(value);
	}

	public JSONArray getJSONArray(int index)
	{
		Object value = list.get(index);
		if (value instanceof JSONArray)
			return (JSONArray)value;
		else
			return (JSONArray)toJSON(value);
	}

	public Object getObject(int index, Class clazz)
	{
		Object obj = list.get(index);
		return TypeUtils.castToJavaBean(obj, clazz);
	}

	public Boolean getBoolean(int index)
	{
		Object value = get(index);
		if (value == null)
			return null;
		else
			return TypeUtils.castToBoolean(value);
	}

	public boolean getBooleanValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return false;
		else
			return TypeUtils.castToBoolean(value).booleanValue();
	}

	public Byte getByte(int index)
	{
		Object value = get(index);
		return TypeUtils.castToByte(value);
	}

	public byte getByteValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return 0;
		else
			return TypeUtils.castToByte(value).byteValue();
	}

	public Short getShort(int index)
	{
		Object value = get(index);
		return TypeUtils.castToShort(value);
	}

	public short getShortValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return 0;
		else
			return TypeUtils.castToShort(value).shortValue();
	}

	public Integer getInteger(int index)
	{
		Object value = get(index);
		return TypeUtils.castToInt(value);
	}

	public int getIntValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return 0;
		else
			return TypeUtils.castToInt(value).intValue();
	}

	public Long getLong(int index)
	{
		Object value = get(index);
		return TypeUtils.castToLong(value);
	}

	public long getLongValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return 0L;
		else
			return TypeUtils.castToLong(value).longValue();
	}

	public Float getFloat(int index)
	{
		Object value = get(index);
		return TypeUtils.castToFloat(value);
	}

	public float getFloatValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return 0.0F;
		else
			return TypeUtils.castToFloat(value).floatValue();
	}

	public Double getDouble(int index)
	{
		Object value = get(index);
		return TypeUtils.castToDouble(value);
	}

	public double getDoubleValue(int index)
	{
		Object value = get(index);
		if (value == null)
			return 0.0D;
		else
			return (double)TypeUtils.castToDouble(value).floatValue();
	}

	public BigDecimal getBigDecimal(int index)
	{
		Object value = get(index);
		return TypeUtils.castToBigDecimal(value);
	}

	public BigInteger getBigInteger(int index)
	{
		Object value = get(index);
		return TypeUtils.castToBigInteger(value);
	}

	public String getString(int index)
	{
		Object value = get(index);
		return TypeUtils.castToString(value);
	}

	public Date getDate(int index)
	{
		Object value = get(index);
		return TypeUtils.castToDate(value);
	}

	public java.sql.Date getSqlDate(int index)
	{
		Object value = get(index);
		return TypeUtils.castToSqlDate(value);
	}

	public Timestamp getTimestamp(int index)
	{
		Object value = get(index);
		return TypeUtils.castToTimestamp(value);
	}

	public Object clone()
	{
		return new JSONArray(new ArrayList(list));
	}

	public boolean equals(Object obj)
	{
		return list.equals(obj);
	}

	public int hashCode()
	{
		return list.hashCode();
	}
}
