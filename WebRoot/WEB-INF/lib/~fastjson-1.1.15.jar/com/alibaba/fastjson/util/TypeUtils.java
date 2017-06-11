// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeUtils.java

package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Referenced classes of package com.alibaba.fastjson.util:
//			FieldInfo, Base64

public class TypeUtils
{

	private static Map mappings;

	public TypeUtils()
	{
	}

	public static final String castToString(Object value)
	{
		if (value == null)
			return null;
		else
			return value.toString();
	}

	public static final Byte castToByte(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Number)
			return Byte.valueOf(((Number)value).byteValue());
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			else
				return Byte.valueOf(Byte.parseByte(strVal));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to byte, value : ").append(value).toString());
		}
	}

	public static final Character castToChar(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Character)
			return (Character)value;
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			if (strVal.length() != 1)
				throw new JSONException((new StringBuilder()).append("can not cast to byte, value : ").append(value).toString());
			else
				return Character.valueOf(strVal.charAt(0));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to byte, value : ").append(value).toString());
		}
	}

	public static final Short castToShort(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Number)
			return Short.valueOf(((Number)value).shortValue());
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			else
				return Short.valueOf(Short.parseShort(strVal));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to short, value : ").append(value).toString());
		}
	}

	public static final BigDecimal castToBigDecimal(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof BigDecimal)
			return (BigDecimal)value;
		if (value instanceof BigInteger)
			return new BigDecimal((BigInteger)value);
		String strVal = value.toString();
		if (strVal.length() == 0)
			return null;
		else
			return new BigDecimal(strVal);
	}

	public static final BigInteger castToBigInteger(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof BigInteger)
			return (BigInteger)value;
		if ((value instanceof Float) || (value instanceof Double))
			return BigInteger.valueOf(((Number)value).longValue());
		String strVal = value.toString();
		if (strVal.length() == 0)
			return null;
		else
			return new BigInteger(strVal);
	}

	public static final Float castToFloat(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Number)
			return Float.valueOf(((Number)value).floatValue());
		if (value instanceof String)
		{
			String strVal = value.toString();
			if (strVal.length() == 0)
				return null;
			else
				return Float.valueOf(Float.parseFloat(strVal));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to float, value : ").append(value).toString());
		}
	}

	public static final Double castToDouble(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Number)
			return Double.valueOf(((Number)value).doubleValue());
		if (value instanceof String)
		{
			String strVal = value.toString();
			if (strVal.length() == 0)
				return null;
			else
				return Double.valueOf(Double.parseDouble(strVal));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to double, value : ").append(value).toString());
		}
	}

	public static final Date castToDate(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Calendar)
			return ((Calendar)value).getTime();
		if (value instanceof Date)
			return (Date)value;
		long longValue = 0L;
		if (value instanceof Number)
			longValue = ((Number)value).longValue();
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			longValue = Long.parseLong(strVal);
		}
		if (longValue <= 0L)
			throw new JSONException((new StringBuilder()).append("can not cast to Date, value : ").append(value).toString());
		else
			return new Date(longValue);
	}

	public static final java.sql.Date castToSqlDate(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Calendar)
			return new java.sql.Date(((Calendar)value).getTimeInMillis());
		if (value instanceof java.sql.Date)
			return (java.sql.Date)value;
		if (value instanceof Date)
			return new java.sql.Date(((Date)value).getTime());
		long longValue = 0L;
		if (value instanceof Number)
			longValue = ((Number)value).longValue();
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			longValue = Long.parseLong(strVal);
		}
		if (longValue <= 0L)
			throw new JSONException((new StringBuilder()).append("can not cast to Date, value : ").append(value).toString());
		else
			return new java.sql.Date(longValue);
	}

	public static final Timestamp castToTimestamp(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Calendar)
			return new Timestamp(((Calendar)value).getTimeInMillis());
		if (value instanceof Timestamp)
			return (Timestamp)value;
		if (value instanceof Date)
			return new Timestamp(((Date)value).getTime());
		long longValue = 0L;
		if (value instanceof Number)
			longValue = ((Number)value).longValue();
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			longValue = Long.parseLong(strVal);
		}
		if (longValue <= 0L)
			throw new JSONException((new StringBuilder()).append("can not cast to Date, value : ").append(value).toString());
		else
			return new Timestamp(longValue);
	}

	public static final Long castToLong(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Number)
			return Long.valueOf(((Number)value).longValue());
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			else
				return Long.valueOf(Long.parseLong(strVal));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to long, value : ").append(value).toString());
		}
	}

	public static final Integer castToInt(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Integer)
			return (Integer)value;
		if (value instanceof Number)
			return Integer.valueOf(((Number)value).intValue());
		if (value instanceof String)
		{
			String strVal = (String)value;
			if (strVal.length() == 0)
				return null;
			else
				return Integer.valueOf(Integer.parseInt(strVal));
		} else
		{
			throw new JSONException((new StringBuilder()).append("can not cast to int, value : ").append(value).toString());
		}
	}

	public static final byte[] castToBytes(Object value)
	{
		if (value instanceof byte[])
			return (byte[])(byte[])value;
		if (value instanceof String)
			return Base64.decodeFast((String)value);
		else
			throw new JSONException((new StringBuilder()).append("can not cast to int, value : ").append(value).toString());
	}

	public static final Boolean castToBoolean(Object value)
	{
		if (value == null)
			return null;
		if (value instanceof Boolean)
			return (Boolean)value;
		if (value instanceof Number)
			return Boolean.valueOf(((Number)value).intValue() == 1);
		if (value instanceof String)
		{
			String str = (String)value;
			if (str.length() == 0)
				return null;
			if ("true".equals(str))
				return Boolean.TRUE;
			if ("false".equals(str))
				return Boolean.FALSE;
			if ("1".equals(str))
				return Boolean.TRUE;
		}
		throw new JSONException((new StringBuilder()).append("can not cast to int, value : ").append(value).toString());
	}

	public static final Object castToJavaBean(Object obj, Class clazz)
	{
		return cast(obj, clazz, ParserConfig.getGlobalInstance());
	}

	public static final Object cast(Object obj, Class clazz, ParserConfig mapping)
	{
		if (obj == null)
			return null;
		if (clazz == obj.getClass())
			return obj;
		if (obj instanceof Map)
			if (clazz == java/util/Map)
				return obj;
			else
				return castToJavaBean((Map)obj, clazz, mapping);
		if (clazz.isArray() && (obj instanceof Collection))
		{
			Collection collection = (Collection)obj;
			int index = 0;
			Object array = Array.newInstance(clazz.getComponentType(), collection.size());
			for (Iterator i$ = collection.iterator(); i$.hasNext();)
			{
				Object item = i$.next();
				Object value = cast(item, clazz.getComponentType(), mapping);
				Array.set(array, index, value);
				index++;
			}

			return array;
		}
		if (clazz.isAssignableFrom(obj.getClass()))
			return obj;
		if (clazz == Boolean.TYPE || clazz == java/lang/Boolean)
			return castToBoolean(obj);
		if (clazz == Byte.TYPE || clazz == java/lang/Byte)
			return castToByte(obj);
		if (clazz == Short.TYPE || clazz == java/lang/Short)
			return castToShort(obj);
		if (clazz == Integer.TYPE || clazz == java/lang/Integer)
			return castToInt(obj);
		if (clazz == Long.TYPE || clazz == java/lang/Long)
			return castToLong(obj);
		if (clazz == Float.TYPE || clazz == java/lang/Float)
			return castToFloat(obj);
		if (clazz == Double.TYPE || clazz == java/lang/Double)
			return castToDouble(obj);
		if (clazz == java/lang/String)
			return castToString(obj);
		if (clazz == java/math/BigDecimal)
			return castToBigDecimal(obj);
		if (clazz == java/math/BigInteger)
			return castToBigInteger(obj);
		if (clazz == java/util/Date)
			return castToDate(obj);
		if (clazz == java/sql/Date)
			return castToSqlDate(obj);
		if (clazz == java/sql/Timestamp)
			return castToTimestamp(obj);
		if (clazz.isEnum())
			return castToEnum(obj, clazz, mapping);
		if (obj instanceof String)
		{
			String strVal = (String)obj;
			if (strVal.length() == 0)
				return null;
		}
		throw new JSONException((new StringBuilder()).append("can not cast to : ").append(clazz.getName()).toString());
	}

	public static final Object castToEnum(Object obj, Class clazz, ParserConfig mapping)
	{
		String name;
		if (!(obj instanceof String))
			break MISSING_BLOCK_LABEL_27;
		name = (String)obj;
		if (name.length() == 0)
			return null;
		return Enum.valueOf(clazz, name);
		int ordinal;
		Object arr$[];
		int len$;
		int i$;
		if (!(obj instanceof Number))
			break MISSING_BLOCK_LABEL_158;
		ordinal = ((Number)obj).intValue();
		Method method = clazz.getMethod("values", new Class[0]);
		Object values[] = (Object[])(Object[])method.invoke(null, new Object[0]);
		arr$ = values;
		len$ = arr$.length;
		i$ = 0;
_L1:
		Enum e;
		if (i$ >= len$)
			break MISSING_BLOCK_LABEL_158;
		Object value = arr$[i$];
		e = (Enum)value;
		if (e.ordinal() == ordinal)
			return e;
		try
		{
			i$++;
		}
		catch (Exception ex)
		{
			throw new JSONException((new StringBuilder()).append("can not cast to : ").append(clazz.getName()).toString(), ex);
		}
		  goto _L1
		throw new JSONException((new StringBuilder()).append("can not cast to : ").append(clazz.getName()).toString());
	}

	public static final Object cast(Object obj, Type type, ParserConfig mapping)
	{
		if (obj == null)
			return null;
		if (type instanceof Class)
			return cast(obj, (Class)type, mapping);
		if (type instanceof ParameterizedType)
			return cast(obj, (ParameterizedType)type, mapping);
		if (obj instanceof String)
		{
			String strVal = (String)obj;
			if (strVal.length() == 0)
				return null;
		}
		if (type instanceof TypeVariable)
			return obj;
		else
			throw new JSONException((new StringBuilder()).append("can not cast to : ").append(type).toString());
	}

	public static final Object cast(Object obj, ParameterizedType type, ParserConfig mapping)
	{
		Type rawTye = type.getRawType();
		if (rawTye == java/util/List || rawTye == java/util/ArrayList)
		{
			Type itemType = type.getActualTypeArguments()[0];
			if (obj instanceof Iterable)
			{
				List list = new ArrayList();
				Object item;
				for (Iterator it = ((Iterable)obj).iterator(); it.hasNext(); list.add(cast(item, itemType, mapping)))
					item = it.next();

				return list;
			}
		}
		if (rawTye == java/util/Map || rawTye == java/util/HashMap)
		{
			Type keyType = type.getActualTypeArguments()[0];
			Type valueType = type.getActualTypeArguments()[1];
			if (obj instanceof Map)
			{
				Map map = new HashMap();
				Object key;
				Object value;
				for (Iterator i$ = ((Map)obj).entrySet().iterator(); i$.hasNext(); map.put(key, value))
				{
					java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
					key = cast(entry.getKey(), keyType, mapping);
					value = cast(entry.getValue(), valueType, mapping);
				}

				return map;
			}
		}
		if (obj instanceof String)
		{
			String strVal = (String)obj;
			if (strVal.length() == 0)
				return null;
		}
		if (type.getActualTypeArguments().length == 1)
		{
			Type argType = type.getActualTypeArguments()[0];
			if (argType instanceof WildcardType)
				return cast(obj, rawTye, mapping);
		}
		throw new JSONException((new StringBuilder()).append("can not cast to : ").append(type).toString());
	}

	public static final Object castToJavaBean(Map map, Class clazz, ParserConfig mapping)
	{
		String declaringClass;
		String methodName;
		String fileName;
		int lineNumber;
		if (clazz != java/lang/StackTraceElement)
			break MISSING_BLOCK_LABEL_91;
		declaringClass = (String)map.get("className");
		methodName = (String)map.get("methodName");
		fileName = (String)map.get("fileName");
		Number value = (Number)map.get("lineNumber");
		if (value == null)
			lineNumber = 0;
		else
			lineNumber = value.intValue();
		return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
		JSONObject object;
		Object iClassObject = map.get("@type");
		if (iClassObject instanceof String)
		{
			String className = (String)iClassObject;
			clazz = loadClass(className);
		}
		if (!clazz.isInterface())
			break MISSING_BLOCK_LABEL_169;
		if (map instanceof JSONObject)
			object = (JSONObject)map;
		else
			object = new JSONObject(map);
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {
			clazz
		}, object);
		Object object;
		Map setters = mapping.getFieldDeserializers(clazz);
		object = clazz.newInstance();
		Iterator i$ = setters.entrySet().iterator();
		do
		{
			if (!i$.hasNext())
				break;
			java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
			String key = (String)entry.getKey();
			Method method = ((FieldDeserializer)entry.getValue()).getMethod();
			if (map.containsKey(key))
			{
				Object value = map.get(key);
				value = cast(value, method.getGenericParameterTypes()[0], mapping);
				method.invoke(object, new Object[] {
					value
				});
			}
		} while (true);
		return object;
		Exception e;
		e;
		throw new JSONException(e.getMessage(), e);
	}

	public static Class loadClass(String className)
	{
		Class clazz;
		if (className == null || className.length() == 0)
			return null;
		clazz = (Class)mappings.get(className);
		if (clazz != null)
			return clazz;
		if (className.charAt(0) == '[')
		{
			Class componentType = loadClass(className.substring(1));
			return Array.newInstance(componentType, 0).getClass();
		}
		clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
		return clazz;
		Throwable e;
		e;
		clazz = Class.forName(className);
		return clazz;
		e;
		return clazz;
	}

	public static List computeGetters(Class clazz, Map aliasMap)
	{
		List fieldInfoList = new ArrayList();
		Map fieldInfoMap = new LinkedHashMap();
		Method arr$[] = clazz.getMethods();
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Method method = arr$[i$];
			String methodName = method.getName();
			if (Modifier.isStatic(method.getModifiers()) || method.getReturnType().equals(Void.TYPE) || method.getParameterTypes().length != 0)
				continue;
			JSONField annotation = (JSONField)method.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
			String propertyName;
			if (annotation != null)
			{
				if (!annotation.serialize())
					continue;
				if (annotation.name().length() != 0)
				{
					propertyName = annotation.name();
					if (aliasMap != null)
					{
						propertyName = (String)aliasMap.get(propertyName);
						if (propertyName == null)
							continue;
					}
					fieldInfoMap.put(propertyName, new FieldInfo(propertyName, method, null));
					continue;
				}
			}
			Field field;
			if (methodName.startsWith("get"))
			{
				if (methodName.length() < 4 || methodName.equals("getClass") || !Character.isUpperCase(methodName.charAt(3)))
					continue;
				propertyName = (new StringBuilder()).append(Character.toLowerCase(methodName.charAt(3))).append(methodName.substring(4)).toString();
				field = ParserConfig.getField(clazz, propertyName);
				if (field != null)
				{
					JSONField fieldAnnotation = (JSONField)field.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
					if (fieldAnnotation != null && fieldAnnotation.name().length() != 0)
					{
						propertyName = fieldAnnotation.name();
						if (aliasMap != null)
						{
							propertyName = (String)aliasMap.get(propertyName);
							if (propertyName == null)
								continue;
						}
					}
				}
				if (aliasMap != null)
				{
					propertyName = (String)aliasMap.get(propertyName);
					if (propertyName == null)
						continue;
				}
				fieldInfoMap.put(propertyName, new FieldInfo(propertyName, method, field));
			}
			if (!methodName.startsWith("is") || methodName.length() < 3 || !Character.isUpperCase(methodName.charAt(2)))
				continue;
			propertyName = (new StringBuilder()).append(Character.toLowerCase(methodName.charAt(2))).append(methodName.substring(3)).toString();
			field = ParserConfig.getField(clazz, propertyName);
			if (field != null)
			{
				JSONField fieldAnnotation = (JSONField)field.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
				if (fieldAnnotation != null && fieldAnnotation.name().length() != 0)
				{
					propertyName = fieldAnnotation.name();
					if (aliasMap != null)
					{
						propertyName = (String)aliasMap.get(propertyName);
						if (propertyName == null)
							continue;
					}
				}
			}
			if (aliasMap != null)
			{
				propertyName = (String)aliasMap.get(propertyName);
				if (propertyName == null)
					continue;
			}
			fieldInfoMap.put(propertyName, new FieldInfo(propertyName, method, field));
		}

		arr$ = clazz.getFields();
		len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Field field = arr$[i$];
			if (!Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()) && !fieldInfoMap.containsKey(field.getName()))
				fieldInfoMap.put(field.getName(), new FieldInfo(field.getName(), null, field));
		}

		FieldInfo fieldInfo;
		for (Iterator i$ = fieldInfoMap.values().iterator(); i$.hasNext(); fieldInfoList.add(fieldInfo))
			fieldInfo = (FieldInfo)i$.next();

		return fieldInfoList;
	}

	static 
	{
		mappings = new HashMap();
		mappings.put("byte", Byte.TYPE);
		mappings.put("short", Short.TYPE);
		mappings.put("int", Integer.TYPE);
		mappings.put("long", Long.TYPE);
		mappings.put("float", Float.TYPE);
		mappings.put("double", Double.TYPE);
		mappings.put("boolean", Boolean.TYPE);
		mappings.put("char", Character.TYPE);
		mappings.put("[byte", [B);
		mappings.put("[short", [S);
		mappings.put("[int", [I);
		mappings.put("[long", [J);
		mappings.put("[float", [F);
		mappings.put("[double", [D);
		mappings.put("[boolean", [Z);
		mappings.put("[char", [C);
	}
}
