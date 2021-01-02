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

public class ExprHash extends Expression {
	
	final Expression e;
	final String attr;
	
	public ExprHash(Token first, Expression e, String attr) {
		super(first);
		this.e = e;
		this.attr = attr;
	}

	public Expression e() {
		return e;
	}

	public String attr() {
		return attr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attr == null) ? 0 : attr.hashCode());
		result = prime * result + ((e == null) ? 0 : e.hashCode());
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
		ExprHash other = (ExprHash) obj;
		if (attr == null) {
			if (other.attr != null)
				return false;
		} else if (!attr.equals(other.attr))
			return false;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExprAttr [e=" + e + ", attr=" + attr + ", type=" + type + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprHash(this,arg);
	}
	
	

}
