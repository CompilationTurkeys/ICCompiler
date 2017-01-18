package ic.ast;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_Literal extends AST_Exp
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		AST_Literal other = (AST_Literal) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Object value ;
	
	
	public AST_Literal(int intVal) {
		value = new Integer(intVal);
	}
	
	public AST_Literal(String stringVal) {
		value = stringVal;
	}
	
	public boolean isInteger() {
		return value != null && value instanceof Integer;
	}	
	
	public boolean isString() {
		return value != null && value instanceof String;
	}	
	
	public boolean isNull() {
		return value == null;
	}
	

	@Override
	public <ContextType, ResultType> ResultType accept(
			Visitor<ContextType, ResultType>  visitor, ContextType context) {
		return visitor.visit(this, context);
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "AST_Literal [value=" + value + "]";
	}
	
	
	
	
}