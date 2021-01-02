package cop5556fa20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import cop5556fa20.AST.ASTNode;
import cop5556fa20.AST.Program;
import cop5556fa20.Parser.SyntaxException;
import cop5556fa20.Scanner.LexicalException;
import cop5556fa20.TypeCheckVisitor.TypeException;

@SuppressWarnings("preview")
class TypeCheckTest {

	/*
	 * To make it easy to print objects and turn this output on and off.
	 */
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	/**
	 * Test input program that is expected to be syntactically correct and pass type checking.
	 * @param input   
	 * @throws Exception
	 */
	void pass(String input) throws Exception {
		Program prog = parseAndGetProgram(input);		
		TypeCheckVisitor v = new TypeCheckVisitor();
		prog.visit(v, null);
		show(prog);
	}
	
	
	/**
	 * Test input program that is expected to be syntactically correct, but fails type checking.
	 * @param input
	 * @throws LexicalException
	 * @throws SyntaxException
	 */
	void fail(String input) throws LexicalException, SyntaxException {
		Program prog = parseAndGetProgram(input);
		//show(prog);  //Display the AST
		TypeCheckVisitor v = new TypeCheckVisitor();
		Exception exception = assertThrows(TypeException.class, () -> {
			prog.visit(v, null);
		});
		show(exception);
	}
	
	//creates and returns a parser for the given input.
	private Parser makeParser(String input) throws LexicalException {
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		//show(scanner);   //Display the Scanner
		Parser parser = new Parser(scanner);
		return parser;
	}
	
	Program parseAndGetProgram(String input) throws LexicalException, SyntaxException{
		Parser parser = makeParser(input);
		ASTNode node = parser.parse();
		assertFalse(parser.scanner.hasTokens());
		return (Program)node;
	}
	
	@Test
	public void testEmpty() throws Exception {
		String input = "";  //The input is the empty string.  This is legal
		Program prog = parseAndGetProgram(input);
		TypeCheckVisitor v = new TypeCheckVisitor();
		prog.visit(v, null);
	}	
	
	
	/**
	 * This is one of the simplest nonempty correct programs.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdec0() throws Exception {
		String input = """
				int x;
				""";  
		pass(input);
	}
	
	@Test
	public void testExprArg() throws Exception {
		String input = """
				string x = @1 + @2;
				""";  
		pass(input);
	}
		

	/**
	 * This program fails type checking due to the attempt to redefine x
	 * @throws Exception
	 */
	@Test
	public void testdec1_fail() throws Exception {
		String input = """
				int x;
				string x;
				""";  
		fail(input);
	}
	

}