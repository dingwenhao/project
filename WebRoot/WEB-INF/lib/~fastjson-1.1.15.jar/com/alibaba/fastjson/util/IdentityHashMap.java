// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IdentityHashMap.java

package com.alibaba.fastjson.util;


public class IdentityHashMap
{
	protected static final class Entry
	{

		public final int hashCode;
		public final Object key;
		public Object value;
		public final Entry next;

		public Entry(Object key, Object value, int hash, Entry next)
		{
			this.key = key;
			this.value = value;
			this.next = next;
			hashCode = hash;
		}
	}


	public static final int DEFAULT_TABLE_SIZE = 1024;
	private final Entry buckets[];
	private final int indexMask;

	public IdentityHashMap()
	{
		this(1024);
	}

	public IdentityHashMap(int tableSize)
	{
		indexMask = tableSize - 1;
		buckets = new Entry[tableSize];
	}

	public final Object get(Object key)
	{
		int hash = System.identityHashCode(key);
		int bucket = hash & indexMask;
		for (Entry entry = buckets[bucket]; entry != null; entry = entry.next)
			if (key == entry.key)
				return entry.value;

		return null;
	}

	public boolean put(Object key, Object value)
	{
		int hash = System.identityHashCode(key);
		int bucket = hash & indexMask;
		Entry entry;
		for (entry = buckets[bucket]; entry != null; entry = entry.next)
			if (key == entry.key)
			{
				entry.value = value;
				return true;
			}

		entry = new Entry(key, value, hash, buckets[bucket]);
		buckets[bucket] = entry;
		return false;
	}

	public int size()
	{
		int size = 0;
		for (int i = 0; i < buckets.length; i++)
		{
			for (Entry entry = buckets[i]; entry != null; entry = entry.next)
				size++;

		}

		return size;
	}
}
