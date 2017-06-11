// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultJSONParser.java

package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.IntegerDeserializer;
import com.alibaba.fastjson.parser.deserializer.ListResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.StringDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser:
//			AbstractJSONParser, JSONScanner, ParseContext, ParserConfig, 
//			JSONLexer, Feature, JSONToken, SymbolTable

public class DefaultJSONParser extends AbstractJSONParser
{
	public static class ResolveTask
	{

		private final ParseContext context;
		private final String referenceValue;
		private FieldDeserializer fieldDeserializer;
		private ParseContext ownerContext;

		public ParseContext getContext()
		{
			return context;
		}

		public String getReferenceValue()
		{
			return referenceValue;
		}

		public FieldDeserializer getFieldDeserializer()
		{
			return fieldDeserializer;
		}

		public void setFieldDeserializer(FieldDeserializer fieldDeserializer)
		{
			this.fieldDeserializer = fieldDeserializer;
		}

		public ParseContext getOwnerContext()
		{
			return ownerContext;
		}

		public void setOwnerContext(ParseContext ownerContext)
		{
			this.ownerContext = ownerContext;
		}

		public ResolveTask(ParseContext context, String referenceValue)
		{
			this.context = context;
			this.referenceValue = referenceValue;
		}
	}


	protected final JSONLexer lexer;
	protected final Object input;
	protected final SymbolTable symbolTable;
	protected ParserConfig config;
	public static final int NONE = 0;
	public static final int NeedToResolve = 1;
	public static final int TypeNameRedirect = 2;
	private int resolveStatus;
	private DefaultObjectDeserializer derializer;
	private ParseContext context;
	private ParseContext contextArray[];
	private int contextArrayIndex;
	private final List resolveTaskList;
	private static final Set primitiveClasses;
	private String dateFormatPattern;
	private DateFormat dateFormat;

	public Object getObject(String path)
	{
		for (int i = 0; i < contextArrayIndex; i++)
			if (path.equals(contextArray[i].getPath()))
				return contextArray[i].getObject();

		return null;
	}

	public String getDateFomartPattern()
	{
		return dateFormatPattern;
	}

	public DateFormat getDateFormat()
	{
		if (dateFormat == null)
			dateFormat = new SimpleDateFormat(dateFormatPattern);
		return dateFormat;
	}

	public void setDateFormat(String dateFormat)
	{
		dateFormatPattern = dateFormat;
		this.dateFormat = null;
	}

