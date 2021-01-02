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
 */

package cop5556fa20.AST;

import static org.junit.Assert.assertEquals;

import java.util.function.Predicate;

import cop5556fa20.Scanner;

public class ASTTestLambdas {

	public static Predicate<ASTNode>  checkExprIntLit(int val){
		return (enode) -> {
			assertEquals(ExprIntLit.class,enode.getClass());
			assertEquals(val,((ExprIntLit)enode).value()); 
			return true; }; 
	}
	
	
	public static Predicate<ASTNode> checkExprConst(String name, int val){
		return (enode) -> {
			assertEquals(ExprConst.class,enode.getClass());
			assertEquals(name,((ExprConst)enode).name());
			assertEquals(val,((ExprConst)enode).value()); 
			return true; }; 
	}		
	
	public static Predicate<ASTNode> checkExprBinary(Predicate<ASTNode> checke0, Predicate<ASTNode> checke1, Scanner.Kind opKind){
		return (enode) -> {
			assertEquals(ExprBinary.class, enode.getClass());
			Expression e0 = ((ExprBinary)enode).e0();
			checke0.test(e0);
			Expression e1 = ((ExprBinary)enode).e1();
			checke1.test(e1);
			assertEquals(opKind, ((ExprBinary)enode).op());
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkExprPixelConstructor(Predicate<ASTNode> redExpr, Predicate<ASTNode> greenExpr,  Predicate<ASTNode> blueExpr){
		return (enode) -> {
			ExprPixelConstructor e = (ExprPixelConstructor)enode;
			redExpr.test(e.redExpr());
			greenExpr.test(e.greenExpr());
			blueExpr.test(e.blueExpr());
			return true;
		};
	}
	
	 public static Predicate<ASTNode> checkExprHash(Predicate<ASTNode> checke, String attr) {
		return (enode) -> {
			ExprHash esel = (ExprHash)enode;
			checke.test(esel.e());
			assertEquals(attr, esel.attr());
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkExprConditional(
			Predicate<ASTNode> checkCond, 
			Predicate<ASTNode> checkTrueCase, 
			Predicate<ASTNode> checkFalseCase){
		return (enode) -> {
			ExprConditional e = (ExprConditional)enode;
			Expression cond = e.condition();
			checkCond.test(cond);
			Expression trueCase = e.trueCase();
			checkTrueCase.test(trueCase);
			Expression falseCase = e.falseCase();
			checkFalseCase.test(falseCase);
			return true;
		};
		
	}
	

	public static Predicate<ASTNode> checkExprUnary(Scanner.Kind op, Predicate<ASTNode> checkexpr){
		return (enode) ->{
			ExprUnary ue = (ExprUnary)enode;
			assertEquals(op, ue.op());
			Expression e = ue.e();
			checkexpr.test(e);
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkExprArg(Predicate<ASTNode> checkexpr){
		return (enode) -> {
			Expression e = ((ExprArg) enode).e();
			checkexpr.test(e);
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkExprStringLit(String text){
		return (enode) -> {
			assertEquals(ExprStringLit.class, enode.getClass());
			assertEquals(text,((ExprStringLit)enode).text()); return true; }; 		
	}
	
	public static Predicate<ASTNode> checkExprVar(String name){
		return (enode) -> {
			assertEquals(ExprVar.class, enode.getClass());
			assertEquals(name,((ExprVar)enode).name()); return true; }; 		
	}
	
	 public static Predicate<ASTNode> checkExprPixelSelector(Predicate<ASTNode> image,
			Predicate<ASTNode> X, Predicate<ASTNode> Y) {
		return (enode) -> {
			ExprPixelSelector e = (ExprPixelSelector)enode;
			Expression e0 = e.image();
			image.test(e0);
			Expression e1 = e.X();
			X.test(e1);
			Expression e2 = e.Y();
			Y.test(e2);
			return true;
		};
	}
	
	 public static Predicate<ASTNode> checkExprPixelSelector(String image,
			String X, String Y) {
		return (enode) -> {
			ExprPixelSelector e = (ExprPixelSelector)enode;
			Expression e0 = e.image();
			checkExprVar(image).test(e0);
			Expression e1 = e.X();
			checkExprVar(X).test(e1);
			Expression e2 = e.Y();
			checkExprVar(Y).test(e2);
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkDecImage(String name, Predicate<ASTNode> checkw, Predicate<ASTNode> checkh, Scanner.Kind op, Predicate<ASTNode> checksource){
		return (node) -> {
			DecImage n = (DecImage)node;
			assertEquals(name, n.name());
			if (checkw != null) {
				checkw.test(n.width());
				checkh.test(n.height());
			}
			if (op != null) {
				assertEquals(op, n.op());
				checksource.test(n.source());
			}
			return true;
		};
	}
	

	public static Predicate<ASTNode> checkDecVar(Type type, String name, Predicate<ASTNode> expr) {
		return (node) -> {
			DecVar d = (DecVar)node;
			assertEquals(type, d.type());
			assertEquals(name, d.name());
			if ( expr !=null ) {
			expr.test(d.expression());
			}
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkStatementImageIn(String name,  Predicate<ASTNode> expr){
		return (node) -> {
			StatementImageIn s = (StatementImageIn)node;
			assertEquals(name, s.name());
			expr.test(s.source());
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkStatementOutScreen(String name,  Predicate<ASTNode> x, Predicate<ASTNode> y){
		return (node) -> {
			StatementOutScreen s = (StatementOutScreen)node;
			assertEquals(name, s.name());
			if (x != null) {
				x.test(s.X());
				y.test(s.Y());
			}
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkStatementOutFile(String name, Predicate<ASTNode> filename){
		return (node) -> {
			StatementOutFile s = (StatementOutFile)node;
			assertEquals(name, s.name());
			filename.test(s.filename());
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkStatementAssignment(String name, Predicate<ASTNode> expression){
		return (node) -> {
			StatementAssign s = (StatementAssign)node;
			assertEquals(name, s.name());
			expression.test(s.expression());
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkStatementLoop(String name, Predicate<ASTNode> expression){
		return (node) -> {
			StatementLoop s = (StatementLoop)node;
			assertEquals(name, s.name());
			expression.test(s.e());
			return true;
		};
	}
	
	public static Predicate<ASTNode> checkStatementLoopWithCond(String name, Predicate<ASTNode> cond, Predicate<ASTNode> expression){
		return (node) -> {
			StatementLoop s = (StatementLoop)node;
			assertEquals(name, s.name());
			cond.test(s.cond());
			expression.test(s.e());
			return true;
		};
	}

}
