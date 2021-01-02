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

public class ExprConditional extends Expression {

	final Expression condition;
	final Expression trueCase;
	final Expression falseCase;
	
	public ExprConditional(Token first, Expression condition, Expression trueCase, Expression falseCase) {
		super(first);
		this.condition = condition;
		this.trueCase = trueCase;
		this.falseCase = falseCase;
	}

	public Expression condition() {
		return condition;
	}

	public Expression trueCase() {
		return trueCase;
	}

	public Expression falseCase() {
		return falseCase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((falseCase == null) ? 0 : falseCase.hashCode());
		result = prime * result + ((trueCase == null) ? 0 : trueCase.hashCode());
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
		ExprConditional other = (ExprConditional) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (falseCase == null) {
			if (other.falseCase != null)
				return false;
		} else if (!falseCase.equals(other.falseCase))
			return false;
		if (trueCase == null) {
			if (other.trueCase != null)
				return false;
		} else if (!trueCase.equals(other.trueCase))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExprConditional [condition=" + condition + ", trueCase=" + trueCase + ", falseCase=" + falseCase
				+ ", type=" + type + "]";
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprConditional(this,arg);
	}
	
	
}
