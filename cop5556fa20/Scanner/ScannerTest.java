/**
 * Example JUnit tests for the Scanner in the class project in COP5556 Programming Language Principles 
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


import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cop5556fa20.Scanner.LexicalException;
import cop5556fa20.Scanner.Token;

import static cop5556fa20.Scanner.Kind.*;

@SuppressWarnings("preview") //text blocks are preview features in Java 14

class ScannerTest {
	
	//To make it easy to print objects and turn this output on and off.
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	public void showChars(char[] chars){
		System.out.println("index\tascii\tcharacter");
		for(int i = 0; i < chars.length; ++i) {
			char ch = chars[i];
			System.out.println(i + "\t" + (int)ch + "\t" + ch);
		}
	}

	/**
	 * Retrieves the next token and checks that its kind, position, length, line, and position in line
	 * match the given parameters.
	 * 
	 * @param scanner
	 * @param kind
	 * @param pos
	 * @param length
	 * @param line
	 * @param pos_in_line
	 * @return  the Token that was retrieved
	 */
	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line) {
		Token t = scanner.nextToken();
		Token expected = new Token(kind,pos,length,line,pos_in_line);
		assertEquals(expected, t);
		return t;
	}
	
	
	/**
	 *Retrieves the next token and checks that it is an EOF token. 
	 *Also checks that this was the last token.
	 *
	 * @param scanner
	 * @return the Token that was retrieved
	 */
	
	Token checkNextIsEOF(Scanner scanner) {
		Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token.kind());
		assertFalse(scanner.hasTokens());
		return token;
	}
	
	/**
	 * Simple test case with a (legal) empty program
	 *   
	 * @throws LexicalException
	 */
	@Test
	public void testEmpty() throws Scanner.LexicalException {
		String input = "";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}	
	
	@Test
	public void testEqual() throws Scanner.LexicalException {
		String input = "\"hello \\n there\\byou\"";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		Token t0 = checkNext(scanner, STRINGLIT, 0, 21, 1, 1); 
		System.out.println("getting the strng from the string lietra");
		System.out.println(scanner.getText(t0));
		show(scanner);   //Display the Scanner
		  //Check that the only token is the EOF token.
	}
	
	@Test
	public void testTest() throws Scanner.LexicalException {
		String input = """
				abc _abc $abc
				0abc abc0 ab0c
				/abc ab/c abc/
				""";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		  //Check that the only token is the EOF token.
	}
	
	/**
	 * Test illustrating how to check content of tokens.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void testSemi() throws Scanner.LexicalException {		
		
		String input = """
				;;
				;;
				""";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		checkNext(scanner, SEMI, 0, 1, 1, 1);
		checkNext(scanner, SEMI, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 3, 1, 2, 1);
		checkNext(scanner, SEMI, 4, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	/**
	 * Another example test, this time with an ident.  While simple tests like this are useful,
	 * many errors occur with sequences of tokens, so make sure that you have more complex test cases
	 * with multiple tokens and test the edge cases. 
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void testIdent() throws LexicalException {
		String input = "ij";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		Token t0 = checkNext(scanner, IDENT, 0, 2, 1, 1);
		//assertEquals("ij", scanner.getText(t0));
		checkNextIsEOF(scanner);
	}
	
	
	/**
	 * This example shows how to test that your scanner is behaving when the
	 * input is illegal.  In this case, a String literal
	 * that is missing the closing ".  
	 * 
	 * In contrast to Java String literals, the text block feature simply passes the characters
	 * to the scanner as given, using a LF (\n) as newline character.  If we had instead used a 
	 * Java String literal, we would have had to escape the double quote and explicitly insert
	 * the LF at the end:  String input = "\"greetings\n";
	 * 
	 * assertThrows takes the class of the expected exception and a lambda with the test code in the body.
	 * The test passes if the expected exception is thrown.  The Exception object is returned and
	 * an be printed.  It should contain an appropriate error message. 
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void failUnclosedStringLiteral() throws LexicalException {
		String input = """
				"greetings
				""";
		show(input);
		Exception exception = assertThrows(LexicalException.class, () -> {new Scanner(input).scan();});
		show(exception);
	}
	
	@Test
	public void testIdentifiers() throws LexicalException {
		String input = "width $1";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, KW_WIDTH, 0, 5,1,1);
		checkNext(scanner,IDENT ,6,2,1,7);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void teststr() throws LexicalException{
		String input="\"str\"";
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		checkNext(scanner,STRINGLIT ,0,5,1,1);
	}
	
	@Test
	public void testIdent1() throws LexicalException {
		String input = "_ij";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		Token t0 = checkNext(scanner, IDENT, 0, 3, 1, 1);
		assertEquals("_ij", scanner.getText(t0));
		checkNextIsEOF(scanner);
	}
	@Test
	public void testDigit() throws Scanner.LexicalException {
		String input1 = "214 456";
		String input = """
				214 456
				""";
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input1).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		Token t = checkNext(scanner, INTLIT, 0, 3, 1, 1);
		assertEquals(214, scanner.intVal(t));
		Token t1 = checkNext(scanner, INTLIT, 4, 3, 1, 5);
		assertEquals(456, scanner.intVal(t1));
		checkNextIsEOF(scanner);
	}
	@Test
	public void testintValConstant() throws LexicalException{

		Scanner scanner = new Scanner("BLACK").scan();
		Token t = scanner.nextToken();
		assertEquals(0xff000000, scanner.intVal(t));
	}
	
	@Test
	public void testComment() throws Scanner.LexicalException {
		String input = "//Commenting"; //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}
	
	@Test
	public void exceedMaxDigit() throws Scanner.LexicalException {
		String input= "2147483648";
		show(input);        //Display the input  
		Exception exception = assertThrows(LexicalException.class, () -> {new Scanner(input).scan();});
		show(exception);
	}
	
	@Test
	public void stringWithEscp() throws Scanner.LexicalException {
		String input= "\"str1\\nstr2\"";
		Scanner scanner = new Scanner(input).scan();
		show(input); 
		show(scanner);
	}
	
	@Test
	public void diffTokens() throws Scanner.LexicalException {
		String input= "123id1";
		Scanner scanner = new Scanner(input).scan();
		show(input); 
		show(scanner);
	}
	
	@Test
	public void singleLetterIdent() throws Scanner.LexicalException {
		String input= """
				r -> screen; 
				g -> screen; 
				b -> screen; 
				""";
		Scanner scanner = new Scanner(input).scan();
		show(input); 
		show(scanner);
	}
	@Test
	public void invalidToken() throws Scanner.LexicalException {
		String input= "123id^";
		Exception exception = assertThrows(LexicalException.class, () -> {new Scanner(input).scan();});
		show(exception);
	}
	@Test
	public void sequenceOfTokens() throws LexicalException {
		String input="""
				$1 "str\\f" 
				123 ??
				TEAL image
				""";
		//String input = "$1 \"str\\t\"\n123 ??\nTEAL image\n";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		Token t1 = checkNext(scanner,IDENT, 0, 2,1,1);
		assertEquals("$1", scanner.getText(t1));
		checkNext(scanner,STRINGLIT ,3,7,1,4);
		Token t3=checkNext(scanner,INTLIT,11,3,2,1);
		assertEquals(123, scanner.intVal(t3));
		checkNext(scanner,Q,15,1,2,5);
		Token t = checkNext(scanner,Q,16,1,2,6);
		assertEquals("?", scanner.getText(t));
		checkNext(scanner,CONST,18,4,3,1);
		checkNext(scanner,KW_image,23,5,3,6);
		checkNextIsEOF(scanner);
	}
	
	
	char[] stringChars(String s){
		 return Arrays.copyOf(s.toCharArray(), s.length()); // input string terminated with null char
	}
	
	@Test
	public void newline0() throws LexicalException {
		String input =  """
              \\n "Example\\nString" 
              """;
		System.out.println("\n\n\n***** newline0 *****");
		System.out.println("Input chars");
		showChars(stringChars(input));
		Scanner scanner = new Scanner(input);
		Exception exception = assertThrows(LexicalException.class, () -> {new Scanner(input).scan();});
		show(exception);
	}
	
	@Test
	public void newline1() throws LexicalException {
		String input =  """
              \n "Example\\nString" 
              """;	
		System.out.println("\n\n\n***** newline1 *****");
		System.out.println("Input chars");
		showChars(stringChars(input));
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Token t = scanner.nextToken();
		assertEquals(Scanner.Kind.STRINGLIT, t.kind());
		String text = scanner.getText(t);
		System.out.println("Token text");
		
		showChars(stringChars(text));
	}
	
	
	/**
	 * Both \n are converted by Java to NL before being passed to the 
	 * Scanner.  
	 * 
	 * This is an error since it has a NL in a String literal which is
	 * not allowed. 
	 * 
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void newline2() throws LexicalException {
		String input =  """
              \n "Example
              String" 
              """;	
		System.out.println("\n\n\n***** newline2 *****");
		System.out.println("Input chars");
		showChars(stringChars(input));
		Exception exception = assertThrows(LexicalException.class, () -> {new Scanner(input).scan();});
		show(exception);
	}
}

