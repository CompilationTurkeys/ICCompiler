package AST;

public class AST_FuncArgument extends AST_Node
{
	public String name;
	public AST_TYPE type;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FuncArgument(AST_TYPE type, String name)
	{

		this.name = name;
		this.type = type;
	}
}