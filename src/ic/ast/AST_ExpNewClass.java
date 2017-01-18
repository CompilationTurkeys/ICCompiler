package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_ExpNewClass extends AST_Exp
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((className == null) ? 0 : className.hashCode());
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
		AST_ExpNewClass other = (AST_ExpNewClass) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}

	public String className;
	
	public AST_ExpNewClass(String name)
	{
		this.className = name;
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
		return "AST_ExpNewClass [className=" + className + "]";
	}
	
	
}