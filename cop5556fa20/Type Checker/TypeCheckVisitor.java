package cop5556fa20;

import java.util.HashMap;

import cop5556fa20.AST.ASTNode;
import cop5556fa20.AST.ASTVisitor;
import cop5556fa20.AST.Dec;
import cop5556fa20.AST.DecImage;
import cop5556fa20.AST.DecVar;
import cop5556fa20.AST.ExprArg;
import cop5556fa20.AST.ExprBinary;
import cop5556fa20.AST.ExprConditional;
import cop5556fa20.AST.ExprConst;
import cop5556fa20.AST.ExprEmpty;
import cop5556fa20.AST.ExprHash;
import cop5556fa20.AST.ExprIntLit;
import cop5556fa20.AST.ExprPixelConstructor;
import cop5556fa20.AST.ExprPixelSelector;
import cop5556fa20.AST.ExprStringLit;
import cop5556fa20.AST.ExprUnary;
import cop5556fa20.AST.ExprVar;
import cop5556fa20.AST.Expression;
import cop5556fa20.AST.Program;
import cop5556fa20.AST.StatementAssign;
import cop5556fa20.AST.StatementImageIn;
import cop5556fa20.AST.StatementLoop;
import cop5556fa20.AST.StatementOutFile;
import cop5556fa20.AST.StatementOutScreen;
import cop5556fa20.AST.Type;
import cop5556fa20.Scanner.Token;

public class TypeCheckVisitor implements ASTVisitor {
	
	public static HashMap<String, Dec> symbolTable;
//	static {
//		symbolTable = new HashMap<String, Dec>();	
//	}
	


	@SuppressWarnings("serial")
	class TypeException extends Exception {
		Token first;
		String message;
		
		public TypeException(Token first, String message) {
			super();
			this.first = first;
			this.message = "Semantic error:  "+first.line() + ":" + first.posInLine() + " " +message;
		}
		
