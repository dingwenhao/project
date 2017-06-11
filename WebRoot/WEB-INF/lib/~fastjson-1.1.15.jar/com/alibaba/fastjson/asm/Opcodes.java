// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Opcodes.java

package com.alibaba.fastjson.asm;


public interface Opcodes
{

	public static final int V1_5 = 49;
	public static final int ACC_PUBLIC = 1;
	public static final int ACC_PRIVATE = 2;
	public static final int ACC_SUPER = 32;
	public static final int ACC_SYNTHETIC = 4096;
	public static final int ACC_DEPRECATED = 0x20000;
	public static final int T_BOOLEAN = 4;
	public static final int T_CHAR = 5;
	public static final int T_FLOAT = 6;
	public static final int T_DOUBLE = 7;
	public static final int T_BYTE = 8;
	public static final int T_SHORT = 9;
	public static final int T_INT = 10;
	public static final int T_LONG = 11;
	public static final String INVOKEDYNAMIC_OWNER = "java/lang/dyn/Dynamic";
	public static final int NOP = 0;
	public static final int ACONST_NULL = 1;
	public static final int ICONST_M1 = 2;
	public static final int ICONST_0 = 3;
	public static final int ICONST_1 = 4;
	public static final int ICONST_2 = 5;
	public static final int ICONST_3 = 6;
	public static final int ICONST_4 = 7;
	public static final int ICONST_5 = 8;
	public static final int LCONST_0 = 9;
	public static final int LCONST_1 = 10;
	public static final int FCONST_0 = 11;
	public static final int FCONST_1 = 12;
	public static final int FCONST_2 = 13;
	public static final int DCONST_0 = 14;
	public static final int DCONST_1 = 15;
	public static final int BIPUSH = 16;
	public static final int LDC = 18;
	public static final int ILOAD = 21;
	public static final int LLOAD = 22;
	public static final int FLOAD = 23;
	public static final int DLOAD = 24;
	public static final int ALOAD = 25;
	public static final int IALOAD = 46;
	public static final int LALOAD = 47;
	public static final int FALOAD = 48;
	public static final int DALOAD = 49;
	public static final int AALOAD = 50;
	public static final int BALOAD = 51;
	public static final int CALOAD = 52;
	public static final int SALOAD = 53;
	public static final int ISTORE = 54;
	public static final int LSTORE = 55;
	public static final int FSTORE = 56;
	public static final int DSTORE = 57;
	public static final int ASTORE = 58;
	public static final int POP = 87;
	public static final int POP2 = 88;
	public static final int DUP = 89;
	public static final int IADD = 96;
	public static final int LADD = 97;
	public static final int ISUB = 100;
	public static final int IINC = 132;
	public static final int I2B = 145;
	public static final int I2C = 146;
	public static final int I2S = 147;
	public static final int LCMP = 148;
	public static final int FCMPL = 149;
	public static final int FCMPG = 150;
	public static final int DCMPL = 151;
	public static final int DCMPG = 152;
	public static final int IFEQ = 153;
	public static final int IFNE = 154;
	public static final int IFLT = 155;
	public static final int IFGE = 156;
	public static final int IFGT = 157;
	public static final int IFLE = 158;
	public static final int IF_ICMPEQ = 159;
	public static final int IF_ICMPNE = 160;
	public static final int IF_ICMPLT = 161;
	public static final int IF_ICMPGE = 162;
	public static final int IF_ICMPGT = 163;
	public static final int IF_ICMPLE = 164;
	public static final int IF_ACMPEQ = 165;
	public static final int IF_ACMPNE = 166;
	public static final int GOTO = 167;
	public static final int JSR = 168;
	public static final int RET = 169;
	public static final int IRETURN = 172;
	public static final int ARETURN = 176;
	public static final int RETURN = 177;
	public static final int GETSTATIC = 178;
	public static final int PUTSTATIC = 179;
	public static final int GETFIELD = 180;
	public static final int PUTFIELD = 181;
	public static final int INVOKEVIRTUAL = 182;
	public static final int INVOKESPECIAL = 183;
	public static final int INVOKESTATIC = 184;
	public static final int INVOKEINTERFACE = 185;
	public static final int NEW = 187;
	public static final int CHECKCAST = 192;
	public static final int INSTANCEOF = 193;
	public static final int IFNULL = 198;
	public static final int IFNONNULL = 199;
}