	public void setDateFomrat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public DefaultJSONParser(String input)
	{
		this(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
	}

	public DefaultJSONParser(String input, ParserConfig config)
	{
		this(input, ((JSONLexer) (new JSONScanner(input, JSON.DEFAULT_PARSER_FEATURE))), config);
	}

	public DefaultJSONParser(String input, ParserConfig config, int features)
	{
		this(input, ((JSONLexer) (new JSONScanner(input, features))), config);
	}

	public DefaultJSONParser(char input[], int length, ParserConfig config, int features)
	{
		this(input, ((JSONLexer) (new JSONScanner(input, length, features))), config);
	}

	public DefaultJSONParser(Object input, JSONLexer lexer, ParserConfig config)
	{
		resolveStatus = 0;
		derializer = new DefaultObjectDeserializer();
		contextArray = new ParseContext[8];
		contextArrayIndex = 0;
		resolveTaskList = new ArrayList();
		dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
		this.input = input;
		this.lexer = lexer;
		this.config = config;
		symbolTable = config.getSymbolTable();
		lexer.nextToken(12);
	}

	public int getResolveStatus()
	{
		return resolveStatus;
	}

	public void setResolveStatus(int resolveStatus)
	{
		this.resolveStatus = resolveStatus;
	}

	public void checkListResolve(Collection array)
	{
		if (resolveStatus == 1)
		{
			int index = array.size() - 1;
			List list = (List)array;
			ResolveTask task = getLastResolveTask();
			task.setFieldDeserializer(new ListResolveFieldDeserializer(list, index));
			task.setOwnerContext(context);
			setResolveStatus(0);
		}
	}

	public SymbolTable getSymbolTable()
	{
		return symbolTable;
	}

	public JSONLexer getLexer()
	{
		return lexer;
	}

	public boolean isEnabled(Feature feature)
	{
		return lexer.isEnabled(feature);
	}

	public String getInput()
	{
		if (input instanceof char[])
			return new String((char[])(char[])input);
		else
			return input.toString();
	}

	public final Object parseObject(Map object, Object fieldName)
	{
		JSONScanner lexer;
		ParseContext context;
		lexer = (JSONScanner)this.lexer;
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException((new StringBuilder()).append("syntax error, expect {, actual ").append(lexer.tokenName()).toString());
		context = getContext();
_L3:
		char ch;
		Object key;
		Map map;
		lexer.skipWhitespace();
		ch = lexer.getCurrent();
		if (isEnabled(Feature.AllowArbitraryCommas))
			for (; ch == ','; ch = lexer.getCurrent())
			{
				lexer.incrementBufferPosition();
				lexer.skipWhitespace();
			}

		if (ch == '"')
		{
			key = lexer.scanSymbol(symbolTable, '"');
			lexer.skipWhitespace();
			ch = lexer.getCurrent();
			if (ch != ':')
				throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).append(", name ").append(key).toString());
			break MISSING_BLOCK_LABEL_526;
		}
		if (ch != '}')
			break MISSING_BLOCK_LABEL_213;
		lexer.incrementBufferPosition();
		lexer.resetStringPosition();
		lexer.nextToken();
		map = object;
		setContext(context);
		return map;
		if (ch == '\'')
		{
			if (!isEnabled(Feature.AllowSingleQuotes))
				throw new JSONException("syntax error");
			key = lexer.scanSymbol(symbolTable, '\'');
			lexer.skipWhitespace();
			ch = lexer.getCurrent();
			if (ch != ':')
				throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).toString());
		} else
		{
			if (ch == '\032')
				throw new JSONException("syntax error");
			if (ch == ',')
				throw new JSONException("syntax error");
			if (ch >= '0' && ch <= '9' || ch == '-')
			{
				lexer.resetStringPosition();
				lexer.scanNumber();
				if (lexer.token() == 2)
					key = lexer.integerValue();
				else
					key = lexer.decimalValue(true);
				ch = lexer.getCurrent();
				if (ch != ':')
					throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).append(", name ").append(key).toString());
			} else
			{
				if (!isEnabled(Feature.AllowUnQuotedFieldNames))
					throw new JSONException("syntax error");
				key = lexer.scanSymbolUnQuoted(symbolTable);
				lexer.skipWhitespace();
				ch = lexer.getCurrent();
				if (ch != ':')
					throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).append(", actual ").append(ch).toString());
			}
		}
		Object obj1;
		lexer.incrementBufferPosition();
		lexer.skipWhitespace();
		ch = lexer.getCurrent();
		lexer.resetStringPosition();
		if (key != "@type")
			break MISSING_BLOCK_LABEL_625;
		String typeName = lexer.scanSymbol(symbolTable, '"');
		Class clazz = TypeUtils.loadClass(typeName);
		ObjectDeserializer deserializer = config.getDeserializer(clazz);
		lexer.nextToken(16);
		resolveStatus = 2;
		if (this.context != null)
			popContext();
		obj1 = deserializer.deserialze(this, clazz, fieldName);
		setContext(context);
		return obj1;
		JSONScanner iso8601Lexer;
		if (key != "$ref")
			break MISSING_BLOCK_LABEL_924;
		lexer.nextToken(4);
		if (lexer.token() != 4)
			break MISSING_BLOCK_LABEL_891;
		String ref = lexer.stringVal();
		lexer.nextToken(13);
		Object refValue = null;
		if ("@".equals(ref))
			refValue = getContext().getObject();
		else
		if ("..".equals(ref))
		{
			ParseContext parentContext = context.getParentContext();
			if (parentContext.getObject() != null)
			{
				refValue = getContext().getObject();
			} else
			{
				getResolveTaskList().add(new ResolveTask(parentContext, ref));
				setResolveStatus(1);
			}
		} else
		if ("$".equals(ref))
		{
			ParseContext rootContext;
			for (rootContext = context; rootContext.getParentContext() != null; rootContext = rootContext.getParentContext());
			if (rootContext.getObject() != null)
			{
				refValue = rootContext.getObject();
			} else
			{
				getResolveTaskList().add(new ResolveTask(rootContext, ref));
				setResolveStatus(1);
			}
		} else
		{
			getResolveTaskList().add(new ResolveTask(context, ref));
			setResolveStatus(1);
		}
		if (lexer.token() != 13)
			throw new JSONException("syntax error");
		lexer.nextToken(16);
		iso8601Lexer = ((JSONScanner) (refValue));
		setContext(context);
		return iso8601Lexer;
		throw new JSONException((new StringBuilder()).append("illegal ref, ").append(JSONToken.name(lexer.token())).toString());
		Object value;
		if (ch == '"')
		{
			lexer.scanString();
			String strValue = lexer.stringVal();
			value = strValue;
			if (lexer.isEnabled(Feature.AllowISO8601DateFormat))
			{
				iso8601Lexer = new JSONScanner(strValue);
				if (iso8601Lexer.scanISO8601DateIfMatch())
					value = iso8601Lexer.getCalendar().getTime();
			}
			object.put(key, value);
			break MISSING_BLOCK_LABEL_1365;
		}
		if (ch >= '0' && ch <= '9' || ch == '-')
		{
			lexer.scanNumber();
			if (lexer.token() == 2)
				value = lexer.integerValue();
			else
				value = lexer.decimalValue();
			object.put(key, value);
			break MISSING_BLOCK_LABEL_1365;
		}
		if (ch != '[')
			break MISSING_BLOCK_LABEL_1148;
		lexer.nextToken();
		JSONArray list = new JSONArray();
		parseArray(list);
		value = list;
		object.put(key, value);
		if (lexer.token() != 13) goto _L2; else goto _L1
