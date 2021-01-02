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

public class ExprPixelConstructor extends Expression {

	final Expression redExpr;
	final Expression greenExpr;
	final Expression blueExpr;
	
	public ExprPixelConstructor(Token first, Expression redExpr, Expression greenExpr, Expression blueExpr) {
		super(first);
		this.redExpr = redExpr;
		this.greenExpr = greenExpr;
		this.blueExpr = blueExpr;
	}

	public Expression redExpr() {
		return redExpr;
	}

	public Expression greenExpr() {
		return greenExpr;
	}

	public Expression blueExpr() {
		return blueExpr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((blueExpr == null) ? 0 : blueExpr.hashCode());
		result = prime * result + ((greenExpr == null) ? 0 : greenExpr.hashCode());
		result = prime * result + ((redExpr == null) ? 0 : redExpr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExprPixelConstructor other = (ExprPixelConstructor) obj;
		if (blueExpr == null) {
			if (other.blueExpr != null)
				return false;
		} else if (!blueExpr.equals(other.blueExpr))
			return false;
		if (greenExpr == null) {
			if (other.greenExpr != null)
				return false;
		} else if (!greenExpr.equals(other.greenExpr))
			return false;
		if (redExpr == null) {
			if (other.redExpr != null)
				return false;
		} else if (!redExpr.equals(other.redExpr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExprPixelConstructor [redExpr=" + redExpr + ", greenExpr=" + greenExpr + ", blueExpr=" + blueExpr
				+ ", type=" + type + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprPixelConstructor(this,arg);
	}
	
}
