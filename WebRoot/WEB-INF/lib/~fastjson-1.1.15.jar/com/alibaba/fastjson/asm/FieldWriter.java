// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldWriter.java

package com.alibaba.fastjson.asm;


// Referenced classes of package com.alibaba.fastjson.asm:
//			FieldVisitor, ClassWriter, ByteVector

final class FieldWriter
	implements FieldVisitor
{

	FieldWriter next;
	private final int access;
	private final int name;
	private final int desc;

	FieldWriter(ClassWriter cw, int access, String name, String desc)
	{
		if (cw.firstField == null)
			cw.firstField = this;
		else
			cw.lastField.next = this;
		cw.lastField = this;
		this.access = access;
		this.name = cw.newUTF8(name);
		this.desc = cw.newUTF8(desc);
	}

	public void visitEnd()
	{
	}

	int getSize()
	{
		return 8;
	}

	void put(ByteVector out)
	{
		int mask = 0x60000 | (access & 0x40000) / 64;
		out.putShort(access & ~mask).putShort(name).putShort(desc);
		int attributeCount = 0;
		out.putShort(attributeCount);
	}
}
