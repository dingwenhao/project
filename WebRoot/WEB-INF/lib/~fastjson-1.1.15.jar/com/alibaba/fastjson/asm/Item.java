// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Item.java

package com.alibaba.fastjson.asm;


final class Item
{

	int index;
	int type;
	int intVal;
	long longVal;
	String strVal1;
	String strVal2;
	String strVal3;
	int hashCode;
	Item next;

	Item()
	{
	}

	Item(int index, Item i)
	{
		this.index = index;
		type = i.type;
		intVal = i.intVal;
		longVal = i.longVal;
		strVal1 = i.strVal1;
		strVal2 = i.strVal2;
		strVal3 = i.strVal3;
		hashCode = i.hashCode;
	}

	void set(int type, String strVal1, String strVal2, String strVal3)
	{
		this.type = type;
		this.strVal1 = strVal1;
		this.strVal2 = strVal2;
		this.strVal3 = strVal3;
		switch (type)
		{
		case 1: // '\001'
		case 7: // '\007'
		case 8: // '\b'
		case 13: // '\r'
			hashCode = 0x7fffffff & type + strVal1.hashCode();
			return;

		case 12: // '\f'
			hashCode = 0x7fffffff & type + strVal1.hashCode() * strVal2.hashCode();
			return;

		case 2: // '\002'
		case 3: // '\003'
		case 4: // '\004'
		case 5: // '\005'
		case 6: // '\006'
		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		default:
			hashCode = 0x7fffffff & type + strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode();
			return;
		}
	}

	boolean isEqualTo(Item i)
	{
		switch (type)
		{
		case 1: // '\001'
		case 7: // '\007'
		case 8: // '\b'
		case 13: // '\r'
			return i.strVal1.equals(strVal1);

		case 5: // '\005'
		case 6: // '\006'
		case 15: // '\017'
			return i.longVal == longVal;

		case 3: // '\003'
		case 4: // '\004'
			return i.intVal == intVal;

		case 14: // '\016'
			return i.intVal == intVal && i.strVal1.equals(strVal1);

		case 12: // '\f'
			return i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2);

		case 2: // '\002'
		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		default:
			return i.strVal1.equals(strVal1) && i.strVal2.equals(strVal2) && i.strVal3.equals(strVal3);
		}
	}
}
