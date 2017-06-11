// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassWriter.java

package com.alibaba.fastjson.asm;


// Referenced classes of package com.alibaba.fastjson.asm:
//			ByteVector, Item, FieldWriter, MethodWriter, 
//			Type, FieldVisitor, MethodVisitor

public class ClassWriter
{

	public static final int COMPUTE_MAXS = 1;
	public static final int COMPUTE_FRAMES = 2;
	static final int ACC_SYNTHETIC_ATTRIBUTE = 0x40000;
	static final int NOARG_INSN = 0;
	static final int SBYTE_INSN = 1;
	static final int SHORT_INSN = 2;
	static final int VAR_INSN = 3;
	static final int IMPLVAR_INSN = 4;
	static final int TYPE_INSN = 5;
	static final int FIELDORMETH_INSN = 6;
	static final int ITFDYNMETH_INSN = 7;
	static final int LABEL_INSN = 8;
	static final int LABELW_INSN = 9;
	static final int LDC_INSN = 10;
	static final int LDCW_INSN = 11;
	static final int IINC_INSN = 12;
	static final int TABL_INSN = 13;
	static final int LOOK_INSN = 14;
	static final int MANA_INSN = 15;
	static final int WIDE_INSN = 16;
	static final byte TYPE[];
	static final int CLASS = 7;
	static final int FIELD = 9;
	static final int METH = 10;
	static final int IMETH = 11;
	static final int STR = 8;
	static final int INT = 3;
	static final int FLOAT = 4;
	static final int LONG = 5;
	static final int DOUBLE = 6;
	static final int NAME_TYPE = 12;
	static final int UTF8 = 1;
	static final int TYPE_NORMAL = 13;
	static final int TYPE_UNINIT = 14;
	static final int TYPE_MERGED = 15;
	int version;
	int index;
	final ByteVector pool;
	Item items[];
	int threshold;
	final Item key;
	final Item key2;
	final Item key3;
	Item typeTable[];
	private int access;
	private int name;
	String thisName;
	private int superName;
	private int interfaceCount;
	private int interfaces[];
	FieldWriter firstField;
	FieldWriter lastField;
	MethodWriter firstMethod;
	MethodWriter lastMethod;

	public ClassWriter()
	{
		this(0);
	}

	private ClassWriter(int flags)
	{
		index = 1;
		pool = new ByteVector();
		items = new Item[256];
		threshold = (int)(0.75D * (double)items.length);
		key = new Item();
		key2 = new Item();
		key3 = new Item();
	}

	public void visit(int version, int access, String name, String superName, String interfaces[])
	{
		this.version = version;
		this.access = access;
		this.name = newClass(name);
		thisName = name;
		this.superName = superName != null ? newClass(superName) : 0;
		if (interfaces != null && interfaces.length > 0)
		{
			interfaceCount = interfaces.length;
			this.interfaces = new int[interfaceCount];
			for (int i = 0; i < interfaceCount; i++)
				this.interfaces[i] = newClass(interfaces[i]);

		}
	}

	public FieldVisitor visitField(int access, String name, String desc)
	{
		return new FieldWriter(this, access, name, desc);
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return new MethodWriter(this, access, name, desc, signature, exceptions);
	}

	public void visitEnd()
	{
	}

	public byte[] toByteArray()
	{
		int size = 24 + 2 * interfaceCount;
		int nbFields = 0;
		for (FieldWriter fb = firstField; fb != null; fb = fb.next)
		{
			nbFields++;
			size += fb.getSize();
		}

		int nbMethods = 0;
		for (MethodWriter mb = firstMethod; mb != null; mb = mb.next)
		{
			nbMethods++;
			size += mb.getSize();
		}

		int attributeCount = 0;
		size += pool.length;
		ByteVector out = new ByteVector(size);
		out.putInt(0xcafebabe).putInt(version);
		out.putShort(index).putByteArray(pool.data, 0, pool.length);
		int mask = 0x60000 | (access & 0x40000) / 64;
		out.putShort(access & ~mask).putShort(name).putShort(superName);
		out.putShort(interfaceCount);
		for (int i = 0; i < interfaceCount; i++)
			out.putShort(interfaces[i]);

		out.putShort(nbFields);
		for (FieldWriter fb = firstField; fb != null; fb = fb.next)
			fb.put(out);

		out.putShort(nbMethods);
		for (MethodWriter mb = firstMethod; mb != null; mb = mb.next)
			mb.put(out);

		out.putShort(attributeCount);
		return out.data;
	}

	Item newConstItem(Object cst)
	{
		if (cst instanceof String)
			return newString((String)cst);
		if (cst instanceof Type)
		{
			Type t = (Type)cst;
			return newClassItem(t.getSort() != 10 ? t.getDescriptor() : t.getInternalName());
		} else
		{
			throw new IllegalArgumentException((new StringBuilder()).append("value ").append(cst).toString());
		}
	}

	public int newUTF8(String value)
	{
		key.set(1, value, null, null);
		Item result = get(key);
		if (result == null)
		{
			pool.putByte(1).putUTF8(value);
			result = new Item(index++, key);
			put(result);
		}
		return result.index;
	}

	Item newClassItem(String value)
	{
		key2.set(7, value, null, null);
		Item result = get(key2);
		if (result == null)
		{
			pool.put12(7, newUTF8(value));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	public int newClass(String value)
	{
		return newClassItem(value).index;
	}

	Item newFieldItem(String owner, String name, String desc)
	{
		key3.set(9, owner, name, desc);
		Item result = get(key3);
		if (result == null)
		{
			put122(9, newClass(owner), newNameType(name, desc));
			result = new Item(index++, key3);
			put(result);
		}
		return result;
	}

	Item newMethodItem(String owner, String name, String desc, boolean itf)
	{
		int type = itf ? 11 : 10;
		key3.set(type, owner, name, desc);
		Item result = get(key3);
		if (result == null)
		{
			put122(type, newClass(owner), newNameType(name, desc));
			result = new Item(index++, key3);
			put(result);
		}
		return result;
	}

	private Item newString(String value)
	{
		key2.set(8, value, null, null);
		Item result = get(key2);
		if (result == null)
		{
			pool.put12(8, newUTF8(value));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	public int newNameType(String name, String desc)
	{
		return newNameTypeItem(name, desc).index;
	}

	Item newNameTypeItem(String name, String desc)
	{
		key2.set(12, name, desc, null);
		Item result = get(key2);
		if (result == null)
		{
			put122(12, newUTF8(name), newUTF8(desc));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	private Item get(Item key)
	{
		Item i;
		for (i = items[key.hashCode % items.length]; i != null && (i.type != key.type || !key.isEqualTo(i)); i = i.next);
		return i;
	}

	private void put(Item i)
	{
		if (this.index > threshold)
		{
			int ll = items.length;
			int nl = ll * 2 + 1;
			Item newItems[] = new Item[nl];
			for (int l = ll - 1; l >= 0; l--)
			{
				Item k;
				for (Item j = items[l]; j != null; j = k)
				{
					int index = j.hashCode % newItems.length;
					k = j.next;
					j.next = newItems[index];
					newItems[index] = j;
				}

			}

			items = newItems;
			threshold = (int)((double)nl * 0.75D);
		}
		int index = i.hashCode % items.length;
		i.next = items[index];
		items[index] = i;
	}

	private void put122(int b, int s1, int s2)
	{
		pool.put12(b, s1).putShort(s2);
	}

	static 
	{
		byte b[] = new byte[220];
		String s = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHHFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
		for (int i = 0; i < b.length; i++)
			b[i] = (byte)(s.charAt(i) - 65);

		TYPE = b;
	}
}