_L1:
		lexer.nextToken();
		iso8601Lexer = object;
		setContext(context);
		return iso8601Lexer;
_L2:
		if (lexer.token() != 16) goto _L4; else goto _L3
_L4:
		throw new JSONException("syntax error");
		if (ch != '{')
			break MISSING_BLOCK_LABEL_1267;
		lexer.nextToken();
		Object obj = parseObject(((Map) (new JSONObject())));
		object.put(key, obj);
		setContext(context, obj, key);
		if (lexer.token() != 13) goto _L6; else goto _L5
_L5:
		lexer.nextToken();
		setContext(context);
		iso8601Lexer = object;
		setContext(context);
		return iso8601Lexer;
_L6:
		if (lexer.token() != 16) goto _L7; else goto _L3
_L7:
		throw new JSONException((new StringBuilder()).append("syntax error, ").append(lexer.tokenName()).toString());
		lexer.nextToken();
		Object value = parse();
		object.put(key, value);
		if (lexer.token() != 13) goto _L9; else goto _L8
_L8:
		Map map1;
		lexer.nextToken();
		map1 = object;
		setContext(context);
		return map1;
_L9:
		if (lexer.token() != 16) goto _L10; else goto _L3
_L10:
		throw new JSONException((new StringBuilder()).append("syntax error, position at ").append(lexer.pos()).append(", name ").append(key).toString());
		lexer.skipWhitespace();
		ch = lexer.getCurrent();
		if (ch != ',')
			break MISSING_BLOCK_LABEL_1389;
		lexer.incrementBufferPosition();
		  goto _L3
		if (ch != '}')
			break MISSING_BLOCK_LABEL_1420;
		lexer.incrementBufferPosition();
		lexer.resetStringPosition();
		lexer.nextToken();
		map1 = object;
		setContext(context);
		return map1;
		throw new JSONException((new StringBuilder()).append("syntax error, position at ").append(lexer.pos()).append(", name ").append(key).toString());
		Exception exception;
		exception;
		setContext(context);
		throw exception;
	}

	public ParseContext getContext()
	{
		return context;
	}

	public List getResolveTaskList()
	{
		return resolveTaskList;
	}

	public ResolveTask getLastResolveTask()
	{
		return (ResolveTask)resolveTaskList.get(resolveTaskList.size() - 1);
	}

	public void setContext(ParseContext context)
	{
		if (isEnabled(Feature.DisableCircularReferenceDetect))
		{
			return;
		} else
		{
			this.context = context;
			return;
		}
	}

	public void popContext()
	{
		if (isEnabled(Feature.DisableCircularReferenceDetect))
		{
			return;
		} else
		{
			context = context.getParentContext();
			return;
		}
	}

	public ParseContext setContext(Object object, Object fieldName)
	{
		if (isEnabled(Feature.DisableCircularReferenceDetect))
			return null;
		else
			return setContext(context, object, fieldName);
	}

	public ParseContext setContext(ParseContext parent, Object object, Object fieldName)
	{
		if (isEnabled(Feature.DisableCircularReferenceDetect))
			return null;
		if (lexer.isResetFlag())
		{
			int i = 0;
			do
			{
				if (i >= contextArrayIndex)
					break;
				ParseContext item = contextArray[i];
				if (item.getParentContext() == parent && item.getFieldName() == fieldName)
				{
					context = item;
					context.setObject(object);
					clearChildContext(context, i + 1);
					break;
				}
				i++;
			} while (true);
			lexer.setResetFlag(false);
		} else
		{
			context = new ParseContext(parent, object, fieldName);
			addContext(context);
		}
		return context;
	}

	private void clearChildContext(ParseContext parent, int start)
	{
		for (int i = start; i < contextArrayIndex; i++)
		{
			ParseContext item = contextArray[i];
			if (item.getParentContext() != parent)
				continue;
			int end = contextArrayIndex - 1;
			if (i != end)
				System.arraycopy(contextArray, i + 1, contextArray, i, end - i);
			contextArray[end] = null;
			contextArrayIndex--;
			clearChildContext(item, i + 1);
		}

	}

	private void addContext(ParseContext context)
	{
		int i = contextArrayIndex++;
		if (i >= contextArray.length)
		{
			int newLen = (contextArray.length * 3) / 2;
			ParseContext newArray[] = new ParseContext[newLen];
			System.arraycopy(contextArray, 0, newArray, 0, contextArray.length);
			contextArray = newArray;
		}
		contextArray[i] = context;
	}

	public ParserConfig getConfig()
	{
		return config;
	}

	public void setConfig(ParserConfig config)
	{
		this.config = config;
	}

	public Object parseObject(Class clazz)
	{
		return parseObject(((Type) (clazz)));
	}

	public Object parseObject(Type type)
	{
		ObjectDeserializer derializer;
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			return null;
		}
		derializer = config.getDeserializer(type);
		return derializer.deserialze(this, type, null);
		JSONException e;
		e;
		throw e;
		e;
		throw new JSONException(e.getMessage(), e);
	}

	public List parseArray(Class clazz)
	{
		List array = new ArrayList();
		parseArray(clazz, ((Collection) (array)));
		return array;
	}

	public void parseArray(Class clazz, Collection array)
	{
		parseArray(((Type) (clazz)), array);
	}

	public void parseArray(Type type, Collection array)
	{
		parseArray(type, array, null);
	}

	public void parseArray(Type type, Collection array, Object fieldName)
	{
		ObjectDeserializer deserializer;
		if (lexer.token() == 21 || lexer.token() == 22)
			lexer.nextToken();
		if (lexer.token() != 14)
			throw new JSONException((new StringBuilder()).append("exepct '[', but ").append(JSONToken.name(lexer.token())).toString());
		deserializer = null;
		if (Integer.TYPE == type)
		{
			deserializer = IntegerDeserializer.instance;
			lexer.nextToken(2);
		} else
		if (java/lang/String == type)
		{
			deserializer = StringDeserializer.instance;
			lexer.nextToken(4);
		} else
		{
			deserializer = config.getDeserializer(type);
			lexer.nextToken(deserializer.getFastMatchToken());
		}
		setContext(array, fieldName);
		int i = 0;
		do
		{
			if (isEnabled(Feature.AllowArbitraryCommas))
				for (; lexer.token() == 16; lexer.nextToken());
			if (lexer.token() == 15)
				break;
			if (Integer.TYPE == type)
			{
				Object val = IntegerDeserializer.deserialze(this);
				array.add(val);
			} else
			if (java/lang/String == type)
			{
				String value;
				if (lexer.token() == 4)
				{
					value = lexer.stringVal();
					lexer.nextToken(16);
				} else
				{
					Object obj = parse();
					if (obj == null)
						value = null;
					else
						value = obj.toString();
				}
				array.add(value);
			} else
			{
				Object val;
				if (lexer.token() == 8)
				{
					lexer.nextToken();
					val = null;
				} else
				{
					val = deserializer.deserialze(this, type, Integer.valueOf(i));
				}
				array.add(val);
			}
			if (lexer.token() == 16)
				lexer.nextToken(deserializer.getFastMatchToken());
			i++;
		} while (true);
		popContext();
		break MISSING_BLOCK_LABEL_443;
		Exception exception;
		exception;
		popContext();
		throw exception;
		lexer.nextToken(16);
		return;
	}

	public Object[] parseArray(Type types[])
	{
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		if (lexer.token() != 14)
			throw new JSONException((new StringBuilder()).append("syntax error : ").append(lexer.tokenName()).toString());
		Object list[] = new Object[types.length];
		if (types.length == 0)
		{
			lexer.nextToken(15);
			if (lexer.token() != 15)
			{
				throw new JSONException("syntax error");
			} else
			{
				lexer.nextToken(16);
				return new Object[0];
			}
		}
		lexer.nextToken(2);
		for (int i = 0; i < types.length; i++)
		{
			Object value;
			if (lexer.token() == 8)
			{
				value = null;
				lexer.nextToken(16);
			} else
			{
				Type type = types[i];
				if (type == Integer.TYPE || type == java/lang/Integer)
				{
					if (lexer.token() == 2)
					{
						value = Integer.valueOf(lexer.intValue());
						lexer.nextToken(16);
					} else
					{
						value = parse();
						value = TypeUtils.cast(value, type, config);
					}
				} else
				if (type == java/lang/String)
				{
					if (lexer.token() == 4)
					{
						value = lexer.stringVal();
						lexer.nextToken(16);
					} else
					{
						value = parse();
						value = TypeUtils.cast(value, type, config);
					}
				} else
				{
					boolean isArray = false;
					Class componentType = null;
					if (i == types.length - 1 && (type instanceof Class))
					{
						Class clazz = (Class)type;
						isArray = clazz.isArray();
						componentType = clazz.getComponentType();
					}
					if (isArray && lexer.token() != 14)
					{
						List varList = new ArrayList();
						ObjectDeserializer derializer = config.getDeserializer(componentType);
						int fastMatch = derializer.getFastMatchToken();
						if (lexer.token() != 15)
						{
							do
							{
								Object item = derializer.deserialze(this, type, null);
								varList.add(item);
								if (lexer.token() != 16)
									break;
								lexer.nextToken(fastMatch);
							} while (true);
							if (lexer.token() != 15)
								throw new JSONException((new StringBuilder()).append("syntax error :").append(JSONToken.name(lexer.token())).toString());
						}
						value = TypeUtils.cast(varList, type, config);
					} else
					{
						ObjectDeserializer derializer = config.getDeserializer(type);
						value = derializer.deserialze(this, type, null);
					}
				}
			}
			list[i] = value;
			if (lexer.token() == 15)
				break;
			if (lexer.token() != 16)
				throw new JSONException((new StringBuilder()).append("syntax error :").append(JSONToken.name(lexer.token())).toString());
			if (i == types.length - 1)
				lexer.nextToken(15);
			else
				lexer.nextToken(2);
		}

		if (lexer.token() != 15)
		{
			throw new JSONException("syntax error");
		} else
		{
			lexer.nextToken(16);
			return list;
		}
	}

	public void parseObject(Object object)
	{
		derializer.parseObject(this, object);
	}

	public Object parseArrayWithType(Type collectionType)
	{
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			return null;
		}
		Type actualTypes[] = ((ParameterizedType)collectionType).getActualTypeArguments();
		if (actualTypes.length != 1)
			throw new JSONException((new StringBuilder()).append("not support type ").append(collectionType).toString());
		Type actualTypeArgument = actualTypes[0];
		if (actualTypeArgument instanceof Class)
		{
			List array = new ArrayList();
			parseArray((Class)actualTypeArgument, array);
			return array;
		}
		if (actualTypeArgument instanceof WildcardType)
		{
			WildcardType wildcardType = (WildcardType)actualTypeArgument;
			Type upperBoundType = wildcardType.getUpperBounds()[0];
			if (java/lang/Object.equals(upperBoundType))
			{
				if (wildcardType.getLowerBounds().length == 0)
					return parse();
				else
					throw new JSONException((new StringBuilder()).append("not support type : ").append(collectionType).toString());
			} else
			{
				List array = new ArrayList();
				parseArray((Class)upperBoundType, array);
				return array;
			}
		}
		if (actualTypeArgument instanceof TypeVariable)
		{
			TypeVariable typeVariable = (TypeVariable)actualTypeArgument;
			Type bounds[] = typeVariable.getBounds();
			if (bounds.length != 1)
				throw new JSONException((new StringBuilder()).append("not support : ").append(typeVariable).toString());
			Type boundType = bounds[0];
			if (boundType instanceof Class)
			{
				List array = new ArrayList();
				parseArray((Class)boundType, array);
				return array;
			}
		}
		if (actualTypeArgument instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType)actualTypeArgument;
			List array = new ArrayList();
			parseArray(parameterizedType, array);
			return array;
		} else
		{
			throw new JSONException((new StringBuilder()).append("TODO : ").append(collectionType).toString());
		}
	}

	public void acceptType(String typeName)
	{
		JSONScanner lexer = (JSONScanner)this.lexer;
		lexer.nextTokenWithColon();
		if (lexer.token() != 4)
			throw new JSONException("type not match error");
		if (typeName.equals(lexer.stringVal()))
		{
			lexer.nextToken();
			if (lexer.token() == 16)
				lexer.nextToken();
		} else
		{
			throw new JSONException("type not match error");
		}
	}

	static 
	{
		primitiveClasses = new HashSet();
		primitiveClasses.add(Boolean.TYPE);
		primitiveClasses.add(Byte.TYPE);
		primitiveClasses.add(Short.TYPE);
		primitiveClasses.add(Integer.TYPE);
		primitiveClasses.add(Long.TYPE);
		primitiveClasses.add(Float.TYPE);
		primitiveClasses.add(Double.TYPE);
		primitiveClasses.add(java/lang/Boolean);
		primitiveClasses.add(java/lang/Byte);
		primitiveClasses.add(java/lang/Short);
		primitiveClasses.add(java/lang/Integer);
		primitiveClasses.add(java/lang/Long);
		primitiveClasses.add(java/lang/Float);
		primitiveClasses.add(java/lang/Double);
		primitiveClasses.add(java/math/BigInteger);
		primitiveClasses.add(java/math/BigDecimal);
		primitiveClasses.add(java/lang/String);
	}
}
