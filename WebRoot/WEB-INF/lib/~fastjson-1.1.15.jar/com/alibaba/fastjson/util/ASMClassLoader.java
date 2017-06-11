// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ASMClassLoader.java

package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import java.security.*;

public class ASMClassLoader extends ClassLoader
{

	private static ProtectionDomain DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {

		public Object run()
		{
			return com/alibaba/fastjson/util/ASMClassLoader.getProtectionDomain();
		}

	});

	public ASMClassLoader()
	{
		super(Thread.currentThread().getContextClassLoader());
	}

	public Class defineClassPublic(String name, byte b[], int off, int len)
		throws ClassFormatError
	{
		Class clazz = defineClass(name, b, off, len, DOMAIN);
		return clazz;
	}

	public static Class forName(String className)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.loadClass(className);
		ClassNotFoundException e;
		e;
		throw new JSONException((new StringBuilder()).append("class nout found : ").append(className).toString());
	}

	public static boolean isExternalClass(Class clazz)
	{
		ClassLoader classLoader = clazz.getClassLoader();
		if (classLoader == null)
			return false;
		for (ClassLoader current = Thread.currentThread().getContextClassLoader(); current != null; current = current.getParent())
			if (current == classLoader)
				return false;

		return true;
	}

}
