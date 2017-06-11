// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParserConfig.java

package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.asm.ASMException;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.ArrayListStringDeserializer;
import com.alibaba.fastjson.parser.deserializer.ArrayListStringFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeDeserializer;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.AtomicIntegerArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.AtomicLongArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.BigDecimalDeserializer;
import com.alibaba.fastjson.parser.deserializer.BigIntegerDeserializer;
import com.alibaba.fastjson.parser.deserializer.BooleanDeserializer;
import com.alibaba.fastjson.parser.deserializer.BooleanFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.CharArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.CharacterDeserializer;
import com.alibaba.fastjson.parser.deserializer.CharsetDeserializer;
import com.alibaba.fastjson.parser.deserializer.ClassDerializer;
import com.alibaba.fastjson.parser.deserializer.CollectionDeserializer;
import com.alibaba.fastjson.parser.deserializer.DateDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FileDeserializer;
import com.alibaba.fastjson.parser.deserializer.FloatDeserializer;
import com.alibaba.fastjson.parser.deserializer.InetAddressDeserializer;
import com.alibaba.fastjson.parser.deserializer.InetSocketAddressDeserializer;
import com.alibaba.fastjson.parser.deserializer.IntegerDeserializer;
import com.alibaba.fastjson.parser.deserializer.IntegerFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.LocaleDeserializer;
import com.alibaba.fastjson.parser.deserializer.LongDeserializer;
import com.alibaba.fastjson.parser.deserializer.LongFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.PatternDeserializer;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.StringDeserializer;
import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeZoneDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimestampDeserializer;
import com.alibaba.fastjson.parser.deserializer.URIDeserializer;
import com.alibaba.fastjson.parser.deserializer.URLDeserializer;
import com.alibaba.fastjson.parser.deserializer.UUIDDeserializer;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.ServiceLoader;
import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.regex.Pattern;

// Referenced classes of package com.alibaba.fastjson.parser:
//			SymbolTable

public class ParserConfig
{

	private final Set primitiveClasses = new HashSet();
	private static ParserConfig global = new ParserConfig();
	private final IdentityHashMap derializers = new IdentityHashMap();
	private DefaultObjectDeserializer defaultSerializer;
	private boolean asmEnable;
	protected final SymbolTable symbolTable = new SymbolTable();

	public static ParserConfig getGlobalInstance()
	{
		return global;
	}

	public DefaultObjectDeserializer getDefaultSerializer()
	{
		return defaultSerializer;
	}

