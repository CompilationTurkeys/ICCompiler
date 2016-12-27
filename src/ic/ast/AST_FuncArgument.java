package ic.ast;
import ic.compiler.MethodAttribute;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_FuncArgument extends AST_Node
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((argName == null) ? 0 : argName.hashCode());
		result = prime * result + ((argType == null) ? 0 : argType.hashCode());
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
		AST_FuncArgument other = (AST_FuncArgument) obj;
		if (argName == null) {
			if (other.argName != null)
				return false;
		} else if (!argName.equals(other.argName))
			return false;
		if (argType == null) {
			if (other.argType != null)
				return false;
		} else if (!argType.equals(other.argType))
			return false;
		return true;
	}

	public String argName;
	public AST_Type argType;

	public AST_FuncArgument(AST_Type type, String name)
	{

		this.argName = name;
		this.argType = type;
	}

	public void setArgName(String argName) {
		this.argName = argName;
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

	public AST_Type getArgType() {
		return argType;
	}
	
	public String getArgName() {
		return argName;
	}

}