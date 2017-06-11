// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractJSONParser.java

package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.*;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser:
//			JSONScanner, JSONLexer, JSONToken, Feature

public abstract class AbstractJSONParser
{

	public AbstractJSONParser()
	{
	}

	public Object parseObject(Map object)
	{
		return parseObject(object, null);
	}

	public abstract Object parseObject(Map map, Object obj);

	public JSONObject parseObject()
	{
		JSONObject object = new JSONObject();
		parseObject(((Map) (object)));
		return object;
	}

	public final void parseArray(Collection array)
	{
		parseArray(array, null);
	}

	public final void parseArray(Collection array, Object fieldName)
	{
		JSONLexer lexer = getLexer();
		if (lexer.token() == 21 || lexer.token() == 22)
			lexer.nextToken();
		if (lexer.token() != 14)
			throw new JSONException((new StringBuilder()).append("syntax error, expect [, actual ").append(JSONToken.name(lexer.token())).toString());
		lexer.nextToken(4);
		do
		{
			do
			{
				if (isEnabled(Feature.AllowArbitraryCommas))
					for (; lexer.token() == 16; lexer.nextToken());
				Object value;
				switch (lexer.token())
				{
				case 2: // '\002'
					value = lexer.integerValue();
					lexer.nextToken(16);
					break;

				case 3: // '\003'
					if (lexer.isEnabled(Feature.UseBigDecimal))
						value = lexer.decimalValue(true);
					else
						value = lexer.decimalValue(false);
					lexer.nextToken(16);
					break;

				case 4: // '\004'
					String stringLiteral = lexer.stringVal();
					lexer.nextToken(16);
					if (lexer.isEnabled(Feature.AllowISO8601DateFormat))
					{
						JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
						if (iso8601Lexer.scanISO8601DateIfMatch())
							value = iso8601Lexer.getCalendar().getTime();
						else
							value = stringLiteral;
					} else
					{
						value = stringLiteral;
					}
					break;

				case 6: // '\006'
					value = Boolean.TRUE;
					lexer.nextToken(16);
					break;

				case 7: // '\007'
					value = Boolean.FALSE;
					lexer.nextToken(16);
					break;

				case 12: // '\f'
					JSONObject object = new JSONObject();
					value = parseObject(object);
					break;

				case 14: // '\016'
					Collection items = new JSONArray();
					parseArray(items);
					value = items;
					break;

				case 8: // '\b'
					value = null;
					lexer.nextToken(4);
					break;

				case 15: // '\017'
					lexer.nextToken(16);
					return;

				case 5: // '\005'
				case 9: // '\t'
				case 10: // '\n'
				case 11: // '\013'
				case 13: // '\r'
				default:
					value = parse();
					break;
				}
				array.add(value);
			} while (lexer.token() != 16);
			lexer.nextToken(4);
		} while (true);
	}

	public Object parse()
	{
		return parse(null);
	}

	public Object parse(Object fieldName)
	{
		JSONLexer lexer = getLexer();
		switch (lexer.token())
		{
		case 21: // '\025'
			lexer.nextToken();
			HashSet set = new HashSet();
			parseArray(set, fieldName);
			return set;

		case 22: // '\026'
			lexer.nextToken();
			TreeSet treeSet = new TreeSet();
			parseArray(treeSet, fieldName);
			return treeSet;

		case 14: // '\016'
			JSONArray array = new JSONArray();
			parseArray(array, fieldName);
			return array;

		case 12: // '\f'
			JSONObject object = new JSONObject();
			return parseObject(object, fieldName);

		case 2: // '\002'
			Number intValue = lexer.integerValue();
			lexer.nextToken();
			return intValue;

		case 3: // '\003'
			Object value = lexer.decimalValue(isEnabled(Feature.UseBigDecimal));
			lexer.nextToken();
			return value;

		case 4: // '\004'
			String stringLiteral = lexer.stringVal();
			lexer.nextToken(16);
			if (lexer.isEnabled(Feature.AllowISO8601DateFormat))
			{
				JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
				if (iso8601Lexer.scanISO8601DateIfMatch())
					return iso8601Lexer.getCalendar().getTime();
			}
			return stringLiteral;

		case 8: // '\b'
			lexer.nextToken();
			return null;

		case 6: // '\006'
			lexer.nextToken();
			return Boolean.TRUE;

		case 7: // '\007'
			lexer.nextToken();
			return Boolean.FALSE;

		case 9: // '\t'
			lexer.nextToken(18);
			if (lexer.token() != 18)
			{
				throw new JSONException("syntax error");
			} else
			{
				lexer.nextToken(10);
				accept(10);
				long time = lexer.integerValue().longValue();
				accept(2);
				accept(11);
				return new Date(time);
			}

		case 20: // '\024'
			if (lexer.isBlankInput())
				return null;
			// fall through

		case 5: // '\005'
		case 10: // '\n'
		case 11: // '\013'
		case 13: // '\r'
		case 15: // '\017'
		case 16: // '\020'
		case 17: // '\021'
		case 18: // '\022'
		case 19: // '\023'
		default:
			throw new JSONException((new StringBuilder()).append("TODO ").append(lexer.tokenName()).append(" ").append(lexer.stringVal()).toString());
		}
	}

	public void config(Feature feature, boolean state)
	{
		getLexer().config(feature, state);
	}

	public boolean isEnabled(Feature feature)
	{
		return getLexer().isEnabled(feature);
	}

	public abstract JSONLexer getLexer();

	public final void accept(int token)
	{
		JSONLexer lexer = getLexer();
		if (lexer.token() == token)
			lexer.nextToken();
		else
			throw new JSONException((new StringBuilder()).append("syntax error, expect ").append(JSONToken.name(token)).append(", actual ").append(JSONToken.name(lexer.token())).toString());
	}

	public void close()
	{
		JSONLexer lexer = getLexer();
		if (isEnabled(Feature.AutoCloseSource) && !lexer.isEOF())
			throw new JSONException((new StringBuilder()).append("not close json text, token : ").append(JSONToken.name(lexer.token())).toString());
		else
			return;
	}
}
