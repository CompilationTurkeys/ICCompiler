package AST;

public class AST_FuncArgument extends AST_Node
{
	public String name;
	public AST_Type type;

	public AST_FuncArgument(AST_Type type, String name)
	{

		this.name = name;
		this.type = type;
	}
}