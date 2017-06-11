// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UTF8Decoder.java

package com.alibaba.fastjson.util;

import java.nio.*;
import java.nio.charset.*;

public class UTF8Decoder extends CharsetDecoder
{
	private static class Surrogate
	{

		public static final int UCS4_MIN = 0x10000;
		public static final int UCS4_MAX = 0x10ffff;
		static final boolean $assertionsDisabled = !com/alibaba/fastjson/util/UTF8Decoder.desiredAssertionStatus();

		public static boolean neededFor(int uc)
		{
			return uc >= 0x10000 && uc <= 0x10ffff;
		}

		public static char high(int uc)
		{
			if (!$assertionsDisabled && !neededFor(uc))
				throw new AssertionError();
			else
				return (char)(0xd800 | uc - 0x10000 >> 10 & 0x3ff);
		}

		public static char low(int uc)
		{
			if (!$assertionsDisabled && !neededFor(uc))
				throw new AssertionError();
			else
				return (char)(0xdc00 | uc - 0x10000 & 0x3ff);
		}


		private Surrogate()
		{
		}
	}


	private static final Charset charset = Charset.forName("UTF-8");

	public UTF8Decoder()
	{
		super(charset, 1.0F, 1.0F);
	}

	private static boolean isNotContinuation(int b)
	{
		return (b & 0xc0) != 128;
	}

	private static final boolean isMalformed2(int b1, int b2)
	{
		return (b1 & 0x1e) == 0 || (b2 & 0xc0) != 128;
	}

	private static boolean isMalformed3(int b1, int b2, int b3)
	{
		return b1 == -32 && (b2 & 0xe0) == 128 || (b2 & 0xc0) != 128 || (b3 & 0xc0) != 128;
	}

	private static final boolean isMalformed4(int b2, int b3, int b4)
	{
		return (b2 & 0xc0) != 128 || (b3 & 0xc0) != 128 || (b4 & 0xc0) != 128;
	}

	private static CoderResult lookupN(ByteBuffer src, int n)
	{
		for (int i = 1; i < n; i++)
			if (isNotContinuation(src.get()))
				return CoderResult.malformedForLength(i);

		return CoderResult.malformedForLength(n);
	}

	public static CoderResult malformedN(ByteBuffer src, int nb)
	{
		switch (nb)
		{
		case 1: // '\001'
		{
			int b1 = src.get();
			if (b1 >> 2 == -2)
				if (src.remaining() < 4)
					return CoderResult.UNDERFLOW;
				else
					return lookupN(src, 5);
			if (b1 >> 1 == -2)
			{
				if (src.remaining() < 5)
					return CoderResult.UNDERFLOW;
				else
					return lookupN(src, 6);
			} else
			{
				return CoderResult.malformedForLength(1);
			}
		}

		case 2: // '\002'
		{
			return CoderResult.malformedForLength(1);
		}

		case 3: // '\003'
		{
			int b1 = src.get();
			int b2 = src.get();
			return CoderResult.malformedForLength((b1 != -32 || (b2 & 0xe0) != 128) && !isNotContinuation(b2) ? 2 : 1);
		}

		case 4: // '\004'
		{
			int b1 = src.get() & 0xff;
			int b2 = src.get() & 0xff;
			if (b1 > 244 || b1 == 240 && (b2 < 144 || b2 > 191) || b1 == 244 && (b2 & 0xf0) != 128 || isNotContinuation(b2))
				return CoderResult.malformedForLength(1);
			if (isNotContinuation(src.get()))
				return CoderResult.malformedForLength(2);
			else
				return CoderResult.malformedForLength(3);
		}
		}
		throw new IllegalStateException();
	}

	private static CoderResult malformed(ByteBuffer src, int sp, CharBuffer dst, int dp, int nb)
	{
		src.position(sp - src.arrayOffset());
		CoderResult cr = malformedN(src, nb);
		updatePositions(src, sp, dst, dp);
		return cr;
	}

	private static CoderResult xflow(Buffer src, int sp, int sl, Buffer dst, int dp, int nb)
	{
		updatePositions(src, sp, dst, dp);
		return nb != 0 && sl - sp >= nb ? CoderResult.OVERFLOW : CoderResult.UNDERFLOW;
	}

	private CoderResult decodeArrayLoop(ByteBuffer src, CharBuffer dst)
	{
		byte srcArray[] = src.array();
		int srcPosition = src.arrayOffset() + src.position();
		int srcLength = src.arrayOffset() + src.limit();
		char destArray[] = dst.array();
		int destPosition = dst.arrayOffset() + dst.position();
		int destLength = dst.arrayOffset() + dst.limit();
		for (int destLengthASCII = destPosition + Math.min(srcLength - srcPosition, destLength - destPosition); destPosition < destLengthASCII && srcArray[srcPosition] >= 0;)
			destArray[destPosition++] = (char)srcArray[srcPosition++];

		while (srcPosition < srcLength) 
		{
			int b1 = srcArray[srcPosition];
			if (b1 >= 0)
			{
				if (destPosition >= destLength)
					return xflow(src, srcPosition, srcLength, dst, destPosition, 1);
				destArray[destPosition++] = (char)b1;
				srcPosition++;
			} else
			if (b1 >> 5 == -2)
			{
				if (srcLength - srcPosition < 2 || destPosition >= destLength)
					return xflow(src, srcPosition, srcLength, dst, destPosition, 2);
				int b2 = srcArray[srcPosition + 1];
				if (isMalformed2(b1, b2))
					return malformed(src, srcPosition, dst, destPosition, 2);
				destArray[destPosition++] = (char)(b1 << 6 ^ b2 ^ 0xf80);
				srcPosition += 2;
			} else
			if (b1 >> 4 == -2)
			{
				if (srcLength - srcPosition < 3 || destPosition >= destLength)
					return xflow(src, srcPosition, srcLength, dst, destPosition, 3);
				int b2 = srcArray[srcPosition + 1];
				int b3 = srcArray[srcPosition + 2];
				if (isMalformed3(b1, b2, b3))
					return malformed(src, srcPosition, dst, destPosition, 3);
				destArray[destPosition++] = (char)(b1 << 12 ^ b2 << 6 ^ b3 ^ 0x1f80);
				srcPosition += 3;
			} else
			if (b1 >> 3 == -2)
			{
				if (srcLength - srcPosition < 4 || destLength - destPosition < 2)
					return xflow(src, srcPosition, srcLength, dst, destPosition, 4);
				int b2 = srcArray[srcPosition + 1];
				int b3 = srcArray[srcPosition + 2];
				int b4 = srcArray[srcPosition + 3];
				int uc = (b1 & 7) << 18 | (b2 & 0x3f) << 12 | (b3 & 0x3f) << 6 | b4 & 0x3f;
				if (isMalformed4(b2, b3, b4) || !Surrogate.neededFor(uc))
					return malformed(src, srcPosition, dst, destPosition, 4);
				destArray[destPosition++] = Surrogate.high(uc);
				destArray[destPosition++] = Surrogate.low(uc);
				srcPosition += 4;
			} else
			{
				return malformed(src, srcPosition, dst, destPosition, 1);
			}
		}
		return xflow(src, srcPosition, srcLength, dst, destPosition, 0);
	}

	protected CoderResult decodeLoop(ByteBuffer src, CharBuffer dst)
	{
		return decodeArrayLoop(src, dst);
	}

	static final void updatePositions(Buffer src, int sp, Buffer dst, int dp)
	{
		src.position(sp);
		dst.position(dp);
	}

}
