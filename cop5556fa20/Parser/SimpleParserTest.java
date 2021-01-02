
/**
 * Test class for  for the class project in COP5556 Programming Language Principles 
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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import cop5556fa20.SimpleParser.SyntaxException;
import cop5556fa20.Scanner.LexicalException;
import static cop5556fa20.Scanner.Kind.*;
/**
 * @author Beverly Sanders
 *
 */
@SuppressWarnings("preview") //text blocks are preview features in Java 14

class SimpleParserTest {
	
	//To make it easy to print objects and turn this output on and off.
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	
	//creates and returns a parser for the given input.
	private SimpleParser makeSimpleParser(String input) throws LexicalException {
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Tokens
		SimpleParser parser = new SimpleParser(scanner);
		return parser;
	}
	
	/*
	 * Parses the given input and expects a normal return from parse.
	 */
	void pass(String input) throws SyntaxException, LexicalException {
		SimpleParser p = makeSimpleParser(input);
		p.parse();
		assertTrue(p.consumedAll());
	}
	
	/*
	 * Parses the given input using the fragment of the grammar
	 * that uses Expression as the start symbol.  This is for convenience in
	 * testing--it allows expressions to be written and tested directly without
	 * having to be part of a statement.
	 * 
	 * This should test that all the tokens have been consumed except the EOF token.
	 */
	void passExpression(String input) throws LexicalException, SyntaxException {
		SimpleParser p = makeSimpleParser(input);
		p.expression();
		assertTrue(p.consumedAll());
		
	}
	
	/**
	 * Use this when a lexical error is expected with the given input
	 * 
	 * @param input
	 */
	void failLexical(String input)  {
		Exception exception = assertThrows(LexicalException.class, () -> {
		 makeSimpleParser(input);
	 });
	show(exception);
	}
	
	/**
	 * Use this when a syntax error is expected for the given input.
	 * 
	 * Kind is the kind of token the manifests an error.  (Not the expected token
	 * kind in a correct program, but the erroneous token)
	 * 
	 * @param input
	 * @param kind
	 */
	void fail(String input, Scanner.Kind kind)  {
		Exception exception = assertThrows(SyntaxException.class, () -> {
		 SimpleParser parser = makeSimpleParser(input);
		 parser.parse();
	 });
	show(exception);
	assertEquals(kind, ((SyntaxException) exception).token().kind());
	}
	
	/**
	 * Use this when a syntax error is expected for the given input.  The
	 * token kind that manifests the error is not given or checked.
	 * 
	 * @param input
	 * @param kind
	 */
	void fail(String input)  {
		Exception exception = assertThrows(SyntaxException.class, () -> {
		 SimpleParser parser = makeSimpleParser(input);
		 parser.parse();
	 });
	show(exception);
	}
	
	/**
	 * Use when a syntax error is expected in a standalone expression.
	 * 
	 * @param input
	 */
	void failExpression(String input)  {
		Exception exception = assertThrows(SyntaxException.class, () -> {
		 SimpleParser parser = makeSimpleParser(input);
		 parser.expression();
	 });
	show(exception);
	}



	@Test
	public void testEmpty() throws Scanner.LexicalException, SyntaxException {
		String input = "";  //The input is the empty string.  This is legal
		pass(input);
	}	
	
	

	@Test
	public void testDec0() throws Scanner.LexicalException, SyntaxException {
		String input = """
				int abc;
				string bcd;
				""";		
		pass(input);
	}
	
	
	/* Extra Ident at end.  The parser treats this as the left hand side of a 
	 * statement and finds EOF instead of ASSIGN, LARROW or RARROW.  
	 */
	@Test
	public void testDec0fail0() throws Scanner.LexicalException, SyntaxException {
		String input = """
				int abc;
				string bcd;
				x
				""";	
		fail(input,EOF);
	}
	
	/*
	 * Missing ; at end of first line.  Error manifests itself on "string".
	 */
	@Test
	public void testDec0fail1() throws Scanner.LexicalException, SyntaxException {
		String input = """
				int abc
				string bcd;
				""";
		fail(input, KW_string);
	}
	
	
	@Test
	public void testImageIn0() throws Scanner.LexicalException, SyntaxException {
		String input = """
				abc <- "https://this.is.a.url";
				""";
		pass(input);
	}
	

	
	@Test
	public void testBinary0() throws LexicalException, SyntaxException {
		String input = """
				a+b
				""";
		passExpression(input);
	}
	
	
	/*
	 * This input fails because these two adjacent operators are not legal.
	 */
	@Test
	public void testBinary0fail() throws LexicalException, SyntaxException {
		String input = """
				a + *b
				""";
		failExpression(input);
	}
	
	/*
	 * This input is OK.  The plus is part of a unary expression.
	 */
	@Test
	public void testBinary1() throws LexicalException, SyntaxException {
		String input = """
				a * + b
				""";
		passExpression(input);
	}
	

		
}