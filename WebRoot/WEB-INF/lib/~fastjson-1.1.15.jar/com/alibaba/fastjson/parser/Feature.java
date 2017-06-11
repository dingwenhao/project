// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Feature.java

package com.alibaba.fastjson.parser;


public final class Feature extends Enum
{

	public static final Feature AutoCloseSource;
	public static final Feature AllowComment;
	public static final Feature AllowUnQuotedFieldNames;
	public static final Feature AllowSingleQuotes;
	public static final Feature InternFieldNames;
	public static final Feature AllowISO8601DateFormat;
	public static final Feature AllowArbitraryCommas;
	public static final Feature UseBigDecimal;
	public static final Feature IgnoreNotMatch;
	public static final Feature SortFeidFastMatch;
	public static final Feature DisableASM;
	public static final Feature DisableCircularReferenceDetect;
	public static final Feature InitStringFieldAsEmpty;
	private final int mask = 1 << ordinal();
	private static final Feature $VALUES[];

	public static Feature[] values()
	{
		return (Feature[])$VALUES.clone();
	}

	public static Feature valueOf(String name)
	{
		return (Feature)Enum.valueOf(com/alibaba/fastjson/parser/Feature, name);
	}

	private Feature(String s, int i)
	{
		super(s, i);
	}

	public final int getMask()
	{
		return mask;
	}

	public static boolean isEnabled(int features, Feature feature)
	{
		return (features & feature.getMask()) != 0;
	}

	public static int config(int features, Feature feature, boolean state)
	{
		if (state)
			features |= feature.getMask();
		else
			features &= ~feature.getMask();
		return features;
	}

	static 
	{
		AutoCloseSource = new Feature("AutoCloseSource", 0);
		AllowComment = new Feature("AllowComment", 1);
		AllowUnQuotedFieldNames = new Feature("AllowUnQuotedFieldNames", 2);
		AllowSingleQuotes = new Feature("AllowSingleQuotes", 3);
		InternFieldNames = new Feature("InternFieldNames", 4);
		AllowISO8601DateFormat = new Feature("AllowISO8601DateFormat", 5);
		AllowArbitraryCommas = new Feature("AllowArbitraryCommas", 6);
		UseBigDecimal = new Feature("UseBigDecimal", 7);
		IgnoreNotMatch = new Feature("IgnoreNotMatch", 8);
		SortFeidFastMatch = new Feature("SortFeidFastMatch", 9);
		DisableASM = new Feature("DisableASM", 10);
		DisableCircularReferenceDetect = new Feature("DisableCircularReferenceDetect", 11);
		InitStringFieldAsEmpty = new Feature("InitStringFieldAsEmpty", 12);
		$VALUES = (new Feature[] {
			AutoCloseSource, AllowComment, AllowUnQuotedFieldNames, AllowSingleQuotes, InternFieldNames, AllowISO8601DateFormat, AllowArbitraryCommas, UseBigDecimal, IgnoreNotMatch, SortFeidFastMatch, 
			DisableASM, DisableCircularReferenceDetect, InitStringFieldAsEmpty
		});
	}
}
