/**
 * Parser for the class project in COP5556 Programming Language Principles 
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
import static cop5556fa20.Scanner.Kind;
import static cop5556fa20.Scanner.Kind.ASSIGN;
import static cop5556fa20.Scanner.Kind.EOF;
import static cop5556fa20.Scanner.Kind.IDENT;
import static cop5556fa20.Scanner.Kind.KW_int;
import static cop5556fa20.Scanner.Kind.KW_string;
import static cop5556fa20.Scanner.Kind.SEMI;

import java.util.ArrayList;
import java.util.List;

import cop5556fa20.Scanner.Kind;
import cop5556fa20.Scanner.LexicalException;
import cop5556fa20.Scanner.Token;
import cop5556fa20.SimpleParser.SyntaxException;
import cop5556fa20.AST.ASTNode;
import cop5556fa20.AST.Dec;
import cop5556fa20.AST.DecImage;
import cop5556fa20.AST.DecVar;
import cop5556fa20.AST.ExprArg;
import cop5556fa20.AST.ExprBinary;
import cop5556fa20.AST.ExprIntLit;
import cop5556fa20.AST.ExprPixelConstructor;
import cop5556fa20.AST.ExprPixelSelector;
import cop5556fa20.AST.ExprStringLit;
import cop5556fa20.AST.ExprUnary;
import cop5556fa20.AST.ExprVar;
import cop5556fa20.AST.Expression;
import cop5556fa20.AST.ExprConditional;
import cop5556fa20.AST.ExprConst;
import cop5556fa20.AST.ExprEmpty;
import cop5556fa20.AST.ExprHash;
import cop5556fa20.AST.Program;
import cop5556fa20.AST.Type;
import cop5556fa20.AST.Statement;
import cop5556fa20.AST.StatementAssign;
import cop5556fa20.AST.StatementImageIn;
import cop5556fa20.AST.StatementLoop;
import cop5556fa20.AST.StatementOutFile;
import cop5556fa20.AST.StatementOutScreen;

public class Parser {

	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		final Token token;  //the token that caused an error to be discovered.

		public SyntaxException(Token token, String message) {
			super(message);
			this.token = token;
		}

		public Token token() {
			return token;
		}

	}


	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken(); // establish invariant that t is always the next token to be processed
	}

	public Program parse() throws SyntaxException, LexicalException {
		Program p = program();
		matchEOF();
		return p;
	}

	private static final Kind[] startDec = {KW_int, KW_string,Kind.KW_image};
	
	//private static final Kind[] firstProgram = {KW_int, KW_string}; //this is not the correct FIRST(Program...), but illustrates a handy programming technique

	private Program program() throws SyntaxException, LexicalException {
		//System.out.println("In prog block");
		Token current = t; //always save the current token.  
		List<ASTNode> decsAndStatements = new ArrayList<ASTNode>();
		while (isKind(startDec) || isKind(Kind.IDENT)) {
			switch (t.kind()) 
			{
				case KW_int, KW_string, KW_image -> 
				{
					Dec dec = declaration();
					decsAndStatements.add(dec);
					break;
				}
				
				case IDENT->
				{
					
					Statement statement = statement();
					decsAndStatements.add(statement);
					break;
				}
			//Your finished parser should NEVER throw UnsupportedOperationException, but it is convenient as a placeholder for unimplemented features.
			default -> throw new UnsupportedOperationException("unimplemented feature in program"); 
			}
			
			match(SEMI);
		}
//		System.out.println("done parsing the program");
		return new Program(current, decsAndStatements);  //return a Program object
	}

	private Dec declaration() throws SyntaxException, LexicalException {
		//System.out.println("In dec block");
		if(isKind(Kind.KW_image))
		{
			consume();
			DecImage decImage = imageDeclaration();
			return decImage;
			
		}
		else
		{
			DecVar decVar = variableDeclaration();
			return decVar;
		}
	}
//	private Dec declaration() throws SyntaxException, LexicalException {
//		Token first = t;  //always save the current token
//		if (isKind(KW_int)) {
//			consume();
//			Type type = Type.Int;
//			Token name = match(IDENT);
//			Expression e = Expression.empty; //use this special Expression object if an optional expression is missing.  
//			                                 //Using null is an obvious alternative, but that requires checking for null all over the place.  Using a dummy object is much easier.
//			if (isKind(ASSIGN)) {
//				consume();
//				e = expression();
//			}
//			return new DecVar(first, type , scanner.getText(name), e);  //returns a DecVar object
//		}
//		return null; //this is hack.  Your completed version should always return some sort of Dec object.
//	}
//	
	private DecVar variableDeclaration() throws SyntaxException, LexicalException {
		//System.out.println("In Dec VAr block");
		Token current = t;  //always save the current token
		Expression expr =Expression.empty;
		Type type = Type.Void;
		
		switch (t.kind()) 
		{
			case KW_int -> 
			{
				type = Type.Int;
				consume();
				break;
			}
			
			case KW_string->
			{
				type = Type.String;
				consume();
				break;
			}
		//Your finished parser should NEVER throw UnsupportedOperationException, but it is convenient as a placeholder for unimplemented features.
		default -> throw new SyntaxException(current, "Invalid Variable type");
		}
		
		
		Token name = match(Kind.IDENT);
		if(isKind(Kind.ASSIGN))
		{
			consume();
			expr = expression();
		}
//		System.out.println(new DecVar(current, type,  scanner.getText(name), expr));
		return new DecVar(current, type,  scanner.getText(name), expr);
	}
	
	private DecImage imageDeclaration() throws SyntaxException, LexicalException {
		//System.out.println("In Dec Image block");
		Token current = t;  //always save the current token
		Expression width = Expression.empty;
		Expression height = Expression.empty;
		Expression source = Expression.empty;
		Type type= Type.Image;
		Kind op=Kind.NOP;
		
		if(isKind(Kind.LSQUARE))
		{
			consume();
			width = expression();
			match(Kind.COMMA);
			height = expression();
			match(Kind.RSQUARE);
		}
		Token name = match(Kind.IDENT);
		if(isKind(Kind.LARROW)|| isKind(Kind.ASSIGN))
		{
			if(isKind(Kind.LARROW))
				op = Kind.LARROW;
			else
				op = Kind.ASSIGN;
			consume();
			source=expression();
		}
		return new DecImage(current,  type, scanner.getText(name), width, height, op,
			 source);
	}
	
	private Statement statement() throws SyntaxException, LexicalException {
		//System.out.println("In statement block");
		Token current = t;  //always save the current token
		Expression x= Expression.empty;
		Expression y = Expression.empty;
		Expression expression = Expression.empty;
		Expression source = Expression.empty;
		Expression cond= Expression.empty;
		Expression e = Expression.empty;
		
		Token name = match(Kind.IDENT);
		
		if(isKind(Kind.RARROW))
		{
			consume();
			
			//Image Out Statement
			System.out.println("Statement out block");
			if(isKind(Kind.KW_SCREEN))
			{
				consume();
				if (isKind(Kind.LSQUARE))
				{
					consume();
					x = expression();
					match(Kind.COMMA);
					y = expression();
					match(Kind.RSQUARE);
				}
				
				
				return new StatementOutScreen(current,  scanner.getText(name), x,y);
				
			}
			//Image In Statement
			else
			{
				source=expression();
				return new StatementOutFile(current,  scanner.getText(name), source);
			}
		}
		//ImageInstatement
		else if(isKind(Kind.LARROW))
		{
			//System.out.println("ImageInStatment block");
			consume();
			source = expression();
			return new StatementImageIn (current, scanner.getText(name),  source);
		}
		
		//AssignmentStatement
		else if(isKind(Kind.ASSIGN))
		{
			//System.out.println("AssignmentStatement block");
			consume();
			
			//Loop Statement
			if(isKind(Kind.STAR))
			{
				System.out.println("INSIDE LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOp");
				consume();
				constXYSelector();
				match(Kind.COLON);
				if(!isKind(Kind.COLON))
				{
					cond = expression();
				}
				match(Kind.COLON);
				e = expression();
				
				return new StatementLoop (current, scanner.getText(name), cond, e);
			}
			else
			{
				expression = expression();
				return new StatementAssign (current, scanner.getText(name),  expression);
			}
		}
		else
		{
			throw new SyntaxException(current,"Invalid Statement");
		}
	}


	//expression has package visibility (rather than private) to allow tests to call expression directly  
	protected Expression expression() throws SyntaxException, LexicalException {
		//System.out.println("In expression block");
		Token current = t;
		Expression el0 = Expression.empty;
		Expression trueCase = Expression.empty;
		Expression falseCase = Expression.empty;
		el0 = orExpression();
		if(isKind(Kind.Q))
		{
			consume();
			trueCase = expression();
			match(Kind.COLON);
			falseCase = expression();
			return new ExprConditional(current, el0, trueCase,falseCase);
		}


		System.out.println(el0);
		return el0;
	}
	
	public Expression orExpression() throws SyntaxException, LexicalException {
		//System.out.println("In or Exp block");
		//System.out.println("orExpression block");
		Token current = t;
		Expression e0 = Expression.empty;
		Expression e1 = Expression.empty;
		Kind op = Kind.NOP;
		e0 = andExpression();
		while(isKind(Kind.OR))
		{
			op = Kind.OR;
			consume();
			e1=andExpression();
			e0 = new ExprBinary(current,e0,op,e1);
		}
		
		return e0;
		//System.out.println("  leaving orExpression");
	}
	
	public Expression andExpression() throws SyntaxException, LexicalException {
		//System.out.println("In and expblock");
		//System.out.println("orExpression block");
		Token current = t;
		Expression e0 = Expression.empty;
		Expression e1 = Expression.empty;
		Kind op = Kind.NOP;
		e0 = eqExpression();
		while(isKind(Kind.AND))
		{
			op=Kind.AND;
			consume();
			e1 = eqExpression();
			e0 = new ExprBinary(current,e0,op,e1);
		}
		return e0;
		//System.out.println("  leaving orExpression");
	}
	
	public Expression eqExpression() throws SyntaxException, LexicalException {
		System.out.println("In eqExpression block parser");
		//System.out.println("orExpression block");
		Token current = t;
		Expression e0 = Expression.empty;
		Expression e1 = Expression.empty;
		Kind op = Kind.NOP;
		e0 = relExpression();
		while(isKind(Kind.EQ) || isKind(Kind.NEQ))
		{
			if(isKind(Kind.EQ))
				op = Kind.EQ;
			else
				op=Kind.NEQ;
			consume();
			e1=relExpression();
			e0= new ExprBinary(current,e0,op,e1);
		}
		return e0;
		//System.out.println("  leaving orExpression");
	}
	
	public Expression relExpression() throws SyntaxException, LexicalException {
		System.out.println("In rel exp block parser");
		//System.out.println("orExpression block");
		Token current = t;
		Expression e0 = Expression.empty;
		Expression e1 = Expression.empty;
		Kind op = Kind.NOP;
		e0 = addExpression();
		while(isKind(Kind.LT) || isKind(Kind.GT) || isKind(Kind.LE) || isKind(Kind.GE) )
		{
			if(isKind(Kind.LT))
				op = Kind.LT;
			else if(isKind(Kind.GT))
				op = Kind.GT;
			else if(isKind(Kind.LE))
				op = Kind.LE;
			else
				op = Kind.GE;
			consume();
			e1=addExpression();
			e0 = new ExprBinary(current,e0,op,e1);
		}
		
		return e0;
		//System.out.println("  leaving orExpression");
	}
	
	public Expression addExpression() throws SyntaxException, LexicalException {
		System.out.println("In add block parser");
		//System.out.println("orExpression block");
		Token current = t;
		Expression e0 = Expression.empty;
		Expression e1 = Expression.empty;
		Kind op = Kind.NOP;
		e0 = multiExpression();
		while(isKind(Kind.PLUS) || isKind(Kind.MINUS))
		{
			if(isKind(Kind.PLUS))
				op=Kind.PLUS;
			else
				op=Kind.MINUS;
			consume();
			e1=multiExpression();
			e0= new ExprBinary(current,e0,op,e1);
		}
		
		return e0; 
		//System.out.println("  leaving orExpression");
	}
	
	public Expression multiExpression() throws SyntaxException, LexicalException {
		System.out.println("In multi exp block parser");
		//System.out.println("orExpression block");
		Token current = t;
		Expression e0 = Expression.empty;
		Expression e1 = Expression.empty;
		Kind op = Kind.NOP;
		e0 = unaryExpression();
		while(isKind(Kind.STAR) || isKind(Kind.DIV) || (isKind(Kind.MOD)))
		{
			if(isKind(Kind.STAR))
				op=(Kind.STAR);
			else if(isKind(Kind.DIV))
				op = Kind.DIV;
			else if(isKind(Kind.MOD))
				op = Kind.MOD;
			else 
				op=Kind.MINUS;
			consume();
			e1=unaryExpression();
			e0= new ExprBinary(current,e0,op,e1);
		}
		return e0;
		//System.out.println("  leaving orExpression");
	}
	
	public Expression unaryExpression() throws SyntaxException, LexicalException {
		System.out.println("In unary block parser file");
		//System.out.println("orExpression block");
		Token current = t;
		Expression expression= Expression.empty;
		Kind op = Kind.NOP;
		if(isKind(Kind.PLUS) || isKind(Kind.MINUS))
		{
			if(isKind(Kind.PLUS))
			{
				op=Kind.PLUS;
				consume();
				expression=unaryExpression();
				return new ExprUnary(current,op,expression);
			}
				
			else
			{
				op=Kind.MINUS;
				consume();
				expression=unaryExpression();
				return new ExprUnary(current,op,expression);
			}
		}
		else
		{
			expression = unaryExpressionNotPM();
			return expression;
		}
		
		
	}
	
	public Expression unaryExpressionNotPM() throws SyntaxException, LexicalException {
		System.out.println("Inunary not pm block");
		//System.out.println("orExpression block");
		Token current = t;
		Expression expression= Expression.empty;
		Kind op = Kind.NOP;
		if(isKind(Kind.EXCL))
		{
			op = Kind.EXCL;
			consume();
			expression = unaryExpression();
			return new ExprUnary(current,op,expression);
		}
		else
		{
			expression = hashExpression();
			return expression;
		}
		
	}
	
	public Expression hashExpression() throws SyntaxException, LexicalException {
		//System.out.println("Inhash exp block");
		//System.out.println("orExpression block");
//		System.out.println("In hashExpression block parser file");
		Token current = t;
		Expression expression= Expression.empty;
		String attribute = "";
		expression = primary();
		while(isKind(Kind.HASH))
		{
			consume();
			if(isKind(Kind.KW_WIDTH))
			{
				attribute= "width";
				consume();
			}
			else if(isKind(Kind.KW_RED))
			{
				attribute= "red";
				consume();
			}
				
			else if(isKind(Kind.KW_GREEN))
			{
				attribute= "green";
				consume();
			}
				
			else if(isKind(Kind.KW_BLUE))
			{
				attribute= "blue";
				consume();
			}
				
			else
				 throw new SyntaxException(current, "Invalid attribute" + t.kind());
			expression = new ExprHash(current,expression,attribute);
		}
//		System.out.println("Returning from hashExpression parser");
		return expression;
		//System.out.println("  leaving orExpression");
	}


	private Expression primary() throws SyntaxException, LexicalException {
		//System.out.println("In pimary block");
		Token current = t;
		Expression exp = Expression.empty;
		Expression pixelConst = Expression.empty;
		Expression pixelSel = Expression.empty;
		Expression arg = Expression.empty;
		Expression e = switch (t.kind()) {
		case INTLIT -> {
			int value = scanner.intVal(t);
			consume();
//			System.out.println("Sending the Intetger li object"+value);
			yield new ExprIntLit(current, value);
		}
		case STRINGLIT -> {
			String text = scanner.getText(t);
			consume();
			yield new ExprStringLit(current, text);
		}
		case IDENT, KW_X, KW_Y -> {
			consume();
//			System.out.println("Returning from primary in parser file");
//			System.out.println(new ExprVar(current, scanner.getText(current)));
			yield new ExprVar(current, scanner.getText(current));
			
		}
		case CONST -> {
			int value = scanner.intVal(t);
			consume();
			yield new ExprConst(current,scanner.getText(current), value);
		}
		case LPAREN -> {
			consume();
			exp = expression();
			match(Kind.RPAREN);
			yield exp;
		}
		case LPIXEL -> {
			System.out.println("Returning from pixel constructor");
			consume();
			pixelConst = pixelConstructor();
			System.out.println("Returning from pixel constructor");
			yield pixelConst;
		}
		case AT->
		{
			
			arg = argExpression();
			yield arg;
		}
		default -> 
		{
			Expression ex = Expression.empty;
			yield ex;
		}
		};
		
		if(e == Expression.empty)
			throw new SyntaxException(current, "Invalid attribute" + t.kind());
		
		if(isKind(Kind.LSQUARE))
		{
			
			pixelSel = pixelSelector(e);
			return pixelSel;
		}
		
		System.out.println(e);
		return e;
	}

	
	public ExprPixelConstructor pixelConstructor() throws SyntaxException, LexicalException {
		//System.out.println("In pixel block");
		//System.out.println("orExpression block");
		Token current = t;
		Expression red= Expression.empty;
		Expression blue= Expression.empty;
		Expression green = Expression.empty;
		red = expression();
		match(Kind.COMMA);
		green = expression();
		match(Kind.COMMA);
		blue = expression();
		match(Kind.RPIXEL);
		return new ExprPixelConstructor(current,red, green, blue);
		//System.out.println("  leaving orExpression");
	}
	
	public ExprPixelSelector pixelSelector(Expression ex) throws SyntaxException, LexicalException {
		System.out.println("In prixel sel block");
		//System.out.println("orExpression block");
		Token current = t;
		Expression x = Expression.empty;
		Expression y = Expression.empty;
		match(Kind.LSQUARE);
		x = expression();
		match(Kind.COMMA);
		y = expression();
		match(Kind.RSQUARE);
		return new ExprPixelSelector(current,ex, x, y);
		//System.out.println("  leaving orExpression");
	}

	public ExprArg argExpression() throws SyntaxException, LexicalException {
		//System.out.println("In arg exp block");
		//System.out.println("orExpression block");
		Token first = t;
		consume();
		Expression pri = primary();
		return new ExprArg(first,pri);
		//System.out.println("  leaving orExpression");
	}
	
	public void constXYSelector() throws SyntaxException, LexicalException {
		//System.out.println(" constXYSelector block");
		match(Kind.LSQUARE);
		match(Kind.KW_X);
		match(Kind.COMMA);
		match(Kind.KW_Y);
		match(Kind.RSQUARE);
	}
	protected boolean isKind(Kind kind) {
		return t.kind() == kind;
	}

	protected boolean isKind(Kind... kinds) {
		for (Kind k : kinds) {
			if (k == t.kind())
				return true;
		}
		return false;
	}


	/**
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		Token tmp = t;
		if (isKind(kind)) {
			consume();
			return tmp;
		}
		error(t, kind.toString());
		return null; // unreachable
	}

	/**
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		Token tmp = t;
		if (isKind(kinds)) {
			consume();
			return tmp;
		}
		error(t, "expected one of " + kinds);
		return null; // unreachable
	}

	private Token consume() throws SyntaxException {
		Token tmp = t;
		if (isKind(EOF)) {
			error(t, "attempting to consume EOF");
		}
		t = scanner.nextToken();
		return tmp;
	}

	private void error(Token t, String m) throws SyntaxException {
		String message = m + " at " + t.line() + ":" + t.posInLine();
		throw new SyntaxException(t, message);
	}
	
	/**
	 * Only for check at end of program. Does not "consume" EOF so there is no
	 * attempt to get the nonexistent next Token.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (isKind(EOF)) {
			return t;
		}
		error(t, EOF.toString());
		return null; // unreachable
	}
}
