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

public abstract class Statement extends ASTNode{
	
	Dec dec; 

	public Statement(Token first) {
		super(first);
	}

	public Dec dec() {
		return dec;
	}

	public void setDec(Dec dec) {
		this.dec = dec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dec == null) ? 0 : dec.hashCode());
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
		Statement other = (Statement) obj;
		if (dec == null) {
			if (other.dec != null)
				return false;
		} else if (!dec.equals(other.dec))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Statement [dec=" + dec + "]";
	}

	
}
