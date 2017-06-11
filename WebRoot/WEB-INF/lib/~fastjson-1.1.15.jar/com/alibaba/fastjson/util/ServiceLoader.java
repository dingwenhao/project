// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ServiceLoader.java

package com.alibaba.fastjson.util;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ServiceLoader
{

	private static final String PREFIX = "META-INF/services/";
	private static final Set loadedUrls = new HashSet();

	public ServiceLoader()
	{
	}

	public static Set load(Class clazz, ClassLoader classLoader)
	{
		Set services = new HashSet();
		String className = clazz.getName();
		String path = (new StringBuilder()).append("META-INF/services/").append(className).toString();
		Set serviceNames = new HashSet();
		try
		{
			Enumeration urls = classLoader.getResources(path);
			do
			{
				if (!urls.hasMoreElements())
					break;
				URL url = (URL)urls.nextElement();
				if (!loadedUrls.contains(url.toString()))
				{
					load(url, serviceNames);
					loadedUrls.add(url.toString());
				}
			} while (true);
		}
		catch (IOException ex) { }
		for (Iterator i$ = serviceNames.iterator(); i$.hasNext();)
		{
			String serviceName = (String)i$.next();
			try
			{
				Class serviceClass = classLoader.loadClass(serviceName);
				Object service = serviceClass.newInstance();
				services.add(service);
			}
			catch (Exception e) { }
		}

		return services;
	}

	public static void load(URL url, Set set)
		throws IOException
	{
		java.io.InputStream is;
		BufferedReader reader;
		is = null;
		reader = null;
		is = url.openStream();
		reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
		do
		{
			String line = reader.readLine();
			if (line == null)
				break;
			int ci = line.indexOf('#');
			if (ci >= 0)
				line = line.substring(0, ci);
			line = line.trim();
			if (line.length() != 0)
				set.add(line);
		} while (true);
		close(reader);
		close(is);
		break MISSING_BLOCK_LABEL_119;
		Exception exception;
		exception;
		close(reader);
		close(is);
		throw exception;
	}

	public static void close(Closeable x)
		throws IOException
	{
		if (x != null)
			x.close();
	}

}
