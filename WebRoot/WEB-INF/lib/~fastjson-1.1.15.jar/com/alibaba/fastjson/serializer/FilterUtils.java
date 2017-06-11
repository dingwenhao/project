// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FilterUtils.java

package com.alibaba.fastjson.serializer;

import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			ValueFilter, NameFilter, PropertyFilter, JSONSerializer

public class FilterUtils
{

	public FilterUtils()
	{
	}

	public static Object processValue(JSONSerializer serializer, Object object, String key, Object propertyValue)
	{
		List valueFilters = serializer.getValueFiltersDirect();
		if (valueFilters != null)
		{
			for (Iterator i$ = valueFilters.iterator(); i$.hasNext();)
			{
				ValueFilter valueFilter = (ValueFilter)i$.next();
				propertyValue = valueFilter.process(object, key, propertyValue);
			}

		}
		return propertyValue;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, Object propertyValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, byte intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Byte.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, short intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Short.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, int intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Integer.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, long intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Long.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, float intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Float.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, double intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Double.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, boolean intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Boolean.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static String processKey(JSONSerializer serializer, Object object, String key, char intValue)
	{
		List nameFilters = serializer.getNameFiltersDirect();
		if (nameFilters != null)
		{
			Object propertyValue = Character.valueOf(intValue);
			for (Iterator i$ = nameFilters.iterator(); i$.hasNext();)
			{
				NameFilter nameFilter = (NameFilter)i$.next();
				key = nameFilter.process(object, key, propertyValue);
			}

		}
		return key;
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, Object propertyValue)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, byte value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Byte.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, short value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Short.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, int value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Integer.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, char value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Character.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, long value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Long.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, float value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Float.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}

	public static boolean apply(JSONSerializer serializer, Object object, String key, double value)
	{
		List propertyFilters = serializer.getPropertyFiltersDirect();
		if (propertyFilters != null)
		{
			boolean apply = true;
			Object propertyValue = Double.valueOf(value);
			for (Iterator i$ = propertyFilters.iterator(); i$.hasNext();)
			{
				PropertyFilter propertyFilter = (PropertyFilter)i$.next();
				if (!propertyFilter.apply(object, key, propertyValue))
					return false;
			}

			return apply;
		} else
		{
			return true;
		}
	}
}