	public ParserConfig()
	{
		defaultSerializer = new DefaultObjectDeserializer();
		asmEnable = !ASMUtils.isAndroid();
		primitiveClasses.add(Boolean.TYPE);
		primitiveClasses.add(java/lang/Boolean);
		primitiveClasses.add(Character.TYPE);
		primitiveClasses.add(java/lang/Character);
		primitiveClasses.add(Byte.TYPE);
		primitiveClasses.add(java/lang/Byte);
		primitiveClasses.add(Short.TYPE);
		primitiveClasses.add(java/lang/Short);
		primitiveClasses.add(Integer.TYPE);
		primitiveClasses.add(java/lang/Integer);
		primitiveClasses.add(Long.TYPE);
		primitiveClasses.add(java/lang/Long);
		primitiveClasses.add(Float.TYPE);
		primitiveClasses.add(java/lang/Float);
		primitiveClasses.add(Double.TYPE);
		primitiveClasses.add(java/lang/Double);
		primitiveClasses.add(java/math/BigInteger);
		primitiveClasses.add(java/math/BigDecimal);
		primitiveClasses.add(java/lang/String);
		primitiveClasses.add(java/util/Date);
		primitiveClasses.add(java/sql/Date);
		primitiveClasses.add(java/sql/Time);
		primitiveClasses.add(java/sql/Timestamp);
		derializers.put(java/sql/Timestamp, TimestampDeserializer.instance);
		derializers.put(java/sql/Date, SqlDateDeserializer.instance);
		derializers.put(java/sql/Time, TimeDeserializer.instance);
		derializers.put(java/util/Date, DateDeserializer.instance);
		derializers.put(com/alibaba/fastjson/JSONObject, JSONObjectDeserializer.instance);
		derializers.put(com/alibaba/fastjson/JSONArray, JSONArrayDeserializer.instance);
		derializers.put(java/util/Map, MapDeserializer.instance);
		derializers.put(java/util/HashMap, MapDeserializer.instance);
		derializers.put(java/util/LinkedHashMap, MapDeserializer.instance);
		derializers.put(java/util/TreeMap, MapDeserializer.instance);
		derializers.put(java/util/concurrent/ConcurrentMap, MapDeserializer.instance);
		derializers.put(java/util/concurrent/ConcurrentHashMap, MapDeserializer.instance);
		derializers.put(java/util/Collection, CollectionDeserializer.instance);
		derializers.put(java/util/List, CollectionDeserializer.instance);
		derializers.put(java/util/ArrayList, CollectionDeserializer.instance);
		derializers.put(java/lang/Object, JavaObjectDeserializer.instance);
		derializers.put(java/lang/String, StringDeserializer.instance);
		derializers.put(Character.TYPE, CharacterDeserializer.instance);
		derializers.put(java/lang/Character, CharacterDeserializer.instance);
		derializers.put(Byte.TYPE, NumberDeserializer.instance);
		derializers.put(java/lang/Byte, NumberDeserializer.instance);
		derializers.put(Short.TYPE, NumberDeserializer.instance);
		derializers.put(java/lang/Short, NumberDeserializer.instance);
		derializers.put(Integer.TYPE, IntegerDeserializer.instance);
		derializers.put(java/lang/Integer, IntegerDeserializer.instance);
		derializers.put(Long.TYPE, LongDeserializer.instance);
		derializers.put(java/lang/Long, LongDeserializer.instance);
		derializers.put(java/math/BigInteger, BigIntegerDeserializer.instance);
		derializers.put(java/math/BigDecimal, BigDecimalDeserializer.instance);
		derializers.put(Float.TYPE, FloatDeserializer.instance);
		derializers.put(java/lang/Float, FloatDeserializer.instance);
		derializers.put(Double.TYPE, NumberDeserializer.instance);
		derializers.put(java/lang/Double, NumberDeserializer.instance);
		derializers.put(Boolean.TYPE, BooleanDeserializer.instance);
		derializers.put(java/lang/Boolean, BooleanDeserializer.instance);
		derializers.put(java/lang/Class, ClassDerializer.instance);
		derializers.put([C, CharArrayDeserializer.instance);
		derializers.put(java/util/UUID, UUIDDeserializer.instance);
		derializers.put(java/util/TimeZone, TimeZoneDeserializer.instance);
		derializers.put(java/util/Locale, LocaleDeserializer.instance);
		derializers.put(java/net/InetAddress, InetAddressDeserializer.instance);
		derializers.put(java/net/Inet4Address, InetAddressDeserializer.instance);
		derializers.put(java/net/Inet6Address, InetAddressDeserializer.instance);
		derializers.put(java/net/InetSocketAddress, InetSocketAddressDeserializer.instance);
		derializers.put(java/io/File, FileDeserializer.instance);
		derializers.put(java/net/URI, URIDeserializer.instance);
		derializers.put(java/net/URL, URLDeserializer.instance);
		derializers.put(java/util/regex/Pattern, PatternDeserializer.instance);
		derializers.put(java/nio/charset/Charset, CharsetDeserializer.instance);
		derializers.put(java/lang/Number, NumberDeserializer.instance);
		derializers.put(java/util/concurrent/atomic/AtomicIntegerArray, AtomicIntegerArrayDeserializer.instance);
		derializers.put(java/util/concurrent/atomic/AtomicLongArray, AtomicLongArrayDeserializer.instance);
		derializers.put(java/lang/StackTraceElement, StackTraceElementDeserializer.instance);
		derializers.put(java/io/Serializable, defaultSerializer);
		derializers.put(java/lang/Cloneable, defaultSerializer);
		derializers.put(java/lang/Comparable, defaultSerializer);
		derializers.put(java/io/Closeable, defaultSerializer);
	}

	public boolean isAsmEnable()
	{
		return asmEnable;
	}

	public void setAsmEnable(boolean asmEnable)
	{
		this.asmEnable = asmEnable;
	}

	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}

