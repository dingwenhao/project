// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONSerializerContext.java

package com.alibaba.fastjson.serializer;


public final class JSONSerializerContext
{
	protected static final class Entry
	{

		public final int hashCode;
		public final Object object;
		public Entry next;

		public Entry(Object object, int hash, Entry next)
		{
			this.object = object;
			this.next = next;
			hashCode = hash;
		}
	}


	public static final int DEFAULT_TABLE_SIZE = 128;
	private final Entry buckets[];
	private final int indexMask;

	public JSONSerializerContext()
	{
		this(128);
	}

	public JSONSerializerContext(int tableSize)
	{
		indexMask = tableSize - 1;
		buckets = new Entry[tableSize];
	}

	public final boolean put(Object o)
	{
		int hash = System.identityHashCode(o);
		int bucket = hash & indexMask;
		Entry entry;
		for (entry = buckets[bucket]; entry != null; entry = entry.next)
			if (o == entry.object)
				return true;

		entry = new Entry(o, hash, buckets[bucket]);
		buckets[bucket] = entry;
		return false;
	}
}
