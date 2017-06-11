// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ThreadLocalCache.java

package com.alibaba.fastjson.util;

import java.lang.ref.SoftReference;
import java.nio.charset.CharsetDecoder;

// Referenced classes of package com.alibaba.fastjson.util:
//			UTF8Decoder

public class ThreadLocalCache
{

	public static final int CHARS_CACH_INIT_SIZE = 1024;
	public static final int CHARS_CACH_MAX_SIZE = 0x20000;
	private static final ThreadLocal charsBufLocal = new ThreadLocal();
	private static final ThreadLocal decoderLocal = new ThreadLocal();
	public static final int BYTES_CACH_INIT_SIZE = 1024;
	public static final int BYTeS_CACH_MAX_SIZE = 0x20000;
	private static final ThreadLocal bytesBufLocal = new ThreadLocal();

	public ThreadLocalCache()
	{
	}

	public static CharsetDecoder getUTF8Decoder()
	{
		CharsetDecoder decoder = (CharsetDecoder)decoderLocal.get();
		if (decoder == null)
		{
			decoder = new UTF8Decoder();
			decoderLocal.set(decoder);
		}
		return decoder;
	}

	public static void clearChars()
	{
		charsBufLocal.set(null);
	}

	public static char[] getChars(int length)
	{
		SoftReference ref = (SoftReference)charsBufLocal.get();
		if (ref == null)
			return allocate(length);
		char chars[] = (char[])ref.get();
		if (chars == null)
			return allocate(length);
		if (chars.length < length)
			chars = allocate(length);
		return chars;
	}

	private static char[] allocate(int length)
	{
		int allocateLength = getAllocateLength(1024, 0x20000, length);
		if (allocateLength <= 0x20000)
		{
			char chars[] = new char[allocateLength];
			charsBufLocal.set(new SoftReference(chars));
			return chars;
		} else
		{
			return new char[length];
		}
	}

	private static int getAllocateLength(int init, int max, int length)
	{
		int value = init;
		do
		{
			if (value >= length)
				return value;
			value *= 2;
		} while (value <= max);
		return length;
	}

	public static void clearBytes()
	{
		bytesBufLocal.set(null);
	}

	public static byte[] getBytes(int length)
	{
		SoftReference ref = (SoftReference)bytesBufLocal.get();
		if (ref == null)
			return allocateBytes(length);
		byte bytes[] = (byte[])ref.get();
		if (bytes == null)
			return allocateBytes(length);
		if (bytes.length < length)
			bytes = allocateBytes(length);
		return bytes;
	}

	private static byte[] allocateBytes(int length)
	{
		int allocateLength = getAllocateLength(1024, 0x20000, length);
		if (allocateLength <= 0x20000)
		{
			byte chars[] = new byte[allocateLength];
			bytesBufLocal.set(new SoftReference(chars));
			return chars;
		} else
		{
			return new byte[length];
		}
	}

}
