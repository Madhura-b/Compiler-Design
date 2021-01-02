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

public class ExprPixelSelector extends Expression {
	
	final Expression image;
	final Expression X;
	final Expression Y;
	
	public ExprPixelSelector(Token first, Expression image, Expression x, Expression y) {
		super(first);
		this.image = image;
		X = x;
		Y = y;
	}

	public Expression image() {
		return image;
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
		result = prime * result + ((image == null) ? 0 : image.hashCode());
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
		ExprPixelSelector other = (ExprPixelSelector) obj;
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
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExprPixelSelector [image=" + image + ", X=" + X + ", Y=" + Y + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprPixelSelector(this,arg);
	}
	
	

}
