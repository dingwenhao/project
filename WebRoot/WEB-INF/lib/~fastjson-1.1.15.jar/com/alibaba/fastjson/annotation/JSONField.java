// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONField.java

package com.alibaba.fastjson.annotation;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.annotation.Annotation;

public interface JSONField
	extends Annotation
{

	public abstract String name();

	public abstract String format();

	public abstract boolean serialize();

	public abstract boolean deserialize();

	public abstract SerializerFeature[] serialzeFeatures();

	public abstract Feature[] parseFeatures();
}
