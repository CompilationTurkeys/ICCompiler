package ic.ast;
import ic.compiler.MethodAttribute;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_FuncArgument extends AST_Node
{
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
	
	@Override
	public boolean equals(Object obj) {
		AST_FuncArgument other = (AST_FuncArgument) obj;
		return this.argName.equals(other.argName) && this.argType.equals(other.argType);
	}

}