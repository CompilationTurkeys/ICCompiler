package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_VariableExpField extends AST_Variable
{
	public AST_Exp exp;
	public String fieldName;
	
	public AST_VariableExpField(AST_Exp exp,String fieldName)
	{
		this.exp = exp;
		this.fieldName = fieldName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((exp == null) ? 0 : exp.hashCode());
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
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
		AST_VariableExpField other = (AST_VariableExpField) obj;
		if (exp == null) {
			if (other.exp != null)
				return false;
		} else if (!exp.equals(other.exp))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}

	@Override
	public <ContextType, ResultType> ResultType accept(
			Visitor<ContextType, ResultType>  visitor, ContextType context) {
		return visitor.visit(this, context);
	}
	
	@Override
	public void accept(PrinterVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "AST_VariableExpField [exp=" + exp + ", fieldName=" + fieldName + "]";
	}
	
	
}