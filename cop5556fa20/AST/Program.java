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

import java.util.List;

import cop5556fa20.Scanner.Token;

public class Program extends ASTNode {
	
	final List<ASTNode> decOrStatement;

	public Program(Token first, List<ASTNode> decOrStatement) {
		super(first);
		this.decOrStatement = decOrStatement;
	}

	public List<ASTNode> decOrStatement() {
		return decOrStatement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((decOrStatement == null) ? 0 : decOrStatement.hashCode());
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
		Program other = (Program) obj;
		if (decOrStatement == null) {
			if (other.decOrStatement != null)
				return false;
		} else if (!decOrStatement.equals(other.decOrStatement))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Program [decOrStatement=" + decOrStatement + ", first=" + first + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitProgram(this, arg);
	}
	
	
	

}
