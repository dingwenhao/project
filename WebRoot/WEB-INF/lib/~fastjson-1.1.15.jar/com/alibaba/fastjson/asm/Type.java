// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Type.java

package com.alibaba.fastjson.asm;


public class Type
{

	public static final int VOID = 0;
	public static final int BOOLEAN = 1;
	public static final int CHAR = 2;
	public static final int BYTE = 3;
	public static final int SHORT = 4;
	public static final int INT = 5;
	public static final int FLOAT = 6;
	public static final int LONG = 7;
	public static final int DOUBLE = 8;
	public static final int ARRAY = 9;
	public static final int OBJECT = 10;
	public static final Type VOID_TYPE = new Type(0, null, 0x56050000, 1);
	public static final Type BOOLEAN_TYPE = new Type(1, null, 0x5a000501, 1);
	public static final Type CHAR_TYPE = new Type(2, null, 0x43000601, 1);
	public static final Type BYTE_TYPE = new Type(3, null, 0x42000501, 1);
	public static final Type SHORT_TYPE = new Type(4, null, 0x53000701, 1);
	public static final Type INT_TYPE = new Type(5, null, 0x49000001, 1);
	public static final Type FLOAT_TYPE = new Type(6, null, 0x46020201, 1);
	public static final Type LONG_TYPE = new Type(7, null, 0x4a010102, 1);
	public static final Type DOUBLE_TYPE = new Type(8, null, 0x44030302, 1);
	private final int sort;
	private final char buf[];
	private final int off;
	private final int len;

	private Type(int sort, char buf[], int off, int len)
	{
		this.sort = sort;
		this.buf = buf;
		this.off = off;
		this.len = len;
	}

	public static Type getType(String typeDescriptor)
	{
		return getType(typeDescriptor.toCharArray(), 0);
	}

	public static int getArgumentsAndReturnSizes(String desc)
	{
		int n = 1;
		int c = 1;
		do
		{
			char car = desc.charAt(c++);
			if (car == ')')
			{
				car = desc.charAt(c);
				return n << 2 | (car != 'V' ? car != 'D' && car != 'J' ? 1 : 2 : 0);
			}
			if (car == 'L')
			{
				while (desc.charAt(c++) != ';') ;
				n++;
			} else
			if (car == '[')
			{
				while ((car = desc.charAt(c)) == '[') 
					c++;
				if (car == 'D' || car == 'J')
					n--;
			} else
			if (car == 'D' || car == 'J')
				n += 2;
			else
				n++;
		} while (true);
	}

	private static Type getType(char buf[], int off)
	{
		int len;
		switch (buf[off])
		{
		case 86: // 'V'
			return VOID_TYPE;

		case 90: // 'Z'
			return BOOLEAN_TYPE;

		case 67: // 'C'
			return CHAR_TYPE;

		case 66: // 'B'
			return BYTE_TYPE;

		case 83: // 'S'
			return SHORT_TYPE;

		case 73: // 'I'
			return INT_TYPE;

		case 70: // 'F'
			return FLOAT_TYPE;

		case 74: // 'J'
			return LONG_TYPE;

		case 68: // 'D'
			return DOUBLE_TYPE;

		case 91: // '['
			for (len = 1; buf[off + len] == '['; len++);
			if (buf[off + len] == 'L')
				for (len++; buf[off + len] != ';'; len++);
			return new Type(9, buf, off, len + 1);

		case 69: // 'E'
		case 71: // 'G'
		case 72: // 'H'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 82: // 'R'
		case 84: // 'T'
		case 85: // 'U'
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		default:
			len = 1;
			break;
		}
		for (; buf[off + len] != ';'; len++);
		return new Type(10, buf, off + 1, len - 1);
	}

	public int getSort()
	{
		return sort;
	}

	public String getInternalName()
	{
		return new String(buf, off, len);
	}

	public String getDescriptor()
	{
		StringBuffer buf = new StringBuffer();
		if (this.buf == null)
			buf.append((char)((off & 0xff000000) >>> 24));
		else
		if (sort == 9)
		{
			buf.append(this.buf, off, len);
		} else
		{
			buf.append('L');
			buf.append(this.buf, off, len);
			buf.append(';');
		}
		return buf.toString();
	}

}
