package AST;

public class AST_FuncArgument extends AST_Node
{
	public String argName;
	public AST_Type argType;

	public AST_FuncArgument(AST_Type type, String name)
	{

		this.argName = name;
		this.argType = type;
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
}