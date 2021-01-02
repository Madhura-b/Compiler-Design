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

public class StatementOutScreen extends Statement {
	
	final String name;
	final Expression X;
	final Expression Y;

	


	public StatementOutScreen(Token first, String name, Expression x, Expression y) {
		super(first);
		this.name = name;
		X = x;
		Y = y;
	}




	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStatementOutScreen(this, arg);
	}




	public String name() {
		return name;
	}




	public Expression X() {
		return X;
	}




	public Expression Y() {
		return Y;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((X == null) ? 0 : X.hashCode());
		result = prime * result + ((Y == null) ? 0 : Y.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		StatementOutScreen other = (StatementOutScreen) obj;
		if (X == null) {
			if (other.X != null)
				return false;
		} else if (!X.equals(other.X))
			return false;
		if (Y == null) {
			if (other.Y != null)
				return false;
		} else if (!Y.equals(other.Y))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}




	@Override
	public String toString() {
		return "StatementOutScreen [name=" + name + ", X=" + X + ", Y=" + Y + ", dec=" + dec + "]";
	}

	
}
