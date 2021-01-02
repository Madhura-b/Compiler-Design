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
import cop5556fa20.Scanner.Kind;

public class DecVar extends Dec {
	
	final String name;
	final Expression expression;
	


	public DecVar(Token first, Type type, String name, Expression expression) {
		super(first, type);
		this.name = name;
		this.expression = expression;
	}



	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDecVar(this,arg);
	}


	public String name() {
		return name;
	}

	public Expression expression() {
		return expression;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DecVar other = (DecVar) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "DecVar [type=" + type + ", name=" + name + ", expression=" + expression + "]";
	}




	
}
