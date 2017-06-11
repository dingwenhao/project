// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializeWriter.java

package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.CharTypes;
import com.alibaba.fastjson.util.Base64;
import com.alibaba.fastjson.util.IOUtils;
import java.io.*;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.nio.charset.Charset;

// Referenced classes of package com.alibaba.fastjson.serializer:
//			SerialWriterStringEncoder, SerializerFeature

public final class SerializeWriter extends Writer
{

	protected char buf[];
	protected int count;
	private static final ThreadLocal bufLocal = new ThreadLocal();
	private int features;

	public SerializeWriter()
	{
		features = JSON.DEFAULT_GENERATE_FEATURE;
		SoftReference ref = (SoftReference)bufLocal.get();
		if (ref != null)
		{
			buf = (char[])ref.get();
			bufLocal.set(null);
		}
		if (buf == null)
			buf = new char[1024];
	}

	public transient SerializeWriter(SerializerFeature features[])
	{
		SoftReference ref = (SoftReference)bufLocal.get();
		if (ref != null)
		{
			buf = (char[])ref.get();
			bufLocal.set(null);
		}
		if (buf == null)
			buf = new char[1024];
		int featuresValue = 0;
		SerializerFeature arr$[] = features;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; i$++)
		{
			SerializerFeature feature = arr$[i$];
			featuresValue |= feature.getMask();
		}

