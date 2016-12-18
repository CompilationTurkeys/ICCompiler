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
}