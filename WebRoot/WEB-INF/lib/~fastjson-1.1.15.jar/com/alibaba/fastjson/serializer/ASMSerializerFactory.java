// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ASMSerializerFactory.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.asm.*;
import com.alibaba.fastjson.util.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			JavaBeanSerializer, JSONSerializer, SerializeWriter, SerializerFeature, 
//			ObjectSerializer, FilterUtils

public class ASMSerializerFactory
	implements Opcodes
{
	static class Context
	{

		private final String className;
		private int variantIndex;
		private Map variants;

		public int serializer()
		{
			return 1;
		}

		public String getClassName()
		{
			return className;
		}

		public int obj()
		{
			return 2;
		}

		public int paramFieldName()
		{
			return 3;
		}

		public int paramFieldType()
		{
			return 4;
		}

		public int fieldName()
		{
			return 5;
		}

		public int original()
		{
			return 6;
		}

		public int processValue()
		{
			return 7;
		}

		public int getVariantCount()
		{
			return variantIndex;
		}

		public int var(String name)
		{
			Integer i = (Integer)variants.get(name);
			if (i == null)
				variants.put(name, Integer.valueOf(variantIndex++));
			i = (Integer)variants.get(name);
			return i.intValue();
		}

		public int var(String name, int increment)
		{
			Integer i = (Integer)variants.get(name);
			if (i == null)
			{
				variants.put(name, Integer.valueOf(variantIndex));
				variantIndex += increment;
			}
			i = (Integer)variants.get(name);
			return i.intValue();
		}

		public Context(String className)
		{
			variantIndex = 8;
			variants = new HashMap();
			this.className = className;
		}
	}


	private ASMClassLoader classLoader;
	private final AtomicLong seed = new AtomicLong();

	public ASMSerializerFactory()
	{
		classLoader = new ASMClassLoader();
	}

	public ObjectSerializer createJavaBeanSerializer(Class clazz)
		throws Exception
	{
		return createJavaBeanSerializer(clazz, (Map)null);
	}

	public String getGenClassName(Class clazz)
	{
		return (new StringBuilder()).append("Serializer_").append(seed.incrementAndGet()).toString();
	}

	public ObjectSerializer createJavaBeanSerializer(Class clazz, Map aliasMap)
		throws Exception
	{
		if (clazz.isPrimitive())
		{
			throw new JSONException((new StringBuilder()).append("unsupportd class ").append(clazz.getName()).toString());
		} else
		{
			String className = getGenClassName(clazz);
			ClassWriter cw = new ClassWriter();
			cw.visit(49, 33, className, "java/lang/Object", new String[] {
				"com/alibaba/fastjson/serializer/ObjectSerializer"
			});
			FieldVisitor fw = cw.visitField(2, "nature", ASMUtils.getDesc(com/alibaba/fastjson/serializer/JavaBeanSerializer));
			fw.visitEnd();
			MethodVisitor mw = cw.visitMethod(1, "<init>", "()V", null, null);
			mw.visitVarInsn(25, 0);
			mw.visitMethodInsn(183, "java/lang/Object", "<init>", "()V");
			mw.visitInsn(177);
			mw.visitMaxs(1, 1);
			mw.visitEnd();
			Context context = new Context(className);
			List getters = TypeUtils.computeGetters(clazz, aliasMap);
			mw = cw.visitMethod(1, "write", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V", null, new String[] {
				"java/io/IOException"
			});
			mw.visitVarInsn(25, context.serializer());
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "getWriter", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/serializer/SerializeWriter)).toString());
			mw.visitVarInsn(58, context.var("out"));
			Label _else = new Label();
			mw.visitVarInsn(25, context.var("out"));
			mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature), "SortField", (new StringBuilder()).append("L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").toString());
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "isEnabled", (new StringBuilder()).append("(L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").append(")Z").toString());
			mw.visitJumpInsn(153, _else);
			mw.visitVarInsn(25, 0);
			mw.visitVarInsn(25, 1);
			mw.visitVarInsn(25, 2);
			mw.visitVarInsn(25, 3);
			mw.visitVarInsn(25, context.paramFieldType());
			mw.visitMethodInsn(182, className, "write1", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
			mw.visitInsn(177);
			mw.visitLabel(_else);
			mw.visitVarInsn(25, context.obj());
			mw.visitTypeInsn(192, ASMUtils.getType(clazz));
			mw.visitVarInsn(58, context.var("entity"));
			generateWriteMethod(clazz, mw, getters, context);
			mw.visitInsn(177);
			mw.visitMaxs(5, context.getVariantCount() + 1);
			mw.visitEnd();
			context = new Context(className);
			getters = TypeUtils.computeGetters(clazz, aliasMap);
			Collections.sort(getters);
			mw = cw.visitMethod(1, "write1", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V", null, new String[] {
				"java/io/IOException"
			});
			mw.visitVarInsn(25, context.serializer());
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "getWriter", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/serializer/SerializeWriter)).toString());
			mw.visitVarInsn(58, context.var("out"));
			mw.visitVarInsn(25, context.obj());
			mw.visitTypeInsn(192, ASMUtils.getType(clazz));
			mw.visitVarInsn(58, context.var("entity"));
			generateWriteMethod(clazz, mw, getters, context);
			mw.visitInsn(177);
			mw.visitMaxs(5, context.getVariantCount() + 1);
			mw.visitEnd();
			byte code[] = cw.toByteArray();
			Class exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);
			Object instance = exampleClass.newInstance();
			return (ObjectSerializer)instance;
		}
	}

	private void generateWriteMethod(Class clazz, MethodVisitor mw, List getters, Context context)
		throws Exception
	{
		Label end = new Label();
		int size = getters.size();
		Label endFormat_ = new Label();
		Label notNull_ = new Label();
		mw.visitVarInsn(25, context.var("out"));
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature), "PrettyFormat", (new StringBuilder()).append("L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").toString());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "isEnabled", (new StringBuilder()).append("(L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").append(")Z").toString());
		mw.visitJumpInsn(153, endFormat_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), "nature", ASMUtils.getDesc(com/alibaba/fastjson/serializer/JavaBeanSerializer));
		mw.visitJumpInsn(199, notNull_);
		initNature(clazz, mw, context);
		mw.visitLabel(notNull_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), "nature", ASMUtils.getDesc(com/alibaba/fastjson/serializer/JavaBeanSerializer));
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, 2);
		mw.visitVarInsn(25, 3);
		mw.visitVarInsn(25, 4);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JavaBeanSerializer), "write", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
		mw.visitInsn(177);
		mw.visitLabel(endFormat_);
		Label endRef_ = new Label();
		notNull_ = new Label();
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.obj());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "containsReference", "(Ljava/lang/Object;)Z");
		mw.visitJumpInsn(153, endRef_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), "nature", ASMUtils.getDesc(com/alibaba/fastjson/serializer/JavaBeanSerializer));
		mw.visitJumpInsn(199, notNull_);
		initNature(clazz, mw, context);
		mw.visitLabel(notNull_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), "nature", ASMUtils.getDesc(com/alibaba/fastjson/serializer/JavaBeanSerializer));
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, 2);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JavaBeanSerializer), "writeReference", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;)V");
		mw.visitInsn(177);
		mw.visitLabel(endRef_);
		mw.visitVarInsn(25, context.serializer());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "getContext", "()Lcom/alibaba/fastjson/serializer/SerialContext;");
		mw.visitVarInsn(58, context.var("parent"));
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.var("parent"));
		mw.visitVarInsn(25, context.obj());
		mw.visitVarInsn(25, context.paramFieldName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "setContext", "(Lcom/alibaba/fastjson/serializer/SerialContext;Ljava/lang/Object;Ljava/lang/Object;)V");
		Label end_ = new Label();
		Label else_ = new Label();
		Label writeClass_ = new Label();
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.paramFieldType());
		mw.visitVarInsn(25, context.obj());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "isWriteClassName", "(Ljava/lang/reflect/Type;Ljava/lang/Object;)Z");
		mw.visitJumpInsn(153, else_);
		mw.visitVarInsn(25, context.paramFieldType());
		mw.visitVarInsn(25, context.obj());
		mw.visitMethodInsn(182, ASMUtils.getType(java/lang/Object), "getClass", "()Ljava/lang/Class;");
		mw.visitJumpInsn(165, else_);
		mw.visitLabel(writeClass_);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitLdcInsn((new StringBuilder()).append("{\"@type\":\"").append(clazz.getName()).append("\"").toString());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(Ljava/lang/String;)V");
		mw.visitVarInsn(16, 44);
		mw.visitJumpInsn(167, end_);
		mw.visitLabel(else_);
		mw.visitVarInsn(16, 123);
		mw.visitLabel(end_);
		mw.visitVarInsn(54, context.var("seperator"));
		for (int i = 0; i < size; i++)
		{
			FieldInfo property = (FieldInfo)getters.get(i);
			Class propertyClass = property.getFieldClass();
			mw.visitLdcInsn(property.getName());
			mw.visitVarInsn(58, context.fieldName());
			if (propertyClass == Byte.TYPE)
			{
				_byte(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Short.TYPE)
			{
				_short(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Integer.TYPE)
			{
				_int(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Long.TYPE)
			{
				_long(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Float.TYPE)
			{
				_float(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Double.TYPE)
			{
				_double(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Boolean.TYPE)
			{
				_boolean(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == Character.TYPE)
			{
				_char(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == java/lang/String)
			{
				_string(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == java/math/BigDecimal)
			{
				_decimal(clazz, mw, property, context);
				continue;
			}
			if (propertyClass == java/util/List || propertyClass == java/util/ArrayList)
			{
				_list(clazz, mw, property, context);
				continue;
			}
			if (propertyClass.isEnum())
				_enum(clazz, mw, property, context);
			else
				_object(clazz, mw, property, context);
		}

		Label _if = new Label();
		Label _else = new Label();
		Label _end_if = new Label();
		mw.visitLabel(_if);
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitIntInsn(16, 123);
		mw.visitJumpInsn(160, _else);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitLdcInsn("{}");
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(Ljava/lang/String;)V");
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_else);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(16, 125);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(C)V");
		mw.visitLabel(_end_if);
		mw.visitLabel(end);
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.var("parent"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "setContext", "(Lcom/alibaba/fastjson/serializer/SerialContext;)V");
	}

	private void initNature(Class clazz, MethodVisitor mw, Context context)
	{
		mw.visitVarInsn(25, 0);
		mw.visitTypeInsn(187, ASMUtils.getType(com/alibaba/fastjson/serializer/JavaBeanSerializer));
		mw.visitInsn(89);
		mw.visitLdcInsn(Type.getType(ASMUtils.getDesc(clazz)));
		mw.visitMethodInsn(183, ASMUtils.getType(com/alibaba/fastjson/serializer/JavaBeanSerializer), "<init>", (new StringBuilder()).append("(").append(ASMUtils.getDesc(java/lang/Class)).append(")V").toString());
		mw.visitFieldInsn(181, context.getClassName(), "nature", ASMUtils.getDesc(com/alibaba/fastjson/serializer/JavaBeanSerializer));
	}

	private void _object(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(58, context.var("object"));
		_filters(mw, property, context, _end);
		_writeObject(mw, property, context, _end);
		mw.visitLabel(_end);
	}

	private void _enum(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		boolean writeEnumUsingToString = false;
		JSONField annotation = (JSONField)property.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
		if (annotation != null)
		{
			SerializerFeature arr$[] = annotation.serialzeFeatures();
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				SerializerFeature feature = arr$[i$];
				if (feature == SerializerFeature.WriteEnumUsingToString)
					writeEnumUsingToString = true;
			}

		}
		Label _not_null = new Label();
		Label _end_if = new Label();
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitTypeInsn(192, ASMUtils.getType(java/lang/Enum));
		mw.visitVarInsn(58, context.var("enum"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("enum"));
		mw.visitJumpInsn(199, _not_null);
		_if_write_null(mw, property, context);
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_not_null);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(25, context.var("enum"));
		if (writeEnumUsingToString)
		{
			mw.visitMethodInsn(182, ASMUtils.getType(java/lang/Object), "toString", "()Ljava/lang/String;");
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;Ljava/lang/String;)V");
		} else
		{
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", (new StringBuilder()).append("(CLjava/lang/String;L").append(ASMUtils.getType(java/lang/Enum)).append(";)V").toString());
		}
		_seperator(mw, context);
		mw.visitLabel(_end_if);
		mw.visitLabel(_end);
	}

	private void _long(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(55, context.var("long", 2));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(22, context.var("long"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;J)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _float(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(56, context.var("float"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(23, context.var("float"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;F)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _double(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(57, context.var("double"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(24, context.var("double"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;D)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _char(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(54, context.var("char"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(21, context.var("char"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;C)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _boolean(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(54, context.var("boolean"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(21, context.var("boolean"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;Z)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _get(MethodVisitor mw, Context context, FieldInfo property)
	{
		Method method = property.getMethod();
		if (method != null)
		{
			mw.visitVarInsn(25, context.var("entity"));
			mw.visitMethodInsn(182, ASMUtils.getType(method.getDeclaringClass()), method.getName(), ASMUtils.getDesc(method));
		} else
		{
			mw.visitVarInsn(25, context.var("entity"));
			mw.visitFieldInsn(180, ASMUtils.getType(property.getDeclaringClass()), property.getName(), ASMUtils.getDesc(property.getFieldClass()));
		}
	}

	private void _byte(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(54, context.var("byte"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(21, context.var("byte"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;I)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _short(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(54, context.var("short"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(21, context.var("short"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;I)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _int(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(54, context.var("int"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(21, context.var("int"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;I)V");
		_seperator(mw, context);
		mw.visitLabel(_end);
	}

	private void _decimal(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(58, context.var("decimal"));
		_filters(mw, property, context, _end);
		Label _if = new Label();
		Label _else = new Label();
		Label _end_if = new Label();
		mw.visitLabel(_if);
		mw.visitVarInsn(25, context.var("decimal"));
		mw.visitJumpInsn(199, _else);
		_if_write_null(mw, property, context);
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_else);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(25, context.var("decimal"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;Ljava/math/BigDecimal;)V");
		_seperator(mw, context);
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_end_if);
		mw.visitLabel(_end);
	}

	private void _string(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		Label _end = new Label();
		_get(mw, context, property);
		mw.visitVarInsn(58, context.var("string"));
		_filters(mw, property, context, _end);
		Label _else = new Label();
		Label _end_if = new Label();
		mw.visitVarInsn(25, context.var("string"));
		mw.visitJumpInsn(199, _else);
		_if_write_null(mw, property, context);
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_else);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitVarInsn(25, context.var("string"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;Ljava/lang/String;)V");
		_seperator(mw, context);
		mw.visitLabel(_end_if);
		mw.visitLabel(_end);
	}

	private void _list(Class clazz, MethodVisitor mw, FieldInfo property, Context context)
	{
		java.lang.reflect.Type propertyType = property.getFieldType();
		java.lang.reflect.Type elementType;
		if (propertyType instanceof Class)
			elementType = java/lang/Object;
		else
			elementType = ((ParameterizedType)propertyType).getActualTypeArguments()[0];
		Class elementClass = null;
		if (elementType instanceof Class)
			elementClass = (Class)elementType;
		Label _end = new Label();
		Label _if = new Label();
		Label _else = new Label();
		Label _end_if = new Label();
		mw.visitLabel(_if);
		_get(mw, context, property);
		mw.visitTypeInsn(192, ASMUtils.getType(java/util/List));
		mw.visitVarInsn(58, context.var("list"));
		_filters(mw, property, context, _end);
		mw.visitVarInsn(25, context.var("list"));
		mw.visitJumpInsn(199, _else);
		_if_write_null(mw, property, context);
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_else);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(C)V");
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldName", "(Ljava/lang/String;)V");
		mw.visitVarInsn(25, context.var("list"));
		mw.visitMethodInsn(185, ASMUtils.getType(java/util/List), "size", "()I");
		mw.visitVarInsn(54, context.var("int"));
		Label _if_3 = new Label();
		Label _else_3 = new Label();
		Label _end_if_3 = new Label();
		mw.visitLabel(_if_3);
		mw.visitVarInsn(21, context.var("int"));
		mw.visitInsn(3);
		mw.visitJumpInsn(160, _else_3);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitLdcInsn("[]");
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(Ljava/lang/String;)V");
		mw.visitJumpInsn(167, _end_if_3);
		mw.visitLabel(_else_3);
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.var("list"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)V");
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(16, 91);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(C)V");
		mw.visitInsn(1);
		mw.visitTypeInsn(192, ASMUtils.getType(com/alibaba/fastjson/serializer/ObjectSerializer));
		mw.visitVarInsn(58, context.var("list_ser"));
		Label _for = new Label();
		Label _end_for = new Label();
		mw.visitInsn(3);
		mw.visitVarInsn(54, context.var("i"));
		mw.visitLabel(_for);
		mw.visitVarInsn(21, context.var("i"));
		mw.visitVarInsn(21, context.var("int"));
		mw.visitInsn(4);
		mw.visitInsn(100);
		mw.visitJumpInsn(162, _end_for);
		if (elementType == java/lang/String)
		{
			mw.visitVarInsn(25, context.var("out"));
			mw.visitVarInsn(25, context.var("list"));
			mw.visitVarInsn(21, context.var("i"));
			mw.visitMethodInsn(185, ASMUtils.getType(java/util/List), "get", "(I)Ljava/lang/Object;");
			mw.visitTypeInsn(192, ASMUtils.getType(java/lang/String));
			mw.visitVarInsn(16, 44);
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeString", "(Ljava/lang/String;C)V");
		} else
		{
			mw.visitVarInsn(25, context.serializer());
			mw.visitVarInsn(25, context.var("list"));
			mw.visitVarInsn(21, context.var("i"));
			mw.visitMethodInsn(185, ASMUtils.getType(java/util/List), "get", "(I)Ljava/lang/Object;");
			mw.visitVarInsn(21, context.var("i"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Integer), "valueOf", "(I)Ljava/lang/Integer;");
			if (elementClass != null && Modifier.isPublic(elementClass.getModifiers()))
			{
				mw.visitLdcInsn(Type.getType(ASMUtils.getDesc((Class)elementType)));
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
			} else
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
			}
			mw.visitVarInsn(25, context.var("out"));
			mw.visitVarInsn(16, 44);
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(C)V");
		}
		mw.visitIincInsn(context.var("i"), 1);
		mw.visitJumpInsn(167, _for);
		mw.visitLabel(_end_for);
		if (elementType == java/lang/String)
		{
			mw.visitVarInsn(25, context.var("out"));
			mw.visitVarInsn(25, context.var("list"));
			mw.visitVarInsn(21, context.var("int"));
			mw.visitInsn(4);
			mw.visitInsn(100);
			mw.visitMethodInsn(185, ASMUtils.getType(java/util/List), "get", "(I)Ljava/lang/Object;");
			mw.visitTypeInsn(192, ASMUtils.getType(java/lang/String));
			mw.visitVarInsn(16, 93);
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeString", "(Ljava/lang/String;C)V");
		} else
		{
			mw.visitVarInsn(25, context.serializer());
			mw.visitVarInsn(25, context.var("list"));
			mw.visitVarInsn(21, context.var("i"));
			mw.visitMethodInsn(185, ASMUtils.getType(java/util/List), "get", "(I)Ljava/lang/Object;");
			mw.visitVarInsn(21, context.var("i"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Integer), "valueOf", "(I)Ljava/lang/Integer;");
			if (elementClass != null && Modifier.isPublic(elementClass.getModifiers()))
			{
				mw.visitLdcInsn(Type.getType(ASMUtils.getDesc((Class)elementType)));
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
			} else
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
			}
			mw.visitVarInsn(25, context.var("out"));
			mw.visitVarInsn(16, 93);
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(C)V");
		}
		mw.visitVarInsn(25, context.serializer());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "popContext", "()V");
		mw.visitLabel(_end_if_3);
		_seperator(mw, context);
		mw.visitLabel(_end_if);
		mw.visitLabel(_end);
	}

	private void _filters(MethodVisitor mw, FieldInfo property, Context context, Label _end)
	{
		if (property.getField() != null && Modifier.isTransient(property.getField().getModifiers()))
		{
			mw.visitVarInsn(25, context.var("out"));
			mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature), "SkipTransientField", (new StringBuilder()).append("L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").toString());
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "isEnabled", (new StringBuilder()).append("(L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").append(")Z").toString());
			mw.visitJumpInsn(154, _end);
		}
		_apply(mw, property, context);
		mw.visitJumpInsn(153, _end);
		_processKey(mw, property, context);
		Label _else_processKey = new Label();
		_processValue(mw, property, context);
		mw.visitVarInsn(25, context.original());
		mw.visitVarInsn(25, context.processValue());
		mw.visitJumpInsn(165, _else_processKey);
		_writeObject(mw, property, context, _end);
		mw.visitJumpInsn(167, _end);
		mw.visitLabel(_else_processKey);
	}

	private void _writeObject(MethodVisitor mw, FieldInfo fieldInfo, Context context, Label _end)
	{
		String format = null;
		JSONField annotation = (JSONField)fieldInfo.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
		if (annotation != null)
		{
			format = annotation.format();
			if (format.trim().length() == 0)
				format = null;
		}
		Label _not_null = new Label();
		mw.visitVarInsn(25, context.processValue());
		mw.visitJumpInsn(199, _not_null);
		_if_write_null(mw, fieldInfo, context);
		mw.visitJumpInsn(167, _end);
		mw.visitLabel(_not_null);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "write", "(C)V");
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(25, context.fieldName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldName", "(Ljava/lang/String;)V");
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.processValue());
		if (format != null)
		{
			mw.visitLdcInsn(format);
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFormat", "(Ljava/lang/Object;Ljava/lang/String;)V");
		} else
		{
			mw.visitVarInsn(25, context.fieldName());
			if ((fieldInfo.getFieldType() instanceof Class) && ((Class)fieldInfo.getFieldType()).isPrimitive())
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;)V");
			} else
			{
				mw.visitLdcInsn(Type.getType(ASMUtils.getDesc(fieldInfo.getDeclaringClass())));
				if (fieldInfo.getMethod() != null)
				{
					mw.visitLdcInsn(fieldInfo.getMethod().getName());
					mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/util/ASMUtils), "getMethodType", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");
				} else
				{
					mw.visitLdcInsn(fieldInfo.getField().getName());
					mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/util/ASMUtils), "getFieldType", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/reflect/Type;");
				}
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/JSONSerializer), "writeWithFieldName", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/reflect/Type;)V");
			}
		}
		_seperator(mw, context);
	}

	private void _apply(MethodVisitor mw, FieldInfo property, Context context)
	{
		Class propertyClass = property.getFieldClass();
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.obj());
		mw.visitVarInsn(25, context.fieldName());
		if (propertyClass == Byte.TYPE)
		{
			mw.visitVarInsn(21, context.var("byte"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Z");
		} else
		if (propertyClass == Short.TYPE)
		{
			mw.visitVarInsn(21, context.var("short"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;S)Z");
		} else
		if (propertyClass == Integer.TYPE)
		{
			mw.visitVarInsn(21, context.var("int"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;I)Z");
		} else
		if (propertyClass == Character.TYPE)
		{
			mw.visitVarInsn(21, context.var("char"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;C)Z");
		} else
		if (propertyClass == Long.TYPE)
		{
			mw.visitVarInsn(22, context.var("long"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;J)Z");
		} else
		if (propertyClass == Float.TYPE)
		{
			mw.visitVarInsn(23, context.var("float"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;F)Z");
		} else
		if (propertyClass == Double.TYPE)
		{
			mw.visitVarInsn(24, context.var("double"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;D)Z");
		} else
		if (propertyClass == Boolean.TYPE)
		{
			mw.visitVarInsn(21, context.var("boolean"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Z");
		} else
		if (propertyClass == java/math/BigDecimal)
		{
			mw.visitVarInsn(25, context.var("decimal"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
		} else
		if (propertyClass == java/lang/String)
		{
			mw.visitVarInsn(25, context.var("string"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
		} else
		if (propertyClass.isEnum())
		{
			mw.visitVarInsn(25, context.var("enum"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
		} else
		if (propertyClass == java/util/List)
		{
			mw.visitVarInsn(25, context.var("list"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
		} else
		{
			mw.visitVarInsn(25, context.var("object"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "apply", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z");
		}
	}

	private void _processValue(MethodVisitor mw, FieldInfo property, Context context)
	{
		Class propertyClass = property.getFieldClass();
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.obj());
		mw.visitVarInsn(25, context.fieldName());
		if (propertyClass == Byte.TYPE)
		{
			mw.visitVarInsn(21, context.var("byte"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Byte), "valueOf", "(B)Ljava/lang/Byte;");
		} else
		if (propertyClass == Short.TYPE)
		{
			mw.visitVarInsn(21, context.var("short"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Short), "valueOf", "(S)Ljava/lang/Short;");
		} else
		if (propertyClass == Integer.TYPE)
		{
			mw.visitVarInsn(21, context.var("int"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Integer), "valueOf", "(I)Ljava/lang/Integer;");
		} else
		if (propertyClass == Character.TYPE)
		{
			mw.visitVarInsn(21, context.var("char"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Character), "valueOf", "(C)Ljava/lang/Character;");
		} else
		if (propertyClass == Long.TYPE)
		{
			mw.visitVarInsn(22, context.var("long"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Long), "valueOf", "(J)Ljava/lang/Long;");
		} else
		if (propertyClass == Float.TYPE)
		{
			mw.visitVarInsn(23, context.var("float"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Float), "valueOf", "(F)Ljava/lang/Float;");
		} else
		if (propertyClass == Double.TYPE)
		{
			mw.visitVarInsn(24, context.var("double"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Double), "valueOf", "(D)Ljava/lang/Double;");
		} else
		if (propertyClass == Boolean.TYPE)
		{
			mw.visitVarInsn(21, context.var("boolean"));
			mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Boolean), "valueOf", "(Z)Ljava/lang/Boolean;");
		} else
		if (propertyClass == java/math/BigDecimal)
			mw.visitVarInsn(25, context.var("decimal"));
		else
		if (propertyClass == java/lang/String)
			mw.visitVarInsn(25, context.var("string"));
		else
		if (propertyClass.isEnum())
			mw.visitVarInsn(25, context.var("enum"));
		else
		if (propertyClass == java/util/List)
			mw.visitVarInsn(25, context.var("list"));
		else
			mw.visitVarInsn(25, context.var("object"));
		mw.visitVarInsn(58, context.original());
		mw.visitVarInsn(25, context.original());
		mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processValue", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;");
		mw.visitVarInsn(58, context.processValue());
	}

	private void _processKey(MethodVisitor mw, FieldInfo property, Context context)
	{
		Class propertyClass = property.getFieldClass();
		mw.visitVarInsn(25, context.serializer());
		mw.visitVarInsn(25, context.obj());
		mw.visitVarInsn(25, context.fieldName());
		if (propertyClass == Byte.TYPE)
		{
			mw.visitVarInsn(21, context.var("byte"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;B)Ljava/lang/String;");
		} else
		if (propertyClass == Short.TYPE)
		{
			mw.visitVarInsn(21, context.var("short"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;S)Ljava/lang/String;");
		} else
		if (propertyClass == Integer.TYPE)
		{
			mw.visitVarInsn(21, context.var("int"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;I)Ljava/lang/String;");
		} else
		if (propertyClass == Character.TYPE)
		{
			mw.visitVarInsn(21, context.var("char"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;C)Ljava/lang/String;");
		} else
		if (propertyClass == Long.TYPE)
		{
			mw.visitVarInsn(22, context.var("long"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;J)Ljava/lang/String;");
		} else
		if (propertyClass == Float.TYPE)
		{
			mw.visitVarInsn(23, context.var("float"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;F)Ljava/lang/String;");
		} else
		if (propertyClass == Double.TYPE)
		{
			mw.visitVarInsn(24, context.var("double"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;D)Ljava/lang/String;");
		} else
		if (propertyClass == Boolean.TYPE)
		{
			mw.visitVarInsn(21, context.var("boolean"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Z)Ljava/lang/String;");
		} else
		if (propertyClass == java/math/BigDecimal)
		{
			mw.visitVarInsn(25, context.var("decimal"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
		} else
		if (propertyClass == java/lang/String)
		{
			mw.visitVarInsn(25, context.var("string"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
		} else
		if (propertyClass.isEnum())
		{
			mw.visitVarInsn(25, context.var("enum"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
		} else
		if (propertyClass == java/util/List)
		{
			mw.visitVarInsn(25, context.var("list"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
		} else
		{
			mw.visitVarInsn(25, context.var("object"));
			mw.visitMethodInsn(184, ASMUtils.getType(com/alibaba/fastjson/serializer/FilterUtils), "processKey", "(Lcom/alibaba/fastjson/serializer/JSONSerializer;Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;");
		}
		mw.visitVarInsn(58, context.fieldName());
	}

	private void _if_write_null(MethodVisitor mw, FieldInfo fieldInfo, Context context)
	{
		Class propertyClass = fieldInfo.getFieldClass();
		Label _if = new Label();
		Label _else = new Label();
		Label _write_null = new Label();
		Label _end_if = new Label();
		mw.visitLabel(_if);
		boolean writeNull = false;
		boolean writeNullNumberAsZero = false;
		boolean writeNullStringAsEmpty = false;
		boolean writeNullBooleanAsFalse = false;
		boolean writeNullListAsEmpty = false;
		JSONField annotation = (JSONField)fieldInfo.getAnnotation(com/alibaba/fastjson/annotation/JSONField);
		if (annotation != null)
		{
			SerializerFeature arr$[] = annotation.serialzeFeatures();
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				SerializerFeature feature = arr$[i$];
				if (feature == SerializerFeature.WriteMapNullValue)
				{
					writeNull = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullNumberAsZero)
				{
					writeNullNumberAsZero = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullStringAsEmpty)
				{
					writeNullStringAsEmpty = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullBooleanAsFalse)
				{
					writeNullBooleanAsFalse = true;
					continue;
				}
				if (feature == SerializerFeature.WriteNullListAsEmpty)
					writeNullListAsEmpty = true;
			}

		}
		if (!writeNull)
		{
			mw.visitVarInsn(25, context.var("out"));
			mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature), "WriteMapNullValue", (new StringBuilder()).append("L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").toString());
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "isEnabled", (new StringBuilder()).append("(L").append(ASMUtils.getType(com/alibaba/fastjson/serializer/SerializerFeature)).append(";").append(")Z").toString());
			mw.visitJumpInsn(153, _else);
		}
		mw.visitLabel(_write_null);
		mw.visitVarInsn(25, context.var("out"));
		mw.visitVarInsn(21, context.var("seperator"));
		mw.visitVarInsn(25, context.fieldName());
		if (propertyClass == java/lang/String || propertyClass == java/lang/Character)
		{
			if (writeNullStringAsEmpty)
			{
				mw.visitLdcInsn("");
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;Ljava/lang/String;)V");
			} else
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldNullString", "(CLjava/lang/String;)V");
			}
		} else
		if (java/lang/Number.isAssignableFrom(propertyClass))
		{
			if (writeNullNumberAsZero)
			{
				mw.visitInsn(3);
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;I)V");
			} else
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldNullNumber", "(CLjava/lang/String;)V");
			}
		} else
		if (propertyClass == java/lang/Boolean)
		{
			if (writeNullBooleanAsFalse)
			{
				mw.visitInsn(3);
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldValue", "(CLjava/lang/String;Z)V");
			} else
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldNullBoolean", "(CLjava/lang/String;)V");
			}
		} else
		if (java/util/Collection.isAssignableFrom(propertyClass) || propertyClass.isArray())
		{
			if (writeNullListAsEmpty)
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldEmptyList", "(CLjava/lang/String;)V");
			else
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldNullList", "(CLjava/lang/String;)V");
		} else
		{
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/serializer/SerializeWriter), "writeFieldNull", "(CLjava/lang/String;)V");
		}
		_seperator(mw, context);
		mw.visitJumpInsn(167, _end_if);
		mw.visitLabel(_else);
		mw.visitLabel(_end_if);
	}

	private void _seperator(MethodVisitor mw, Context context)
	{
		mw.visitVarInsn(16, 44);
		mw.visitVarInsn(54, context.var("seperator"));
	}
}
