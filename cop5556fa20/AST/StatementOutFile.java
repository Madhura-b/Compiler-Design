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

public class StatementOutFile extends Statement{

	final String name;
	final Expression filename;
	public StatementOutFile(Token first, String name, Expression filename) {
		super(first);
		this.name = name;
		this.filename = filename;
	}
	public String name() {
		return name;
	}
	public Expression filename() {
		return filename;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
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
		StatementOutFile other = (StatementOutFile) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
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
		return "StatementOutFile [name=" + name + ", filename=" + filename + "]";
	}
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStatementOutFile(this, arg);
	}
	



}
