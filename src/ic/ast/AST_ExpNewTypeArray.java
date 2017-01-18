package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_ExpNewTypeArray extends AST_Exp
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((arrayType == null) ? 0 : arrayType.hashCode());
		result = prime * result + ((sizeExpression == null) ? 0 : sizeExpression.hashCode());
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
		AST_ExpNewTypeArray other = (AST_ExpNewTypeArray) obj;
		if (arrayType == null) {
			if (other.arrayType != null)
				return false;
		} else if (!arrayType.equals(other.arrayType))
			return false;
		if (sizeExpression == null) {
			if (other.sizeExpression != null)
				return false;
		} else if (!sizeExpression.equals(other.sizeExpression))
			return false;
		return true;
	}

	public AST_Type arrayType;
	public AST_Exp	sizeExpression;

	public AST_ExpNewTypeArray(AST_Type type, AST_Exp exp)
	{
		this.arrayType = type;
		this.sizeExpression = exp;
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
		return "AST_ExpNewTypeArray [arrayType=" + arrayType + ", sizeExpression=" + sizeExpression + "]";
	}
	
	
}