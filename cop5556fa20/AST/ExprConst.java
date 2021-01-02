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

public class ExprConst extends Expression {
	
	final String name;
	final int value;
	public ExprConst(Token first, String name, int value) {
		super(first);
		this.name = name;
		this.value = value;
	}
	public String name() {
		return name;
	}
	public int value() {
		return value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + value;
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
		ExprConst other = (ExprConst) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ExprConst [name=" + name + ", value=" + value + "]";
	}
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprConst(this,arg);
	}
	
	
}
