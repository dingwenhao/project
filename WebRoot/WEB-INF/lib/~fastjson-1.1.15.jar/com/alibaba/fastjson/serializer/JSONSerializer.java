// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONSerializer.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.util.ServiceLoader;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			SerializeWriter, SerialContext, ObjectSerializer, AutowiredObjectSerializer, 
//			ArraySerializer, ExceptionSerializer, SerializeConfig, SerializerFeature, 
//			StringSerializer, MapSerializer, ListSerializer, CollectionSerializer, 
//			DateSerializer, JSONAwareSerializer, JSONStreamAwareSerializer, EnumSerializer, 
//			TimeZoneSerializer, AppendableSerializer, CharsetSerializer, EnumerationSeriliazer, 
//			JSONSerializerMap

public class JSONSerializer
{

	private final SerializeConfig config;
	private final SerializeWriter out;
	private List propertyFilters;
	private List valueFilters;
	private List nameFilters;
	private int indentCount;
	private String indent;
	private String dateFormatPatterm;
	private DateFormat dateFormat;
	private IdentityHashMap references;
	private SerialContext context;

	public JSONSerializer()
	{
		this(new SerializeWriter(), SerializeConfig.getGlobalInstance());
	}

	public JSONSerializer(SerializeWriter out)
	{
		this(out, SerializeConfig.getGlobalInstance());
	}

	public JSONSerializer(SerializeConfig config)
	{
		this(new SerializeWriter(), config);
	}

	/**
	 * @deprecated Method JSONSerializer is deprecated
	 */

	public JSONSerializer(JSONSerializerMap mapping)
	{
		this(new SerializeWriter(), ((SerializeConfig) (mapping)));
	}

	public JSONSerializer(SerializeWriter out, SerializeConfig config)
	{
		propertyFilters = null;
		valueFilters = null;
		nameFilters = null;
		indentCount = 0;
		indent = "\t";
		dateFormatPatterm = JSON.DEFFAULT_DATE_FORMAT;
		references = null;
		this.out = out;
		this.config = config;
	}

	public String getDateFormatPattern()
	{
		return dateFormatPatterm;
	}

