// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerialWriterStringEncoder.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ThreadLocalCache;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public class SerialWriterStringEncoder
{

	private final CharsetEncoder encoder;

	public SerialWriterStringEncoder(Charset cs)
	{
		this(cs.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
	}

	public SerialWriterStringEncoder(CharsetEncoder encoder)
	{
		this.encoder = encoder;
	}

	public byte[] encode(char chars[], int off, int len)
	{
		if (len == 0)
		{
			return new byte[0];
		} else
		{
			encoder.reset();
			int bytesLength = scale(len, encoder.maxBytesPerChar());
			byte bytes[] = ThreadLocalCache.getBytes(bytesLength);
			return encode(chars, off, len, bytes);
		}
	}

	public CharsetEncoder getEncoder()
	{
		return encoder;
	}

	public byte[] encode(char chars[], int off, int len, byte bytes[])
	{
		ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
		CharBuffer charBuf = CharBuffer.wrap(chars, off, len);
		try
		{
			CoderResult cr = encoder.encode(charBuf, byteBuf, true);
			if (!cr.isUnderflow())
				cr.throwException();
			cr = encoder.flush(byteBuf);
			if (!cr.isUnderflow())
				cr.throwException();
		}
		catch (CharacterCodingException x)
		{
			throw new JSONException(x.getMessage(), x);
		}
		int bytesLength = byteBuf.position();
		byte copy[] = new byte[bytesLength];
		System.arraycopy(bytes, 0, copy, 0, bytesLength);
		return copy;
	}

	private static int scale(int len, float expansionFactor)
	{
		return (int)((double)len * (double)expansionFactor);
	}
}
