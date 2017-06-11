// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   JSONScanner.java

package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

// Referenced classes of package com.alibaba.fastjson.parser:
//			JSONLexer, Keywords, JSONToken, Feature, 
//			CharTypes, SymbolTable

public class JSONScanner
	implements JSONLexer
{

	public static final byte EOI = 26;
	private final char buf[];
	private int bp;
	private final int buflen;
	private int eofPos;
	private char ch;
	private int pos;
	private char sbuf[];
	private int sp;
	private int np;
	private int token;
	private Keywords keywods;
	private static final ThreadLocal sbufRef = new ThreadLocal();
	private int features;
	private Calendar calendar;
	private boolean resetFlag;
	private static boolean whitespaceFlags[];
	boolean hasSpecial;
	public static final int NOT_MATCH = -1;
	public static final int NOT_MATCH_NAME = -2;
	public static final int UNKOWN = 0;
	public static final int OBJECT = 1;
	public static final int ARRAY = 2;
	public static final int VALUE = 3;
	public static final int END = 4;
	private static final char typeFieldName[] = "\"@type\":\"".toCharArray();
	public int matchStat;
	private static final long MULTMIN_RADIX_TEN = 0xf333333333333334L;
	private static final long N_MULTMAX_RADIX_TEN = 0xf333333333333334L;
	private static final int INT_MULTMIN_RADIX_TEN = 0xf3333334;
	private static final int INT_N_MULTMAX_RADIX_TEN = 0xf3333334;
	private static final int digits[];
	public final int ISO8601_LEN_0;
	public final int ISO8601_LEN_1;
	public final int ISO8601_LEN_2;

	public JSONScanner(String input)
	{
		this(input, JSON.DEFAULT_PARSER_FEATURE);
	}

	public JSONScanner(String input, int features)
	{
		this(input.toCharArray(), input.length(), features);
	}

	public JSONScanner(char input[], int inputLength)
	{
		this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
	}

	public JSONScanner(char input[], int inputLength, int features)
	{
		keywods = Keywords.DEFAULT_KEYWORDS;
		this.features = JSON.DEFAULT_PARSER_FEATURE;
		calendar = null;
		resetFlag = false;
		matchStat = 0;
		ISO8601_LEN_0 = "0000-00-00".length();
		ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
		ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();
		this.features = features;
		sbuf = (char[])sbufRef.get();
		if (sbuf == null)
		{
			sbuf = new char[64];
			sbufRef.set(sbuf);
		}
		eofPos = inputLength;
		if (inputLength == input.length)
			if (input.length > 0 && isWhitespace(input[input.length - 1]))
			{
				inputLength--;
			} else
			{
				char newInput[] = new char[inputLength + 1];
				System.arraycopy(input, 0, newInput, 0, input.length);
				input = newInput;
			}
		buf = input;
		buflen = inputLength;
		buf[buflen] = '\032';
		bp = -1;
		ch = buf[++bp];
	}

	public boolean isResetFlag()
	{
		return resetFlag;
	}

	public void setResetFlag(boolean resetFlag)
	{
		this.resetFlag = resetFlag;
	}

	public final int getBufferPosition()
	{
		return bp;
	}

	public void reset(int mark, char mark_ch, int token)
	{
		bp = mark;
		ch = mark_ch;
		this.token = token;
		resetFlag = true;
	}

	public boolean isBlankInput()
	{
		for (int i = 0; i < buflen; i++)
			if (!isWhitespace(buf[i]))
				return false;

		return true;
	}

	public static final boolean isWhitespace(char ch)
	{
		return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
	}

	private transient void lexError(String key, Object args[])
	{
		token = 1;
	}

	public final int token()
	{
		return token;
	}

	public final String tokenName()
	{
		return JSONToken.name(token);
	}

	public final void skipWhitespace()
	{
		for (; whitespaceFlags[ch]; ch = buf[++bp]);
	}

	public final char getCurrent()
	{
		return ch;
	}

	public final void nextTokenWithColon()
	{
		do
		{
			if (ch == ':')
			{
				ch = buf[++bp];
				nextToken();
				return;
			}
			if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b')
				ch = buf[++bp];
			else
				throw new JSONException((new StringBuilder()).append("not match ':' - ").append(ch).toString());
		} while (true);
	}

	public final void nextTokenWithColon(int expect)
	{
		do
		{
			if (ch == ':')
			{
				ch = buf[++bp];
				break;
			}
			if (isWhitespace(ch))
				ch = buf[++bp];
			else
				throw new JSONException((new StringBuilder()).append("not match ':', actual ").append(ch).toString());
		} while (true);
		do
		{
			if (expect == 2)
			{
				if (ch >= '0' && ch <= '9')
				{
					sp = 0;
					pos = bp;
					scanNumber();
					return;
				}
				if (ch == '"')
				{
					sp = 0;
					pos = bp;
					scanString();
					return;
				}
			} else
			if (expect == 4)
			{
				if (ch == '"')
				{
					sp = 0;
					pos = bp;
					scanString();
					return;
				}
				if (ch >= '0' && ch <= '9')
				{
					sp = 0;
					pos = bp;
					scanNumber();
					return;
				}
			} else
			if (expect == 12)
			{
				if (ch == '{')
				{
					token = 12;
					ch = buf[++bp];
					return;
				}
				if (ch == '[')
				{
					token = 14;
					ch = buf[++bp];
					return;
				}
			} else
			if (expect == 14)
			{
				if (ch == '[')
				{
					token = 14;
					ch = buf[++bp];
					return;
				}
				if (ch == '{')
				{
					token = 12;
					ch = buf[++bp];
					return;
				}
			}
			if (isWhitespace(ch))
			{
				ch = buf[++bp];
			} else
			{
				nextToken();
				return;
			}
		} while (true);
	}

	public final void incrementBufferPosition()
	{
		ch = buf[++bp];
	}

	public final void resetStringPosition()
	{
		sp = 0;
	}

	public void nextToken(int expect)
	{
		do
		{
			switch (expect)
			{
			case 12: // '\f'
				if (ch == '{')
				{
					token = 12;
					ch = buf[++bp];
					return;
				}
				if (ch == '[')
				{
					token = 14;
					ch = buf[++bp];
					return;
				}
				break;

			case 16: // '\020'
				if (ch == ',')
				{
					token = 16;
					ch = buf[++bp];
					return;
				}
				if (ch == '}')
				{
					token = 13;
					ch = buf[++bp];
					return;
				}
				if (ch == ']')
				{
					token = 15;
					ch = buf[++bp];
					return;
				}
				if (ch == '\032')
				{
					token = 20;
					return;
				}
				break;

			case 2: // '\002'
				if (ch >= '0' && ch <= '9')
				{
					sp = 0;
					pos = bp;
					scanNumber();
					return;
				}
				if (ch == '"')
				{
					sp = 0;
					pos = bp;
					scanString();
					return;
				}
				if (ch == '[')
				{
					token = 14;
					ch = buf[++bp];
					return;
				}
				if (ch == '{')
				{
					token = 12;
					ch = buf[++bp];
					return;
				}
				break;

			case 4: // '\004'
				if (ch == '"')
				{
					sp = 0;
					pos = bp;
					scanString();
					return;
				}
				if (ch >= '0' && ch <= '9')
				{
					sp = 0;
					pos = bp;
					scanNumber();
					return;
				}
				if (ch == '[')
				{
					token = 14;
					ch = buf[++bp];
					return;
				}
				if (ch == '{')
				{
					token = 12;
					ch = buf[++bp];
					return;
				}
				break;

			case 14: // '\016'
				if (ch == '[')
				{
					token = 14;
					ch = buf[++bp];
					return;
				}
				if (ch == '{')
				{
					token = 12;
					ch = buf[++bp];
					return;
				}
				break;

			case 15: // '\017'
				if (ch == ']')
				{
					token = 15;
					ch = buf[++bp];
					return;
				}
				// fall through

			case 20: // '\024'
				if (ch == '\032')
				{
					token = 20;
					return;
				}
				break;
			}
			if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b')
			{
				ch = buf[++bp];
			} else
			{
				nextToken();
				return;
			}
		} while (true);
	}

	public final void nextToken()
	{
		sp = 0;
label0:
		do
		{
			pos = bp;
			if (ch == '"')
			{
				scanString();
				return;
			}
			if (ch == ',')
			{
				ch = buf[++bp];
				token = 16;
				return;
			}
			if (ch >= '0' && ch <= '9')
			{
				scanNumber();
				return;
			}
			if (ch == '-')
			{
				scanNumber();
				return;
			}
			switch (ch)
			{
			default:
				break label0;

			case 39: // '\''
				if (!isEnabled(Feature.AllowSingleQuotes))
				{
					throw new JSONException("Feature.AllowSingleQuotes is false");
				} else
				{
					scanStringSingleQuote();
					return;
				}

			case 8: // '\b'
			case 9: // '\t'
			case 10: // '\n'
			case 12: // '\f'
			case 13: // '\r'
			case 32: // ' '
				ch = buf[++bp];
				break;

			case 116: // 't'
				scanTrue();
				return;

			case 84: // 'T'
				scanTreeSet();
				return;

			case 83: // 'S'
				scanSet();
				return;

			case 102: // 'f'
				scanFalse();
				return;

			case 110: // 'n'
				scanNullOrNew();
				return;

			case 68: // 'D'
				scanIdent();
				return;

			case 40: // '('
				ch = buf[++bp];
				token = 10;
				return;

			case 41: // ')'
				ch = buf[++bp];
				token = 11;
				return;

			case 91: // '['
				ch = buf[++bp];
				token = 14;
				return;

			case 93: // ']'
				ch = buf[++bp];
				token = 15;
				return;

			case 123: // '{'
				ch = buf[++bp];
				token = 12;
				return;

			case 125: // '}'
				ch = buf[++bp];
				token = 13;
				return;

			case 58: // ':'
				ch = buf[++bp];
				token = 17;
				return;
			}
		} while (true);
		if (bp == buflen || ch == '\032' && bp + 1 == buflen)
		{
			if (token == 20)
				throw new JSONException("EOF error");
			token = 20;
			pos = bp = eofPos;
		} else
		{
			lexError("illegal.char", new Object[] {
				String.valueOf(ch)
			});
			ch = buf[++bp];
		}
	}

	public final void scanStringSingleQuote()
	{
		np = bp;
		hasSpecial = false;
		do
		{
			char ch = buf[++bp];
			if (ch != '\'')
			{
				if (ch == '\032')
					throw new JSONException("unclosed single-quote string");
				if (ch == '\\')
				{
					if (!hasSpecial)
					{
						hasSpecial = true;
						if (sp > sbuf.length)
						{
							char newsbuf[] = new char[sp * 2];
							System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
							sbuf = newsbuf;
						}
						System.arraycopy(buf, np + 1, sbuf, 0, sp);
					}
					ch = buf[++bp];
					switch (ch)
					{
					case 34: // '"'
						putChar('"');
						break;

					case 92: // '\\'
						putChar('\\');
						break;

					case 47: // '/'
						putChar('/');
						break;

					case 39: // '\''
						putChar('\'');
						break;

					case 98: // 'b'
						putChar('\b');
						break;

					case 70: // 'F'
					case 102: // 'f'
						putChar('\f');
						break;

					case 110: // 'n'
						putChar('\n');
						break;

					case 114: // 'r'
						putChar('\r');
						break;

					case 116: // 't'
						putChar('\t');
						break;

					case 117: // 'u'
						char c1 = ch = buf[++bp];
						char c2 = ch = buf[++bp];
						char c3 = ch = buf[++bp];
						char c4 = ch = buf[++bp];
						int val = Integer.parseInt(new String(new char[] {
							c1, c2, c3, c4
						}), 16);
						putChar((char)val);
						break;

					default:
						this.ch = ch;
						throw new JSONException("unclosed single-quote string");
					}
				} else
				if (!hasSpecial)
					sp++;
				else
				if (sp == sbuf.length)
					putChar(ch);
				else
					sbuf[sp++] = ch;
			} else
			{
				token = 4;
				this.ch = buf[++bp];
				return;
			}
		} while (true);
	}

	public final void scanString()
	{
		np = bp;
		hasSpecial = false;
		do
		{
			char ch = buf[++bp];
			if (ch != '"')
			{
				if (ch == '\\')
				{
					if (!hasSpecial)
					{
						hasSpecial = true;
						if (sp >= sbuf.length)
						{
							int newCapcity = sbuf.length * 2;
							if (sp > newCapcity)
								newCapcity = sp;
							char newsbuf[] = new char[newCapcity];
							System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
							sbuf = newsbuf;
						}
						System.arraycopy(buf, np + 1, sbuf, 0, sp);
					}
					ch = buf[++bp];
					switch (ch)
					{
					case 34: // '"'
						putChar('"');
						break;

					case 92: // '\\'
						putChar('\\');
						break;

					case 47: // '/'
						putChar('/');
						break;

					case 98: // 'b'
						putChar('\b');
						break;

					case 70: // 'F'
					case 102: // 'f'
						putChar('\f');
						break;

					case 110: // 'n'
						putChar('\n');
						break;

					case 114: // 'r'
						putChar('\r');
						break;

					case 116: // 't'
						putChar('\t');
						break;

					case 120: // 'x'
						char x1 = ch = buf[++bp];
						char x2 = ch = buf[++bp];
						int x_val = digits[x1] * 16 + digits[x2];
						char x_char = (char)x_val;
						putChar(x_char);
						break;

					case 117: // 'u'
						char u1 = ch = buf[++bp];
						char u2 = ch = buf[++bp];
						char u3 = ch = buf[++bp];
						char u4 = ch = buf[++bp];
						int val = Integer.parseInt(new String(new char[] {
							u1, u2, u3, u4
						}), 16);
						putChar((char)val);
						break;

					default:
						this.ch = ch;
						throw new JSONException((new StringBuilder()).append("unclosed string : ").append(ch).toString());
					}
				} else
				if (!hasSpecial)
					sp++;
				else
				if (sp == sbuf.length)
					putChar(ch);
				else
					sbuf[sp++] = ch;
			} else
			{
				token = 4;
				this.ch = buf[++bp];
				return;
			}
		} while (true);
	}

	public final String scanSymbolUnQuoted(SymbolTable symbolTable)
	{
		boolean firstIdentifierFlags[] = CharTypes.firstIdentifierFlags;
		char first = this.ch;
		boolean firstFlag = this.ch >= firstIdentifierFlags.length || firstIdentifierFlags[first];
		if (!firstFlag)
			throw new JSONException((new StringBuilder()).append("illegal identifier : ").append(this.ch).toString());
		boolean identifierFlags[] = CharTypes.identifierFlags;
		int hash = first;
		np = bp;
		sp = 1;
		do
		{
			char ch = buf[++bp];
			if (ch < identifierFlags.length && !identifierFlags[ch])
				break;
			hash = 31 * hash + ch;
			sp++;
		} while (true);
		this.ch = buf[bp];
		token = 18;
		int NULL_HASH = 0x33c587;
		if (sp == 4 && hash == 0x33c587 && buf[np] == 'n' && buf[np + 1] == 'u' && buf[np + 2] == 'l' && buf[np + 3] == 'l')
			return null;
		else
			return symbolTable.addSymbol(buf, np, sp, hash);
	}

	public int scanType(String type)
	{
		matchStat = 0;
		int fieldNameLength = typeFieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (typeFieldName[i] != buf[this.bp + i])
				return -2;

		int bp = this.bp + fieldNameLength;
		int typeLength = type.length();
		for (int i = 0; i < typeLength; i++)
			if (type.charAt(i) != buf[bp + i])
				return -1;

		bp += typeLength;
		if (buf[bp] != '"')
			return -1;
		ch = buf[++bp];
		if (ch == ',')
		{
			ch = buf[++bp];
			this.bp = bp;
			token = 16;
			return 3;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				ch = buf[++bp];
			} else
			if (ch == '\032')
				token = 20;
			else
				return -1;
			matchStat = 4;
		}
		this.bp = bp;
		return matchStat;
	}

	public boolean matchField(char fieldName[])
	{
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
				return false;

		bp = bp + fieldNameLength;
		ch = buf[bp];
		if (ch == '{')
		{
			ch = buf[++bp];
			token = 12;
		} else
		if (ch == '[')
		{
			ch = buf[++bp];
			token = 14;
		} else
		{
			nextToken();
		}
		return true;
	}

	public String scanFieldString(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return null;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		if (ch != '"')
		{
			matchStat = -1;
			return null;
		}
		int start = index;
		String strVal;
		do
		{
			ch = buf[index++];
			if (ch == '"')
			{
				bp = index;
				this.ch = ch = buf[bp];
				strVal = new String(buf, start, index - start - 1);
				break;
			}
			if (ch == '\\')
			{
				matchStat = -1;
				return null;
			}
		} while (true);
		if (ch == ',')
		{
			this.ch = buf[++bp];
			matchStat = 3;
			return strVal;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return null;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return null;
		}
		return strVal;
	}

	public String scanFieldSymbol(char fieldName[], SymbolTable symbolTable)
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return null;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		if (ch != '"')
		{
			matchStat = -1;
			return null;
		}
		int start = index;
		int hash = 0;
		String strVal;
		do
		{
			ch = buf[index++];
			if (ch == '"')
			{
				bp = index;
				this.ch = ch = buf[bp];
				strVal = symbolTable.addSymbol(buf, start, index - start - 1, hash);
				break;
			}
			hash = 31 * hash + ch;
			if (ch == '\\')
			{
				matchStat = -1;
				return null;
			}
		} while (true);
		if (ch == ',')
		{
			this.ch = buf[++bp];
			matchStat = 3;
			return strVal;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return null;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return null;
		}
		return strVal;
	}

	public ArrayList scanFieldStringArray(char fieldName[])
	{
		matchStat = 0;
		ArrayList list = new ArrayList();
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return null;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		if (ch != '[')
		{
			matchStat = -1;
			return null;
		}
		ch = buf[index++];
		do
		{
			if (ch != '"')
			{
				matchStat = -1;
				return null;
			}
			int start = index;
			do
			{
				ch = buf[index++];
				if (ch == '"')
				{
					String strVal = new String(buf, start, index - start - 1);
					list.add(strVal);
					ch = buf[index++];
					break;
				}
				if (ch == '\\')
				{
					matchStat = -1;
					return null;
				}
			} while (true);
			if (ch != ',')
				break;
			ch = buf[index++];
		} while (true);
		if (ch == ']')
		{
			ch = buf[index++];
		} else
		{
			matchStat = -1;
			return null;
		}
		bp = index;
		if (ch == ',')
		{
			this.ch = buf[bp];
			matchStat = 3;
			return list;
		}
		if (ch == '}')
		{
			ch = buf[bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
				this.ch = ch;
			} else
			{
				matchStat = -1;
				return null;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return null;
		}
		return list;
	}

	public int scanFieldInt(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return 0;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		int value;
		if (ch >= '0' && ch <= '9')
		{
			value = digits[ch];
			do
			{
				ch = buf[index++];
				if (ch < '0' || ch > '9')
					break;
				value = value * 10 + digits[ch];
			} while (true);
			if (ch == '.')
			{
				matchStat = -1;
				return 0;
			}
			bp = index - 1;
			if (value < 0)
			{
				matchStat = -1;
				return 0;
			}
		} else
		{
			matchStat = -1;
			return 0;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			matchStat = 3;
			token = 16;
			return value;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return 0;
			}
			matchStat = 4;
		}
		return value;
	}

	public boolean scanFieldBoolean(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return false;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		boolean value;
		if (ch == 't')
		{
			if (buf[index++] != 'r')
			{
				matchStat = -1;
				return false;
			}
			if (buf[index++] != 'u')
			{
				matchStat = -1;
				return false;
			}
			if (buf[index++] != 'e')
			{
				matchStat = -1;
				return false;
			}
			bp = index;
			ch = buf[bp];
			value = true;
		} else
		if (ch == 'f')
		{
			if (buf[index++] != 'a')
			{
				matchStat = -1;
				return false;
			}
			if (buf[index++] != 'l')
			{
				matchStat = -1;
				return false;
			}
			if (buf[index++] != 's')
			{
				matchStat = -1;
				return false;
			}
			if (buf[index++] != 'e')
			{
				matchStat = -1;
				return false;
			}
			bp = index;
			ch = buf[bp];
			value = false;
		} else
		{
			matchStat = -1;
			return false;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			matchStat = 3;
			token = 16;
		} else
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return false;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return false;
		}
		return value;
	}

	public long scanFieldLong(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return 0L;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		long value;
		if (ch >= '0' && ch <= '9')
		{
			value = digits[ch];
			do
			{
				ch = buf[index++];
				if (ch < '0' || ch > '9')
					break;
				value = value * 10L + (long)digits[ch];
			} while (true);
			if (ch == '.')
			{
				token = -1;
				return 0L;
			}
			bp = index - 1;
			if (value < 0L)
			{
				matchStat = -1;
				return 0L;
			}
		} else
		{
			matchStat = -1;
			return 0L;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			matchStat = 3;
			token = 16;
			return value;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return 0L;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return 0L;
		}
		return value;
	}

	public float scanFieldFloat(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return 0.0F;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		float value;
		if (ch >= '0' && ch <= '9')
		{
			int start = index - 1;
			do
				ch = buf[index++];
			while (ch >= '0' && ch <= '9');
			if (ch == '.')
			{
				ch = buf[index++];
				if (ch >= '0' && ch <= '9')
				{
					do
						ch = buf[index++];
					while (ch >= '0' && ch <= '9');
				} else
				{
					matchStat = -1;
					return 0.0F;
				}
			}
			bp = index - 1;
			String text = new String(buf, start, index - start - 1);
			value = Float.parseFloat(text);
		} else
		{
			matchStat = -1;
			return 0.0F;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			matchStat = 3;
			token = 16;
			return value;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return 0.0F;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return 0.0F;
		}
		return value;
	}

	public byte[] scanFieldByteArray(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return null;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		byte value[];
		if (ch == '"' || ch == '\'')
		{
			char sep = ch;
			int startIndex = index;
			int endIndex = index;
			for (endIndex = index; endIndex < buf.length && buf[endIndex] != sep; endIndex++);
			int base64Len = endIndex - startIndex;
			value = Base64.decodeFast(buf, startIndex, base64Len);
			if (value == null)
			{
				matchStat = -1;
				return null;
			}
			bp = endIndex + 1;
			ch = buf[bp];
		} else
		{
			matchStat = -1;
			return null;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			matchStat = 3;
			token = 16;
			return value;
		}
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return null;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return null;
		}
		return value;
	}

	public byte[] bytesValue()
	{
		return Base64.decodeFast(buf, np + 1, sp);
	}

	public double scanFieldDouble(char fieldName[])
	{
		matchStat = 0;
		int fieldNameLength = fieldName.length;
		for (int i = 0; i < fieldNameLength; i++)
			if (fieldName[i] != buf[bp + i])
			{
				matchStat = -2;
				return 0.0D;
			}

		int index = bp + fieldNameLength;
		char ch = buf[index++];
		double value;
		if (ch >= '0' && ch <= '9')
		{
			int start = index - 1;
			do
				ch = buf[index++];
			while (ch >= '0' && ch <= '9');
			if (ch == '.')
			{
				ch = buf[index++];
				if (ch >= '0' && ch <= '9')
				{
					do
						ch = buf[index++];
					while (ch >= '0' && ch <= '9');
				} else
				{
					matchStat = -1;
					return 0.0D;
				}
			}
			bp = index - 1;
			String text = new String(buf, start, index - start - 1);
			value = Double.parseDouble(text);
		} else
		{
			matchStat = -1;
			return 0.0D;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			matchStat = 3;
			token = 16;
		} else
		if (ch == '}')
		{
			ch = buf[++bp];
			if (ch == ',')
			{
				token = 16;
				this.ch = buf[++bp];
			} else
			if (ch == ']')
			{
				token = 15;
				this.ch = buf[++bp];
			} else
			if (ch == '}')
			{
				token = 13;
				this.ch = buf[++bp];
			} else
			if (ch == '\032')
			{
				token = 20;
			} else
			{
				matchStat = -1;
				return 0.0D;
			}
			matchStat = 4;
		} else
		{
			matchStat = -1;
			return 0.0D;
		}
		return value;
	}

	public String scanSymbol(SymbolTable symbolTable)
	{
		skipWhitespace();
		if (ch == '"')
			return scanSymbol(symbolTable, '"');
		if (ch == '\'')
			if (!isEnabled(Feature.AllowSingleQuotes))
				throw new JSONException("syntax error");
			else
				return scanSymbol(symbolTable, '\'');
		if (ch == '}')
		{
			ch = buf[++bp];
			token = 13;
			return null;
		}
		if (ch == ',')
		{
			ch = buf[++bp];
			token = 16;
			return null;
		}
		if (ch == '\032')
		{
			token = 20;
			return null;
		}
		if (!isEnabled(Feature.AllowUnQuotedFieldNames))
			throw new JSONException("syntax error");
		else
			return scanSymbolUnQuoted(symbolTable);
	}

	public final String scanSymbol(SymbolTable symbolTable, char quote)
	{
		int hash = 0;
		np = bp;
		sp = 0;
		boolean hasSpecial = false;
		do
		{
			char ch = buf[++bp];
			if (ch == quote)
				break;
			if (ch == '\032')
				throw new JSONException("unclosed.str");
			if (ch == '\\')
			{
				if (!hasSpecial)
				{
					hasSpecial = true;
					if (sp >= sbuf.length)
					{
						int newCapcity = sbuf.length * 2;
						if (sp > newCapcity)
							newCapcity = sp;
						char newsbuf[] = new char[newCapcity];
						System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
						sbuf = newsbuf;
					}
					System.arraycopy(buf, np + 1, sbuf, 0, sp);
				}
				ch = buf[++bp];
				switch (ch)
				{
				case 34: // '"'
					hash = 31 * hash + 34;
					putChar('"');
					break;

				case 92: // '\\'
					hash = 31 * hash + 92;
					putChar('\\');
					break;

				case 47: // '/'
					hash = 31 * hash + 47;
					putChar('/');
					break;

				case 98: // 'b'
					hash = 31 * hash + 8;
					putChar('\b');
					break;

				case 70: // 'F'
				case 102: // 'f'
					hash = 31 * hash + 12;
					putChar('\f');
					break;

				case 110: // 'n'
					hash = 31 * hash + 10;
					putChar('\n');
					break;

				case 114: // 'r'
					hash = 31 * hash + 13;
					putChar('\r');
					break;

				case 116: // 't'
					hash = 31 * hash + 9;
					putChar('\t');
					break;

				case 117: // 'u'
					char c1 = ch = buf[++bp];
					char c2 = ch = buf[++bp];
					char c3 = ch = buf[++bp];
					char c4 = ch = buf[++bp];
					int val = Integer.parseInt(new String(new char[] {
						c1, c2, c3, c4
					}), 16);
					hash = 31 * hash + val;
					putChar((char)val);
					break;

				default:
					this.ch = ch;
					throw new JSONException("unclosed.str.lit");
				}
			} else
			{
				hash = 31 * hash + ch;
				if (!hasSpecial)
					sp++;
				else
				if (sp == sbuf.length)
					putChar(ch);
				else
					sbuf[sp++] = ch;
			}
		} while (true);
		token = 4;
		this.ch = buf[++bp];
		if (!hasSpecial)
			return symbolTable.addSymbol(buf, np + 1, sp, hash);
		else
			return symbolTable.addSymbol(sbuf, 0, sp, hash);
	}

	public void scanTrue()
	{
		if (buf[bp++] != 't')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'r')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'u')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'e')
			throw new JSONException("error parse true");
		ch = buf[bp];
		if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\032' || ch == '\f' || ch == '\b')
			token = 6;
		else
			throw new JSONException("scan true error");
	}

	public void scanSet()
	{
		if (buf[bp++] != 'S')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'e')
			throw new JSONException("error parse true");
		if (buf[bp++] != 't')
			throw new JSONException("error parse true");
		ch = buf[bp];
		if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '[' || ch == '(')
			token = 21;
		else
			throw new JSONException("scan set error");
	}

	public void scanTreeSet()
	{
		if (buf[bp++] != 'T')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'r')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'e')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'e')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'S')
			throw new JSONException("error parse true");
		if (buf[bp++] != 'e')
			throw new JSONException("error parse true");
		if (buf[bp++] != 't')
			throw new JSONException("error parse true");
		ch = buf[bp];
		if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b' || ch == '[' || ch == '(')
			token = 22;
		else
			throw new JSONException("scan set error");
	}

	public void scanNullOrNew()
	{
		if (buf[bp++] != 'n')
			throw new JSONException("error parse null or new");
		if (buf[bp] == 'u')
		{
			bp++;
			if (buf[bp++] != 'l')
				throw new JSONException("error parse true");
			if (buf[bp++] != 'l')
				throw new JSONException("error parse true");
			ch = buf[bp];
			if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\032' || ch == '\f' || ch == '\b')
				token = 8;
			else
				throw new JSONException("scan true error");
			return;
		}
		if (buf[bp] != 'e')
			throw new JSONException("error parse e");
		bp++;
		if (buf[bp++] != 'w')
			throw new JSONException("error parse w");
		ch = buf[bp];
		if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\032' || ch == '\f' || ch == '\b')
			token = 9;
		else
			throw new JSONException("scan true error");
	}

	public void scanFalse()
	{
		if (buf[bp++] != 'f')
			throw new JSONException("error parse false");
		if (buf[bp++] != 'a')
			throw new JSONException("error parse false");
		if (buf[bp++] != 'l')
			throw new JSONException("error parse false");
		if (buf[bp++] != 's')
			throw new JSONException("error parse false");
		if (buf[bp++] != 'e')
			throw new JSONException("error parse false");
		ch = buf[bp];
		if (ch == ' ' || ch == ',' || ch == '}' || ch == ']' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\032' || ch == '\f' || ch == '\b')
			token = 7;
		else
			throw new JSONException("scan false error");
	}

	public void scanIdent()
	{
		np = bp - 1;
		hasSpecial = false;
		do
		{
			sp++;
			ch = buf[++bp];
		} while (Character.isLetterOrDigit(ch));
		String ident = stringVal();
		Integer tok = keywods.getKeyword(ident);
		if (tok != null)
			token = tok.intValue();
		else
			token = 18;
	}

	public void scanNumber()
	{
		np = bp;
		if (ch == '-')
		{
			sp++;
			ch = buf[++bp];
		}
		for (; ch >= '0' && ch <= '9'; ch = buf[++bp])
			sp++;

		boolean isDouble = false;
		if (ch == '.')
		{
			sp++;
			ch = buf[++bp];
			isDouble = true;
			for (; ch >= '0' && ch <= '9'; ch = buf[++bp])
				sp++;

		}
		if (ch == 'L')
		{
			sp++;
			ch = buf[++bp];
		} else
		if (ch == 'S')
		{
			sp++;
			ch = buf[++bp];
		} else
		if (ch == 'B')
		{
			sp++;
			ch = buf[++bp];
		} else
		if (ch == 'F')
		{
			sp++;
			ch = buf[++bp];
			isDouble = true;
		} else
		if (ch == 'D')
		{
			sp++;
			ch = buf[++bp];
			isDouble = true;
		} else
		if (ch == 'e' || ch == 'E')
		{
			sp++;
			ch = buf[++bp];
			if (ch == '+' || ch == '-')
			{
				sp++;
				ch = buf[++bp];
			}
			for (; ch >= '0' && ch <= '9'; ch = buf[++bp])
				sp++;

			isDouble = true;
		}
		if (isDouble)
			token = 3;
		else
			token = 2;
	}

	private final void putChar(char ch)
	{
		if (sp == sbuf.length)
		{
			char newsbuf[] = new char[sbuf.length * 2];
			System.arraycopy(sbuf, 0, newsbuf, 0, sbuf.length);
			sbuf = newsbuf;
		}
		sbuf[sp++] = ch;
	}

	public final int pos()
	{
		return pos;
	}

	public final String stringVal()
	{
		if (!hasSpecial)
			return new String(buf, np + 1, sp);
		else
			return new String(sbuf, 0, sp);
	}

	public boolean isRef()
	{
		if (hasSpecial)
			return false;
		if (sp != 4)
			return false;
		else
			return buf[np + 1] == '$' && buf[np + 2] == 'r' && buf[np + 3] == 'e' && buf[np + 4] == 'f';
	}

	public final String symbol(SymbolTable symbolTable)
	{
		if (symbolTable == null)
			if (!hasSpecial)
				return new String(buf, np + 1, sp);
			else
				return new String(sbuf, 0, sp);
		if (!hasSpecial)
			return symbolTable.addSymbol(buf, np + 1, sp);
		else
			return symbolTable.addSymbol(sbuf, 0, sp);
	}

	public Number integerValue()
		throws NumberFormatException
	{
		long result = 0L;
		boolean negative = false;
		int i = np;
		int max = np + sp;
		char type = ' ';
		if (max > 0)
			switch (buf[max - 1])
			{
			case 76: // 'L'
				max--;
				type = 'L';
				break;

			case 83: // 'S'
				max--;
				type = 'S';
				break;

			case 66: // 'B'
				max--;
				type = 'B';
				break;
			}
		long limit;
		if (buf[np] == '-')
		{
			negative = true;
			limit = 0x8000000000000000L;
			i++;
		} else
		{
			limit = 0x8000000000000001L;
		}
		long multmin = negative ? 0xf333333333333334L : 0xf333333333333334L;
		if (i < max)
		{
			int digit = digits[buf[i++]];
			result = -digit;
		}
		while (i < max) 
		{
			int digit = digits[buf[i++]];
			if (result < multmin)
				return new BigInteger(numberString());
			result *= 10L;
			if (result < limit + (long)digit)
				return new BigInteger(numberString());
			result -= digit;
		}
		if (negative)
			if (i > np + 1)
			{
				if (result >= 0xffffffff80000000L && type != 'L')
				{
					if (type == 'S')
						return Short.valueOf((short)(int)result);
					if (type == 'B')
						return Byte.valueOf((byte)(int)result);
					else
						return Integer.valueOf((int)result);
				} else
				{
					return Long.valueOf(result);
				}
			} else
			{
				throw new NumberFormatException(numberString());
			}
		result = -result;
		if (result <= 0x7fffffffL && type != 'L')
		{
			if (type == 'S')
				return Short.valueOf((short)(int)result);
			if (type == 'B')
				return Byte.valueOf((byte)(int)result);
			else
				return Integer.valueOf((int)result);
		} else
		{
			return Long.valueOf(result);
		}
	}

	public long longValue()
		throws NumberFormatException
	{
		long result = 0L;
		boolean negative = false;
		int i = np;
		int max = np + sp;
		long limit;
		if (buf[np] == '-')
		{
			negative = true;
			limit = 0x8000000000000000L;
			i++;
		} else
		{
			limit = 0x8000000000000001L;
		}
		long multmin = negative ? 0xf333333333333334L : 0xf333333333333334L;
		if (i < max)
		{
			int digit = digits[buf[i++]];
			result = -digit;
		}
		do
		{
			if (i >= max)
				break;
			char ch = buf[i++];
			if (ch == 'L' || ch == 'S' || ch == 'B')
				break;
			int digit = digits[ch];
			if (result < multmin)
				throw new NumberFormatException(numberString());
			result *= 10L;
			if (result < limit + (long)digit)
				throw new NumberFormatException(numberString());
			result -= digit;
		} while (true);
		if (negative)
		{
			if (i > np + 1)
				return result;
			else
				throw new NumberFormatException(numberString());
		} else
		{
			return -result;
		}
	}

	public int intValue()
	{
		int result = 0;
		boolean negative = false;
		int i = np;
		int max = np + sp;
		int limit;
		if (buf[np] == '-')
		{
			negative = true;
			limit = 0x80000000;
			i++;
		} else
		{
			limit = 0x80000001;
		}
		int multmin = negative ? 0xf3333334 : 0xf3333334;
		if (i < max)
		{
			int digit = digits[buf[i++]];
			result = -digit;
		}
		do
		{
			if (i >= max)
				break;
			char ch = buf[i++];
			if (ch == 'L' || ch == 'S' || ch == 'B')
				break;
			int digit = digits[ch];
			if (result < multmin)
				throw new NumberFormatException(numberString());
			result *= 10;
			if (result < limit + digit)
				throw new NumberFormatException(numberString());
			result -= digit;
		} while (true);
		if (negative)
		{
			if (i > np + 1)
				return result;
			else
				throw new NumberFormatException(numberString());
		} else
		{
			return -result;
		}
	}

	public final String numberString()
	{
		char ch = buf[(np + this.sp) - 1];
		int sp = this.sp;
		if (ch == 'L' || ch == 'S' || ch == 'B' || ch == 'F' || ch == 'D')
			sp--;
		return new String(buf, np, sp);
	}

	public float floatValue()
	{
		return Float.parseFloat(numberString());
	}

	public double doubleValue()
	{
		return Double.parseDouble(numberString());
	}

	public Number decimalValue(boolean decimal)
	{
		char ch = buf[(np + sp) - 1];
		if (ch == 'F')
			return Float.valueOf(Float.parseFloat(new String(buf, np, sp - 1)));
		if (ch == 'D')
			return Double.valueOf(Double.parseDouble(new String(buf, np, sp - 1)));
		if (decimal)
			return decimalValue();
		else
			return Double.valueOf(doubleValue());
	}

	public BigDecimal decimalValue()
	{
		char ch = buf[(np + this.sp) - 1];
		int sp = this.sp;
		if (ch == 'L' || ch == 'S' || ch == 'B' || ch == 'F' || ch == 'D')
			sp--;
		return new BigDecimal(buf, np, sp);
	}

	public void config(Feature feature, boolean state)
	{
		features = Feature.config(features, feature, state);
	}

	public boolean isEnabled(Feature feature)
	{
		return Feature.isEnabled(features, feature);
	}

	public boolean scanISO8601DateIfMatch()
	{
		int rest = buflen - bp;
		if (rest < ISO8601_LEN_0)
			return false;
		char y0 = buf[bp];
		char y1 = buf[bp + 1];
		char y2 = buf[bp + 2];
		char y3 = buf[bp + 3];
		if (y0 != '1' && y0 != '2')
			return false;
		if (y1 < '0' || y1 > '9')
			return false;
		if (y2 < '0' || y2 > '9')
			return false;
		if (y3 < '0' || y3 > '9')
			return false;
		if (buf[bp + 4] != '-')
			return false;
		char M0 = buf[bp + 5];
		char M1 = buf[bp + 6];
		if (M0 == '0')
		{
			if (M1 < '1' || M1 > '9')
				return false;
		} else
		if (M0 == '1')
		{
			if (M1 != '0' && M1 != '1' && M1 != '2')
				return false;
		} else
		{
			return false;
		}
		if (buf[bp + 7] != '-')
			return false;
		char d0 = buf[bp + 8];
		char d1 = buf[bp + 9];
		if (d0 == '0')
		{
			if (d1 < '1' || d1 > '9')
				return false;
		} else
		if (d0 == '1' || d0 == '2')
		{
			if (d1 < '0' || d1 > '9')
				return false;
		} else
		if (d0 == '3')
		{
			if (d1 != '0' && d1 != '1')
				return false;
		} else
		{
			return false;
		}
		Locale local = Locale.getDefault();
		calendar = Calendar.getInstance(TimeZone.getDefault(), local);
		int year = digits[y0] * 1000 + digits[y1] * 100 + digits[y2] * 10 + digits[y3];
		int month = (digits[M0] * 10 + digits[M1]) - 1;
		int day = digits[d0] * 10 + digits[d1];
		calendar.set(1, year);
		calendar.set(2, month);
		calendar.set(5, day);
		char t = buf[bp + 10];
		if (t == 'T')
		{
			if (rest < ISO8601_LEN_1)
				return false;
		} else
		if (t == '"' || t == '\032')
		{
			calendar.set(11, 0);
			calendar.set(12, 0);
			calendar.set(13, 0);
			calendar.set(14, 0);
			ch = buf[bp += 10];
			token = 5;
			return true;
		} else
		{
			return false;
		}
		char h0 = buf[bp + 11];
		char h1 = buf[bp + 12];
		if (h0 == '0')
		{
			if (h1 < '0' || h1 > '9')
				return false;
		} else
		if (h0 == '1')
		{
			if (h1 < '0' || h1 > '9')
				return false;
		} else
		if (h0 == '2')
		{
			if (h1 < '0' || h1 > '4')
				return false;
		} else
		{
			return false;
		}
		if (buf[bp + 13] != ':')
			return false;
		char m0 = buf[bp + 14];
		char m1 = buf[bp + 15];
		if (m0 >= '0' && m0 <= '5')
		{
			if (m1 < '0' || m1 > '9')
				return false;
		} else
		if (m0 == '6')
		{
			if (m1 != '0')
				return false;
		} else
		{
			return false;
		}
		if (buf[bp + 16] != ':')
			return false;
		char s0 = buf[bp + 17];
		char s1 = buf[bp + 18];
		if (s0 >= '0' && s0 <= '5')
		{
			if (s1 < '0' || s1 > '9')
				return false;
		} else
		if (s0 == '6')
		{
			if (s1 != '0')
				return false;
		} else
		{
			return false;
		}
		int hour = digits[h0] * 10 + digits[h1];
		int minute = digits[m0] * 10 + digits[m1];
		int seconds = digits[s0] * 10 + digits[s1];
		calendar.set(11, hour);
		calendar.set(12, minute);
		calendar.set(13, seconds);
		char dot = buf[bp + 19];
		if (dot == '.')
		{
			if (rest < ISO8601_LEN_2)
				return false;
		} else
		{
			calendar.set(14, 0);
			ch = buf[bp += 19];
			token = 5;
			return true;
		}
		char S0 = buf[bp + 20];
		char S1 = buf[bp + 21];
		char S2 = buf[bp + 22];
		if (S0 < '0' || S0 > '9')
			return false;
		if (S1 < '0' || S1 > '9')
			return false;
		if (S2 < '0' || S2 > '9')
		{
			return false;
		} else
		{
			int millis = digits[S0] * 100 + digits[S1] * 10 + digits[S2];
			calendar.set(14, millis);
			ch = buf[bp += 23];
			token = 5;
			return true;
		}
	}

	public Calendar getCalendar()
	{
		return calendar;
	}

	public boolean isEOF()
	{
		switch (token)
		{
		case 20: // '\024'
			return true;

		case 1: // '\001'
			return false;

		case 13: // '\r'
			return false;
		}
		return false;
	}

	static 
	{
		whitespaceFlags = new boolean[256];
		whitespaceFlags[32] = true;
		whitespaceFlags[10] = true;
		whitespaceFlags[13] = true;
		whitespaceFlags[9] = true;
		whitespaceFlags[12] = true;
		whitespaceFlags[8] = true;
		digits = new int[103];
		for (int i = 48; i <= 57; i++)
			digits[i] = i - 48;

		for (int i = 97; i <= 102; i++)
			digits[i] = (i - 97) + 10;

		for (int i = 65; i <= 70; i++)
			digits[i] = (i - 65) + 10;

	}
}
