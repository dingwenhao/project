// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ByteVector.java

package com.alibaba.fastjson.asm;


public class ByteVector
{

	byte data[];
	int length;

	public ByteVector()
	{
		data = new byte[64];
	}

	public ByteVector(int initialSize)
	{
		data = new byte[initialSize];
	}

	public ByteVector putByte(int b)
	{
		int length = this.length;
		if (length + 1 > data.length)
			enlarge(1);
		data[length++] = (byte)b;
		this.length = length;
		return this;
	}

	ByteVector put11(int b1, int b2)
	{
		int length = this.length;
		if (length + 2 > this.data.length)
			enlarge(2);
		byte data[] = this.data;
		data[length++] = (byte)b1;
		data[length++] = (byte)b2;
		this.length = length;
		return this;
	}

	public ByteVector putShort(int s)
	{
		int length = this.length;
		if (length + 2 > this.data.length)
			enlarge(2);
		byte data[] = this.data;
		data[length++] = (byte)(s >>> 8);
		data[length++] = (byte)s;
		this.length = length;
		return this;
	}

	ByteVector put12(int b, int s)
	{
		int length = this.length;
		if (length + 3 > this.data.length)
			enlarge(3);
		byte data[] = this.data;
		data[length++] = (byte)b;
		data[length++] = (byte)(s >>> 8);
		data[length++] = (byte)s;
		this.length = length;
		return this;
	}

	public ByteVector putInt(int i)
	{
		int length = this.length;
		if (length + 4 > this.data.length)
			enlarge(4);
		byte data[] = this.data;
		data[length++] = (byte)(i >>> 24);
		data[length++] = (byte)(i >>> 16);
		data[length++] = (byte)(i >>> 8);
		data[length++] = (byte)i;
		this.length = length;
		return this;
	}

	public ByteVector putUTF8(String s)
	{
		int charLength = s.length();
		int len = length;
		if (len + 2 + charLength > this.data.length)
			enlarge(2 + charLength);
		byte data[] = this.data;
		data[len++] = (byte)(charLength >>> 8);
		data[len++] = (byte)charLength;
		for (int i = 0; i < charLength; i++)
		{
			char c = s.charAt(i);
			if (c >= '\001' && c <= '\177')
				data[len++] = (byte)c;
			else
				throw new UnsupportedOperationException();
		}

		length = len;
		return this;
	}

	public ByteVector putByteArray(byte b[], int off, int len)
	{
		if (length + len > data.length)
			enlarge(len);
		if (b != null)
			System.arraycopy(b, off, data, length, len);
		length += len;
		return this;
	}

	private void enlarge(int size)
	{
		int length1 = 2 * data.length;
		int length2 = length + size;
		byte newData[] = new byte[length1 <= length2 ? length2 : length1];
		System.arraycopy(data, 0, newData, 0, length);
		data = newData;
	}
}
