package ic.ast;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_Literal extends AST_Exp
{
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
	
	
}