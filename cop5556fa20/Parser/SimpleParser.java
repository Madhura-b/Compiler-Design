/**
 * Class for  for the class project in COP5556 Programming Language Principles 
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

import cop5556fa20.Scanner.LexicalException;
import cop5556fa20.Scanner.Token;
import cop5556fa20.Scanner.Kind;

public class SimpleParser {

	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		final Token token;

		public SyntaxException(Token token, String message) {
			super(message);
			this.token = token;
		}

		public Token token() {
			return token;
		}

	}


	final Scanner scanner;
	Token token;
	Kind[] startDec = {Kind.KW_int,Kind.KW_string, Kind.KW_image};
	Kind startStatement = Kind.IDENT;

	SimpleParser(Scanner scanner) {
		this.scanner = scanner;
		token = scanner.nextToken();
		//TODO ??
	}
	
	

	public void parse() throws SyntaxException, LexicalException {
		program();
		if (!consumedAll()) throw new SyntaxException(scanner.nextToken(), "tokens remain after parsing");
			//If consumedAll returns false, then there is at least one
		    //token left (the EOF token) so the call to nextToken is safe. 
	}
	

	public boolean consumedAll() {
		if (scanner.hasTokens()) { 
			Token t = scanner.nextToken();
			if (t.kind() != Scanner.Kind.EOF) return false;
		}
		return true;
	}


	private void program() throws SyntaxException, LexicalException {
		while(isKind(startDec) || isKind(startStatement))
		{
			System.out.println("program block");
			if(isKind(startDec))
			{
				declaration();
			}
			else
			{
				accept();
				statement();
			}

			match(Kind.SEMI);
		}
		//TODO
	}
	
	public void declaration() throws SyntaxException,LexicalException
	{
		System.out.println("declaration block");
		if(isKind(Kind.KW_image))
		{
			match(Kind.KW_image);
			imageDeclaraton();
		}
		else if(isKind(startDec))
		{
			accept();
			variableDeclaration();
		}
	}
	
	public void variableDeclaration() throws SyntaxException,LexicalException
	{
		System.out.println("variable dec block");
		match(Kind.IDENT);
		if(isKind(Kind.ASSIGN))
		{
			accept();
			expression();
		}
	}
	
	public void imageDeclaraton() throws SyntaxException,LexicalException
	{
		System.out.println("image dec block");
		if(isKind(Kind.LSQUARE))
		{
			accept();
			expression();
			match(Kind.COMMA);
			expression();
			match(Kind.RSQUARE);
		}
		match(Kind.IDENT);
		if(isKind(Kind.LARROW)|| isKind(Kind.ASSIGN))
		{
			accept();
			expression();
		}
	}
	
	public void statement() throws SyntaxException,LexicalException
	{
		System.out.println("statement block :"+token.kind());
		//ImageOutStatment
		if(isKind(Kind.RARROW))
		{
			accept();
			System.out.println("ImageOutStatment block");
			if(isKind(Kind.KW_SCREEN))
			{
				accept();
				if (isKind(Kind.LSQUARE))
				{
					accept();
					expression();
					match(Kind.COMMA);
					expression();
					match(Kind.RSQUARE);
				}
			}
			else
			{
				expression();
			}
		}
		//ImageInstatement
		else if(isKind(Kind.LARROW))
		{
			System.out.println("ImageInStatment block");
			accept();
			expression();
		}
		
		//AssignmentStatement
		else if(isKind(Kind.ASSIGN))
		{
			System.out.println("AssignmentStatement block");
			accept();
			
			//Loop Statement
			if(isKind(Kind.STAR))
			{
				accept();
				constXYSelector();
				match(Kind.COLON);
				if(!isKind(Kind.COLON))
				{
					expression();
				}
				match(Kind.COLON);
				expression();
			}
			else
			{
				expression();
			}
		}
		else
		{
			throw new SyntaxException(token,"Syntax Error");
		}
	}


	//make this public for convenience testing
	public void expression() throws SyntaxException, LexicalException {
		System.out.println("Expression block");
		System.out.println("Token Kind: "+ token.kind());
		orExpression();
		if(isKind(Kind.Q))
		{
			accept();
			expression();
			match(Kind.COLON);
			expression();
		}
	}
	
	public void orExpression() throws SyntaxException, LexicalException {
		//System.out.println("orExpression block");
		andExpression();
		while(isKind(Kind.OR))
		{
			accept();
			andExpression();
		}
		//System.out.println("  leaving orExpression");
	}
	
	public void andExpression() throws SyntaxException, LexicalException {
		//System.out.println("andExpression block");
		eqExpression();
		
		
		while(isKind(Kind.AND))
		{
			accept();
			eqExpression();
		}
		//System.out.println("  leaving andExpression");
	}
	
	public void eqExpression() throws SyntaxException, LexicalException {
		//System.out.println("eqExpression block");
		relExpression();
		while(isKind(Kind.EQ) || isKind(Kind.NEQ))
		{
			accept();
			relExpression();
		}
		//System.out.println("  leaving eqExpression");
	}
	
	public void relExpression() throws SyntaxException, LexicalException {
		System.out.println("relExpression block");
		addExpression();
		while(isKind(Kind.LT) || isKind(Kind.GT) || isKind(Kind.LE) || isKind(Kind.GE) )
		{
			accept();
			addExpression();
		}
		//System.out.println("  leaving relExpression");
	}
	
	public void addExpression() throws SyntaxException, LexicalException {
		//System.out.println("addExpression block");
		multiExpression();
		while(isKind(Kind.PLUS) || isKind(Kind.MINUS))
		{
			accept();
			multiExpression();
		}
		//System.out.println("  leaving addExpression");
	}
	
	public void multiExpression() throws SyntaxException, LexicalException {
		//System.out.println("multiExpression block");
		unaryExpression();
		while(isKind(Kind.STAR) || isKind(Kind.DIV) || (isKind(Kind.MOD)))
		{
			accept();
			unaryExpression();
		}
		//System.out.println("leaving multiExpression");
	}
	
	public void unaryExpression() throws SyntaxException, LexicalException {
		//System.out.println("unaryExpression block");
		if(isKind(Kind.PLUS) || isKind(Kind.MINUS))
		{
			accept();
			unaryExpression();
		}
		else
		{
			unaryExpressionNotPM();
		}
		//System.out.println("leaving unaryExpression");
	}
	
	public void unaryExpressionNotPM() throws SyntaxException, LexicalException {
		//System.out.println("unaryExpressionNotPM block");
		if(isKind(Kind.EXCL))
		{
			unaryExpression();
		}
		else
		{
			hashExpression();
		}
		//System.out.println("leaving unaryExpressionNotPM");
	}
	
	public void hashExpression() throws SyntaxException, LexicalException {
		//System.out.println("hashExpression block");
		primary();
		while(isKind(Kind.HASH))
		{
			accept();
			attribute();
		}
		//System.out.println("leaving hash block");
	}
	
	public void primary() throws SyntaxException, LexicalException {
		System.out.println("primary block");
		if(isKind(Kind.INTLIT) || isKind(Kind.IDENT) || isKind(Kind.STRINGLIT) || isKind(Kind.KW_X) || isKind(Kind.KW_Y) || isKind(Kind.CONST))
		{
			//System.out.println("accepted "+token.kind());
			accept();
		}
		else if(isKind(Kind.LPIXEL))
		{
			accept();
			pixelConstructor();
		}
		else if(isKind(Kind.LPAREN))
		{
			accept();
			expression();
			match(Kind.RPAREN);
		}
		else if(isKind(Kind.AT))
		{
			accept();
			argExpression();
		}
		else
		{
			throw new SyntaxException(token, "Syntax Error");
		}
		if(isKind(Kind.LSQUARE))
		{
			accept();
			pixelSelector();
		}
		//System.out.println("leaving primary block");
	}
	
	public void pixelConstructor() throws SyntaxException, LexicalException {
		System.out.println("pixelConstructor block");
		match(Kind.LPIXEL);
		expression();
		match(Kind.COMMA);
		expression();
		match(Kind.COMMA);
		expression();
		match(Kind.RPIXEL);
	}
	
	public void pixelSelector() throws SyntaxException, LexicalException {
		System.out.println("pixelSelector block");
		match(Kind.LSQUARE);
		expression();
		match(Kind.COMMA);
		expression();
		match(Kind.RSQUARE);
	}
	
	public void argExpression() throws SyntaxException, LexicalException {
		//System.out.println("argExpressionblock");
		primary();
	}
	
	public void attribute() throws SyntaxException, LexicalException {
		//System.out.println("attribute block");
		if(isKind(Kind.KW_WIDTH) || isKind(Kind.KW_HEIGHT) || isKind(Kind.KW_RED) || isKind(Kind.KW_GREEN) || isKind(Kind.KW_BLUE))
		{
			accept();
		}
		else
		{
			throw new SyntaxException(token, "Syntax Error");
		}
	}
	
	public void constXYSelector() throws SyntaxException, LexicalException {
		//System.out.println(" constXYSelector block");
		match(Kind.LSQUARE);
		match(Kind.KW_X);
		match(Kind.COMMA);
		match(Kind.KW_Y);
		match(Kind.RSQUARE);
	}

	
	public boolean  isKind(Kind kind) {
		return token.kind()== kind;
	}
	
	public boolean isKind(Kind... kinds) {
		for (Kind k : kinds) {
			if (k == token.kind())
				return true;
		}
		return false;
	}
	
	private Token accept() throws SyntaxException {
		Token tmp = token;
		if (isKind(Kind.EOF)) {
			throw new SyntaxException(token, "Syntax Error"); // TODO give a better error message!
			// Note that EOF should be matched by the matchEOF method which is called only
			// in parse().
			// Anywhere else is an error. */
		}
		token = scanner.nextToken();
		return tmp;
	}
	
	/**
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		Token tmp = token;
		if (isKind(kind)) {
			accept();
			return tmp;
		}
		throw new SyntaxException(token,"Syntax Error"); //TODO  give a better error message!
	}


   //TODO--everything else.  Have fun!!
}
