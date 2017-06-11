// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializeConfig.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.*;
import java.io.File;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			JavaBeanSerializer, ASMSerializerFactory, BooleanSerializer, CharacterSerializer, 
//			ByteSerializer, ShortSerializer, IntegerSerializer, LongSerializer, 
//			FloatSerializer, DoubleSerializer, BigDecimalSerializer, BigIntegerSerializer, 
//			StringSerializer, ByteArraySerializer, ShortArraySerializer, IntArraySerializer, 
//			LongArraySerializer, FloatArraySerializer, DoubleArraySerializer, BooleanArraySerializer, 
//			CharArraySerializer, ObjectArraySerializer, ClassSerializer, LocaleSerializer, 
//			TimeZoneSerializer, UUIDSerializer, InetAddressSerializer, InetSocketAddressSerializer, 
//			FileSerializer, URISerializer, URLSerializer, AppendableSerializer, 
//			PatternSerializer, CharsetSerializer, AtomicBooleanSerializer, AtomicIntegerSerializer, 
//			AtomicLongSerializer, AtomicReferenceSerializer, AtomicIntegerArraySerializer, AtomicLongArraySerializer, 
//			ObjectSerializer

public class SerializeConfig extends IdentityHashMap
{

	private static final SerializeConfig globalInstance = new SerializeConfig();
	private boolean asm;
	private final ASMSerializerFactory asmFactory;

	public final ObjectSerializer createASMSerializer(Class clazz)
		throws Exception
	{
		return asmFactory.createJavaBeanSerializer(clazz);
	}

	public ObjectSerializer createJavaBeanSerializer(Class clazz)
	{
		if (!Modifier.isPublic(clazz.getModifiers()))
			return new JavaBeanSerializer(clazz);
		boolean asm = this.asm;
		if (asm && ASMClassLoader.isExternalClass(clazz))
			asm = false;
		if (!asm)
			break MISSING_BLOCK_LABEL_76;
		return createASMSerializer(clazz);
		Throwable e;
		e;
		throw new JSONException((new StringBuilder()).append("create asm serilizer error, class ").append(clazz).toString(), e);
		return new JavaBeanSerializer(clazz);
	}

	public boolean isAsmEnable()
	{
		return asm;
	}

	public void setAsmEnable(boolean asmEnable)
	{
		asm = asmEnable;
	}

	public static final SerializeConfig getGlobalInstance()
	{
		return globalInstance;
	}

	public SerializeConfig()
	{
		this(1024);
	}

	public SerializeConfig(int tableSize)
	{
		super(tableSize);
		asm = !ASMUtils.isAndroid();
		asmFactory = new ASMSerializerFactory();
		put(java/lang/Boolean, BooleanSerializer.instance);
		put(java/lang/Character, CharacterSerializer.instance);
		put(java/lang/Byte, ByteSerializer.instance);
		put(java/lang/Short, ShortSerializer.instance);
		put(java/lang/Integer, IntegerSerializer.instance);
		put(java/lang/Long, LongSerializer.instance);
		put(java/lang/Float, FloatSerializer.instance);
		put(java/lang/Double, DoubleSerializer.instance);
		put(java/math/BigDecimal, BigDecimalSerializer.instance);
		put(java/math/BigInteger, BigIntegerSerializer.instance);
		put(java/lang/String, StringSerializer.instance);
		put([B, ByteArraySerializer.instance);
		put([S, ShortArraySerializer.instance);
		put([I, IntArraySerializer.instance);
		put([J, LongArraySerializer.instance);
		put([F, FloatArraySerializer.instance);
		put([D, DoubleArraySerializer.instance);
		put([Z, BooleanArraySerializer.instance);
		put([C, CharArraySerializer.instance);
		put([Ljava/lang/Object;, ObjectArraySerializer.instance);
		put(java/lang/Class, ClassSerializer.instance);
		put(java/util/Locale, LocaleSerializer.instance);
		put(java/util/TimeZone, TimeZoneSerializer.instance);
		put(java/util/UUID, UUIDSerializer.instance);
		put(java/net/InetAddress, InetAddressSerializer.instance);
		put(java/net/Inet4Address, InetAddressSerializer.instance);
		put(java/net/Inet6Address, InetAddressSerializer.instance);
		put(java/net/InetSocketAddress, InetSocketAddressSerializer.instance);
		put(java/io/File, FileSerializer.instance);
		put(java/net/URI, URISerializer.instance);
		put(java/net/URL, URLSerializer.instance);
		put(java/lang/Appendable, AppendableSerializer.instance);
		put(java/lang/StringBuffer, AppendableSerializer.instance);
		put(java/lang/StringBuilder, AppendableSerializer.instance);
		put(java/util/regex/Pattern, PatternSerializer.instance);
		put(java/nio/charset/Charset, CharsetSerializer.instance);
		put(java/util/concurrent/atomic/AtomicBoolean, AtomicBooleanSerializer.instance);
		put(java/util/concurrent/atomic/AtomicInteger, AtomicIntegerSerializer.instance);
		put(java/util/concurrent/atomic/AtomicLong, AtomicLongSerializer.instance);
		put(java/util/concurrent/atomic/AtomicReference, AtomicReferenceSerializer.instance);
		put(java/util/concurrent/atomic/AtomicIntegerArray, AtomicIntegerArraySerializer.instance);
		put(java/util/concurrent/atomic/AtomicLongArray, AtomicLongArraySerializer.instance);
	}

}
