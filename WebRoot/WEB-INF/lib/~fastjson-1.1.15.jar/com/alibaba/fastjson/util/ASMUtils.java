// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ASMUtils.java

package com.alibaba.fastjson.util;

import java.lang.reflect.*;

public class ASMUtils
{

	public ASMUtils()
	{
	}

	public static boolean isAndroid(String vmName)
	{
		return "Dalvik".equals(vmName) || "Lemur".equals(vmName);
	}

	public static boolean isAndroid()
	{
		return isAndroid(System.getProperty("java.vm.name"));
	}

	public static String getDesc(Method method)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		Class types[] = method.getParameterTypes();
		for (int i = 0; i < types.length; i++)
			buf.append(getDesc(types[i]));

		buf.append(")");
		buf.append(getDesc(method.getReturnType()));
		return buf.toString();
	}

	public static String getDesc(Constructor constructor)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		Class types[] = constructor.getParameterTypes();
		for (int i = 0; i < types.length; i++)
			buf.append(getDesc(types[i]));

		buf.append(")V");
		return buf.toString();
	}

	public static String getDesc(Class returnType)
	{
		if (returnType.isPrimitive())
			return getPrimitiveLetter(returnType);
		if (returnType.isArray())
			return (new StringBuilder()).append("[").append(getDesc(returnType.getComponentType())).toString();
		else
			return (new StringBuilder()).append("L").append(getType(returnType)).append(";").toString();
	}

	public static String getType(Class parameterType)
	{
		if (parameterType.isArray())
			return (new StringBuilder()).append("[").append(getDesc(parameterType.getComponentType())).toString();
		if (!parameterType.isPrimitive())
		{
			String clsName = parameterType.getName();
			return clsName.replaceAll("\\.", "/");
		} else
		{
			return getPrimitiveLetter(parameterType);
		}
	}

	public static String getPrimitiveLetter(Class type)
	{
		if (Integer.TYPE.equals(type))
			return "I";
		if (Void.TYPE.equals(type))
			return "V";
		if (Boolean.TYPE.equals(type))
			return "Z";
		if (Character.TYPE.equals(type))
			return "C";
		if (Byte.TYPE.equals(type))
			return "B";
		if (Short.TYPE.equals(type))
			return "S";
		if (Float.TYPE.equals(type))
			return "F";
		if (Long.TYPE.equals(type))
			return "J";
		if (Double.TYPE.equals(type))
			return "D";
		else
			throw new IllegalStateException((new StringBuilder()).append("Type: ").append(type.getCanonicalName()).append(" is not a primitive type").toString());
	}

	public static Type getMethodType(Class clazz, String methodName)
	{
		Method method = clazz.getMethod(methodName, new Class[0]);
		return method.getGenericReturnType();
		Exception ex;
		ex;
		return null;
	}

	public static Type getFieldType(Class clazz, String fieldName)
	{
		Field field = clazz.getField(fieldName);
		return field.getGenericType();
		Exception ex;
		ex;
		return null;
	}
}
