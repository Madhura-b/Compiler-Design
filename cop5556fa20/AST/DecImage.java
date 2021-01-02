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
import cop5556fa20.Scanner;
import static cop5556fa20.Scanner.Kind;

public class DecImage extends Dec {
	
	final String name;
	final Expression width;
	final Expression height;
	final Kind op;
	final Expression source;
	

	public DecImage(Token first, Type type, String name, Expression width, Expression height, Kind op,
			Expression source) {
		super(first, type);
		this.name = name;
		this.width = width;
		this.height = height;
		this.op = op;
		this.source = source;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDecImage(this,arg);
	}




	public String name() {
		return name;
	}


	public Expression width() {
		return width;
	}


	public Expression height() {
		return height;
	}


	public Kind op() {
		return op;
	}


	public Expression source() {
		return source;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		DecImage other = (DecImage) obj;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (op != other.op)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (type != other.type)
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "DecImage [type=" + type + ", name=" + name + ", width=" + width + ", height=" + height + ", op=" + op
				+ ", source=" + source + "]";
	}
	
	

}
