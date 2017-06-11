// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodWriter.java

package com.alibaba.fastjson.asm;


// Referenced classes of package com.alibaba.fastjson.asm:
//			ByteVector, MethodVisitor, ClassWriter, Item, 
//			Type, Label

class MethodWriter
	implements MethodVisitor
{

	static final int ACC_CONSTRUCTOR = 0x40000;
	static final int SAME_FRAME = 0;
	static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
	static final int RESERVED = 128;
	static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
	static final int CHOP_FRAME = 248;
	static final int SAME_FRAME_EXTENDED = 251;
	static final int APPEND_FRAME = 252;
	static final int FULL_FRAME = 255;
	MethodWriter next;
	final ClassWriter cw;
	private int access;
	private final int name;
	private final int desc;
	int classReaderLength;
	int exceptionCount;
	int exceptions[];
	private ByteVector code;
	private int maxStack;
	private int maxLocals;
	private boolean resize;

	MethodWriter(ClassWriter cw, int access, String name, String desc, String signature, String exceptions[])
	{
		code = new ByteVector();
		if (cw.firstMethod == null)
			cw.firstMethod = this;
		else
			cw.lastMethod.next = this;
		cw.lastMethod = this;
		this.cw = cw;
		this.access = access;
		this.name = cw.newUTF8(name);
		this.desc = cw.newUTF8(desc);
		if (exceptions != null && exceptions.length > 0)
		{
			exceptionCount = exceptions.length;
			this.exceptions = new int[exceptionCount];
			for (int i = 0; i < exceptionCount; i++)
				this.exceptions[i] = cw.newClass(exceptions[i]);

		}
	}

	public void visitInsn(int opcode)
	{
		code.putByte(opcode);
	}

	public void visitIntInsn(int opcode, int operand)
	{
		code.put11(opcode, operand);
	}

	public void visitVarInsn(int opcode, int var)
	{
		if (var < 4 && opcode != 169)
		{
			int opt;
			if (opcode < 54)
				opt = 26 + (opcode - 21 << 2) + var;
			else
				opt = 59 + (opcode - 54 << 2) + var;
			code.putByte(opt);
		} else
		if (var >= 256)
			code.putByte(196).put12(opcode, var);
		else
			code.put11(opcode, var);
	}

	public void visitTypeInsn(int opcode, String type)
	{
		Item i = cw.newClassItem(type);
		code.put12(opcode, i.index);
	}

	public void visitFieldInsn(int opcode, String owner, String name, String desc)
	{
		Item i = cw.newFieldItem(owner, name, desc);
		code.put12(opcode, i.index);
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		boolean itf = opcode == 185;
		Item i = cw.newMethodItem(owner, name, desc, itf);
		int argSize = i.intVal;
		if (itf)
		{
			if (argSize == 0)
			{
				argSize = Type.getArgumentsAndReturnSizes(desc);
				i.intVal = argSize;
			}
			code.put12(185, i.index).put11(argSize >> 2, 0);
		} else
		{
			code.put12(opcode, i.index);
		}
	}

	public void visitJumpInsn(int opcode, Label label)
	{
		if ((label.status & 2) != 0 && label.position - code.length < -32768)
		{
			throw new UnsupportedOperationException();
		} else
		{
			code.putByte(opcode);
			label.put(this, code, code.length - 1);
			return;
		}
	}

	public void visitLabel(Label label)
	{
		resize |= label.resolve(this, code.length, code.data);
	}

	public void visitLdcInsn(Object cst)
	{
		Item i = cw.newConstItem(cst);
		int index = i.index;
		if (i.type == 5 || i.type == 6)
			code.put12(20, index);
		else
		if (index >= 256)
			code.put12(19, index);
		else
			code.put11(18, index);
	}

	public void visitIincInsn(int var, int increment)
	{
		if (var > 255 || increment > 127 || increment < -128)
			code.putByte(196).put12(132, var).putShort(increment);
		else
			code.putByte(132).put11(var, increment);
	}

	public void visitMaxs(int maxStack, int maxLocals)
	{
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
	}

	public void visitEnd()
	{
	}

	final int getSize()
	{
		if (resize)
			throw new UnsupportedOperationException();
		int size = 8;
		if (code.length > 0)
		{
			cw.newUTF8("Code");
			size += 18 + code.length + 0;
		}
		if (exceptionCount > 0)
		{
			cw.newUTF8("Exceptions");
			size += 8 + 2 * exceptionCount;
		}
		return size;
	}

	final void put(ByteVector out)
	{
		int mask = 0x60000 | (access & 0x40000) / 64;
		out.putShort(access & ~mask).putShort(name).putShort(desc);
		int attributeCount = 0;
		if (code.length > 0)
			attributeCount++;
		if (exceptionCount > 0)
			attributeCount++;
		out.putShort(attributeCount);
		if (code.length > 0)
		{
			int size = 12 + code.length + 0;
			out.putShort(cw.newUTF8("Code")).putInt(size);
			out.putShort(maxStack).putShort(maxLocals);
			out.putInt(code.length).putByteArray(code.data, 0, code.length);
			out.putShort(0);
			attributeCount = 0;
			out.putShort(attributeCount);
		}
		if (exceptionCount > 0)
		{
			out.putShort(cw.newUTF8("Exceptions")).putInt(2 * exceptionCount + 2);
			out.putShort(exceptionCount);
			for (int i = 0; i < exceptionCount; i++)
				out.putShort(exceptions[i]);

		}
	}
}
