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

import cop5556fa20.Scanner.Kind;
import cop5556fa20.Scanner.Token;


public class ExprBinary extends Expression {
	
	final Expression e0;
	final Kind op;
	final Expression e1;
	
	public ExprBinary(Token first, Expression e0, Kind op, Expression e1) {
		super(first);
		this.e0 = e0;
		this.op = op;
		this.e1 = e1;
	}

	public Expression e0() {
		return e0;
	}

	public Kind op() {
		return op;
	}

	public Expression e1() {
		return e1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((e0 == null) ? 0 : e0.hashCode());
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
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
		ExprBinary other = (ExprBinary) obj;
		if (e0 == null) {
			if (other.e0 != null)
				return false;
		} else if (!e0.equals(other.e0))
			return false;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		} else if (!e1.equals(other.e1))
			return false;
		if (op == null) {
			if (other.op != null)
				return false;
		} else if (!op.equals(other.op))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExprBinary [e0=" + e0 + ", op=" + op + ", e1=" + e1 + ", type=" + type + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprBinary(this,arg);
	}
	
	

}
