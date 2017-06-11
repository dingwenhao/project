// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JavaBeanDeserializer.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.util.*;
import java.lang.reflect.*;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			FieldDeserializer, ObjectDeserializer

public class JavaBeanDeserializer
	implements ObjectDeserializer
{

	private final Map feildDeserializerMap;
	private final List fieldDeserializers;
	private final Class clazz;
	private DeserializeBeanInfo beanInfo;

	public JavaBeanDeserializer(DeserializeBeanInfo beanInfo)
	{
		feildDeserializerMap = new IdentityHashMap();
		fieldDeserializers = new ArrayList();
		this.beanInfo = beanInfo;
		clazz = beanInfo.getClass();
	}

	public JavaBeanDeserializer(ParserConfig config, Class clazz)
	{
		feildDeserializerMap = new IdentityHashMap();
		fieldDeserializers = new ArrayList();
		this.clazz = clazz;
		beanInfo = DeserializeBeanInfo.computeSetters(clazz);
		FieldInfo fieldInfo;
		for (Iterator i$ = beanInfo.getFieldList().iterator(); i$.hasNext(); addFieldDeserializer(config, clazz, fieldInfo))
			fieldInfo = (FieldInfo)i$.next();

	}

	public Map getFieldDeserializerMap()
	{
		return feildDeserializerMap;
	}

	public Class getClazz()
	{
		return clazz;
	}

	private void addFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		FieldDeserializer fieldDeserializer = createFieldDeserializer(mapping, clazz, fieldInfo);
		feildDeserializerMap.put(fieldInfo.getName().intern(), fieldDeserializer);
		fieldDeserializers.add(fieldDeserializer);
	}

	public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
	{
		return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
	}

	public Object createInstance(DefaultJSONParser parser, Type type)
	{
		if ((type instanceof Class) && this.clazz.isInterface())
		{
			Class clazz = (Class)type;
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			JSONObject obj = new JSONObject();
			Object proxy = Proxy.newProxyInstance(loader, new Class[] {
				clazz
			}, obj);
			return proxy;
		}
		if (beanInfo.getDefaultConstructor() == null)
			return null;
		Object object;
		try
		{
			Constructor constructor = beanInfo.getDefaultConstructor();
			if (constructor.getParameterTypes().length == 0)
				object = constructor.newInstance(new Object[0]);
			else
				object = constructor.newInstance(new Object[] {
					parser.getContext().getObject()
				});
		}
		catch (Exception e)
		{
			throw new JSONException((new StringBuilder()).append("create instance error, class ").append(this.clazz.getName()).toString(), e);
		}
		if (parser.isEnabled(Feature.InitStringFieldAsEmpty))
		{
			Iterator i$ = beanInfo.getFieldList().iterator();
			do
			{
				if (!i$.hasNext())
					break;
				FieldInfo fieldInfo = (FieldInfo)i$.next();
				if (fieldInfo.getFieldClass() == java/lang/String)
					try
					{
						fieldInfo.set(object, "");
					}
					catch (Exception e)
					{
						throw new JSONException((new StringBuilder()).append("create instance error, class ").append(this.clazz.getName()).toString(), e);
					}
			} while (true);
		}
		return object;
	}

	public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName)
	{
		JSONScanner lexer;
		ParseContext context;
		ParseContext childContext;
		Object object;
		lexer = (JSONScanner)parser.getLexer();
		if (lexer.token() == 8)
		{
			lexer.nextToken(16);
			return null;
		}
		context = parser.getContext();
		childContext = null;
		object = null;
		Map fieldValues;
		String key;
		fieldValues = null;
		if (lexer.token() != 13)
			break MISSING_BLOCK_LABEL_89;
		lexer.nextToken(16);
		key = ((String) (createInstance(parser, type)));
		if (childContext != null)
			childContext.setObject(object);
		parser.setContext(context);
		return key;
		if (lexer.token() != 12 && lexer.token() != 16)
			throw new JSONException((new StringBuilder()).append("syntax error, expect {, actual ").append(lexer.tokenName()).toString());
		if (parser.getResolveStatus() == 2)
			parser.setResolveStatus(0);
_L3:
		key = lexer.scanSymbol(parser.getSymbolTable());
		if (key != null) goto _L2; else goto _L1
_L1:
		if (lexer.token() == 13)
		{
			lexer.nextToken(16);
			break MISSING_BLOCK_LABEL_813;
		}
		if (lexer.token() != 16 || !parser.isEnabled(Feature.AllowArbitraryCommas)) goto _L2; else goto _L3
_L2:
		if ("$ref" != key) goto _L5; else goto _L4
_L4:
		Object obj;
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
		childContext = parser.setContext(context, object, fieldName);
		obj = object;
		if (childContext != null)
			childContext.setObject(object);
		parser.setContext(context);
		return obj;
_L5:
		Object obj1;
		if ("@type" != key)
			break MISSING_BLOCK_LABEL_635;
		lexer.nextTokenWithColon(4);
		if (lexer.token() != 4)
			break MISSING_BLOCK_LABEL_625;
		String typeName = lexer.stringVal();
		lexer.nextToken(16);
		Class userType = TypeUtils.loadClass(typeName);
		ObjectDeserializer deserizer = parser.getConfig().getDeserializer(userType);
		obj1 = deserizer.deserialze(parser, userType, fieldName);
		if (childContext != null)
			childContext.setObject(object);
		parser.setContext(context);
		return obj1;
		throw new JSONException("syntax error");
		if (object == null && fieldValues == null)
		{
			object = createInstance(parser, type);
			if (object == null)
				fieldValues = new HashMap(fieldDeserializers.size());
			childContext = parser.setContext(context, object, fieldName);
		}
		boolean match = parseField(parser, key, object, type, fieldValues);
		if (!match)
		{
			if (lexer.token() == 13)
			{
				lexer.nextToken();
				break MISSING_BLOCK_LABEL_813;
			}
		} else
		if (lexer.token() != 16)
		{
			if (lexer.token() == 13)
			{
				lexer.nextToken(16);
				break MISSING_BLOCK_LABEL_813;
			}
			if (lexer.token() == 18 || lexer.token() == 1)
				throw new JSONException((new StringBuilder()).append("syntax error, unexpect token ").append(JSONToken.name(lexer.token())).toString());
		}
		  goto _L3
		if (object != null)
			break MISSING_BLOCK_LABEL_1063;
		if (fieldValues != null)
			break MISSING_BLOCK_LABEL_856;
		object = createInstance(parser, type);
		key = ((String) (object));
		if (childContext != null)
			childContext.setObject(object);
		parser.setContext(context);
		return key;
		List fieldInfoList = beanInfo.getFieldList();
		int size = fieldInfoList.size();
		Object params[] = new Object[size];
		for (int i = 0; i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)fieldInfoList.get(i);
			params[i] = fieldValues.get(fieldInfo.getName());
		}

		if (beanInfo.getCreatorConstructor() != null)
			try
			{
				object = beanInfo.getCreatorConstructor().newInstance(params);
			}
			catch (Exception e)
			{
				throw new JSONException((new StringBuilder()).append("create instance error, ").append(beanInfo.getCreatorConstructor().toGenericString()).toString(), e);
			}
		else
		if (beanInfo.getFactoryMethod() != null)
			try
			{
				object = beanInfo.getFactoryMethod().invoke(null, params);
			}
			catch (Exception e)
			{
				throw new JSONException((new StringBuilder()).append("create factory method error, ").append(beanInfo.getFactoryMethod().toString()).toString(), e);
			}
		fieldInfoList = ((List) (object));
		if (childContext != null)
			childContext.setObject(object);
		parser.setContext(context);
		return fieldInfoList;
		Exception exception;
		exception;
		if (childContext != null)
			childContext.setObject(object);
		parser.setContext(context);
		throw exception;
	}

	public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map fieldValues)
	{
		JSONScanner lexer = (JSONScanner)parser.getLexer();
		FieldDeserializer fieldDeserializer = (FieldDeserializer)feildDeserializerMap.get(key);
		if (fieldDeserializer == null)
		{
			if (!parser.isEnabled(Feature.IgnoreNotMatch))
			{
				throw new JSONException((new StringBuilder()).append("setter not found, class ").append(clazz.getName()).append(", property ").append(key).toString());
			} else
			{
				lexer.nextTokenWithColon();
				parser.parse();
				return false;
			}
		} else
		{
			lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
			fieldDeserializer.parseField(parser, object, objectType, fieldValues);
			return true;
		}
	}

	public int getFastMatchToken()
	{
		return 12;
	}
}
