// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONLexer.java

package com.alibaba.fastjson.parser;

import java.math.BigDecimal;
import java.util.Calendar;

// Referenced classes of package com.alibaba.fastjson.parser:
//			Feature, SymbolTable

public interface JSONLexer
{

	public abstract boolean isResetFlag();

	public abstract void setResetFlag(boolean flag);

	public abstract void nextToken();

	public abstract void nextToken(int i);

	public abstract int token();

	public abstract String tokenName();

	public abstract int pos();

	public abstract String stringVal();

	public abstract Number integerValue();

	public abstract void nextTokenWithColon(int i);

	public abstract BigDecimal decimalValue();

	public abstract Number decimalValue(boolean flag);

	public abstract double doubleValue();

	public abstract float floatValue();

	public abstract void config(Feature feature, boolean flag);

	public abstract boolean isEnabled(Feature feature);

	public abstract String numberString();

	public abstract boolean isEOF();

	public abstract String symbol(SymbolTable symboltable);

	public abstract boolean isBlankInput();

	public abstract char getCurrent();

	public abstract void skipWhitespace();

	public abstract void incrementBufferPosition();

	public abstract String scanSymbol(SymbolTable symboltable);

	public abstract String scanSymbol(SymbolTable symboltable, char c);

	public abstract void resetStringPosition();

	public abstract String scanSymbolUnQuoted(SymbolTable symboltable);

	public abstract void scanString();

	public abstract void scanNumber();

	public abstract boolean scanISO8601DateIfMatch();

	public abstract Calendar getCalendar();

	public abstract int intValue()
		throws NumberFormatException;

	public abstract long longValue()
		throws NumberFormatException;

	public abstract byte[] bytesValue();
}
