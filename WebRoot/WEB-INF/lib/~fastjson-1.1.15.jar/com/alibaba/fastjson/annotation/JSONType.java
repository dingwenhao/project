// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONType.java

package com.alibaba.fastjson.annotation;

import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.annotation.Annotation;

public interface JSONType
	extends Annotation
{

	public abstract String[] orders();

	public abstract SerializerFeature[] serialzeFeatures();
}