	public DateFormat getDateFormat()
	{
		if (dateFormat == null)
			dateFormat = new SimpleDateFormat(dateFormatPatterm);
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public void setDateFormat(String dateFormat)
	{
		dateFormatPatterm = dateFormat;
		if (this.dateFormat != null)
			this.dateFormat = null;
	}

	public SerialContext getContext()
	{
		return context;
	}

	public void setContext(SerialContext context)
	{
		this.context = context;
	}

	public void setContext(SerialContext parent, Object object, Object fieldName)
	{
		if (isEnabled(SerializerFeature.DisableCircularReferenceDetect))
			return;
		context = new SerialContext(parent, object, fieldName);
		if (references == null)
			references = new IdentityHashMap();
		references.put(object, context);
	}

	public void setContext(Object object, Object fieldName)
	{
		setContext(context, object, fieldName);
	}

	public void popContext()
	{
		if (context != null)
			context = context.getParent();
	}

	public void setContext(SerialContext parent, Object object)
	{
		if (isEnabled(SerializerFeature.DisableCircularReferenceDetect))
			return;
		context = new SerialContext(parent, object, null);
		if (references == null)
			references = new IdentityHashMap();
		references.put(object, context);
	}

	public boolean isWriteClassName()
	{
		return isEnabled(SerializerFeature.WriteClassName);
	}

	public final boolean isWriteClassName(Type fieldType, Object obj)
	{
		boolean result = out.isEnabled(SerializerFeature.WriteClassName);
		if (!result)
			return false;
		if (fieldType == null && isEnabled(SerializerFeature.NotWriteRootClassName))
		{
			boolean isRoot = context.getParent() == null;
			if (isRoot)
				return false;
		}
		return true;
	}

	public Collection getReferences()
	{
		if (references == null)
			references = new IdentityHashMap();
		return references.values();
	}

	public SerialContext getSerialContext(Object object)
	{
		if (references == null)
			return null;
		else
			return (SerialContext)references.get(object);
	}

	public boolean containsReference(Object value)
	{
		if (isEnabled(SerializerFeature.DisableCircularReferenceDetect))
			return false;
		if (references == null)
			return false;
		else
			return references.containsKey(value);
	}

	public void writeReference(Object object)
	{
		if (isEnabled(SerializerFeature.DisableCircularReferenceDetect))
			return;
		SerialContext context = getContext();
		Object current = context.getObject();
		if (object == current)
		{
			out.write("{\"$ref\":\"@\"}");
			return;
		}
		SerialContext parentContext = context.getParent();
		if (parentContext != null && object == parentContext.getObject())
		{
			out.write("{\"$ref\":\"..\"}");
			return;
		}
		SerialContext rootContext;
		for (rootContext = context; rootContext.getParent() != null; rootContext = rootContext.getParent());
		if (object == rootContext.getObject())
		{
			out.write("{\"$ref\":\"$\"}");
			return;
		} else
		{
			SerialContext refContext = getSerialContext(object);
			String path = refContext.getPath();
			out.write("{\"$ref\":\"");
			out.write(path);
			out.write("\"}");
			return;
		}
	}

	public List getValueFilters()
	{
		if (valueFilters == null)
			valueFilters = new ArrayList();
		return valueFilters;
	}

	public List getValueFiltersDirect()
	{
		return valueFilters;
	}

	public int getIndentCount()
	{
		return indentCount;
	}

	public void incrementIndent()
	{
		indentCount++;
	}

	public void decrementIdent()
	{
		indentCount--;
	}

	public void println()
	{
		out.write('\n');
		for (int i = 0; i < indentCount; i++)
			out.write(indent);

	}

	public List getNameFilters()
	{
		if (nameFilters == null)
			nameFilters = new ArrayList();
		return nameFilters;
	}

	public List getNameFiltersDirect()
	{
		return nameFilters;
	}

	public List getPropertyFilters()
	{
		if (propertyFilters == null)
			propertyFilters = new ArrayList();
		return propertyFilters;
	}

	public List getPropertyFiltersDirect()
	{
		return propertyFilters;
	}

	public SerializeWriter getWriter()
	{
		return out;
	}

	public String toString()
	{
		return out.toString();
	}

	public void config(SerializerFeature feature, boolean state)
	{
		out.config(feature, state);
	}

	public boolean isEnabled(SerializerFeature feature)
	{
		return out.isEnabled(feature);
	}

	public void writeNull()
	{
		out.writeNull();
	}

	public SerializeConfig getMapping()
	{
		return config;
	}

	public static final void write(Writer out, Object object)
	{
		Exception exception;
		SerializeWriter writer = new SerializeWriter();
		try
		{
			JSONSerializer serializer = new JSONSerializer(writer);
			serializer.write(object);
			writer.writeTo(out);
		}
		catch (IOException ex)
		{
			throw new JSONException(ex.getMessage(), ex);
		}
		finally
		{
			writer.close();
		}
		writer.close();
		break MISSING_BLOCK_LABEL_57;
		throw exception;
	}

	public static final void write(SerializeWriter out, Object object)
	{
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.write(object);
	}

	public final void write(Object object)
	{
		if (object == null)
		{
			out.writeNull();
			return;
		}
		Class clazz = object.getClass();
		ObjectSerializer writer = getObjectWriter(clazz);
		try
		{
			writer.write(this, object, null, null);
		}
		catch (IOException e)
		{
			throw new JSONException(e.getMessage(), e);
		}
	}

	public final void writeWithFieldName(Object object, Object fieldName)
	{
		writeWithFieldName(object, fieldName, null);
	}

	public final void writeWithFieldName(Object object, Object fieldName, Type fieldType)
	{
		Class clazz;
		ObjectSerializer writer;
		try
		{
			if (object == null)
			{
				out.writeNull();
				return;
			}
		}
		catch (IOException e)
		{
			throw new JSONException(e.getMessage(), e);
		}
		clazz = object.getClass();
		writer = getObjectWriter(clazz);
		writer.write(this, object, fieldName, fieldType);
	}

	public final void writeWithFormat(Object object, String format)
	{
		if (object instanceof Date)
		{
			String text = (new SimpleDateFormat(format)).format((Date)object);
			out.writeString(text);
			return;
		} else
		{
			write(object);
			return;
		}
	}

	public final void write(String text)
	{
		StringSerializer.instance.write(this, text);
	}

	public ObjectSerializer getObjectWriter(Class clazz)
	{
		ObjectSerializer writer = (ObjectSerializer)config.get(clazz);
		if (writer == null)
		{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			for (Iterator i$ = ServiceLoader.load(com/alibaba/fastjson/serializer/AutowiredObjectSerializer, classLoader).iterator(); i$.hasNext();)
			{
				AutowiredObjectSerializer autowired = (AutowiredObjectSerializer)i$.next();
				Iterator i$ = autowired.getAutowiredFor().iterator();
				while (i$.hasNext()) 
				{
					Type forType = (Type)i$.next();
					config.put(forType, autowired);
				}
			}

			writer = (ObjectSerializer)config.get(clazz);
		}
		if (writer == null)
		{
			if (java/util/Map.isAssignableFrom(clazz))
				config.put(clazz, MapSerializer.instance);
			else
			if (java/util/List.isAssignableFrom(clazz))
				config.put(clazz, ListSerializer.instance);
			else
			if (java/util/Collection.isAssignableFrom(clazz))
				config.put(clazz, CollectionSerializer.instance);
			else
			if (java/util/Date.isAssignableFrom(clazz))
				config.put(clazz, DateSerializer.instance);
			else
			if (com/alibaba/fastjson/JSONAware.isAssignableFrom(clazz))
				config.put(clazz, JSONAwareSerializer.instance);
			else
			if (com/alibaba/fastjson/JSONStreamAware.isAssignableFrom(clazz))
				config.put(clazz, JSONStreamAwareSerializer.instance);
			else
			if (clazz.isEnum() || clazz.getSuperclass() != null && clazz.getSuperclass().isEnum())
				config.put(clazz, EnumSerializer.instance);
			else
			if (clazz.isArray())
			{
				Class componentType = clazz.getComponentType();
				ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
				config.put(clazz, new ArraySerializer(compObjectSerializer));
			} else
			if (java/lang/Throwable.isAssignableFrom(clazz))
				config.put(clazz, new ExceptionSerializer(clazz));
			else
			if (java/util/TimeZone.isAssignableFrom(clazz))
				config.put(clazz, TimeZoneSerializer.instance);
			else
			if (java/lang/Appendable.isAssignableFrom(clazz))
				config.put(clazz, AppendableSerializer.instance);
			else
			if (java/nio/charset/Charset.isAssignableFrom(clazz))
				config.put(clazz, CharsetSerializer.instance);
			else
			if (java/util/Enumeration.isAssignableFrom(clazz))
				config.put(clazz, EnumerationSeriliazer.instance);
			else
			if (Proxy.isProxyClass(clazz))
				config.put(clazz, config.createJavaBeanSerializer(clazz));
			else
				config.put(clazz, config.createJavaBeanSerializer(clazz));
			writer = (ObjectSerializer)config.get(clazz);
		}
		return writer;
	}
}
