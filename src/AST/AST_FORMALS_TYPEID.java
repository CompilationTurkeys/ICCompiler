package AST;

public class AST_FORMALS_TYPEID extends AST_FORMALS
{
	public String name;
	public AST_TYPE type;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FORMALS_TYPEID(AST_TYPE type, String name)
	{

		this.name = name;
		this.type = type;
	}
}