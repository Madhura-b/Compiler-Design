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

import cop5556fa20.Scanner.Token;

public class StatementLoop extends Statement {

	final String name;
	final Expression cond;
	final Expression e;
	
	public StatementLoop(Token first, String name, Expression cond, Expression e) {
		super(first);
		this.name = name;
		this.cond = cond;
		this.e = e;
	}

	public String name() {
		return name;
	}

	public Expression cond() {
		return cond;
	}

	public Expression e() {
		return e;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStatementLoop(this, arg);
	}




	
}
