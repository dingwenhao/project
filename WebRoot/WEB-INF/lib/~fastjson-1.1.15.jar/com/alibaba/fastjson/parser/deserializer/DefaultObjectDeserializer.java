// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultObjectDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer, IntegerDeserializer, StringDeserializer, 
//			LongDeserializer, CollectionDeserializer

public class DefaultObjectDeserializer
	implements ObjectDeserializer
{

	public static final DefaultObjectDeserializer instance = new DefaultObjectDeserializer();

	public DefaultObjectDeserializer()
	{
	}

	public Object parseMap(DefaultJSONParser parser, Map map, Type keyType, Type valueType, Object fieldName)
	{
		JSONScanner lexer;
		ObjectDeserializer keyDeserializer;
		ObjectDeserializer valueDeserializer;
		ParseContext context;
		lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException((new StringBuilder()).append("syntax error, expect {, actual ").append(lexer.tokenName()).toString());
		keyDeserializer = parser.getConfig().getDeserializer(keyType);
		valueDeserializer = parser.getConfig().getDeserializer(valueType);
		lexer.nextToken(keyDeserializer.getFastMatchToken());
		context = parser.getContext();
_L2:
		Object obj;
		if (lexer.token() == 13)
		{
			lexer.nextToken(16);
			break; /* Loop/switch isn't completed */
		}
		if (lexer.token() != 4 || !lexer.isRef())
			break MISSING_BLOCK_LABEL_434;
		Object object = null;
		lexer.nextTokenWithColon(4);
		if (lexer.token() == 4)
		{
			String ref = lexer.stringVal();
			if ("@".equals(ref))
				object = context.getObject();
			else
			if ("..".equals(ref))
			{
				ParseContext parentContext = context.getParentContext();
				if (parentContext.getObject() != null)
				{
					object = parentContext.getObject();
				} else
				{
					parser.getResolveTaskList().add(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(parentContext, ref));
					parser.setResolveStatus(1);
				}
			} else
			if ("$".equals(ref))
			{
				ParseContext rootContext;
				for (rootContext = context; rootContext.getParentContext() != null; rootContext = rootContext.getParentContext());
				if (rootContext.getObject() != null)
				{
					object = rootContext.getObject();
				} else
				{
					parser.getResolveTaskList().add(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(rootContext, ref));
					parser.setResolveStatus(1);
				}
			} else
			{
				parser.getResolveTaskList().add(new com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask(context, ref));
				parser.setResolveStatus(1);
			}
		} else
		{
			throw new JSONException((new StringBuilder()).append("illegal ref, ").append(JSONToken.name(lexer.token())).toString());
		}
		lexer.nextToken(13);
		if (lexer.token() != 13)
			throw new JSONException("illegal ref");
		lexer.nextToken(16);
		obj = object;
		parser.setContext(context);
		return obj;
		if (map.size() == 0 && lexer.token() == 4 && "@type".equals(lexer.stringVal()))
		{
			lexer.nextTokenWithColon(4);
			lexer.nextToken(16);
			lexer.nextToken(keyDeserializer.getFastMatchToken());
		}
		Object key = keyDeserializer.deserialze(parser, keyType, null);
		if (lexer.token() != 17)
			throw new JSONException((new StringBuilder()).append("syntax error, expect :, actual ").append(lexer.token()).toString());
		lexer.nextToken(valueDeserializer.getFastMatchToken());
		Object value = valueDeserializer.deserialze(parser, valueType, key);
		if (map.size() == 0 && context != null && context.getObject() != map)
			parser.setContext(context, map, fieldName);
		map.put(key, value);
		if (lexer.token() == 16)
			lexer.nextToken(keyDeserializer.getFastMatchToken());
		if (true) goto _L2; else goto _L1
_L1:
		parser.setContext(context);
		break MISSING_BLOCK_LABEL_658;
		Exception exception;
		exception;
		parser.setContext(context);
		throw exception;
		return map;
	}

	public Map parseMap(DefaultJSONParser parser, Map map, Type valueType, Object fieldName)
	{
		JSONScanner lexer;
		ParseContext context;
		lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() != 12)
			throw new JSONException((new StringBuilder()).append("syntax error, expect {, actual ").append(lexer.token()).toString());
		context = parser.getContext();
_L1:
		char ch;
		String key;
		Map map1;
		lexer.skipWhitespace();
		ch = lexer.getCurrent();
		if (parser.isEnabled(Feature.AllowArbitraryCommas))
			for (; ch == ','; ch = lexer.getCurrent())
			{
				lexer.incrementBufferPosition();
				lexer.skipWhitespace();
			}

		if (ch == '"')
		{
			key = lexer.scanSymbol(parser.getSymbolTable(), '"');
			lexer.skipWhitespace();
			ch = lexer.getCurrent();
			if (ch != ':')
				throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).toString());
			break MISSING_BLOCK_LABEL_385;
		}
		if (ch != '}')
			break MISSING_BLOCK_LABEL_204;
		lexer.incrementBufferPosition();
		lexer.resetStringPosition();
		map1 = map;
		parser.setContext(context);
		return map1;
		if (ch == '\'')
		{
			if (!parser.isEnabled(Feature.AllowSingleQuotes))
				throw new JSONException("syntax error");
			key = lexer.scanSymbol(parser.getSymbolTable(), '\'');
			lexer.skipWhitespace();
			ch = lexer.getCurrent();
			if (ch != ':')
				throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).toString());
		} else
		{
			if (!parser.isEnabled(Feature.AllowUnQuotedFieldNames))
				throw new JSONException("syntax error");
			key = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
			lexer.skipWhitespace();
			ch = lexer.getCurrent();
			if (ch != ':')
				throw new JSONException((new StringBuilder()).append("expect ':' at ").append(lexer.pos()).append(", actual ").append(ch).toString());
		}
		Class clazz;
		lexer.incrementBufferPosition();
		lexer.skipWhitespace();
		ch = lexer.getCurrent();
		lexer.resetStringPosition();
		if (key != "@type")
			break MISSING_BLOCK_LABEL_502;
		String typeName = lexer.scanSymbol(parser.getSymbolTable(), '"');
		clazz = TypeUtils.loadClass(typeName);
		if (clazz != map.getClass())
			break MISSING_BLOCK_LABEL_453;
		lexer.nextToken(16);
		  goto _L1
		Map map2;
		ObjectDeserializer deserializer = parser.getConfig().getDeserializer(clazz);
		lexer.nextToken(16);
		parser.setResolveStatus(2);
		map2 = (Map)deserializer.deserialze(parser, clazz, fieldName);
		parser.setContext(context);
		return map2;
		lexer.nextToken();
		Object value;
		if (lexer.token() == 8)
		{
			value = null;
			lexer.nextToken();
		} else
		{
			value = parser.parseObject(valueType);
		}
		map.put(key, value);
		parser.setContext(context, value, key);
		if (lexer.token() != 13) goto _L1; else goto _L2
