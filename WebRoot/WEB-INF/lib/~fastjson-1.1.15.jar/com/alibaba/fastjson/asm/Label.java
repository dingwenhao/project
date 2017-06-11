// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Label.java

package com.alibaba.fastjson.asm;


// Referenced classes of package com.alibaba.fastjson.asm:
//			ByteVector, MethodWriter

public class Label
{

	static final int RESOLVED = 2;
	public Object info;
	int status;
	int line;
	int position;
	private int referenceCount;
	private int srcAndRefPositions[];
	int inputStackTop;
	int outputStackMax;
	Label successor;
	Label next;

	public Label()
	{
	}

	void put(MethodWriter owner, ByteVector out, int source)
	{
		if ((status & 2) == 0)
		{
			addReference(source, out.length);
			out.putShort(-1);
		} else
		{
			out.putShort(position - source);
		}
	}

	private void addReference(int sourcePosition, int referencePosition)
	{
		if (srcAndRefPositions == null)
			srcAndRefPositions = new int[6];
		if (referenceCount >= srcAndRefPositions.length)
		{
			int a[] = new int[srcAndRefPositions.length + 6];
			System.arraycopy(srcAndRefPositions, 0, a, 0, srcAndRefPositions.length);
			srcAndRefPositions = a;
		}
		srcAndRefPositions[referenceCount++] = sourcePosition;
		srcAndRefPositions[referenceCount++] = referencePosition;
	}

	boolean resolve(MethodWriter owner, int position, byte data[])
	{
		boolean needUpdate = false;
		status |= 2;
		this.position = position;
		for (int i = 0; i < referenceCount;)
		{
			int source = srcAndRefPositions[i++];
			int reference = srcAndRefPositions[i++];
			if (source >= 0)
			{
				int offset = position - source;
				if (offset < -32768 || offset > 32767)
				{
					int opcode = data[reference - 1] & 0xff;
					if (opcode <= 168)
						data[reference - 1] = (byte)(opcode + 49);
					else
						data[reference - 1] = (byte)(opcode + 20);
					needUpdate = true;
				}
				data[reference++] = (byte)(offset >>> 8);
				data[reference] = (byte)offset;
			} else
			{
				int offset = position + source + 1;
				data[reference++] = (byte)(offset >>> 24);
				data[reference++] = (byte)(offset >>> 16);
				data[reference++] = (byte)(offset >>> 8);
				data[reference] = (byte)offset;
			}
		}

		return needUpdate;
	}
}
