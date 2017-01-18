package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_VariableExpArray extends AST_Variable
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((arrayExp == null) ? 0 : arrayExp.hashCode());
		result = prime * result + ((arraySize == null) ? 0 : arraySize.hashCode());
		result = prime * result + (isDeclarationExp ? 1231 : 1237);
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
		AST_VariableExpArray other = (AST_VariableExpArray) obj;
		if (arrayExp == null) {
			if (other.arrayExp != null)
				return false;
		} else if (!arrayExp.equals(other.arrayExp))
			return false;
		if (arraySize == null) {
			if (other.arraySize != null)
				return false;
		} else if (!arraySize.equals(other.arraySize))
			return false;
		if (isDeclarationExp != other.isDeclarationExp)
			return false;
		return true;
	}

	public AST_Exp arrayExp;
	public AST_Exp arraySize;
	
	public boolean isDeclarationExp;
	
	public AST_VariableExpArray(AST_Exp e1,AST_Exp e2)
	{
		this.arrayExp = e1;
		this.arraySize = e2;
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
		return "AST_VariableExpArray [arrayExp=" + arrayExp + ", arraySize=" + arraySize + "]";
	}
	
	
}