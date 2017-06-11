// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ASMDeserializerFactory.java

package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.asm.ASMException;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.FieldVisitor;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.SymbolTable;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

// Referenced classes of package com.alibaba.fastjson.parser.deserializer:
//			ASMJavaBeanDeserializer, ObjectDeserializer, FieldDeserializer, IntegerFieldDeserializer, 
//			LongFieldDeserializer, StringFieldDeserializer, JavaBeanDeserializer

public class ASMDeserializerFactory
	implements Opcodes
{
	static class Context
	{

		private int variantIndex;
		private Map variants;
		private Class clazz;
		private final DeserializeBeanInfo beanInfo;
		private String className;
		private List fieldInfoList;

		public String getClassName()
		{
			return className;
		}

		public List getFieldInfoList()
		{
			return fieldInfoList;
		}

		public DeserializeBeanInfo getBeanInfo()
		{
			return beanInfo;
		}

		public Class getClazz()
		{
			return clazz;
		}

		public int getVariantCount()
		{
			return variantIndex;
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

		public int var(String name)
		{
			Integer i = (Integer)variants.get(name);
			if (i == null)
				variants.put(name, Integer.valueOf(variantIndex++));
			i = (Integer)variants.get(name);
			return i.intValue();
		}

		public Context(String className, ParserConfig config, DeserializeBeanInfo beanInfo, int initVariantIndex)
		{
			variantIndex = 5;
			variants = new HashMap();
			this.className = className;
			clazz = beanInfo.getClazz();
			variantIndex = initVariantIndex;
			this.beanInfo = beanInfo;
			fieldInfoList = new ArrayList(beanInfo.getFieldList());
		}
	}


	private static final ASMDeserializerFactory instance = new ASMDeserializerFactory();
	private ASMClassLoader classLoader;
	private final AtomicLong seed = new AtomicLong();

	public String getGenClassName(Class clazz)
	{
		return (new StringBuilder()).append("Fastjson_ASM_").append(clazz.getSimpleName()).append("_").append(seed.incrementAndGet()).toString();
	}

	public String getGenFieldDeserializer(Class clazz, FieldInfo fieldInfo)
	{
		String name = (new StringBuilder()).append("Fastjson_ASM__Field_").append(clazz.getSimpleName()).toString();
		name = (new StringBuilder()).append(name).append("_").append(fieldInfo.getName()).append("_").append(seed.incrementAndGet()).toString();
		return name;
	}

	public ASMDeserializerFactory()
	{
		classLoader = new ASMClassLoader();
	}

	public static final ASMDeserializerFactory getInstance()
	{
		return instance;
	}

	public ObjectDeserializer createJavaBeanDeserializer(ParserConfig config, Class clazz)
		throws Exception
	{
		if (clazz.isPrimitive())
		{
			throw new IllegalArgumentException((new StringBuilder()).append("not support type :").append(clazz.getName()).toString());
		} else
		{
			String className = getGenClassName(clazz);
			ClassWriter cw = new ClassWriter();
			cw.visit(49, 33, className, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), null);
			DeserializeBeanInfo beanInfo = DeserializeBeanInfo.computeSetters(clazz);
			_init(cw, new Context(className, config, beanInfo, 3));
			_createInstance(cw, new Context(className, config, beanInfo, 3));
			_deserialze(cw, new Context(className, config, beanInfo, 4));
			byte code[] = cw.toByteArray();
			Class exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);
			Constructor constructor = exampleClass.getConstructor(new Class[] {
				com/alibaba/fastjson/parser/ParserConfig, java/lang/Class
			});
			Object instance = constructor.newInstance(new Object[] {
				config, clazz
			});
			return (ObjectDeserializer)instance;
		}
	}

	void _deserialze(ClassWriter cw, Context context)
	{
		if (context.getFieldInfoList().size() == 0)
			return;
		for (Iterator i$ = context.getFieldInfoList().iterator(); i$.hasNext();)
		{
			FieldInfo fieldInfo = (FieldInfo)i$.next();
			Class fieldClass = fieldInfo.getFieldClass();
			Type fieldType = fieldInfo.getFieldType();
			if (fieldClass == Character.TYPE)
				return;
			if (java/util/Collection.isAssignableFrom(fieldClass))
				if (fieldType instanceof ParameterizedType)
				{
					Type itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
					if (!(itemType instanceof Class))
						return;
				} else
				{
					return;
				}
		}

		Collections.sort(context.getFieldInfoList());
		MethodVisitor mw = cw.visitMethod(1, "deserialze", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser)).append(ASMUtils.getDesc(java/lang/reflect/Type)).append("Ljava/lang/Object;)Ljava/lang/Object;").toString(), null, null);
		Label reset_ = new Label();
		Label super_ = new Label();
		Label return_ = new Label();
		Label end_ = new Label();
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getLexer", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/JSONLexer)).toString());
		mw.visitTypeInsn(192, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner));
		mw.visitVarInsn(58, context.var("lexer"));
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/Feature), "SortFeidFastMatch", (new StringBuilder()).append("L").append(ASMUtils.getType(com/alibaba/fastjson/parser/Feature)).append(";").toString());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "isEnabled", (new StringBuilder()).append("(L").append(ASMUtils.getType(com/alibaba/fastjson/parser/Feature)).append(";").append(")Z").toString());
		mw.visitJumpInsn(153, super_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitLdcInsn(context.getClazz().getName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanType", "(Ljava/lang/String;)I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "NOT_MATCH", "I");
		mw.visitJumpInsn(159, super_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "getBufferPosition", "()I");
		mw.visitVarInsn(54, context.var("mark"));
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "getCurrent", "()C");
		mw.visitVarInsn(54, context.var("mark_ch"));
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "token", "()I");
		mw.visitVarInsn(54, context.var("mark_token"));
		Constructor defaultConstructor = context.getBeanInfo().getDefaultConstructor();
		if (context.getClazz().isInterface())
		{
			mw.visitVarInsn(25, 0);
			mw.visitVarInsn(25, 1);
			mw.visitMethodInsn(183, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "createInstance", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser)).append(")Ljava/lang/Object;").toString());
			mw.visitTypeInsn(192, ASMUtils.getType(context.getClazz()));
			mw.visitVarInsn(58, context.var("instance"));
		} else
		if (defaultConstructor != null)
		{
			if (Modifier.isPublic(defaultConstructor.getModifiers()))
			{
				mw.visitTypeInsn(187, ASMUtils.getType(context.getClazz()));
				mw.visitInsn(89);
				mw.visitMethodInsn(183, ASMUtils.getType(context.getClazz()), "<init>", "()V");
				mw.visitVarInsn(58, context.var("instance"));
			} else
			{
				mw.visitVarInsn(25, 0);
				mw.visitVarInsn(25, 1);
				mw.visitMethodInsn(183, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "createInstance", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser)).append(")Ljava/lang/Object;").toString());
				mw.visitTypeInsn(192, ASMUtils.getType(context.getClazz()));
				mw.visitVarInsn(58, context.var("instance"));
			}
		} else
		{
			mw.visitInsn(1);
			mw.visitTypeInsn(192, ASMUtils.getType(context.getClazz()));
			mw.visitVarInsn(58, context.var("instance"));
		}
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getContext", "()Lcom/alibaba/fastjson/parser/ParseContext;");
		mw.visitVarInsn(58, context.var("context"));
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, context.var("context"));
		mw.visitVarInsn(25, context.var("instance"));
		mw.visitVarInsn(25, 3);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "setContext", "(Lcom/alibaba/fastjson/parser/ParseContext;Ljava/lang/Object;Ljava/lang/Object;)Lcom/alibaba/fastjson/parser/ParseContext;");
		mw.visitVarInsn(58, context.var("childContext"));
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitFieldInsn(180, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "matchStat", "I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "END", "I");
		mw.visitJumpInsn(159, return_);
		int i = 0;
		for (int size = context.getFieldInfoList().size(); i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)context.getFieldInfoList().get(i);
			Class fieldClass = fieldInfo.getFieldClass();
			Type fieldType = fieldInfo.getFieldType();
			mw.visitVarInsn(25, context.var("lexer"));
			mw.visitVarInsn(25, 0);
			mw.visitFieldInsn(180, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_prefix__").toString(), "[C");
			if (fieldClass == Boolean.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldBoolean", "([C)Z");
				mw.visitVarInsn(54, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass == Byte.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldInt", "([C)I");
				mw.visitVarInsn(54, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass == Short.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldInt", "([C)I");
				mw.visitVarInsn(54, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass == Integer.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldInt", "([C)I");
				mw.visitVarInsn(54, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass == Long.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldLong", "([C)J");
				mw.visitVarInsn(55, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString(), 2));
			} else
			if (fieldClass == Float.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldFloat", "([C)F");
				mw.visitVarInsn(56, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass == Double.TYPE)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldDouble", "([C)D");
				mw.visitVarInsn(57, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString(), 2));
			} else
			if (fieldClass == java/lang/String)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldString", "([C)Ljava/lang/String;");
				mw.visitInsn(89);
				Label endCheck_ = new Label();
				mw.visitJumpInsn(199, endCheck_);
				mw.visitVarInsn(25, 1);
				mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/Feature), "InitStringFieldAsEmpty", (new StringBuilder()).append("L").append(ASMUtils.getType(com/alibaba/fastjson/parser/Feature)).append(";").toString());
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "isEnabled", (new StringBuilder()).append("(L").append(ASMUtils.getType(com/alibaba/fastjson/parser/Feature)).append(";").append(")Z").toString());
				mw.visitJumpInsn(153, endCheck_);
				mw.visitInsn(87);
				mw.visitLdcInsn("");
				mw.visitLabel(endCheck_);
				mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass == [B)
			{
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldByteArray", "([C)[B");
				mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			if (fieldClass.isEnum())
			{
				Label enumNull_ = new Label();
				mw.visitInsn(1);
				mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
				mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				mw.visitVarInsn(25, 1);
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getSymbolTable", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/SymbolTable)).toString());
				mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldSymbol", (new StringBuilder()).append("([C").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/SymbolTable)).append(")Ljava/lang/String;").toString());
				mw.visitInsn(89);
				mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm_enumName").toString()));
				mw.visitJumpInsn(198, enumNull_);
				mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm_enumName").toString()));
				mw.visitMethodInsn(184, ASMUtils.getType(fieldClass), "valueOf", (new StringBuilder()).append("(Ljava/lang/String;)").append(ASMUtils.getDesc(fieldClass)).toString());
				mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				mw.visitLabel(enumNull_);
			} else
			if (java/util/Collection.isAssignableFrom(fieldClass))
			{
				Type actualTypeArgument = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
				if (actualTypeArgument instanceof Class)
				{
					Class itemClass = (Class)actualTypeArgument;
					if (!Modifier.isPublic(itemClass.getModifiers()))
						throw new ASMException("can not create ASMParser");
					if (itemClass == java/lang/String)
					{
						mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "scanFieldStringArray", (new StringBuilder()).append("([C)").append(ASMUtils.getDesc(java/util/ArrayList)).toString());
						mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
					} else
					{
						_deserialze_list_obj(context, mw, reset_, fieldInfo, fieldClass, itemClass);
						if (i == size - 1)
							_deserialize_endCheck(context, mw, reset_);
						continue;
					}
				} else
				{
					throw new ASMException("can not create ASMParser");
				}
			} else
			{
				_deserialze_obj(context, mw, reset_, fieldInfo, fieldClass);
				if (i == size - 1)
					_deserialize_endCheck(context, mw, reset_);
				continue;
			}
			mw.visitVarInsn(25, context.var("lexer"));
			mw.visitFieldInsn(180, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "matchStat", "I");
			mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "NOT_MATCH", "I");
			mw.visitJumpInsn(159, reset_);
			if (i == size - 1)
			{
				mw.visitVarInsn(25, context.var("lexer"));
				mw.visitFieldInsn(180, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "matchStat", "I");
				mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "END", "I");
				mw.visitJumpInsn(160, reset_);
			}
		}

		mw.visitLabel(end_);
		if (!context.getClazz().isInterface() && !Modifier.isAbstract(context.getClazz().getModifiers()))
			if (defaultConstructor != null)
			{
				_batchSet(context, mw);
			} else
			{
				Constructor creatorConstructor = context.getBeanInfo().getCreatorConstructor();
				if (creatorConstructor != null)
				{
					mw.visitTypeInsn(187, ASMUtils.getType(context.getClazz()));
					mw.visitInsn(89);
					_loadCreatorParameters(context, mw);
					mw.visitMethodInsn(183, ASMUtils.getType(context.getClazz()), "<init>", ASMUtils.getDesc(creatorConstructor));
					mw.visitVarInsn(58, context.var("instance"));
				} else
				{
					Method factoryMethod = context.getBeanInfo().getFactoryMethod();
					if (factoryMethod != null)
					{
						_loadCreatorParameters(context, mw);
						mw.visitMethodInsn(184, ASMUtils.getType(factoryMethod.getDeclaringClass()), factoryMethod.getName(), ASMUtils.getDesc(factoryMethod));
						mw.visitVarInsn(58, context.var("instance"));
					} else
					{
						throw new JSONException("TODO");
					}
				}
			}
		mw.visitLabel(return_);
		_setContext(context, mw, true);
		mw.visitVarInsn(25, context.var("instance"));
		mw.visitInsn(176);
		mw.visitLabel(reset_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitVarInsn(21, context.var("mark"));
		mw.visitVarInsn(21, context.var("mark_ch"));
		mw.visitVarInsn(21, context.var("mark_token"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "reset", "(ICI)V");
		_setContext(context, mw, false);
		mw.visitLabel(super_);
		mw.visitVarInsn(25, 0);
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, 2);
		mw.visitVarInsn(25, 3);
		mw.visitMethodInsn(183, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "deserialze", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser)).append(ASMUtils.getDesc(java/lang/reflect/Type)).append("Ljava/lang/Object;)Ljava/lang/Object;").toString());
		mw.visitInsn(176);
		int maxStack = 4;
		Constructor creatorConstructor = context.getBeanInfo().getCreatorConstructor();
		if (creatorConstructor != null)
		{
			int constructorTypeStack = 2;
			Class arr$[] = creatorConstructor.getParameterTypes();
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++)
			{
				Class type = arr$[i$];
				if (type == Long.TYPE || type == Double.TYPE)
					constructorTypeStack += 2;
				else
					constructorTypeStack++;
			}

			if (maxStack < constructorTypeStack)
				maxStack = constructorTypeStack;
		} else
		{
			Method factoryMethod = context.getBeanInfo().getFactoryMethod();
			if (factoryMethod != null)
			{
				int paramStacks = 2;
				Class arr$[] = factoryMethod.getParameterTypes();
				int len$ = arr$.length;
				for (int i$ = 0; i$ < len$; i$++)
				{
					Class type = arr$[i$];
					if (type == Long.TYPE || type == Double.TYPE)
						paramStacks += 2;
					else
						paramStacks++;
				}

				if (maxStack < paramStacks)
					maxStack = paramStacks;
			}
		}
		mw.visitMaxs(maxStack, context.getVariantCount());
		mw.visitEnd();
	}

	private void _loadCreatorParameters(Context context, MethodVisitor mw)
	{
		List fieldInfoList = context.getBeanInfo().getFieldList();
		int i = 0;
		for (int size = fieldInfoList.size(); i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)fieldInfoList.get(i);
			Class fieldClass = fieldInfo.getFieldClass();
			Type fieldType = fieldInfo.getFieldType();
			if (fieldClass == Boolean.TYPE)
			{
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (fieldClass == Byte.TYPE)
			{
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (fieldClass == Short.TYPE)
			{
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (fieldClass == Integer.TYPE)
			{
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (fieldClass == Long.TYPE)
			{
				mw.visitVarInsn(22, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString(), 2));
				continue;
			}
			if (fieldClass == Float.TYPE)
			{
				mw.visitVarInsn(23, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (fieldClass == Double.TYPE)
			{
				mw.visitVarInsn(24, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString(), 2));
				continue;
			}
			if (fieldClass == java/lang/String)
			{
				mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (fieldClass.isEnum())
			{
				mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				continue;
			}
			if (java/util/Collection.isAssignableFrom(fieldClass))
			{
				Type itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
				if (itemType == java/lang/String)
				{
					mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
					mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
				} else
				{
					mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				}
			} else
			{
				mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			}
		}

	}

	private void _batchSet(Context context, MethodVisitor mw)
	{
		int i = 0;
		for (int size = context.getFieldInfoList().size(); i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)context.getFieldInfoList().get(i);
			Class fieldClass = fieldInfo.getFieldClass();
			Type fieldType = fieldInfo.getFieldType();
			mw.visitVarInsn(25, context.var("instance"));
			if (fieldClass == Boolean.TYPE)
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			else
			if (fieldClass == Byte.TYPE)
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			else
			if (fieldClass == Short.TYPE)
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			else
			if (fieldClass == Integer.TYPE)
			{
				mw.visitVarInsn(21, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
			} else
			{
				if (fieldClass == Long.TYPE)
				{
					mw.visitVarInsn(22, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString(), 2));
					mw.visitMethodInsn(182, ASMUtils.getType(context.getClazz()), fieldInfo.getMethod().getName(), "(J)V");
					continue;
				}
				if (fieldClass == Float.TYPE)
					mw.visitVarInsn(23, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				else
				if (fieldClass == Double.TYPE)
					mw.visitVarInsn(24, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString(), 2));
				else
				if (fieldClass == java/lang/String)
					mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				else
				if (fieldClass.isEnum())
					mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				else
				if (java/util/Collection.isAssignableFrom(fieldClass))
				{
					Type itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
					if (itemType == java/lang/String)
					{
						mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
						mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
					} else
					{
						mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
					}
				} else
				{
					mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
				}
			}
			int INVAKE_TYPE;
			if (context.getClazz().isInterface())
				INVAKE_TYPE = 185;
			else
				INVAKE_TYPE = 182;
			if (fieldInfo.getMethod() != null)
				mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(fieldInfo.getDeclaringClass()), fieldInfo.getMethod().getName(), ASMUtils.getDesc(fieldInfo.getMethod()));
			else
				mw.visitFieldInsn(181, ASMUtils.getType(fieldInfo.getDeclaringClass()), fieldInfo.getField().getName(), ASMUtils.getDesc(fieldInfo.getFieldClass()));
		}

	}

	private void _setContext(Context context, MethodVisitor mw, boolean setObject)
	{
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, context.var("context"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "setContext", "(Lcom/alibaba/fastjson/parser/ParseContext;)V");
		if (setObject)
		{
			Label endIf_ = new Label();
			mw.visitVarInsn(25, context.var("childContext"));
			mw.visitJumpInsn(198, endIf_);
			mw.visitVarInsn(25, context.var("childContext"));
			mw.visitVarInsn(25, context.var("instance"));
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/ParseContext), "setObject", "(Ljava/lang/Object;)V");
			mw.visitLabel(endIf_);
		}
	}

	private void _deserialize_endCheck(Context context, MethodVisitor mw, Label reset_)
	{
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "token", "()I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "RBRACE", "I");
		mw.visitJumpInsn(160, reset_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "COMMA", "I");
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "nextToken", "(I)V");
	}

	private void _deserialze_list_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo, Class fieldClass, Class itemType)
	{
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "matchField", "([C)Z");
		mw.visitJumpInsn(153, reset_);
		Label notNull_ = new Label();
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_list_item_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitJumpInsn(199, notNull_);
		mw.visitVarInsn(25, 0);
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getConfig", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParserConfig)).toString());
		mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(itemType)));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/ParserConfig), "getDeserializer", (new StringBuilder()).append("(").append(ASMUtils.getDesc(java/lang/reflect/Type)).append(")").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer)).toString());
		mw.visitFieldInsn(181, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_list_item_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitLabel(notNull_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "token", "()I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "LBRACKET", "I");
		mw.visitJumpInsn(160, reset_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_list_item_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitMethodInsn(185, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer), "getFastMatchToken", "()I");
		mw.visitVarInsn(54, context.var("fastMatchToken"));
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitVarInsn(21, context.var("fastMatchToken"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "nextToken", "(I)V");
		if (fieldClass.isAssignableFrom(java/util/ArrayList))
		{
			mw.visitTypeInsn(187, ASMUtils.getType(java/util/ArrayList));
			mw.visitInsn(89);
			mw.visitMethodInsn(183, ASMUtils.getType(java/util/ArrayList), "<init>", "()V");
		} else
		if (fieldClass.isAssignableFrom(java/util/LinkedList))
		{
			mw.visitTypeInsn(187, ASMUtils.getType(java/util/LinkedList));
			mw.visitInsn(89);
			mw.visitMethodInsn(183, ASMUtils.getType(java/util/LinkedList), "<init>", "()V");
		} else
		if (fieldClass.isAssignableFrom(java/util/HashSet))
		{
			mw.visitTypeInsn(187, ASMUtils.getType(java/util/HashSet));
			mw.visitInsn(89);
			mw.visitMethodInsn(183, ASMUtils.getType(java/util/HashSet), "<init>", "()V");
		} else
		if (fieldClass.isAssignableFrom(java/util/TreeSet))
		{
			mw.visitTypeInsn(187, ASMUtils.getType(java/util/TreeSet));
			mw.visitInsn(89);
			mw.visitMethodInsn(183, ASMUtils.getType(java/util/TreeSet), "<init>", "()V");
		} else
		{
			mw.visitTypeInsn(187, ASMUtils.getType(fieldClass));
			mw.visitInsn(89);
			mw.visitMethodInsn(183, ASMUtils.getType(fieldClass), "<init>", "()V");
		}
		mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
		mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
		mw.visitLdcInsn(fieldInfo.getName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/alibaba/fastjson/parser/ParseContext;");
		mw.visitInsn(87);
		Label loop_ = new Label();
		Label loop_end_ = new Label();
		mw.visitInsn(3);
		mw.visitVarInsn(54, context.var("i"));
		mw.visitLabel(loop_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "token", "()I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "RBRACKET", "I");
		mw.visitJumpInsn(159, loop_end_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_list_item_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitVarInsn(25, 1);
		mw.visitInsn(1);
		mw.visitVarInsn(21, context.var("i"));
		mw.visitMethodInsn(184, ASMUtils.getType(java/lang/Integer), "valueOf", "(I)Ljava/lang/Integer;");
		mw.visitMethodInsn(185, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer), "deserialze", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
		mw.visitVarInsn(58, context.var("list_item_value"));
		mw.visitIincInsn(context.var("i"), 1);
		mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
		mw.visitVarInsn(25, context.var("list_item_value"));
		if (fieldClass.isInterface())
			mw.visitMethodInsn(185, ASMUtils.getType(fieldClass), "add", "(Ljava/lang/Object;)Z");
		else
			mw.visitMethodInsn(182, ASMUtils.getType(fieldClass), "add", "(Ljava/lang/Object;)Z");
		mw.visitInsn(87);
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "checkListResolve", "(Ljava/util/Collection;)V");
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "token", "()I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "COMMA", "I");
		mw.visitJumpInsn(160, loop_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitVarInsn(21, context.var("fastMatchToken"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "nextToken", "(I)V");
		mw.visitJumpInsn(167, loop_);
		mw.visitLabel(loop_end_);
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "popContext", "()V");
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "token", "()I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "RBRACKET", "I");
		mw.visitJumpInsn(160, reset_);
		mw.visitVarInsn(25, context.var("lexer"));
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/JSONToken), "COMMA", "I");
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "nextToken", "(I)V");
	}

	private void _deserialze_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo, Class fieldClass)
	{
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/JSONScanner), "matchField", "([C)Z");
		mw.visitJumpInsn(153, reset_);
		Label notNull_ = new Label();
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitJumpInsn(199, notNull_);
		mw.visitVarInsn(25, 0);
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getConfig", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParserConfig)).toString());
		mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldInfo.getFieldClass())));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/ParserConfig), "getDeserializer", (new StringBuilder()).append("(").append(ASMUtils.getDesc(java/lang/reflect/Type)).append(")").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer)).toString());
		mw.visitFieldInsn(181, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitLabel(notNull_);
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
		mw.visitVarInsn(25, 1);
		if (fieldInfo.getFieldType() instanceof Class)
		{
			mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldInfo.getFieldClass())));
		} else
		{
			mw.visitVarInsn(25, 0);
			mw.visitLdcInsn(fieldInfo.getName());
			mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "getFieldType", "(Ljava/lang/String;)Ljava/lang/reflect/Type;");
		}
		mw.visitLdcInsn(fieldInfo.getName());
		mw.visitMethodInsn(185, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer), "deserialze", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser)).append(ASMUtils.getDesc(java/lang/reflect/Type)).append("Ljava/lang/Object;)Ljava/lang/Object;").toString());
		mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
		mw.visitVarInsn(58, context.var((new StringBuilder()).append(fieldInfo.getName()).append("_asm").toString()));
		Label _end_if = new Label();
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getResolveStatus", "()I");
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "NeedToResolve", "I");
		mw.visitJumpInsn(160, _end_if);
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getLastResolveTask", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask)).toString());
		mw.visitVarInsn(58, context.var("resolveTask"));
		mw.visitVarInsn(25, context.var("resolveTask"));
		mw.visitVarInsn(25, 1);
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "getContext", (new StringBuilder()).append("()").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParseContext)).toString());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask), "setOwnerContext", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParseContext)).append(")V").toString());
		mw.visitVarInsn(25, context.var("resolveTask"));
		mw.visitVarInsn(25, 0);
		mw.visitLdcInsn(fieldInfo.getName());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "getFieldDeserializer", (new StringBuilder()).append("(Ljava/lang/String;)").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/FieldDeserializer)).toString());
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask), "setFieldDeserializer", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/FieldDeserializer)).append(")V").toString());
		mw.visitVarInsn(25, 1);
		mw.visitFieldInsn(178, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "NONE", "I");
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/DefaultJSONParser), "setResolveStatus", "(I)V");
		mw.visitLabel(_end_if);
	}

	public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
		throws Exception
	{
		Class fieldClass = fieldInfo.getFieldClass();
		if (fieldClass == Integer.TYPE || fieldClass == Long.TYPE || fieldClass == java/lang/String)
		{
			return createStringFieldDeserializer(mapping, clazz, fieldInfo);
		} else
		{
			FieldDeserializer fieldDeserializer = mapping.createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
			return fieldDeserializer;
		}
	}

	public FieldDeserializer createStringFieldDeserializer(ParserConfig mapping, Class clazz, FieldInfo fieldInfo)
		throws Exception
	{
		Class fieldClass = fieldInfo.getFieldClass();
		Method method = fieldInfo.getMethod();
		String className = getGenFieldDeserializer(clazz, fieldInfo);
		ClassWriter cw = new ClassWriter();
		Class superClass;
		if (fieldClass == Integer.TYPE)
			superClass = com/alibaba/fastjson/parser/deserializer/IntegerFieldDeserializer;
		else
		if (fieldClass == Long.TYPE)
			superClass = com/alibaba/fastjson/parser/deserializer/LongFieldDeserializer;
		else
			superClass = com/alibaba/fastjson/parser/deserializer/StringFieldDeserializer;
		int INVAKE_TYPE;
		if (clazz.isInterface())
			INVAKE_TYPE = 185;
		else
			INVAKE_TYPE = 182;
		cw.visit(49, 33, className, ASMUtils.getType(superClass), null);
		MethodVisitor mw = cw.visitMethod(1, "<init>", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParserConfig)).append(ASMUtils.getDesc(java/lang/Class)).append(ASMUtils.getDesc(com/alibaba/fastjson/util/FieldInfo)).append(")V").toString(), null, null);
		mw.visitVarInsn(25, 0);
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, 2);
		mw.visitVarInsn(25, 3);
		mw.visitMethodInsn(183, ASMUtils.getType(superClass), "<init>", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParserConfig)).append(ASMUtils.getDesc(java/lang/Class)).append(ASMUtils.getDesc(com/alibaba/fastjson/util/FieldInfo)).append(")V").toString());
		mw.visitInsn(177);
		mw.visitMaxs(4, 6);
		mw.visitEnd();
		if (method != null)
			if (fieldClass == Integer.TYPE)
			{
				mw = cw.visitMethod(1, "setValue", (new StringBuilder()).append("(").append(ASMUtils.getDesc(java/lang/Object)).append("I)V").toString(), null, null);
				mw.visitVarInsn(25, 1);
				mw.visitTypeInsn(192, ASMUtils.getType(method.getDeclaringClass()));
				mw.visitVarInsn(21, 2);
				mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(method.getDeclaringClass()), method.getName(), "(I)V");
				mw.visitInsn(177);
				mw.visitMaxs(3, 3);
				mw.visitEnd();
			} else
			if (fieldClass == Long.TYPE)
			{
				mw = cw.visitMethod(1, "setValue", (new StringBuilder()).append("(").append(ASMUtils.getDesc(java/lang/Object)).append("J)V").toString(), null, null);
				mw.visitVarInsn(25, 1);
				mw.visitTypeInsn(192, ASMUtils.getType(method.getDeclaringClass()));
				mw.visitVarInsn(22, 2);
				mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(method.getDeclaringClass()), method.getName(), "(J)V");
				mw.visitInsn(177);
				mw.visitMaxs(3, 4);
				mw.visitEnd();
			} else
			{
				mw = cw.visitMethod(1, "setValue", (new StringBuilder()).append("(").append(ASMUtils.getDesc(java/lang/Object)).append(ASMUtils.getDesc(java/lang/Object)).append(")V").toString(), null, null);
				mw.visitVarInsn(25, 1);
				mw.visitTypeInsn(192, ASMUtils.getType(method.getDeclaringClass()));
				mw.visitVarInsn(25, 2);
				mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
				mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(method.getDeclaringClass()), method.getName(), (new StringBuilder()).append("(").append(ASMUtils.getDesc(fieldClass)).append(")V").toString());
				mw.visitInsn(177);
				mw.visitMaxs(3, 3);
				mw.visitEnd();
			}
		byte code[] = cw.toByteArray();
		Class exampleClass = classLoader.defineClassPublic(className, code, 0, code.length);
		Constructor constructor = exampleClass.getConstructor(new Class[] {
			com/alibaba/fastjson/parser/ParserConfig, java/lang/Class, com/alibaba/fastjson/util/FieldInfo
		});
		Object instance = constructor.newInstance(new Object[] {
			mapping, clazz, fieldInfo
		});
		return (FieldDeserializer)instance;
	}

	private void _init(ClassWriter cw, Context context)
	{
		int i = 0;
		for (int size = context.getFieldInfoList().size(); i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)context.getFieldInfoList().get(i);
			FieldVisitor fw = cw.visitField(1, (new StringBuilder()).append(fieldInfo.getName()).append("_asm_prefix__").toString(), "[C");
			fw.visitEnd();
		}

		i = 0;
		for (int size = context.getFieldInfoList().size(); i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)context.getFieldInfoList().get(i);
			Class fieldClass = fieldInfo.getFieldClass();
			if (fieldClass.isPrimitive() || fieldClass.isEnum())
				continue;
			if (java/util/Collection.isAssignableFrom(fieldClass))
			{
				FieldVisitor fw = cw.visitField(1, (new StringBuilder()).append(fieldInfo.getName()).append("_asm_list_item_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
				fw.visitEnd();
			} else
			{
				FieldVisitor fw = cw.visitField(1, (new StringBuilder()).append(fieldInfo.getName()).append("_asm_deser__").toString(), ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ObjectDeserializer));
				fw.visitEnd();
			}
		}

		MethodVisitor mw = cw.visitMethod(1, "<init>", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParserConfig)).append(ASMUtils.getDesc(java/lang/Class)).append(")V").toString(), null, null);
		mw.visitVarInsn(25, 0);
		mw.visitVarInsn(25, 1);
		mw.visitVarInsn(25, 2);
		mw.visitMethodInsn(183, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "<init>", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/ParserConfig)).append(ASMUtils.getDesc(java/lang/Class)).append(")V").toString());
		mw.visitVarInsn(25, 0);
		mw.visitFieldInsn(180, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer), "serializer", ASMUtils.getDesc(com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer$InnerJavaBeanDeserializer));
		mw.visitMethodInsn(182, ASMUtils.getType(com/alibaba/fastjson/parser/deserializer/JavaBeanDeserializer), "getFieldDeserializerMap", (new StringBuilder()).append("()").append(ASMUtils.getDesc(java/util/Map)).toString());
		mw.visitInsn(87);
		int i = 0;
		for (int size = context.getFieldInfoList().size(); i < size; i++)
		{
			FieldInfo fieldInfo = (FieldInfo)context.getFieldInfoList().get(i);
			mw.visitVarInsn(25, 0);
			mw.visitLdcInsn((new StringBuilder()).append("\"").append(fieldInfo.getName()).append("\":").toString());
			mw.visitMethodInsn(182, ASMUtils.getType(java/lang/String), "toCharArray", (new StringBuilder()).append("()").append(ASMUtils.getDesc([C)).toString());
			mw.visitFieldInsn(181, context.getClassName(), (new StringBuilder()).append(fieldInfo.getName()).append("_asm_prefix__").toString(), "[C");
		}

		mw.visitInsn(177);
		mw.visitMaxs(4, 4);
		mw.visitEnd();
	}

	private void _createInstance(ClassWriter cw, Context context)
	{
		MethodVisitor mw = cw.visitMethod(1, "createInstance", (new StringBuilder()).append("(").append(ASMUtils.getDesc(com/alibaba/fastjson/parser/DefaultJSONParser)).append(ASMUtils.getDesc(java/lang/reflect/Type)).append(")Ljava/lang/Object;").toString(), null, null);
		mw.visitTypeInsn(187, ASMUtils.getType(context.getClazz()));
		mw.visitInsn(89);
		mw.visitMethodInsn(183, ASMUtils.getType(context.getClazz()), "<init>", "()V");
		mw.visitInsn(176);
		mw.visitMaxs(3, 3);
		mw.visitEnd();
	}

}