		public String toString() {
			return message;
		}	
	}
	
	
	public TypeCheckVisitor() {
		super();
		symbolTable = new HashMap<String, Dec>();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object visitDecImage(DecImage decImage, Object arg) throws Exception {
		
		String varName = decImage.name();
		Expression width = decImage.width();
		Expression height = decImage.height();
		Expression source = decImage.source();
		Scanner.Kind op = decImage.op();
		
		if (symbolTable.containsKey(varName))
		{
			throw new TypeException(decImage.first()," Variable already declared ");
		}
		else
		{
			width.visit(this, decImage.type());
			height.visit(this, decImage.type());
			source.visit(this, decImage.type());
			
			if (width.type()==Type.Int || width.type()==Type.Void)
			{
				
				if (height.type()==Type.Int || height.type()==Type.Void)
				{
					if (width.type()==height.type())
					{
						if (op == Scanner.Kind.LARROW)
						{
							if (source.type()==Type.String || source.type() == Type.Image)
							{
								
							}
							else
								throw new TypeException(decImage.first(),"DecImage: Source must be of Type String ");
						}
						else if (op == Scanner.Kind.ASSIGN)
						{
							if (source.type()==Type.Image)
							{
								
							}
							else
								throw new TypeException(decImage.first()," DecImage: Source must be of Type Image");
						}
						else
						{
							if (op == Scanner.Kind.NOP)
							{
								
							}
							else
								throw new TypeException(decImage.first(),"DecImage: Op must be NOP ");
						}
					}
					else
					{
						throw new TypeException(decImage.first()," DecImage: Width and Heigth are not of identical types ");
					}
				}
				else
				{
					throw new TypeException(decImage.first()," DecImage: Heigth is of invalid type ");
				}
			}
			else
			{
				throw new TypeException(decImage.first(),"DecImage: Width is of invalid type ");
			}
			symbolTable.put(varName, decImage);
		}
		return  null;
		
	}

	@Override
	public Object visitDecVar(DecVar decVar, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		if (symbolTable.containsKey(decVar.name()))
		{
			throw new TypeException(decVar.first()," Variable already declared ");
		}
		else
		{
			decVar.expression().visit(this, decVar.type());
			
			if (decVar.type() == decVar.expression().type() || (decVar.expression().type()==Type.Void))
			{
				symbolTable.put(decVar.name(), decVar);
			}
			else throw new TypeException(decVar.first(),"DecVar: Type Mismatch");
		}
		
		return null;
	}

	@Override
	public Object visitExprArg(ExprArg exprArg, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression expr = exprArg.e();
		expr.visit(this, arg);
		
		Type type = (Type) arg;
		if (expr.type() == Type.Int)
		{
			if (type==Type.Int)
				exprArg.setType(Type.Int);
			else
				exprArg.setType(Type.String);
		}
		else
		{
			throw new TypeException(exprArg.first()," ExprArg: Argument must be of Type Int ");
		}
		return null;
	}

	@Override
	public Object visitExprBinary(ExprBinary exprBinary, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression e0 = exprBinary.e0();
		Expression e1 = exprBinary.e1();
		Scanner.Kind op =exprBinary.op();
		
		e0.visit(this, arg);
		e1.visit(this, arg);
		
		
		if (op == Scanner.Kind.OR || op == Scanner.Kind.AND)
		{
			if (e0.first().kind()==Scanner.Kind.AT || e0.first().kind()==Scanner.Kind.AT)
			{
				throw new TypeException(exprBinary.first()," ExprArg: Expression Type MisMatch ");
				
			}
				
			if (e0.type()==Type.Boolean && e1.type()==Type.Boolean)
			{
				exprBinary.setType(Type.Boolean);
			}
			else
			{
				throw new TypeException(exprBinary.first(),"BinaryExpr: Invalid type for e0 or e1 ");
			}
				
		}
		else if (op==Scanner.Kind.EQ || op ==Scanner.Kind.NEQ)
		{
			if (e0.first().kind()==Scanner.Kind.AT || e0.first().kind()==Scanner.Kind.AT)
				throw new TypeException(exprBinary.first(),"ExprArg: Expression Type MisMatch ");
			
			
			if (e0.type() == e1.type())
			{
				System.out.println("Seting type to be Boolean,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
				exprBinary.setType(Type.Boolean);
			}
			else
			{
				throw new TypeException(exprBinary.first()," EqExpression type mismatch ");
			}
				
		}
		else if (op == Scanner.Kind.LT || op==Scanner.Kind.GT || op==Scanner.Kind.LE || op==Scanner.Kind.GE)	
		{
			if (e0.first().kind()==Scanner.Kind.AT || e0.first().kind()==Scanner.Kind.AT)
				throw new TypeException(exprBinary.first(),"ExprArg: Expression Type MisMatch");
			
			if (e0.type() == e1.type() && e0.type() == Type.Int)
			{
				exprBinary.setType(Type.Boolean);
			}
			else
			{
				throw new TypeException(exprBinary.first()," RelExpression type mismatch ");
			}
				
		}
		else if (op == Scanner.Kind.PLUS)
		{
			if (e0.type() == e1.type() && (e0.type() == Type.Int || e0.type()==Type.String))
			{
				exprBinary.setType(e0.type());
			}
			else
				throw new TypeException(exprBinary.first()," AddExpression Types do not match");
		}
		else if (op == Scanner.Kind.MINUS)
		{
			if (e0.type() == e1.type() && (e0.type() == Type.Int))
			{
				exprBinary.setType(e0.type());
			}
			else
			{
				throw new TypeException(exprBinary.first()," AddExpression: Types do not match");
			}
				
		}
		else if (e0.type() == e1.type() && (e0.type() == Type.Int))
			
		{
			exprBinary.setType(Type.Int);
		}
		else 
		{
			throw new TypeException(exprBinary.first(), "BinaryExpr: Invalid Op ");
		}
		return null;
	}

	@Override
	public Object visitExprConditional(ExprConditional exprConditional, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression cond = exprConditional.condition();
		Expression e1 = exprConditional.trueCase();
		Expression e2 = exprConditional.falseCase();
		
		cond.visit(this, null);
		e1.visit(this, null);
		e2.visit(this, null);
		
		if (cond.type()==Type.Boolean && (e1.type() == e2.type()))
		{
			System.out.println("Setting var of conditional statement to be of type of first expression.................................."+e1.type());
			exprConditional.setType(e1.type());
		}
		else
		{
			throw new TypeException(exprConditional.first()," ExprConditional: Type mismatch");
		}
		return null;
	}

	@Override
	public Object visitExprConst(ExprConst exprConst, Object arg) throws Exception {
		// TODO Auto-generated method stub
		exprConst.setType(Type.Int);
		
		return null;
	}

	@Override
	public Object visitExprHash(ExprHash exprHash, Object arg) throws Exception {
		Expression expr = exprHash.e();
		String attribute = exprHash.attr();
		
		expr.visit(this, arg);
		if (expr.type() == Type.Int || expr.type() == Type.Image)
		{
			
			if (expr.type() == Type.Int)
			{
				if (attribute.equals("red") || attribute.equals("green") || attribute.equals("blue"))
				{
					exprHash.setType(Type.Int);
				}
				else
				{
					throw new TypeException(exprHash.first(),"Type mismatch in Hash Expr ");
				}
			}
			else if (expr.type() == Type.Image)
			{
				if (attribute.equals("width") || attribute.equals("height"))
				{
					exprHash.setType(Type.Int);
				}
				else
				{
					throw new TypeException(exprHash.first(),"Type mismatch in Hash Expr ");
				}
			}
			else
			{
				throw new TypeException(exprHash.first(),"Type mismatch in Hash Expr ");
			}
		}
		else
		{
			throw new TypeException(exprHash.first(),"Type mismatch in Hash Expr ");
		}
		return null;
	}

	@Override
	public Object visitExprIntLit(ExprIntLit exprIntLit, Object arg) throws Exception {
		// TODO Auto-generated method stub
		exprIntLit.setType(Type.Int);
		return null;
	}

	@Override
	public Object visitExprPixelConstructor(ExprPixelConstructor exprPixelConstructor, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression red = exprPixelConstructor.redExpr();
		Expression green = exprPixelConstructor.greenExpr();
		Expression blue = exprPixelConstructor.blueExpr();
		
		red.visit(this, arg);
		green.visit(this, arg);
		blue.visit(this, arg);
		
		if ((red.type() == blue.type()) && ( red.type() == green.type()) && (red.type()== Type.Int))
		{
			exprPixelConstructor.setType(Type.Int);
		}
		else
		{
			throw new TypeException(exprPixelConstructor.first()," PixelConstructor: Type mismatch ");
		}
			
		return null;
	}

	@Override
	public Object visitExprPixelSelector(ExprPixelSelector exprPixelSelector, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e = exprPixelSelector.image();
		Expression x = exprPixelSelector.X();
		Expression y = exprPixelSelector.Y();
		
		e.visit(this, arg);
		x .visit(this, arg);
		exprPixelSelector.Y().visit(this, arg);
		
		if (e.type() == Type.Image && (x.type() == y.type()) && (x.type() == Type.Int) )
		{
			exprPixelSelector.setType(Type.Int);
		}
		else
		{
			throw new TypeException(exprPixelSelector.first(), "PixelSelector: Type Mismatch");
		}
		return null;
	}

	@Override
	public Object visitExprStringLit(ExprStringLit exprStringLit, Object arg) throws Exception {
		// TODO Auto-generated method stub
		exprStringLit.setType(Type.String);
		return null;
	}

	@Override
	public Object visitExprUnary(ExprUnary exprUnary, Object arg) throws Exception {
		
		Expression expr = exprUnary.e();
		expr.visit(this, arg);
		Scanner.Kind op = exprUnary.op();
		
		if (op == Scanner.Kind.PLUS || op == Scanner.Kind.MINUS)
		{
			if (expr.type()==Type.Int)
			{
				exprUnary.setType(Type.Int);
			}
			else
			{
				throw new TypeException(exprUnary.first()," Unary expression type mismatch ");
			}
				
		}
		else if (op == Scanner.Kind.EXCL)
		{
			exprUnary.setType(Type.Boolean);
		}
		else
		{
			throw new TypeException(exprUnary.first()," Unary expression type mismatch ");
		}
		
		return null;
	}

	@Override
	public Object visitExprVar(ExprVar exprVar, Object arg) throws Exception {
		
		String varName = exprVar.name();
		
		if (varName.equals("X") || varName.equals("Y"))
		{
			symbolTable.put("X", new DecVar(null,Type.Int,varName,Expression.empty));
			symbolTable.put("Y", new DecVar(null,Type.Int,varName,Expression.empty));
		}
		
		if (!symbolTable.containsKey(varName))
		{
			throw new TypeException(exprVar.first(),"ExprVar: Variable must be declared before use");
		}
			
		Dec dec = symbolTable.get(varName);
		
		exprVar.setType(dec.type());
		return null;
	}


	/**
	 * First visit method that is called.  It simply visits its children and returns null if no type errors were encountered.  
	 */
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for(ASTNode node: program.decOrStatement()) {
			//System.out.println("Node = "+node);
			node.visit(this, arg);
		}
		System.out.println("Exiting prog");
		return null;
	}
	

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws Exception {
		
		Expression expr = statementAssign.expression();
		String varName = statementAssign.name();
		if (!symbolTable.containsKey(varName))
		{
			throw new TypeException(statementAssign.first(),"StatementAssign: Variable must be declared before use\\n");
		}
		statementAssign.setDec(symbolTable.get(varName));
		expr.visit(this, statementAssign.dec().type());
		if (statementAssign.dec().type() != expr.type())
		{
			throw new TypeException(statementAssign.first()," StatementAssign: Ident type and expression type do not match ");
			
		}
		
		return null;
	}

	@Override
	public Object visitStatementImageIn(StatementImageIn statementImageIn, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression source = statementImageIn.source();
		String varName = statementImageIn.name();
		if (!symbolTable.containsKey(varName))
		{
			throw new TypeException(statementImageIn.first()," StatementImageIn: Variable must be declared before use\n");
		}
		
		statementImageIn.setDec(symbolTable.get(varName));
		source.visit(this, Type.String);
		if (statementImageIn.dec().type() != Type.Image)
		{
			throw new TypeException(statementImageIn.first()," StatementImageIn: Identifier Type  is not image ");
		}
		
		if (source.type() != Type.Image && source.type() != Type.String)
		{
			throw new TypeException(statementImageIn.first(),"StatementImageIn: Source expression must be String/Image ");
		}
		
		
		
		return null;
	}

	@Override
	public Object visitStatementLoop(StatementLoop statementLoop, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expr = statementLoop.e();
		Expression condition = statementLoop.cond();
		String name =  statementLoop.name();
		
		if (!symbolTable.containsKey(name))
		{
			throw new TypeException(statementLoop.first(),"StatementLoop: Variable must be declared before use ");
		}
		
		statementLoop.setDec(symbolTable.get(name));
		condition.visit(this, null);
		expr.visit(this, arg);
		
		if (statementLoop.dec().type() != Type.Image)
		{
			throw new TypeException(statementLoop.first(), "StatementLoop: Identifer must be of Type Image ");
		}
		
		if (condition.type() != Type.Boolean && condition.type() != Type.Void)
		{
			throw new TypeException(statementLoop.first(),"StatementLoop: Cond Expression type must be  Empty or Boolean");
		}
		
		if (expr.type() != Type.Int)
		{
			throw new TypeException(statementLoop.first()," StatementLoop: Expression type must be type int");
		}
		
		return null;
	}

	@Override
	public Object visitExprEmpty(ExprEmpty exprEmpty, Object arg) throws Exception {
		// TODO Auto-generated method stub
		exprEmpty.setType(Type.Void);
		
		return null;
	}

	@Override
	public Object visitStatementOutFile(StatementOutFile statementOutFile, Object arg) throws Exception {
		// TODO Auto-generated method stub
		String varName = statementOutFile.name();
		Expression file = statementOutFile.filename();
		
		if (!symbolTable.containsKey(varName))
		{
			throw new TypeException(statementOutFile.first(),"StatementOutFile: Variable must be declared before use ");
		}
 
		statementOutFile.setDec(symbolTable.get(varName));

		if (statementOutFile.dec().type() != Type.Image)
		{
			throw new TypeException(statementOutFile.first(),"Type of Identifier does not match the Image");
		}

		file.visit(this, arg);
		if (file.type() != Type.String)
		{
			throw new TypeException(statementOutFile.first(),"Filename in Statement Out File must be a String");
		}
		
 		return null;
	}

	@Override
	public Object visitStatementOutScreen(StatementOutScreen statementOutScreen, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		Expression X = statementOutScreen.X();
		Expression Y = statementOutScreen.Y();
		if (!symbolTable.containsKey(statementOutScreen.name()))
		{
			throw new TypeException(statementOutScreen.first(),"Variable must be declared before use ");
		}
		statementOutScreen.setDec(symbolTable.get(statementOutScreen.name()));
		X.visit(this, statementOutScreen.dec().type());
		Y.visit(this, statementOutScreen.dec().type());
		
		if (X.type() != Y.type())
		{
			throw new TypeException(statementOutScreen.first()," Types mismatch StatementoutScreen");
		}
		
		if (statementOutScreen.dec().type() == Type.Int || statementOutScreen.dec().type() == Type.String)
		{
			if (X.type() != Type.Void)
			{
				throw new TypeException(statementOutScreen.first(),"Type of Expression X should be Void ");
			}
		}
		else if (statementOutScreen.dec().type() == Type.Image)
		{
			if (X.type() != Type.Void && X.type() != Type.Int)
			{
				throw new TypeException(statementOutScreen.first(),"The expression X must be of Type Void or Integer");
			}
		}
		else
		{
			throw new TypeException(statementOutScreen.first(),"Type of Identifier must be an Integer, Image or a String ");
		}
		return null;
	}

}