_L2:
		lexer.nextToken();
		clazz = map;
		parser.setContext(context);
		return clazz;
		Exception exception;
		exception;
		parser.setContext(context);
		throw exception;
	}

	public void parseObject(DefaultJSONParser parser, Object object)
	{
		Class clazz = object.getClass();
		Map setters = parser.getConfig().getFieldDeserializers(clazz);
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() == 13)
		{
			lexer.nextToken(16);
			return;
		}
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException((new StringBuilder()).append("syntax error, expect {, actual ").append(lexer.tokenName()).toString());
		Object args[] = new Object[1];
label0:
		do
		{
			FieldDeserializer fieldDeser;
			do
			{
				String key = lexer.scanSymbol(parser.getSymbolTable());
				if (key == null)
				{
					if (lexer.token() == 13)
					{
						lexer.nextToken(16);
						break label0;
					}
					if (lexer.token() == 16 && parser.isEnabled(Feature.AllowArbitraryCommas))
						continue;
				}
				fieldDeser = (FieldDeserializer)setters.get(key);
				if (fieldDeser != null)
					break;
				if (!parser.isEnabled(Feature.IgnoreNotMatch))
					throw new JSONException((new StringBuilder()).append("setter not found, class ").append(clazz.getName()).append(", property ").append(key).toString());
				lexer.nextTokenWithColon();
				parser.parse();
				if (lexer.token() == 13)
				{
					lexer.nextToken();
					return;
				}
			} while (true);
			Method method = fieldDeser.getMethod();
			Class fieldClass = method.getParameterTypes()[0];
			Type fieldType = method.getGenericParameterTypes()[0];
			if (fieldClass == Integer.TYPE)
			{
				lexer.nextTokenWithColon(2);
				args[0] = IntegerDeserializer.deserialze(parser);
			} else
			if (fieldClass == java/lang/String)
			{
				lexer.nextTokenWithColon(4);
				args[0] = StringDeserializer.deserialze(parser);
			} else
			if (fieldClass == Long.TYPE)
			{
				lexer.nextTokenWithColon(2);
				args[0] = LongDeserializer.deserialze(parser);
			} else
			if (fieldClass == java/util/List)
			{
				lexer.nextTokenWithColon(12);
				args[0] = CollectionDeserializer.instance.deserialze(parser, fieldType, null);
			} else
			{
				ObjectDeserializer fieldValueDeserializer = parser.getConfig().getDeserializer(fieldClass, fieldType);
				lexer.nextTokenWithColon(fieldValueDeserializer.getFastMatchToken());
				args[0] = fieldValueDeserializer.deserialze(parser, fieldType, null);
			}
			try
			{
				method.invoke(object, args);
			}
			catch (Exception e)
			{
				throw new JSONException((new StringBuilder()).append("set proprety error, ").append(method.getName()).toString(), e);
			}
			if (lexer.token() != 16 && lexer.token() == 13)
			{
				lexer.nextToken(16);
				return;
			}
		} while (true);
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		if (type instanceof Class)
			return deserialze(parser, (Class)type);
		if (type instanceof ParameterizedType)
			return deserialze(parser, (ParameterizedType)type, fieldName);
		if (type instanceof TypeVariable)
			return parser.parse(fieldName);
		if (type instanceof WildcardType)
			return parser.parse(fieldName);
		if (type instanceof GenericArrayType)
		{
			Type componentType = ((GenericArrayType)type).getGenericComponentType();
			List list = new ArrayList();
			parser.parseArray(componentType, list);
			if (componentType instanceof Class)
			{
				Class componentClass = (Class)componentType;
				Object array[] = (Object[])(Object[])Array.newInstance(componentClass, list.size());
				list.toArray(array);
				return ((Object) (array));
			}
		}
		throw new JSONException((new StringBuilder()).append("not support type : ").append(type).toString());
	}

	public Object deserialze(DefaultJSONParser parser, ParameterizedType type, Object fieldName)
	{
		JSONLexer lexer = parser.getLexer();
		if (lexer.token() != 8)
			break MISSING_BLOCK_LABEL_27;
		lexer.nextToken();
		return null;
		Map map;
		Type keyType;
		Type valueType;
		Type rawType = type.getRawType();
		if (!(rawType instanceof Class))
			break MISSING_BLOCK_LABEL_242;
		Class rawClass = (Class)rawType;
		if (!java/util/Map.isAssignableFrom(rawClass))
			break MISSING_BLOCK_LABEL_242;
		if (Modifier.isAbstract(rawClass.getModifiers()))
		{
			if (rawClass == java/util/Map)
				map = new HashMap();
			else
			if (rawClass == java/util/SortedMap)
				map = new TreeMap();
			else
			if (rawClass == java/util/concurrent/ConcurrentMap)
				map = new ConcurrentHashMap();
			else
				throw new JSONException((new StringBuilder()).append("can not create instance : ").append(rawClass).toString());
		} else
		if (rawClass == java/util/HashMap)
			map = new HashMap();
		else
			map = (Map)rawClass.newInstance();
		keyType = type.getActualTypeArguments()[0];
		valueType = type.getActualTypeArguments()[1];
		if (keyType == java/lang/String)
			return parseMap(parser, map, valueType, fieldName);
		return parseMap(parser, map, keyType, valueType, fieldName);
		try
		{
			throw new JSONException((new StringBuilder()).append("not support type : ").append(type).toString());
		}
		catch (JSONException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new JSONException(e.getMessage(), e);
		}
	}

	public Object deserialze(DefaultJSONParser parser, Class clazz)
	{
		Object value;
		value = null;
		if (clazz.isAssignableFrom(java/util/HashMap))
			value = new HashMap();
		else
		if (clazz.isAssignableFrom(java/util/TreeMap))
			value = new TreeMap();
		else
		if (clazz.isAssignableFrom(java/util/concurrent/ConcurrentHashMap))
			value = new ConcurrentHashMap();
		else
		if (clazz.isAssignableFrom(java/util/Properties))
			value = new Properties();
		else
		if (clazz.isAssignableFrom(java/util/IdentityHashMap))
			value = new IdentityHashMap();
		if (clazz == java/lang/Class)
		{
			Object classValue = parser.parse();
			if (classValue == null)
				return null;
			if (classValue instanceof String)
				return ASMClassLoader.forName((String)classValue);
		}
		if (value == null)
			throw new JSONException((new StringBuilder()).append("not support type : ").append(clazz).toString());
		parseObject(parser, value);
		return value;
		JSONException e;
		e;
		throw e;
		e;
		throw new JSONException(e.getMessage(), e);
	}

	public int getFastMatchToken()
	{
		return 12;
	}

}
