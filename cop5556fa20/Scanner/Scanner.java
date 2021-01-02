/**
 * Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2020.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2020 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2020
 *
 */

package cop5556fa20;

import java.util.ArrayList;
import java.util.HashMap;

public class Scanner {
	
	@SuppressWarnings("preview")
	public record Token(
		Kind kind,
		int pos, //position in char array.  Starts at zero
		int length, //number of chars in token
		int line, //line number of token in source.  Starts at 1
		int posInLine //position in line of source.  Starts at 1
		
		) {
		public Kind kind()
		{
			return this.kind;
		}
	}
	
	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {
		int pos;
		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}
		public int pos() { return pos; }
	}
	
	
	public static enum Kind {
		IDENT, INTLIT, STRINGLIT, CONST,
		KW_X/* X */,  KW_Y/* Y */, KW_WIDTH/* width */,KW_HEIGHT/* height */, 
		KW_SCREEN/* screen */, KW_SCREEN_WIDTH /* screen_width */, KW_SCREEN_HEIGHT /*screen_height */,
		KW_image/* image */, KW_int/* int */, KW_string /* string */,
		KW_RED /* red */,  KW_GREEN /* green */, KW_BLUE /* blue */,
		ASSIGN/* = */, GT/* > */, LT/* < */, 
		EXCL/* ! */, Q/* ? */, COLON/* : */, EQ/* == */, NEQ/* != */, GE/* >= */, LE/* <= */, 
		AND/* & */, OR/* | */, PLUS/* + */, MINUS/* - */, STAR/* * */, DIV/* / */, MOD/* % */, 
	    AT/* @ */, HASH /* # */, RARROW/* -> */, LARROW/* <- */, LPAREN/* ( */, RPAREN/* ) */, 
		LSQUARE/* [ */, RSQUARE/* ] */, LPIXEL /* << */, RPIXEL /* >> */,  SEMI/* ; */, COMMA/* , */, NOT_SYMBOL,  EOF,NOP
	}
	
	public static enum State{
		START, IDENTIFIER,INTLIT,STRLIT,SYMBOL,COMMENT,ESCPSEQ
	}
	

	/**
	 * Returns the text of the token.  If the token represents a String literal, then
	 * the returned text omits the delimiting double quotes and replaces escape sequences with
	 * the represented character.
	 * 
	 * @param token
	 * @return
	 */
	public String getText(Token token) {
		
		String text="";
		if (token.kind == Kind.STRINGLIT)
		{
			
			System.out.println("Inside getText() string function");
			text = inputText.substring(token.pos,token.pos+token.length);
			StringBuilder sb = new StringBuilder();
			for (int i=1;i<text.length()-1;i++)
			{
				if (text.charAt(i)=='\\')
					{
						switch (text.charAt(i+1)) {
							case 'b' -> {sb.append("\b");}
							case 't' -> {sb.append("\t");}
							case 'n' -> {sb.append("\n");}
							case 'f' -> {sb.append("\f");}
							case 'r' -> {sb.append("\r");}
							case '\'' -> {sb.append("\'");}
							case '\"' -> {sb.append("\"");}
							case '\\' -> {sb.append("\\");}
							default -> {sb.append(text.charAt(i));}
						}
							i++;
					}
				else
					sb.append(text.charAt(i));
			}
			text = sb.toString();
			//System.out.println(" The get text result is "+result);
			
		}
		else
		{
			System.out.println(" Not string lit ");
			text = inputText.substring(token.pos,token.pos+token.length);
			System.out.println(" The result is "+text);
		}
		return text;
	}
	
	
	/**
	 * Returns true if the internal interator has more Tokens
	 * 
	 * @return
	 */
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}
	
	/**
	 * Returns the next Token and updates the internal iterator so that
	 * the next call to nextToken will return the next token in the list.
	 * 
	 * Precondition:  hasTokens()
	 * @return
	 */
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}
	
	public Kind isSymbol(String s)
	{
		if(s.equals("="))
			return Kind.ASSIGN;
		else if(s.equals(">"))
			return Kind.GT;
		else if(s.equals("<"))
			return Kind.LT;
		else if(s.equals("!"))
			return Kind.EXCL;
		else if(s.equals("?"))
			return Kind.Q;
		else if(s.equals(":"))
			return Kind.COLON;
		else if(s.equals("&"))
			return Kind.AND;
		else if(s.equals("|"))
			return Kind.OR;
		else if(s.equals("+"))
			return Kind.PLUS;
		else if(s.equals("-"))
			return Kind.MINUS;
		else if(s.equals("*"))
			return Kind.STAR;
		else if(s.equals("/"))
			return Kind.DIV;
		else if(s.equals("%"))
			return Kind.MOD;
		else if(s.equals("@"))
			return Kind.AT;
		else if(s.equals("#"))
			return Kind.HASH;
		else if(s.equals("("))
			return Kind.LPAREN;
		else if(s.equals(")"))
			return Kind.RPAREN;
		else if(s.equals("["))
			return Kind.LSQUARE;
		else if(s.equals("]"))
			return Kind.RSQUARE;
		else if(s.equals(";"))
			return Kind.SEMI;
		else if(s.equals(","))
			return Kind.COMMA;
		return Kind.NOT_SYMBOL;
	}
	/**
	 * The list of tokens created by the scan method.
	 */
	private final ArrayList<Token> tokens = new ArrayList<Token>();
	private String inputText;
	private final char[] inputArray;
	private State state;
	

	/**
	 * position of the next token to be returned by a call to nextToken
	 */
	private int nextTokenPos = 0;

	Scanner(String inputString) {
		/* IMPLEMENT THIS */
		this.inputText=inputString;
		this.inputText = inputString + 0;
		this.inputArray = inputText.toCharArray();
		this.state=State.START;
	}
	

	
	
	public Scanner scan() throws LexicalException {
		/* IMPLEMENT THIS */
		int pos = 0;
		int line = 1;
		int posInLine = 1;
		int start=0;
		System.out.println("Input string is "+inputText);
		System.out.println("The length of the input is  "+inputArray.length);
		while(pos<inputArray.length)
		{
			if((pos>=inputArray.length-1) && state == State.START)
			{
				break;
			}
			
//			System.out.println("The pos is   "+pos);
//			System.out.println("current character is "+ inputArray[pos]);
//			System.out.println("ASCII of current character is "+ (int)inputArray[pos]);
//			System.out.println("Current state is "+state);
//			
			switch(state)
			{
			
			case START->{
				start=pos;
				System.out.println(inputArray[start]);
				if(Character.isJavaIdentifierStart(inputArray[pos])) {
					pos++;
					//System.out.println("entering identifier");
					state=State.IDENTIFIER;
				}
				
				else if(inputArray[pos]=='0')
				{
					tokens.add(new Token(Kind.INTLIT, start,1, line, posInLine));
					pos++;
					posInLine+=1;
				}
				else if(Character.isDigit(inputArray[pos]))
				{
					pos++;
					state=State.INTLIT;
				}
				else if(inputArray[pos]=='"') {
					pos++;
					state=State.STRLIT;
				}
				else if((inputArray[pos] == '\f') || ((inputArray[pos] == ' ') || (inputArray[pos] == '\t')))
				{
					pos++;
					posInLine++;
					state=State.START;
				}
				else if(inputArray[pos]=='\r' || inputArray[pos]=='\n')
				{
					if(inputArray[pos]=='\n')
					{
						
						pos++;
						posInLine=1;
						line++;
					}
					else if(inputArray[pos]=='\r')
					{
						pos++;
						if((pos<inputArray.length) && inputArray[pos] =='\n')
						{
							pos++;
							line++;
							posInLine=1;
							
						}
						else
						{
							line++;
							posInLine=1;
						}
					}
					state=State.START;
				}
				
				else 
				{
					state=State.SYMBOL;
				}
			}
			case IDENTIFIER->
				{
					if ((Character.isJavaIdentifierStart(inputArray[pos]) || Character.isDigit(inputArray[pos])) 
							&& (pos<inputArray.length-1))
					{
						pos++;
					}
					
					else
					{
						System.out.println("entering else block with char \n"+inputArray[start]);
						String s = inputText.substring(start,pos);
						System.out.println("entering else block with substring of token"+s);
						int isReserveWord=0, isConstant=0;
						
						if(s.equals("X"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_X, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("Y"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_Y, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("width"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_WIDTH, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("height"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_HEIGHT, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("screen"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_SCREEN,start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("screen_width"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_SCREEN_WIDTH, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("screen_height"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_SCREEN_HEIGHT, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("image"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_image, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("int"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_int, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("string"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_string, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("red"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_RED, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("green"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_GREEN, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						else if(s.equals("blue"))
						{
							isReserveWord=1;
							tokens.add(new Token(Kind.KW_BLUE, start, pos-start, line, posInLine));
							posInLine+=pos-start;
							state=State.START;
						}
						
						else
						{
							System.out.println("entering else within else. Need to check if identifer is const or kw");
							if (isReserveWord==0)
							 {
								 
								 if((s.equals("Z")) ||  (s.equals("WHITE")) ||(s.equals("SILVER")) ||(s.equals("GRAY")) ||(s.equals("BLACK")) ||(s.equals("RED")) ||(s.equals("MAROON")) 
										 ||(s.equals("YELLOW")) ||(s.equals("OLIVE")) || (s.equals("LIME")) ||(s.equals("GREEN")) ||(s.equals("AQUA")) ||(s.equals("TEAL")) ||(s.equals("BLUE")) ||(s.equals("NAVY")) ||
											(s.equals("FUCHSIA")) || (s.equals("PURPLE")))
											{
												
									 			tokens.add(new Token(Kind.CONST, start, pos-start, line, posInLine));
									 			posInLine+=pos-start;
									 			isConstant=1;
												state=State.START;
											}
							 }
							 
							 if(isConstant==0 && isReserveWord ==0)
							 {
								 System.out.println("Adding idenntifer token");
								 tokens.add(new Token(Kind.IDENT, start, pos-start, line, posInLine));
								 posInLine+=pos-start;
								 state=State.START;
							 }
						}
					}
				}
			
			case INTLIT->
			{
				if (Character.isDigit(inputArray[pos]) && (pos<inputArray.length-1))
				{
					pos++;
				}
				else
				{
					String intLit=inputText.substring(start,pos);
					if(Long.parseLong(intLit)>Integer.MAX_VALUE)
					{
						throw new LexicalException("Number is out of range of Java integer",pos);
					}
					else
					{
					tokens.add(new Token(Kind.INTLIT,start,pos-start,line, posInLine));
					posInLine+=pos-start;
					state = State.START;
					}
					
				}
			}
			
			case SYMBOL->
			{
				Kind symbol = isSymbol(String.valueOf(inputArray[pos]));
				if(symbol == Kind.NOT_SYMBOL)
				{
					
					throw new LexicalException("Illegal token", pos);
					
				}
				else
				{
					switch(symbol)
					{
					case ASSIGN:
						pos++;
						if(pos<inputArray.length) 
						{
							String nextSymbol = (String.valueOf(inputArray[pos]));
							if(isSymbol(nextSymbol)==Kind.ASSIGN)
							{
								
								tokens.add(new Token(Kind.EQ, pos-1, 2, line, posInLine));
								state=State.START;
								pos++;
								posInLine+=2;
								
							}
							else
							{
								tokens.add(new Token(Kind.ASSIGN, pos-1,1, line, posInLine));
								posInLine+=1;
								state=State.START;
							}
						}
						
						else {
							tokens.add(new Token(Kind.ASSIGN, pos-1,1, line, posInLine));
						}
						break;
					case GT:
						pos++;
						if(pos<inputArray.length)
						{
							String nextSymbol = (String.valueOf(inputArray[pos]));
							
							if(isSymbol(nextSymbol)==Kind.GT)
							{
								
								tokens.add(new Token(Kind.RPIXEL, pos-1, 2, line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else if(isSymbol(nextSymbol)==Kind.ASSIGN)
							{
								
								tokens.add(new Token(Kind.GE, pos-1, 2, line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else
							{
								tokens.add(new Token(Kind.GT, pos-1, 1, line, posInLine));
								posInLine+=1;
								state=State.START;
							}
						}
						else
						{
							tokens.add(new Token(Kind.GT, pos-1, 1, line, posInLine));
						}
						break;
					case LT:
						pos++;
						if(pos<inputArray.length)
						{
							String nextSymbol = (String.valueOf(inputArray[pos]));
							
							if(isSymbol(nextSymbol)==Kind.LT)
							{
								tokens.add(new Token(Kind.LPIXEL, pos-1, 2, line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else if(isSymbol(nextSymbol)==Kind.ASSIGN)
							{
								
								tokens.add(new Token(Kind.LE,pos-1, 2,  line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else if(isSymbol(nextSymbol)==Kind.MINUS)
							{
								
								tokens.add(new Token(Kind.LARROW, pos-1, 2, line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else
							{
								tokens.add(new Token(Kind.LT, pos-1, 1, line, posInLine));
								posInLine+=1;
								state=State.START;
							}
						}
						else
						{
							tokens.add(new Token(Kind.LT, pos-1, 1, line, posInLine));
						}
						break;
					case EXCL:
						pos++;
						if(pos<inputArray.length)
						{
							String nextSymbol = (String.valueOf(inputArray[pos]));
							if(isSymbol(nextSymbol)==Kind.ASSIGN)
							{
								
								tokens.add(new Token(Kind.NEQ, pos-1, 2, line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else
							{
								
								tokens.add(new Token(Kind.EXCL, pos-1, 1, line, posInLine));
								posInLine+=1;
								state=State.START;
							}
						}
						else
						{
							
							tokens.add(new Token(symbol, pos-1, 1, line, posInLine));
						}
						break;
					case MINUS:
						pos++;
						if(pos<inputArray.length)
						{
							String nextSymbol = (String.valueOf(inputArray[pos]));
							if(isSymbol(nextSymbol)==Kind.GT)
							{
								
								tokens.add(new Token(Kind.RARROW, pos-1, 2, line, posInLine));
								pos++;
								posInLine+=2;
								state=State.START;
							}
							else
							{
								
								tokens.add(new Token(Kind.MINUS, pos-1, 1, line, posInLine));
								posInLine+=1;
								state=State.START;
							}
						}
						else
						{
							
							tokens.add(new Token(Kind.MINUS, pos-1, 1, line, posInLine));
						}
						break;
					case DIV:
						pos++;
						
						if(pos<inputArray.length)
						{
							String nextSymbol = (String.valueOf(inputArray[pos]));
							if(isSymbol(nextSymbol)==Kind.DIV)
							{
								System.out.println("in Comment block");
								pos++;
								state=State.COMMENT;
							}
							else
							{
								tokens.add(new Token(Kind.DIV, pos-1, 1, line, posInLine));
								posInLine+=1;
								state=State.START;
							}
						}
						else
						{
							
							tokens.add(new Token(symbol, pos-1, 1, line, posInLine));
						}
					break;	
					default:
						System.out.println("Adding symbol "+symbol);
						tokens.add(new Token(symbol, pos, 1, line, posInLine));
						pos++;
						posInLine+=1;
						state=State.START;
					}	
				}
			}
			case COMMENT -> {
				//TODO add handling of EOF line terminator
				//System.out.println("Comment encountered");
				
				if (inputArray[pos]=='\n')
				{
					System.out.println(" New line LF encountered ");
					pos++;
					
					System.out.println(" New char is "+inputArray[pos]);
					line++;
					posInLine=1;
					state = State.START;
					
				}
				if(inputArray[pos]=='\r')
				{
					pos++;
					if((pos<inputArray.length) && inputArray[pos] =='\n')
					{
						pos++;
						line++;
						posInLine=1;
						
					}
					else
					{
						line++;
						posInLine=1;
					}
					state = State.START;
				}
				else
				{
					pos++;
					posInLine++;
				}
			}
			
			case STRLIT->
			{
				if(pos==inputArray.length-1 && inputArray[pos]!='"')
				{
					throw new LexicalException("String not terminated on line", line);
				}
				else if(inputArray[pos]=='\\')
				{
							pos++;
							state=State.ESCPSEQ;
				}
				else if(inputArray[pos]=='"')
				{
					pos++;
					tokens.add(new Token(Kind.STRINGLIT, start, pos-start, line, posInLine));
					posInLine+=pos-start;
					state=State.START;
				}
				else if(inputArray[pos] == 10 || inputArray[pos] == 13)
				{
					throw new LexicalException("Cannot handle the escape sequence in the string", line);
				}
				else {
					pos++;
				}
			}
			case ESCPSEQ -> {
				
				//System.out.println(" String lit escape sequence lit case ");
				
				switch(inputArray[pos])
				{
					
					case 'b','t','n','f','\"','\'','\\' -> {	pos++;
																if(pos<inputArray.length-1)
																{
																state = State.STRLIT;
																}
																else
																{
																	tokens.add(new Token(Kind.STRINGLIT, start, pos-start, line, posInLine));
																	posInLine+=pos-start;
																	pos++;
																	state=State.START;
																}
														   }
					default -> { throw new LexicalException(" Invalid escape sequence encountered on line "+line,pos); }//Because \ is not allowed
				}
				
			}
		}
	}
		System.out.println("Done scanning");
		tokens.add(new Token(Kind.EOF, pos, 0, line, posInLine));
		System.out.println(tokens);
		return this;
	}
	

	/**
	 * precondition:  This Token is an INTLIT or CONST
	 * @throws LexicalException 
	 * 
	 * @returns the integer value represented by the token
	 */
	public int intVal(Token t) throws LexicalException {String value = "";	
		int i =0;
		int len=t.pos+t.length;
		for(i=t.pos;i<len;i++)
			value=value+inputArray[i];
		if(t.kind == Kind.CONST) {
			return (constants.get(value)).intValue();
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * Hashmap containing the values of the predefined colors.
	 * Included for your convenience.  
	 * 
	 */
	public static HashMap<String, Integer> constants;
	static {
		constants = new HashMap<String, Integer>();	
		constants.put("Z", 255);
		constants.put("WHITE", 0xffffffff);
		constants.put("SILVER", 0xffc0c0c0);
		constants.put("GRAY", 0xff808080);
		constants.put("BLACK", 0xff000000);
		constants.put("RED", 0xffff0000);
		constants.put("MAROON", 0xff800000);
		constants.put("YELLOW", 0xffffff00);
		constants.put("OLIVE", 0xff808000);
		constants.put("LIME", 0xff00ff00);
		constants.put("GREEN", 0xff008000);
		constants.put("AQUA", 0xff00ffff);
		constants.put("TEAL", 0xff008080);
		constants.put("BLUE", 0xff0000ff);
		constants.put("NAVY", 0xff000080);
		constants.put("FUCHSIA", 0xffff00ff);
		constants.put("PURPLE", 0xff800080);
	}
	
	/**
	 * Returns a String representation of the list of Tokens.
	 * You may modify this as desired. 
	 */
	public String toString() {
		return tokens.toString();
	}
}