	public IdentityHashMap getDerializers()
	{
		return derializers;
	}

	public ObjectDeserializer getDeserializer(Type type)
	{
		ObjectDeserializer derializer = (ObjectDeserializer)derializers.get(type);
		if (derializer != null)
			return derializer;
		if (type instanceof Class)
			return getDeserializer((Class)type, type);
		if (type instanceof ParameterizedType)
		{
			Type rawType = ((ParameterizedType)type).getRawType();
			return getDeserializer(rawType);
		} else
		{
			return defaultSerializer;
		}
	}

	public ObjectDeserializer getDeserializer(Class clazz, Type type)
	{
		ObjectDeserializer derializer = (ObjectDeserializer)derializers.get(type);
		if (derializer != null)
			return derializer;
		derializer = (ObjectDeserializer)derializers.get(clazz);
		if (derializer != null)
			return derializer;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		for (Iterator i$ = ServiceLoader.load(com/alibaba/fastjson/parser/deserializer/AutowiredObjectDeserializer, classLoader).iterator(); i$.hasNext();)
		{
			AutowiredObjectDeserializer autowired = (AutowiredObjectDeserializer)i$.next();
			Iterator i$ = autowired.getAutowiredFor().iterator();
			while (i$.hasNext()) 
			{
				Type forType = (Type)i$.next();
				derializers.put(forType, autowired);
			}
		}

		derializer = (ObjectDeserializer)derializers.get(type);
		if (derializer != null)
			return derializer;
		if (clazz.isEnum())
		{
			derializer = new EnumDeserializer(clazz);
		} else
		{
			if (clazz.isArray())
				return ArrayDeserializer.instance;
			if (clazz == java/util/Set || clazz == java/util/HashSet || clazz == java/util/Collection || clazz == java/util/List || clazz == java/util/ArrayList)
			{
				if (type instanceof ParameterizedType)
				{
					Type itemType = ((ParameterizedType)type).getActualTypeArguments()[0];
					if (itemType == java/lang/String)
						derializer = ArrayListStringDeserializer.instance;
					else
						derializer = new ArrayListTypeDeserializer(clazz, itemType);
				} else
				{
					derializer = CollectionDeserializer.instance;
				}
			} else
			if (java/util/Collection.isAssignableFrom(clazz))
				derializer = CollectionDeserializer.instance;
			else
			if (java/util/Map.isAssignableFrom(clazz))
				derializer = MapDeserializer.instance;
			else
			if (java/lang/Throwable.isAssignableFrom(clazz))
				derializer = new ThrowableDeserializer(this, clazz);
			else
				derializer = createJavaBeanDeserializer(clazz);
		}
		putDeserializer(type, derializer);
		return derializer;
	}

