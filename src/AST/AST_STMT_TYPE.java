package AST;

public class AST_STMT_TYPE extends AST_STMT
{
	public AST_TYPE type;
	public String name;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_TYPE(AST_TYPE type,String name)
	{
		this.type = type;
		this.name = name;
	}
}