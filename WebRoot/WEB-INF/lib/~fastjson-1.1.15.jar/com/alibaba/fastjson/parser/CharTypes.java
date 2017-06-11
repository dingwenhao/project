// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CharTypes.java

package com.alibaba.fastjson.parser;


public final class CharTypes
{

	public static final char digits[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F'
	};
	public static final boolean firstIdentifierFlags[];
	public static final boolean identifierFlags[];
	public static final boolean specicalFlags_doubleQuotes[];
	public static final boolean specicalFlags_singleQuotes[];
	public static final char replaceChars[];
	public static final char ASCII_CHARS[] = {
		'0', '0', '0', '1', '0', '2', '0', '3', '0', '4', 
		'0', '5', '0', '6', '0', '7', '0', '8', '0', '9', 
		'0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', 
		'0', 'F', '1', '0', '1', '1', '1', '2', '1', '3', 
		'1', '4', '1', '5', '1', '6', '1', '7', '1', '8', 
		'1', '9', '1', 'A', '1', 'B', '1', 'C', '1', 'D', 
		'1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', 
		'2', '3', '2', '4', '2', '5', '2', '6', '2', '7', 
		'2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', 
		'2', 'D', '2', 'E', '2', 'F'
	};

	public CharTypes()
	{
	}

	public static boolean isSpecial_doubleQuotes(char ch)
	{
		return ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch];
	}

	public static final boolean isEmoji(char ch)
	{
		if (ch >= '\uE001' && ch <= '\uE05A')
			return true;
		if (ch >= '\uE101' && ch <= '\uE15A')
			return true;
		if (ch >= '\uE201' && ch <= '\uE253')
			return true;
		if (ch >= '\uE401' && ch <= '\uE44C')
			return true;
		return ch >= '\uE501' && ch <= '\uE537';
	}

	static 
	{
		firstIdentifierFlags = new boolean[256];
		for (char c = '\0'; c < firstIdentifierFlags.length; c++)
		{
			if (c >= 'A' && c <= 'Z')
			{
				firstIdentifierFlags[c] = true;
				continue;
			}
			if (c >= 'a' && c <= 'z')
			{
				firstIdentifierFlags[c] = true;
				continue;
			}
			if (c == '_')
				firstIdentifierFlags[c] = true;
		}

		identifierFlags = new boolean[256];
		for (char c = '\0'; c < identifierFlags.length; c++)
		{
			if (c >= 'A' && c <= 'Z')
			{
				identifierFlags[c] = true;
				continue;
			}
			if (c >= 'a' && c <= 'z')
			{
				identifierFlags[c] = true;
				continue;
			}
			if (c == '_')
			{
				identifierFlags[c] = true;
				continue;
			}
			if (c >= '0' && c <= '9')
				identifierFlags[c] = true;
		}

		specicalFlags_doubleQuotes = new boolean[128];
		specicalFlags_singleQuotes = new boolean[128];
		replaceChars = new char[128];
		specicalFlags_doubleQuotes[8] = true;
		specicalFlags_doubleQuotes[10] = true;
		specicalFlags_doubleQuotes[12] = true;
		specicalFlags_doubleQuotes[13] = true;
		specicalFlags_doubleQuotes[34] = true;
		specicalFlags_doubleQuotes[92] = true;
		specicalFlags_doubleQuotes[11] = true;
		specicalFlags_singleQuotes[8] = true;
		specicalFlags_singleQuotes[10] = true;
		specicalFlags_singleQuotes[12] = true;
		specicalFlags_singleQuotes[13] = true;
		specicalFlags_singleQuotes[39] = true;
		specicalFlags_singleQuotes[92] = true;
		specicalFlags_singleQuotes[11] = true;
		replaceChars[8] = 'b';
		replaceChars[10] = 'n';
		replaceChars[12] = 'f';
		replaceChars[13] = 'r';
		replaceChars[34] = '"';
		replaceChars[39] = '\'';
		replaceChars[92] = '\\';
		replaceChars[9] = 't';
		replaceChars[47] = '/';
		replaceChars[11] = 'v';
	}
}
