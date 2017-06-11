// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodVisitor.java

package com.alibaba.fastjson.asm;


// Referenced classes of package com.alibaba.fastjson.asm:
//			Label

public interface MethodVisitor
{

	public abstract void visitInsn(int i);

	public abstract void visitIntInsn(int i, int j);

	public abstract void visitVarInsn(int i, int j);

	public abstract void visitTypeInsn(int i, String s);

	public abstract void visitFieldInsn(int i, String s, String s1, String s2);

	public abstract void visitMethodInsn(int i, String s, String s1, String s2);

	public abstract void visitJumpInsn(int i, Label label);

	public abstract void visitLabel(Label label);

	public abstract void visitLdcInsn(Object obj);

	public abstract void visitIincInsn(int i, int j);

	public abstract void visitMaxs(int i, int j);

	public abstract void visitEnd();
}