	public ObjectDeserializer createJavaBeanDeserializer(Class clazz)
	{
		if (clazz == java/lang/Class)
			return defaultSerializer;
		boolean asmEnable = this.asmEnable;
		if (asmEnable && !Modifier.isPublic(clazz.getModifiers()))
			asmEnable = false;
		if (clazz.getTypeParameters().length != 0)
			asmEnable = false;
		if (ASMClassLoader.isExternalClass(clazz))
			asmEnable = false;
		if (asmEnable)
		{
			DeserializeBeanInfo beanInfo = DeserializeBeanInfo.computeSetters(clazz);
			Iterator i$ = beanInfo.getFieldList().iterator();
			do
			{
				if (!i$.hasNext())
					break;
				FieldInfo fieldInfo = (FieldInfo)i$.next();
				Class fieldClass = fieldInfo.getFieldClass();
				if (!Modifier.isPublic(fieldClass.getModifiers()))
				{
					asmEnable = false;
					break;
				}
				if (fieldClass.isMemberClass() && !Modifier.isStatic(fieldClass.getModifiers()))
					asmEnable = false;
			} while (true);
		}
		if (asmEnable && clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()))
			asmEnable = false;
		if (!asmEnable)
			return new JavaBeanDeserializer(this, clazz);
		return ASMDeserializerFactory.getInstance().createJavaBeanDeserializer(this, clazz);
		ASMException asmError;
		asmError;
		return new JavaBeanDeserializer(this, clazz);
		Exception e;
		e;
		throw new JSONException((new StringBuilder()).append("create asm deserializer error, ").append(clazz.getName()).toString(), e);
	}

	public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		boolean asmEnable = this.asmEnable;
		if (!Modifier.isPublic(clazz.getModifiers()))
			asmEnable = false;
		if (fieldInfo.getFieldClass() == java/lang/Class)
			asmEnable = false;
		if (ASMClassLoader.isExternalClass(clazz))
			asmEnable = false;
		if (!asmEnable)
			return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
		return ASMDeserializerFactory.getInstance().createFieldDeserializer(mapping, clazz, fieldInfo);
		Throwable e;
		e;
		e.printStackTrace();
		return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
	}

	public FieldDeserializer createFieldDeserializerWithoutASM(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		Class fieldClass = fieldInfo.getFieldClass();
		if (fieldClass == Boolean.TYPE || fieldClass == java/lang/Boolean)
			return new BooleanFieldDeserializer(mapping, clazz, fieldInfo);
		if (fieldClass == Integer.TYPE || fieldClass == java/lang/Integer)
			return new IntegerFieldDeserializer(mapping, clazz, fieldInfo);
		if (fieldClass == Long.TYPE || fieldClass == java/lang/Long)
			return new LongFieldDeserializer(mapping, clazz, fieldInfo);
		if (fieldClass == java/lang/String)
			return new StringFieldDeserializer(mapping, clazz, fieldInfo);
		if (fieldClass == java/util/List || fieldClass == java/util/ArrayList)
		{
			Type fieldType = fieldInfo.getFieldType();
			if (fieldType instanceof ParameterizedType)
			{
				Type itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
				if (itemType == java/lang/String)
					return new ArrayListStringFieldDeserializer(mapping, clazz, fieldInfo);
			}
			return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
		} else
		{
			return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
		}
	}

	public void putDeserializer(Type type, ObjectDeserializer deserializer)
	{
		derializers.put(type, deserializer);
	}

	public ObjectDeserializer getDeserializer(FieldInfo fieldInfo)
	{
		return getDeserializer(fieldInfo.getFieldClass(), fieldInfo.getFieldType());
	}

	public boolean isPrimitive(Class clazz)
	{
		return primitiveClasses.contains(clazz);
	}

	public static Field getField(Class clazz, String fieldName)
	{
		return clazz.getDeclaredField(fieldName);
		Exception e;
		e;
		return null;
	}

	public Map getFieldDeserializers(Class clazz)
	{
		ObjectDeserializer deserizer = getDeserializer(clazz);
		if (deserizer instanceof JavaBeanDeserializer)
			return ((JavaBeanDeserializer)deserizer).getFieldDeserializerMap();
		if (deserizer instanceof ASMJavaBeanDeserializer)
			return ((ASMJavaBeanDeserializer)deserizer).getInnterSerializer().getFieldDeserializerMap();
		else
			return Collections.emptyMap();
	}

}
