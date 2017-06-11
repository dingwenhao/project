// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializerFeature.java

package com.alibaba.fastjson.serializer;


public final class SerializerFeature extends Enum
{

	public static final SerializerFeature QuoteFieldNames;
	public static final SerializerFeature UseSingleQuotes;
	public static final SerializerFeature WriteMapNullValue;
	public static final SerializerFeature WriteEnumUsingToString;
	public static final SerializerFeature UseISO8601DateFormat;
	public static final SerializerFeature WriteNullListAsEmpty;
	public static final SerializerFeature WriteNullStringAsEmpty;
	public static final SerializerFeature WriteNullNumberAsZero;
	public static final SerializerFeature WriteNullBooleanAsFalse;
	public static final SerializerFeature SkipTransientField;
	public static final SerializerFeature SortField;
	public static final SerializerFeature WriteTabAsSpecial;
	public static final SerializerFeature PrettyFormat;
	public static final SerializerFeature WriteClassName;
	public static final SerializerFeature DisableCircularReferenceDetect;
	public static final SerializerFeature WriteSlashAsSpecial;
	public static final SerializerFeature BrowserCompatible;
	public static final SerializerFeature WriteDateUseDateFormat;
	public static final SerializerFeature NotWriteRootClassName;
	private final int mask = 1 << ordinal();
	private static final SerializerFeature $VALUES[];

	public static SerializerFeature[] values()
	{
		return (SerializerFeature[])$VALUES.clone();
	}

	public static SerializerFeature valueOf(String name)
	{
		return (SerializerFeature)Enum.valueOf(com/alibaba/fastjson/serializer/SerializerFeature, name);
	}

	private SerializerFeature(String s, int i)
	{
		super(s, i);
	}

	public final int getMask()
	{
		return mask;
	}

	public static boolean isEnabled(int features, SerializerFeature feature)
	{
		return (features & feature.getMask()) != 0;
	}

	public static int config(int features, SerializerFeature feature, boolean state)
	{
		if (state)
			features |= feature.getMask();
		else
			features &= ~feature.getMask();
		return features;
	}

	static 
	{
		QuoteFieldNames = new SerializerFeature("QuoteFieldNames", 0);
		UseSingleQuotes = new SerializerFeature("UseSingleQuotes", 1);
		WriteMapNullValue = new SerializerFeature("WriteMapNullValue", 2);
		WriteEnumUsingToString = new SerializerFeature("WriteEnumUsingToString", 3);
		UseISO8601DateFormat = new SerializerFeature("UseISO8601DateFormat", 4);
		WriteNullListAsEmpty = new SerializerFeature("WriteNullListAsEmpty", 5);
		WriteNullStringAsEmpty = new SerializerFeature("WriteNullStringAsEmpty", 6);
		WriteNullNumberAsZero = new SerializerFeature("WriteNullNumberAsZero", 7);
		WriteNullBooleanAsFalse = new SerializerFeature("WriteNullBooleanAsFalse", 8);
		SkipTransientField = new SerializerFeature("SkipTransientField", 9);
		SortField = new SerializerFeature("SortField", 10);
		WriteTabAsSpecial = new SerializerFeature("WriteTabAsSpecial", 11);
		PrettyFormat = new SerializerFeature("PrettyFormat", 12);
		WriteClassName = new SerializerFeature("WriteClassName", 13);
		DisableCircularReferenceDetect = new SerializerFeature("DisableCircularReferenceDetect", 14);
		WriteSlashAsSpecial = new SerializerFeature("WriteSlashAsSpecial", 15);
		BrowserCompatible = new SerializerFeature("BrowserCompatible", 16);
		WriteDateUseDateFormat = new SerializerFeature("WriteDateUseDateFormat", 17);
		NotWriteRootClassName = new SerializerFeature("NotWriteRootClassName", 18);
		$VALUES = (new SerializerFeature[] {
			QuoteFieldNames, UseSingleQuotes, WriteMapNullValue, WriteEnumUsingToString, UseISO8601DateFormat, WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero, WriteNullBooleanAsFalse, SkipTransientField, 
			SortField, WriteTabAsSpecial, PrettyFormat, WriteClassName, DisableCircularReferenceDetect, WriteSlashAsSpecial, BrowserCompatible, WriteDateUseDateFormat, NotWriteRootClassName
		});
	}
}
