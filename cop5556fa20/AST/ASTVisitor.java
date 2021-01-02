/**
 * Code for the class project in COP5556 Programming Language Principles 
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
 */package cop5556fa20.AST;

public interface ASTVisitor {

	Object visitDecImage(DecImage decImage, Object arg)throws Exception;

	Object visitDecVar(DecVar decVar, Object arg)throws Exception;

	Object visitExprArg(ExprArg exprArg, Object arg)throws Exception;

	Object visitExprBinary(ExprBinary exprBinary, Object arg)throws Exception;

	Object visitExprConditional(ExprConditional exprConditional, Object arg)throws Exception;

	Object visitExprConst(ExprConst exprConst, Object arg)throws Exception;

	Object visitExprHash(ExprHash exprHash, Object arg)throws Exception;

	Object visitExprIntLit(ExprIntLit exprIntLit, Object arg)throws Exception;

	Object visitExprPixelConstructor(ExprPixelConstructor exprPixelConstructor, Object arg)throws Exception;

	Object visitExprPixelSelector(ExprPixelSelector exprPixelSelector, Object arg)throws Exception;

	Object visitExprStringLit(ExprStringLit exprStringLit, Object arg)throws Exception;

	Object visitExprUnary(ExprUnary exprUnary, Object arg)throws Exception;

	Object visitExprVar(ExprVar exprVar, Object arg)throws Exception;

	Object visitProgram(Program program, Object arg) throws Exception;

	Object visitStatementAssign(StatementAssign statementAssign, Object arg)throws Exception;

	Object visitStatementImageIn(StatementImageIn statementImageIn, Object arg)throws Exception;

	Object visitStatementLoop(StatementLoop statementLoop, Object arg)throws Exception;

	Object visitExprEmpty(ExprEmpty exprEmpty, Object arg)throws Exception;

	Object visitStatementOutFile(StatementOutFile statementOutFile, Object arg) throws Exception;

	Object visitStatementOutScreen(StatementOutScreen statementOutScreen, Object arg) throws Exception;

}
