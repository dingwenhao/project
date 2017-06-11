// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Base64.java

package com.alibaba.fastjson.util;

import java.util.Arrays;

public class Base64
{

	public static final char CA[];
	public static final int IA[];

	public Base64()
	{
	}

	public static final byte[] decodeFast(char chars[], int offset, int charsLen)
	{
		if (charsLen == 0)
			return new byte[0];
		int sIx = offset;
		int eIx;
		for (eIx = (offset + charsLen) - 1; sIx < eIx && IA[chars[sIx]] < 0; sIx++);
		for (; eIx > 0 && IA[chars[eIx]] < 0; eIx--);
		int pad = chars[eIx] != '=' ? 0 : ((int) (chars[eIx - 1] != '=' ? 1 : 2));
		int cCnt = (eIx - sIx) + 1;
		int sepCnt = charsLen <= 76 ? 0 : (chars[76] != '\r' ? 0 : cCnt / 78) << 1;
		int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
		byte bytes[] = new byte[len];
		int d = 0;
		int cc = 0;
		int eLen = (len / 3) * 3;
		do
		{
			if (d >= eLen)
				break;
			int i = IA[chars[sIx++]] << 18 | IA[chars[sIx++]] << 12 | IA[chars[sIx++]] << 6 | IA[chars[sIx++]];
			bytes[d++] = (byte)(i >> 16);
			bytes[d++] = (byte)(i >> 8);
			bytes[d++] = (byte)i;
			if (sepCnt > 0 && ++cc == 19)
			{
				sIx += 2;
				cc = 0;
			}
		} while (true);
		if (d < len)
		{
			int i = 0;
			for (int j = 0; sIx <= eIx - pad; j++)
				i |= IA[chars[sIx++]] << 18 - j * 6;

			for (int r = 16; d < len; r -= 8)
				bytes[d++] = (byte)(i >> r);

		}
		return bytes;
	}

	public static final byte[] decodeFast(String s)
	{
		int sLen = s.length();
		if (sLen == 0)
			return new byte[0];
		int sIx = 0;
		int eIx;
		for (eIx = sLen - 1; sIx < eIx && IA[s.charAt(sIx) & 0xff] < 0; sIx++);
		for (; eIx > 0 && IA[s.charAt(eIx) & 0xff] < 0; eIx--);
		int pad = s.charAt(eIx) != '=' ? 0 : ((int) (s.charAt(eIx - 1) != '=' ? 1 : 2));
		int cCnt = (eIx - sIx) + 1;
		int sepCnt = sLen <= 76 ? 0 : (s.charAt(76) != '\r' ? 0 : cCnt / 78) << 1;
		int len = ((cCnt - sepCnt) * 6 >> 3) - pad;
		byte dArr[] = new byte[len];
		int d = 0;
		int cc = 0;
		int eLen = (len / 3) * 3;
		do
		{
			if (d >= eLen)
				break;
			int i = IA[s.charAt(sIx++)] << 18 | IA[s.charAt(sIx++)] << 12 | IA[s.charAt(sIx++)] << 6 | IA[s.charAt(sIx++)];
			dArr[d++] = (byte)(i >> 16);
			dArr[d++] = (byte)(i >> 8);
			dArr[d++] = (byte)i;
			if (sepCnt > 0 && ++cc == 19)
			{
				sIx += 2;
				cc = 0;
			}
		} while (true);
		if (d < len)
		{
			int i = 0;
			for (int j = 0; sIx <= eIx - pad; j++)
				i |= IA[s.charAt(sIx++)] << 18 - j * 6;

			for (int r = 16; d < len; r -= 8)
				dArr[d++] = (byte)(i >> r);

		}
		return dArr;
	}

	static 
	{
		CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
		IA = new int[256];
		Arrays.fill(IA, -1);
		int i = 0;
		for (int iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;

		IA[61] = 0;
	}
}
