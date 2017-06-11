// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONToken.java

package com.alibaba.fastjson.parser;


public class JSONToken
{

	public static final int ERROR = 1;
	public static final int LITERAL_INT = 2;
	public static final int LITERAL_FLOAT = 3;
	public static final int LITERAL_STRING = 4;
	public static final int LITERAL_ISO8601_DATE = 5;
	public static final int TRUE = 6;
	public static final int FALSE = 7;
	public static final int NULL = 8;
	public static final int NEW = 9;
	public static final int LPAREN = 10;
	public static final int RPAREN = 11;
	public static final int LBRACE = 12;
	public static final int RBRACE = 13;
	public static final int LBRACKET = 14;
	public static final int RBRACKET = 15;
	public static final int COMMA = 16;
	public static final int COLON = 17;
	public static final int IDENTIFIER = 18;
	public static final int FIELD_NAME = 19;
	public static final int EOF = 20;
	public static final int SET = 21;
	public static final int TREE_SET = 22;

	public JSONToken()
	{
	}

	public static String name(int value)
	{
		switch (value)
		{
		case 1: // '\001'
			return "error";

		case 2: // '\002'
			return "int";

		case 3: // '\003'
			return "float";

		case 4: // '\004'
			return "string";

		case 5: // '\005'
			return "iso8601";

		case 6: // '\006'
			return "true";

		case 7: // '\007'
			return "false";

		case 8: // '\b'
			return "null";

		case 9: // '\t'
			return "new";

		case 10: // '\n'
			return "(";

		case 11: // '\013'
			return ")";

		case 12: // '\f'
			return "{";

		case 13: // '\r'
			return "}";

		case 14: // '\016'
			return "[";

		case 15: // '\017'
			return "]";

		case 16: // '\020'
			return ",";

		case 17: // '\021'
			return ":";

		case 18: // '\022'
			return "ident";

		case 19: // '\023'
			return "fieldName";

		case 20: // '\024'
			return "EOF";

		case 21: // '\025'
			return "Set";

		case 22: // '\026'
			return "TreeSet";
		}
		return "Unkown";
	}
}