		this.features = featuresValue;
	}

	public SerializeWriter(int initialSize)
	{
		if (initialSize <= 0)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Negative initial size: ").append(initialSize).toString());
		} else
		{
			buf = new char[initialSize];
			return;
		}
	}

	public void config(SerializerFeature feature, boolean state)
	{
		if (state)
			features |= feature.getMask();
		else
			features &= ~feature.getMask();
	}

	public boolean isEnabled(SerializerFeature feature)
	{
		return SerializerFeature.isEnabled(features, feature);
	}

	public void write(int c)
	{
		int newcount = count + 1;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = (char)c;
		count = newcount;
	}

	public void write(char c)
	{
		int newcount = count + 1;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = c;
		count = newcount;
	}

	public void write(char c[], int off, int len)
	{
		if (off < 0 || off > c.length || len < 0 || off + len > c.length || off + len < 0)
			throw new IndexOutOfBoundsException();
		if (len == 0)
			return;
		int newcount = count + len;
		if (newcount > buf.length)
			expandCapacity(newcount);
		System.arraycopy(c, off, buf, count, len);
		count = newcount;
	}

	public void expandCapacity(int minimumCapacity)
	{
		int newCapacity = (buf.length * 3) / 2 + 1;
		if (newCapacity < minimumCapacity)
			newCapacity = minimumCapacity;
		char newValue[] = new char[newCapacity];
		System.arraycopy(buf, 0, newValue, 0, count);
		buf = newValue;
	}

	public void write(String str, int off, int len)
	{
		int newcount = count + len;
		if (newcount > buf.length)
			expandCapacity(newcount);
		str.getChars(off, off + len, buf, count);
		count = newcount;
	}

	public void writeTo(Writer out)
		throws IOException
	{
		out.write(buf, 0, count);
	}

	public void writeTo(OutputStream out, String charset)
		throws IOException
	{
		byte bytes[] = (new String(buf, 0, count)).getBytes(charset);
		out.write(bytes);
	}

	public void writeTo(OutputStream out, Charset charset)
		throws IOException
	{
		byte bytes[] = (new String(buf, 0, count)).getBytes(charset);
		out.write(bytes);
	}

	public SerializeWriter append(CharSequence csq)
	{
		String s = csq != null ? csq.toString() : "null";
		write(s, 0, s.length());
		return this;
	}

	public SerializeWriter append(CharSequence csq, int start, int end)
	{
		String s = ((CharSequence) (csq != null ? csq : "null")).subSequence(start, end).toString();
		write(s, 0, s.length());
		return this;
	}

	public SerializeWriter append(char c)
	{
		write(c);
		return this;
	}

	public void reset()
	{
		count = 0;
	}

	public char[] toCharArray()
	{
		char newValue[] = new char[count];
		System.arraycopy(buf, 0, newValue, 0, count);
		return newValue;
	}

	public byte[] toBytes(String charsetName)
	{
		if (charsetName == null)
			charsetName = "UTF-8";
		Charset cs = Charset.forName(charsetName);
		SerialWriterStringEncoder encoder = new SerialWriterStringEncoder(cs);
		return encoder.encode(buf, 0, count);
	}

	public int size()
	{
		return count;
	}

	public String toString()
	{
		return new String(buf, 0, count);
	}

	public void flush()
	{
	}

	public void close()
	{
		bufLocal.set(new SoftReference(buf));
		buf = null;
	}

	public void writeBooleanArray(boolean array[])
		throws IOException
	{
		int sizeArray[] = new int[array.length];
		int totalSize = 2;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				totalSize++;
			boolean val = array[i];
			int size;
			if (val)
				size = 4;
			else
				size = 5;
			sizeArray[i] = size;
			totalSize += size;
		}

		int newcount = count + totalSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = '[';
		int currentSize = count + 1;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				buf[currentSize++] = ',';
			boolean val = array[i];
			if (val)
			{
				buf[currentSize++] = 't';
				buf[currentSize++] = 'r';
				buf[currentSize++] = 'u';
				buf[currentSize++] = 'e';
			} else
			{
				buf[currentSize++] = 'f';
				buf[currentSize++] = 'a';
				buf[currentSize++] = 'l';
				buf[currentSize++] = 's';
				buf[currentSize++] = 'e';
			}
		}

		buf[currentSize] = ']';
		count = newcount;
	}

	public void write(String text)
	{
		if (text == null)
		{
			writeNull();
			return;
		}
		int length = text.length();
		int newcount = count + length;
		if (newcount > buf.length)
			expandCapacity(newcount);
		text.getChars(0, length, buf, count);
		count = newcount;
	}

	public void writeInt(int i)
	{
		if (i == 0x80000000)
		{
			write("-2147483648");
			return;
		}
		int size = i >= 0 ? IOUtils.stringSize(i) : IOUtils.stringSize(-i) + 1;
		int newcount = count + size;
		if (newcount > buf.length)
			expandCapacity(newcount);
		IOUtils.getChars(i, newcount, buf);
		count = newcount;
	}

	public void writeShortArray(short array[])
		throws IOException
	{
		int sizeArray[] = new int[array.length];
		int totalSize = 2;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				totalSize++;
			short val = array[i];
			int size = IOUtils.stringSize(val);
			sizeArray[i] = size;
			totalSize += size;
		}

		int newcount = count + totalSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = '[';
		int currentSize = count + 1;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				buf[currentSize++] = ',';
			short val = array[i];
			currentSize += sizeArray[i];
			IOUtils.getChars(val, currentSize, buf);
		}

		buf[currentSize] = ']';
		count = newcount;
	}

	public void writeByteArray(byte bytes[])
	{
		int bytesLen = bytes.length;
		if (bytesLen == 0)
		{
			write("\"\"");
			return;
		}
		char CA[] = Base64.CA;
		int eLen = (bytesLen / 3) * 3;
		int charsLen = (bytesLen - 1) / 3 + 1 << 2;
		int offset = count;
		int newcount = count + charsLen + 2;
		if (newcount > buf.length)
			expandCapacity(newcount);
		count = newcount;
		buf[offset++] = '"';
		int s = 0;
		int d = offset;
		while (s < eLen) 
		{
			int i = (bytes[s++] & 0xff) << 16 | (bytes[s++] & 0xff) << 8 | bytes[s++] & 0xff;
			buf[d++] = CA[i >>> 18 & 0x3f];
			buf[d++] = CA[i >>> 12 & 0x3f];
			buf[d++] = CA[i >>> 6 & 0x3f];
			buf[d++] = CA[i & 0x3f];
		}
		int left = bytesLen - eLen;
		if (left > 0)
		{
			int i = (bytes[eLen] & 0xff) << 10 | (left != 2 ? 0 : (bytes[bytesLen - 1] & 0xff) << 2);
			buf[newcount - 5] = CA[i >> 12];
			buf[newcount - 4] = CA[i >>> 6 & 0x3f];
			buf[newcount - 3] = left != 2 ? '=' : CA[i & 0x3f];
			buf[newcount - 2] = '=';
		}
		buf[newcount - 1] = '"';
	}

	public void writeIntArray(int array[])
	{
		int sizeArray[] = new int[array.length];
		int totalSize = 2;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				totalSize++;
			int val = array[i];
			int size;
			if (val == 0x80000000)
				size = "-2147483648".length();
			else
				size = val >= 0 ? IOUtils.stringSize(val) : IOUtils.stringSize(-val) + 1;
			sizeArray[i] = size;
			totalSize += size;
		}

		int newcount = count + totalSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = '[';
		int currentSize = count + 1;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				buf[currentSize++] = ',';
			int val = array[i];
			if (val == 0x80000000)
			{
				System.arraycopy("-2147483648".toCharArray(), 0, buf, currentSize, sizeArray[i]);
				currentSize += sizeArray[i];
			} else
			{
				currentSize += sizeArray[i];
				IOUtils.getChars(val, currentSize, buf);
			}
		}

		buf[currentSize] = ']';
		count = newcount;
	}

	public void writeIntAndChar(int i, char c)
	{
		if (i == 0x80000000)
		{
			write("-2147483648");
			write(c);
			return;
		}
		int size = i >= 0 ? IOUtils.stringSize(i) : IOUtils.stringSize(-i) + 1;
		int newcount0 = count + size;
		int newcount1 = newcount0 + 1;
		if (newcount1 > buf.length)
			expandCapacity(newcount1);
		IOUtils.getChars(i, newcount0, buf);
		buf[newcount0] = c;
		count = newcount1;
	}

	public void writeLongAndChar(long i, char c)
		throws IOException
	{
		if (i == 0x8000000000000000L)
		{
			write("-9223372036854775808");
			write(c);
			return;
		}
		int size = i >= 0L ? IOUtils.stringSize(i) : IOUtils.stringSize(-i) + 1;
		int newcount0 = count + size;
		int newcount1 = newcount0 + 1;
		if (newcount1 > buf.length)
			expandCapacity(newcount1);
		IOUtils.getChars(i, newcount0, buf);
		buf[newcount0] = c;
		count = newcount1;
	}

	public void writeLong(long i)
	{
		if (i == 0x8000000000000000L)
		{
			write("-9223372036854775808");
			return;
		}
		int size = i >= 0L ? IOUtils.stringSize(i) : IOUtils.stringSize(-i) + 1;
		int newcount = count + size;
		if (newcount > buf.length)
			expandCapacity(newcount);
		IOUtils.getChars(i, newcount, buf);
		count = newcount;
	}

	public void writeNull()
	{
		int newcount = count + 4;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = 'n';
		buf[count + 1] = 'u';
		buf[count + 2] = 'l';
		buf[count + 3] = 'l';
		count = newcount;
	}

	public void writeLongArray(long array[])
	{
		int sizeArray[] = new int[array.length];
		int totalSize = 2;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				totalSize++;
			long val = array[i];
			int size;
			if (val == 0x8000000000000000L)
				size = "-9223372036854775808".length();
			else
				size = val >= 0L ? IOUtils.stringSize(val) : IOUtils.stringSize(-val) + 1;
			sizeArray[i] = size;
			totalSize += size;
		}

		int newcount = count + totalSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = '[';
		int currentSize = count + 1;
		for (int i = 0; i < array.length; i++)
		{
			if (i != 0)
				buf[currentSize++] = ',';
			long val = array[i];
			if (val == 0x8000000000000000L)
			{
				System.arraycopy("-9223372036854775808".toCharArray(), 0, buf, currentSize, sizeArray[i]);
				currentSize += sizeArray[i];
			} else
			{
				currentSize += sizeArray[i];
				IOUtils.getChars(val, currentSize, buf);
			}
		}

		buf[currentSize] = ']';
		count = newcount;
	}

	private void writeStringWithDoubleQuote(String text, char seperator)
	{
		if (text == null)
		{
			writeNull();
			return;
		}
		int len = text.length();
		int newcount = count + len + 2;
		if (seperator != 0)
			newcount++;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count + 1;
		int end = start + len;
		buf[count] = '"';
		text.getChars(0, len, buf, start);
		count = newcount;
		if (isEnabled(SerializerFeature.BrowserCompatible))
		{
			int lastSpecialIndex = -1;
			for (int i = start; i < end; i++)
			{
				char ch = buf[i];
				if (ch == '"' || ch == '/' || ch == '\\')
				{
					lastSpecialIndex = i;
					newcount++;
					continue;
				}
				if (ch == '\b' || ch == '\f' || ch == '\n' || ch == '\r' || ch == '\t')
				{
					lastSpecialIndex = i;
					newcount++;
					continue;
				}
				if (ch < ' ')
				{
					lastSpecialIndex = i;
					newcount += 5;
					continue;
				}
				if (CharTypes.isEmoji(ch))
				{
					lastSpecialIndex = i;
					newcount += 5;
				}
			}

			if (newcount > buf.length)
				expandCapacity(newcount);
			count = newcount;
			for (int i = lastSpecialIndex; i >= start; i--)
			{
				char ch = buf[i];
				if (ch == '\b' || ch == '\f' || ch == '\n' || ch == '\r' || ch == '\t')
				{
					System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
					buf[i] = '\\';
					buf[i + 1] = CharTypes.replaceChars[ch];
					end++;
					continue;
				}
				if (ch == '"' || ch == '/' || ch == '\\')
				{
					System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
					buf[i] = '\\';
					buf[i + 1] = ch;
					end++;
					continue;
				}
				if (ch < ' ')
				{
					System.arraycopy(buf, i + 1, buf, i + 6, end - i - 1);
					buf[i] = '\\';
					buf[i + 1] = 'u';
					buf[i + 2] = '0';
					buf[i + 3] = '0';
					buf[i + 4] = CharTypes.ASCII_CHARS[ch * 2];
					buf[i + 5] = CharTypes.ASCII_CHARS[ch * 2 + 1];
					end += 5;
					continue;
				}
				if (CharTypes.isEmoji(ch))
				{
					System.arraycopy(buf, i + 1, buf, i + 6, end - i - 1);
					buf[i] = '\\';
					buf[i + 1] = 'u';
					buf[i + 2] = CharTypes.digits[ch >>> 12 & 0xf];
					buf[i + 3] = CharTypes.digits[ch >>> 8 & 0xf];
					buf[i + 4] = CharTypes.digits[ch >>> 4 & 0xf];
					buf[i + 5] = CharTypes.digits[ch & 0xf];
					end += 5;
				}
			}

			if (seperator != 0)
			{
				buf[count - 2] = '"';
				buf[count - 1] = seperator;
			} else
			{
				buf[count - 1] = '"';
			}
			return;
		}
		int specialCount = 0;
		int lastSpecialIndex = -1;
		char lastSpecial = '\0';
		for (int i = start; i < end; i++)
		{
			char ch = buf[i];
			if (ch < CharTypes.specicalFlags_doubleQuotes.length && CharTypes.specicalFlags_doubleQuotes[ch] || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
			{
				specialCount++;
				lastSpecialIndex = i;
				lastSpecial = ch;
			}
		}

		newcount += specialCount;
		if (newcount > buf.length)
			expandCapacity(newcount);
		count = newcount;
		if (specialCount == 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, end - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
		} else
		if (specialCount > 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, end - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
			end++;
			for (int i = lastSpecialIndex - 2; i >= start; i--)
			{
				char ch = buf[i];
				if (ch < CharTypes.specicalFlags_doubleQuotes.length && CharTypes.specicalFlags_doubleQuotes[ch] || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
				{
					System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
					buf[i] = '\\';
					buf[i + 1] = CharTypes.replaceChars[ch];
					end++;
				}
			}

		}
		if (seperator != 0)
		{
			buf[count - 2] = '"';
			buf[count - 1] = seperator;
		} else
		{
			buf[count - 1] = '"';
		}
	}

	public void writeKeyWithDoubleQuote(String text)
	{
		writeKeyWithDoubleQuote(text, true);
	}

	public void writeKeyWithDoubleQuote(String text, boolean checkSpecial)
	{
		boolean specicalFlags_doubleQuotes[] = CharTypes.specicalFlags_doubleQuotes;
		int len = text.length();
		int newcount = count + len + 3;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count + 1;
		int end = start + len;
		buf[count] = '"';
		text.getChars(0, len, buf, start);
		count = newcount;
		if (checkSpecial)
		{
			for (int i = start; i < end; i++)
			{
				char ch = buf[i];
				if ((ch >= specicalFlags_doubleQuotes.length || !specicalFlags_doubleQuotes[ch]) && (ch != '\t' || !isEnabled(SerializerFeature.WriteTabAsSpecial)) && (ch != '/' || !isEnabled(SerializerFeature.WriteSlashAsSpecial)))
					continue;
				if (++newcount > buf.length)
					expandCapacity(newcount);
				count = newcount;
				System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
				buf[i] = '\\';
				buf[++i] = CharTypes.replaceChars[ch];
				end++;
			}

		}
		buf[count - 2] = '"';
		buf[count - 1] = ':';
	}

	public void writeFieldNull(char seperator, String name)
	{
		write(seperator);
		writeFieldName(name);
		writeNull();
	}

	public void writeFieldEmptyList(char seperator, String key)
	{
		write(seperator);
		writeFieldName(key);
		write("[]");
	}

	public void writeFieldNullString(char seperator, String name)
	{
		write(seperator);
		writeFieldName(name);
		if (isEnabled(SerializerFeature.WriteNullStringAsEmpty))
			writeString("");
		else
			writeNull();
	}

	public void writeFieldNullBoolean(char seperator, String name)
	{
		write(seperator);
		writeFieldName(name);
		if (isEnabled(SerializerFeature.WriteNullBooleanAsFalse))
			write("false");
		else
			writeNull();
	}

	public void writeFieldNullList(char seperator, String name)
	{
		write(seperator);
		writeFieldName(name);
		if (isEnabled(SerializerFeature.WriteNullListAsEmpty))
			write("[]");
		else
			writeNull();
	}

	public void writeFieldNullNumber(char seperator, String name)
	{
		write(seperator);
		writeFieldName(name);
		if (isEnabled(SerializerFeature.WriteNullNumberAsZero))
			write('0');
		else
			writeNull();
	}

	public void writeFieldValue(char seperator, String name, char value)
	{
		write(seperator);
		writeFieldName(name);
		if (value == 0)
			writeString("\0");
		else
			writeString(Character.toString(value));
	}

	public void writeFieldValue(char seperator, String name, boolean value)
	{
		char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '"';
		int intSize = value ? 4 : 5;
		int nameLen = name.length();
		int newcount = count + nameLen + 4 + intSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count;
		count = newcount;
		buf[start] = seperator;
		int nameEnd = start + nameLen + 1;
		buf[start + 1] = keySeperator;
		name.getChars(0, nameLen, buf, start + 2);
		buf[nameEnd + 1] = keySeperator;
		if (value)
			System.arraycopy(":true".toCharArray(), 0, buf, nameEnd + 2, 5);
		else
			System.arraycopy(":false".toCharArray(), 0, buf, nameEnd + 2, 6);
	}

	public void writeFieldValue1(char seperator, String name, boolean value)
	{
		write(seperator);
		writeFieldName(name);
		if (value)
			write("true");
		else
			write("false");
	}

	public void writeFieldValue(char seperator, String name, int value)
	{
		if (value == 0x80000000 || !isEnabled(SerializerFeature.QuoteFieldNames))
		{
			writeFieldValue1(seperator, name, value);
			return;
		}
		char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '"';
		int intSize = value >= 0 ? IOUtils.stringSize(value) : IOUtils.stringSize(-value) + 1;
		int nameLen = name.length();
		int newcount = count + nameLen + 4 + intSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count;
		count = newcount;
		buf[start] = seperator;
		int nameEnd = start + nameLen + 1;
		buf[start + 1] = keySeperator;
		name.getChars(0, nameLen, buf, start + 2);
		buf[nameEnd + 1] = keySeperator;
		buf[nameEnd + 2] = ':';
		IOUtils.getChars(value, count, buf);
	}

	public void writeFieldValue1(char seperator, String name, int value)
	{
		write(seperator);
		writeFieldName(name);
		writeInt(value);
	}

	public void writeFieldValue(char seperator, String name, long value)
	{
		if (value == 0x8000000000000000L || !isEnabled(SerializerFeature.QuoteFieldNames))
		{
			writeFieldValue1(seperator, name, value);
			return;
		}
		char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '"';
		int intSize = value >= 0L ? IOUtils.stringSize(value) : IOUtils.stringSize(-value) + 1;
		int nameLen = name.length();
		int newcount = count + nameLen + 4 + intSize;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count;
		count = newcount;
		buf[start] = seperator;
		int nameEnd = start + nameLen + 1;
		buf[start + 1] = keySeperator;
		name.getChars(0, nameLen, buf, start + 2);
		buf[nameEnd + 1] = keySeperator;
		buf[nameEnd + 2] = ':';
		IOUtils.getChars(value, count, buf);
	}

	public void writeFieldValue1(char seperator, String name, long value)
	{
		write(seperator);
		writeFieldName(name);
		writeLong(value);
	}

	public void writeFieldValue(char seperator, String name, float value)
	{
		write(seperator);
		writeFieldName(name);
		if (value == 0.0F)
			write('0');
		else
		if (Float.isNaN(value))
			writeNull();
		else
		if (Float.isInfinite(value))
		{
			writeNull();
		} else
		{
			String text = Float.toString(value);
			if (text.endsWith(".0"))
				text = text.substring(0, text.length() - 2);
			write(text);
		}
	}

	public void writeFieldValue(char seperator, String name, double value)
	{
		write(seperator);
		writeFieldName(name);
		if (value == 0.0D)
			write('0');
		else
		if (Double.isNaN(value))
			writeNull();
		else
		if (Double.isInfinite(value))
		{
			writeNull();
		} else
		{
			String text = Double.toString(value);
			if (text.endsWith(".0"))
				text = text.substring(0, text.length() - 2);
			write(text);
		}
	}

	public void writeFieldValue(char seperator, String name, String value)
	{
		if (isEnabled(SerializerFeature.QuoteFieldNames))
		{
			if (isEnabled(SerializerFeature.UseSingleQuotes))
			{
				write(seperator);
				writeFieldName(name);
				if (value == null)
					writeNull();
				else
					writeString(value);
			} else
			if (isEnabled(SerializerFeature.BrowserCompatible))
			{
				write(seperator);
				writeStringWithDoubleQuote(name, ':');
				writeStringWithDoubleQuote(value, '\0');
			} else
			{
				writeFieldValueStringWithDoubleQuote(seperator, name, value);
			}
		} else
		{
			write(seperator);
			writeFieldName(name);
			if (value == null)
				writeNull();
			else
				writeString(value);
		}
	}

	private void writeFieldValueStringWithDoubleQuote(char seperator, String name, String value)
	{
		int nameLen = name.length();
		int newcount = count;
		int valueLen;
		if (value == null)
		{
			valueLen = 4;
			newcount += nameLen + 8;
		} else
		{
			valueLen = value.length();
			newcount += nameLen + valueLen + 6;
		}
		if (newcount > buf.length)
			expandCapacity(newcount);
		buf[count] = seperator;
		int nameStart = count + 2;
		int nameEnd = nameStart + nameLen;
		buf[count + 1] = '"';
		name.getChars(0, nameLen, buf, nameStart);
		count = newcount;
		int specialCount = 0;
		int lastSpecialIndex = -1;
		char lastSpecial = '\0';
		for (int i = nameStart; i < nameEnd; i++)
		{
			char ch = buf[i];
			if (ch == '\b' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\\' || ch == '"' || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
			{
				specialCount++;
				lastSpecialIndex = i;
				lastSpecial = ch;
			}
		}

		if (specialCount > 0)
		{
			newcount += specialCount;
			if (newcount > buf.length)
				expandCapacity(newcount);
			count = newcount;
		}
		if (specialCount == 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, nameEnd - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
			nameEnd++;
		} else
		if (specialCount > 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, nameEnd - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
			nameEnd++;
			for (int i = lastSpecialIndex - 2; i >= nameStart; i--)
			{
				char ch = buf[i];
				if (ch == '\b' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\\' || ch == '"' || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
				{
					System.arraycopy(buf, i + 1, buf, i + 2, nameEnd - i - 1);
					buf[i] = '\\';
					buf[i + 1] = CharTypes.replaceChars[ch];
					nameEnd++;
				}
			}

		}
		buf[nameEnd] = '"';
		int index = nameEnd + 1;
		buf[index++] = ':';
		if (value == null)
		{
			buf[index++] = 'n';
			buf[index++] = 'u';
			buf[index++] = 'l';
			buf[index++] = 'l';
			return;
		}
		buf[index++] = '"';
		int valueStart = index;
		int valueEnd = valueStart + valueLen;
		value.getChars(0, valueLen, buf, valueStart);
		specialCount = 0;
		lastSpecialIndex = -1;
		lastSpecial = '\0';
		for (int i = valueStart; i < valueEnd; i++)
		{
			char ch = buf[i];
			if (ch == '\b' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\\' || ch == '"' || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
			{
				specialCount++;
				lastSpecialIndex = i;
				lastSpecial = ch;
			}
		}

		if (specialCount > 0)
		{
			newcount += specialCount;
			if (newcount > buf.length)
				expandCapacity(newcount);
			count = newcount;
		}
		if (specialCount == 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, valueEnd - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
		} else
		if (specialCount > 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, valueEnd - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
			valueEnd++;
			for (int i = lastSpecialIndex - 2; i >= valueStart; i--)
			{
				char ch = buf[i];
				if (ch == '\b' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\\' || ch == '"' || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
				{
					System.arraycopy(buf, i + 1, buf, i + 2, valueEnd - i - 1);
					buf[i] = '\\';
					buf[i + 1] = CharTypes.replaceChars[ch];
					valueEnd++;
				}
			}

		}
		buf[count - 1] = '"';
	}

	public void writeFieldValue(char seperator, String name, Enum value)
	{
		if (value == null)
		{
			write(seperator);
			writeFieldName(name);
			writeNull();
			return;
		}
		if (isEnabled(SerializerFeature.WriteEnumUsingToString))
			writeFieldValue(seperator, name, value.name());
		else
			writeFieldValue(seperator, name, value.ordinal());
	}

	public void writeFieldValue(char seperator, String name, BigDecimal value)
	{
		write(seperator);
		writeFieldName(name);
		if (value == null)
			writeNull();
		else
			write(value.toString());
	}

	public void writeString(String text, char seperator)
	{
		if (isEnabled(SerializerFeature.UseSingleQuotes))
		{
			writeStringWithSingleQuote(text);
			write(seperator);
		} else
		{
			writeStringWithDoubleQuote(text, seperator);
		}
	}

	public void writeString(String text)
	{
		if (isEnabled(SerializerFeature.UseSingleQuotes))
			writeStringWithSingleQuote(text);
		else
			writeStringWithDoubleQuote(text, '\0');
	}

	private void writeStringWithSingleQuote(String text)
	{
		if (text == null)
		{
			int newcount = count + 4;
			if (newcount > buf.length)
				expandCapacity(newcount);
			"null".getChars(0, 4, buf, count);
			count = newcount;
			return;
		}
		int len = text.length();
		int newcount = count + len + 2;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count + 1;
		int end = start + len;
		buf[count] = '\'';
		text.getChars(0, len, buf, start);
		count = newcount;
		int specialCount = 0;
		int lastSpecialIndex = -1;
		char lastSpecial = '\0';
		for (int i = start; i < end; i++)
		{
			char ch = buf[i];
			if (ch == '\b' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\\' || ch == '\'' || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
			{
				specialCount++;
				lastSpecialIndex = i;
				lastSpecial = ch;
			}
		}

		newcount += specialCount;
		if (newcount > buf.length)
			expandCapacity(newcount);
		count = newcount;
		if (specialCount == 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, end - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
		} else
		if (specialCount > 1)
		{
			System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, end - lastSpecialIndex - 1);
			buf[lastSpecialIndex] = '\\';
			buf[++lastSpecialIndex] = CharTypes.replaceChars[lastSpecial];
			end++;
			for (int i = lastSpecialIndex - 2; i >= start; i--)
			{
				char ch = buf[i];
				if (ch == '\b' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\\' || ch == '\'' || ch == '\t' && isEnabled(SerializerFeature.WriteTabAsSpecial) || ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))
				{
					System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
					buf[i] = '\\';
					buf[i + 1] = CharTypes.replaceChars[ch];
					end++;
				}
			}

		}
		buf[count - 1] = '\'';
	}

	public void writeFieldName(String key)
	{
		writeFieldName(key, false);
	}

	public void writeFieldName(String key, boolean checkSpecial)
	{
		if (key == null)
		{
			write("null:");
			return;
		}
		if (isEnabled(SerializerFeature.UseSingleQuotes))
		{
			if (isEnabled(SerializerFeature.QuoteFieldNames))
				writeKeyWithSingleQuote(key);
			else
				writeKeyWithSingleQuoteIfHasSpecial(key);
		} else
		if (isEnabled(SerializerFeature.QuoteFieldNames))
			writeKeyWithDoubleQuote(key, checkSpecial);
		else
			writeKeyWithDoubleQuoteIfHasSpecial(key);
	}

	private void writeKeyWithSingleQuote(String text)
	{
		boolean specicalFlags_singleQuotes[] = CharTypes.specicalFlags_singleQuotes;
		int len = text.length();
		int newcount = count + len + 3;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count + 1;
		int end = start + len;
		buf[count] = '\'';
		text.getChars(0, len, buf, start);
		count = newcount;
		for (int i = start; i < end; i++)
		{
			char ch = buf[i];
			if ((ch >= specicalFlags_singleQuotes.length || !specicalFlags_singleQuotes[ch]) && (ch != '\t' || !isEnabled(SerializerFeature.WriteTabAsSpecial)) && (ch != '/' || !isEnabled(SerializerFeature.WriteSlashAsSpecial)))
				continue;
			if (++newcount > buf.length)
				expandCapacity(newcount);
			count = newcount;
			System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
			buf[i] = '\\';
			buf[++i] = CharTypes.replaceChars[ch];
			end++;
		}

		buf[count - 2] = '\'';
		buf[count - 1] = ':';
	}

	private void writeKeyWithDoubleQuoteIfHasSpecial(String text)
	{
		boolean specicalFlags_doubleQuotes[] = CharTypes.specicalFlags_doubleQuotes;
		int len = text.length();
		int newcount = count + len + 1;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count;
		int end = start + len;
		text.getChars(0, len, buf, start);
		count = newcount;
		boolean hasSpecial = false;
		for (int i = start; i < end; i++)
		{
			char ch = buf[i];
			if (ch >= specicalFlags_doubleQuotes.length || !specicalFlags_doubleQuotes[ch])
				continue;
			if (!hasSpecial)
			{
				if ((newcount += 3) > buf.length)
					expandCapacity(newcount);
				count = newcount;
				System.arraycopy(buf, i + 1, buf, i + 3, end - i - 1);
				System.arraycopy(buf, 0, buf, 1, i);
				buf[start] = '"';
				buf[++i] = '\\';
				buf[++i] = CharTypes.replaceChars[ch];
				end += 2;
				buf[count - 2] = '"';
				hasSpecial = true;
				continue;
			}
			if (++newcount > buf.length)
				expandCapacity(newcount);
			count = newcount;
			System.arraycopy(buf, i + 1, buf, i + 2, end - i);
			buf[i] = '\\';
			buf[++i] = CharTypes.replaceChars[ch];
			end++;
		}

		buf[count - 1] = ':';
	}

	private void writeKeyWithSingleQuoteIfHasSpecial(String text)
	{
		boolean specicalFlags_singleQuotes[] = CharTypes.specicalFlags_singleQuotes;
		int len = text.length();
		int newcount = count + len + 1;
		if (newcount > buf.length)
			expandCapacity(newcount);
		int start = count;
		int end = start + len;
		text.getChars(0, len, buf, start);
		count = newcount;
		boolean hasSpecial = false;
		for (int i = start; i < end; i++)
		{
			char ch = buf[i];
			if (ch >= specicalFlags_singleQuotes.length || !specicalFlags_singleQuotes[ch])
				continue;
			if (!hasSpecial)
			{
				if ((newcount += 3) > buf.length)
					expandCapacity(newcount);
				count = newcount;
				System.arraycopy(buf, i + 1, buf, i + 3, end - i - 1);
				System.arraycopy(buf, 0, buf, 1, i);
				buf[start] = '\'';
				buf[++i] = '\\';
				buf[++i] = CharTypes.replaceChars[ch];
				end += 2;
				buf[count - 2] = '\'';
				hasSpecial = true;
				continue;
			}
			if (++newcount > buf.length)
				expandCapacity(newcount);
			count = newcount;
			System.arraycopy(buf, i + 1, buf, i + 2, end - i);
			buf[i] = '\\';
			buf[++i] = CharTypes.replaceChars[ch];
			end++;
		}

		buf[newcount - 1] = ':';
	}

	public volatile Writer append(char x0)
		throws IOException
	{
		return append(x0);
	}

	public volatile Writer append(CharSequence x0, int x1, int x2)
		throws IOException
	{
		return append(x0, x1, x2);
	}

	public volatile Writer append(CharSequence x0)
		throws IOException
	{
		return append(x0);
	}

	public volatile Appendable append(char x0)
		throws IOException
	{
		return append(x0);
	}

	public volatile Appendable append(CharSequence x0, int x1, int x2)
		throws IOException
	{
		return append(x0, x1, x2);
	}

	public volatile Appendable append(CharSequence x0)
		throws IOException
	{
		return append(x0);
	}

}
