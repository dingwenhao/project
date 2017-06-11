// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SymbolTable.java

package com.alibaba.fastjson.parser;


public class SymbolTable
{
	protected static final class Entry
	{

		public final String symbol;
		public final int hashCode;
		public final char characters[];
		public final byte bytes[] = null;
		public Entry next;

		public Entry(char ch[], int offset, int length, int hash, Entry next)
		{
			characters = new char[length];
			System.arraycopy(ch, offset, characters, 0, length);
			symbol = (new String(characters)).intern();
			this.next = next;
			hashCode = hash;
		}
	}


	public static final int DEFAULT_TABLE_SIZE = 128;
	private final Entry buckets[];
	private final String symbols[];
	private final char symbols_char[][];
	private final int indexMask;

	public SymbolTable()
	{
		this(128);
	}

	public SymbolTable(int tableSize)
	{
		indexMask = tableSize - 1;
		buckets = new Entry[tableSize];
		symbols = new String[tableSize];
		symbols_char = new char[tableSize][];
	}

	public String addSymbol(char buffer[], int offset, int len)
	{
		int hash = hash(buffer, offset, len);
		return addSymbol(buffer, offset, len, hash);
	}

	public String addSymbol(char buffer[], int offset, int len, int hash)
	{
		int bucket = hash & indexMask;
		String sym = symbols[bucket];
		boolean match = true;
		if (sym != null)
			if (sym.length() == len)
			{
				char characters[] = symbols_char[bucket];
				int i = 0;
				do
				{
					if (i >= len)
						break;
					if (buffer[offset + i] != characters[i])
					{
						match = false;
						break;
					}
					i++;
				} while (true);
				if (match)
					return sym;
			} else
			{
				match = false;
			}
		Entry entry;
label0:
		for (entry = buckets[bucket]; entry != null; entry = entry.next)
		{
			char characters[] = entry.characters;
			if (len != characters.length || hash != entry.hashCode)
				continue;
			for (int i = 0; i < len; i++)
				if (buffer[offset + i] != characters[i])
					continue label0;

			return entry.symbol;
		}

		entry = new Entry(buffer, offset, len, hash, buckets[bucket]);
		buckets[bucket] = entry;
		if (match)
		{
			symbols[bucket] = entry.symbol;
			symbols_char[bucket] = entry.characters;
		}
		return entry.symbol;
	}

	public static final int hash(char buffer[], int offset, int len)
	{
		int h = 0;
		int off = offset;
		for (int i = 0; i < len; i++)
			h = 31 * h + buffer[off++];

		return h;
	}
}
