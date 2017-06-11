// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONException.java

package com.alibaba.fastjson;


public class JSONException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public JSONException()
	{
	}

	public JSONException(String message)
	{
		super(message);
	}

	public JSONException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
