// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ValueFilter.java

package com.alibaba.fastjson.serializer;


public interface ValueFilter
{

	public abstract Object process(Object obj, String s, Object obj1);
}
