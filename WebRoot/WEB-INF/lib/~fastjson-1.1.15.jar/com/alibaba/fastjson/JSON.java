// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSON.java

package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.ThreadLocalCache;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson:
//			JSONObject, JSONArray, JSONException, JSONStreamAware, 
//			JSONAware, TypeReference

public abstract class JSON
	implements JSONStreamAware, JSONAware
{

	public static int DEFAULT_PARSER_FEATURE;
	public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static int DEFAULT_GENERATE_FEATURE;
	public static final String VERSION = "1.1.15";

	public JSON()
	{
	}

	public static final Object parse(String text)
	{
		return parse(text, DEFAULT_PARSER_FEATURE);
	}

	public static final Object parse(String text, int features)
	{
		if (text == null)
		{
			return null;
		} else
		{
			DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance(), features);
			Object value = parser.parse();
			handleResovleTask(parser, value);
			parser.close();
			return value;
		}
	}

	public static final transient Object parse(byte input[], Feature features[])
	{
		return parse(input, 0, input.length, ThreadLocalCache.getUTF8Decoder(), features);
	}

	public static final transient Object parse(byte input[], int off, int len, CharsetDecoder charsetDecoder, Feature features[])
	{
		if (input == null || input.length == 0)
			return null;
		int featureValues = DEFAULT_PARSER_FEATURE;
		Feature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Feature featrue = arr$[i$];
			featureValues = Feature.config(featureValues, featrue, true);
		}

		return parse(input, off, len, charsetDecoder, featureValues);
	}

	public static final Object parse(byte input[], int off, int len, CharsetDecoder charsetDecoder, int features)
	{
		charsetDecoder.reset();
		int scaleLength = (int)((double)len * (double)charsetDecoder.maxCharsPerByte());
		char chars[] = ThreadLocalCache.getChars(scaleLength);
		ByteBuffer byteBuf = ByteBuffer.wrap(input, off, len);
		CharBuffer charBuf = CharBuffer.wrap(chars);
		IOUtils.decode(charsetDecoder, byteBuf, charBuf);
		int position = charBuf.position();
		DefaultJSONParser parser = new DefaultJSONParser(chars, position, ParserConfig.getGlobalInstance(), features);
		Object value = parser.parse();
		handleResovleTask(parser, value);
		parser.close();
		return value;
	}

	public static final transient Object parse(String text, Feature features[])
	{
		int featureValues = DEFAULT_PARSER_FEATURE;
		Feature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Feature featrue = arr$[i$];
			featureValues = Feature.config(featureValues, featrue, true);
		}

		return parse(text, featureValues);
	}

	public static final transient JSONObject parseObject(String text, Feature features[])
	{
		return (JSONObject)parse(text, features);
	}

	public static final JSONObject parseObject(String text)
	{
		Object obj = parse(text);
		if (obj instanceof JSONObject)
			return (JSONObject)obj;
		else
			return (JSONObject)toJSON(obj);
	}

	public static final transient Object parseObject(String text, TypeReference type, Feature features[])
	{
		return parseObject(text, type.getType(), ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, features);
	}

	public static final transient Object parseObject(String text, Class clazz, Feature features[])
	{
		return parseObject(text, ((Type) (clazz)), ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, features);
	}

	public static final transient Object parseObject(String input, Type clazz, Feature features[])
	{
		return parseObject(input, clazz, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, features);
	}

	public static final transient Object parseObject(String input, Type clazz, int featureValues, Feature features[])
	{
		if (input == null)
			return null;
		Feature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Feature featrue = arr$[i$];
			featureValues = Feature.config(featureValues, featrue, true);
		}

		DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
		Object value = parser.parseObject(clazz);
		handleResovleTask(parser, value);
		parser.close();
		return value;
	}

	public static final transient Object parseObject(String input, Type clazz, ParserConfig config, int featureValues, Feature features[])
	{
		if (input == null)
			return null;
		Feature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Feature featrue = arr$[i$];
			featureValues = Feature.config(featureValues, featrue, true);
		}

		DefaultJSONParser parser = new DefaultJSONParser(input, config, featureValues);
		Object value = parser.parseObject(clazz);
		handleResovleTask(parser, value);
		parser.close();
		return value;
	}

	public static void handleResovleTask(DefaultJSONParser parser, Object value)
	{
		if (parser.isEnabled(Feature.DisableCircularReferenceDetect))
			return;
		int size = parser.getResolveTaskList().size();
		for (int i = 0; i < size; i++)
		{
			com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask task = (com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask)parser.getResolveTaskList().get(i);
			FieldDeserializer fieldDeser = task.getFieldDeserializer();
			Object object = null;
			if (task.getOwnerContext() != null)
				object = task.getOwnerContext().getObject();
			String ref = task.getReferenceValue();
			Object refValue;
			if (ref.startsWith("$"))
				refValue = parser.getObject(ref);
			else
				refValue = task.getContext().getObject();
			fieldDeser.setValue(object, refValue);
		}

	}

	public static final transient Object parseObject(byte input[], Type clazz, Feature features[])
	{
		return parseObject(input, 0, input.length, ThreadLocalCache.getUTF8Decoder(), clazz, features);
	}

	public static final transient Object parseObject(byte input[], int off, int len, CharsetDecoder charsetDecoder, Type clazz, Feature features[])
	{
		charsetDecoder.reset();
		int scaleLength = (int)((double)len * (double)charsetDecoder.maxCharsPerByte());
		char chars[] = ThreadLocalCache.getChars(scaleLength);
		ByteBuffer byteBuf = ByteBuffer.wrap(input, off, len);
		CharBuffer charByte = CharBuffer.wrap(chars);
		IOUtils.decode(charsetDecoder, byteBuf, charByte);
		int position = charByte.position();
		return parseObject(chars, position, clazz, features);
	}

	public static final transient Object parseObject(char input[], int length, Type clazz, Feature features[])
	{
		if (input == null || input.length == 0)
			return null;
		int featureValues = DEFAULT_PARSER_FEATURE;
		Feature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			Feature featrue = arr$[i$];
			featureValues = Feature.config(featureValues, featrue, true);
		}

		DefaultJSONParser parser = new DefaultJSONParser(input, length, ParserConfig.getGlobalInstance(), featureValues);
		Object value = parser.parseObject(clazz);
		handleResovleTask(parser, value);
		parser.close();
		return value;
	}

	public static final Object parseObject(String text, Class clazz)
	{
		return parseObject(text, clazz, new Feature[0]);
	}

	public static final JSONArray parseArray(String text)
	{
		if (text == null)
			return null;
		DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());
		JSONLexer lexer = parser.getLexer();
		JSONArray array;
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			array = null;
		} else
		if (lexer.token() == 20)
		{
			array = null;
		} else
		{
			array = new JSONArray();
			parser.parseArray(array);
			handleResovleTask(parser, array);
		}
		parser.close();
		return array;
	}

	public static final List parseArray(String text, Class clazz)
	{
		if (text == null)
			return null;
		DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());
		JSONLexer lexer = parser.getLexer();
		List list;
		if (lexer.token() == 8)
		{
			lexer.nextToken();
			list = null;
		} else
		{
			list = new ArrayList();
			parser.parseArray(clazz, list);
			handleResovleTask(parser, list);
		}
		parser.close();
		return list;
	}

	public static final List parseArray(String text, Type types[])
	{
		if (text == null)
			return null;
		DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());
		Object objectArray[] = parser.parseArray(types);
		List list;
		if (objectArray == null)
			list = null;
		else
			list = Arrays.asList(objectArray);
		handleResovleTask(parser, list);
		parser.close();
		return list;
	}

	public static final String toJSONString(Object object)
	{
		return toJSONString(object, new SerializerFeature[0]);
	}

	public static final transient String toJSONString(Object object, SerializerFeature features[])
	{
		SerializeWriter out = new SerializeWriter();
		String s;
		JSONSerializer serializer = new JSONSerializer(out);
		SerializerFeature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			SerializerFeature feature = arr$[i$];
			serializer.config(feature, true);
		}

		serializer.write(object);
		s = out.toString();
		out.close();
		return s;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static final transient String toJSONStringWithDateFormat(Object object, String dateFormat, SerializerFeature features[])
	{
		SerializeWriter out = new SerializeWriter();
		String s;
		JSONSerializer serializer = new JSONSerializer(out);
		SerializerFeature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			SerializerFeature feature = arr$[i$];
			serializer.config(feature, true);
		}

		serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
		if (dateFormat != null)
			serializer.setDateFormat(dateFormat);
		serializer.write(object);
		s = out.toString();
		out.close();
		return s;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static final transient byte[] toJSONBytes(Object object, SerializerFeature features[])
	{
		SerializeWriter out = new SerializeWriter();
		SerializerFeature arr$[];
		JSONSerializer serializer = new JSONSerializer(out);
		arr$ = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			SerializerFeature feature = arr$[i$];
			serializer.config(feature, true);
		}

		serializer.write(object);
		arr$ = out.toBytes("UTF-8");
		out.close();
		return arr$;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static final transient String toJSONString(Object object, SerializeConfig config, SerializerFeature features[])
	{
		SerializeWriter out = new SerializeWriter();
		String s;
		JSONSerializer serializer = new JSONSerializer(out, config);
		SerializerFeature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			SerializerFeature feature = arr$[i$];
			serializer.config(feature, true);
		}

		serializer.write(object);
		s = out.toString();
		out.close();
		return s;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static final transient String toJSONStringZ(Object object, SerializeConfig mapping, SerializerFeature features[])
	{
		SerializeWriter out = new SerializeWriter(features);
		String s;
		JSONSerializer serializer = new JSONSerializer(out, mapping);
		serializer.write(object);
		s = out.toString();
		out.close();
		return s;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static final transient byte[] toJSONBytes(Object object, SerializeConfig config, SerializerFeature features[])
	{
		SerializeWriter out = new SerializeWriter();
		SerializerFeature arr$[];
		JSONSerializer serializer = new JSONSerializer(out, config);
		arr$ = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			SerializerFeature feature = arr$[i$];
			serializer.config(feature, true);
		}

		serializer.write(object);
		arr$ = out.toBytes("UTF-8");
		out.close();
		return arr$;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public static final String toJSONString(Object object, boolean prettyFormat)
	{
		if (!prettyFormat)
			return toJSONString(object);
		else
			return toJSONString(object, new SerializerFeature[] {
				SerializerFeature.PrettyFormat
			});
	}

	public String toString()
	{
		return toJSONString();
	}

	public String toJSONString()
	{
		SerializeWriter out = new SerializeWriter();
		String s;
		(new JSONSerializer(out)).write(this);
		s = out.toString();
		out.close();
		return s;
		Exception exception;
		exception;
		out.close();
		throw exception;
	}

	public void writeJSONString(Appendable appendable)
	{
		Exception exception;
		SerializeWriter out = new SerializeWriter();
		try
		{
			(new JSONSerializer(out)).write(this);
			appendable.append(out.toString());
		}
		catch (IOException e)
		{
			throw new JSONException(e.getMessage(), e);
		}
		finally
		{
			out.close();
		}
		out.close();
		break MISSING_BLOCK_LABEL_61;
		throw exception;
	}

	public static final Object toJSON(Object javaObject)
	{
		return toJSON(javaObject, ParserConfig.getGlobalInstance());
	}

	public static final Object toJSON(Object javaObject, ParserConfig mapping)
	{
		Class clazz;
		if (javaObject == null)
			return null;
		if (javaObject instanceof JSON)
			return (JSON)javaObject;
		if (javaObject instanceof Map)
		{
			Map map = (Map)javaObject;
			JSONObject json = new JSONObject(map.size());
			java.util.Map.Entry entry;
			Object jsonValue;
			for (Iterator i$ = map.entrySet().iterator(); i$.hasNext(); json.put((String)entry.getKey(), jsonValue))
			{
				entry = (java.util.Map.Entry)i$.next();
				jsonValue = toJSON(entry.getValue());
			}

			return json;
		}
		if (javaObject instanceof Collection)
		{
			Collection collection = (Collection)javaObject;
			JSONArray array = new JSONArray(collection.size());
			Object jsonValue;
			for (Iterator i$ = collection.iterator(); i$.hasNext(); array.add(jsonValue))
			{
				Object item = i$.next();
				jsonValue = toJSON(item);
			}

			return array;
		}
		clazz = javaObject.getClass();
		if (clazz.isEnum())
			return ((Enum)javaObject).name();
		if (clazz.isArray())
		{
			int len = Array.getLength(javaObject);
			JSONArray array = new JSONArray(len);
			for (int i = 0; i < len; i++)
			{
				Object item = Array.get(javaObject, i);
				Object jsonValue = toJSON(item);
				array.add(jsonValue);
			}

			return array;
		}
		if (mapping.isPrimitive(clazz))
			return javaObject;
		JSONObject json;
		List getters = TypeUtils.computeGetters(clazz, null);
		json = new JSONObject(getters.size());
		FieldInfo field;
		Object jsonValue;
		for (Iterator i$ = getters.iterator(); i$.hasNext(); json.put(field.getName(), jsonValue))
		{
			field = (FieldInfo)i$.next();
			Object value = field.get(javaObject);
			jsonValue = toJSON(value);
		}

		return json;
		Exception e;
		e;
		throw new JSONException("toJSON error", e);
	}

	public static final Object toJavaObject(JSON json, Class clazz)
	{
		return TypeUtils.cast(json, clazz, ParserConfig.getGlobalInstance());
	}

	static 
	{
		int features = 0;
		features |= Feature.AutoCloseSource.getMask();
		features |= Feature.InternFieldNames.getMask();
		features |= Feature.UseBigDecimal.getMask();
		features |= Feature.AllowUnQuotedFieldNames.getMask();
		features |= Feature.AllowSingleQuotes.getMask();
		features |= Feature.AllowArbitraryCommas.getMask();
		features |= Feature.SortFeidFastMatch.getMask();
		features |= Feature.IgnoreNotMatch.getMask();
		DEFAULT_PARSER_FEATURE = features;
		features = 0;
		features |= SerializerFeature.QuoteFieldNames.getMask();
		features |= SerializerFeature.SkipTransientField.getMask();
		features |= SerializerFeature.WriteEnumUsingToString.getMask();
		features |= SerializerFeature.SortField.getMask();
		DEFAULT_GENERATE_FEATURE = features;
	}
}
