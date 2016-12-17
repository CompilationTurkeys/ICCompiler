package AST;

public class AST_METHOD_TYPE_SIMPLE extends AST_METHOD
{
	public AST_TYPE type;
	public String id;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_TYPE_SIMPLE(AST_TYPE type,String id)
	{
		this.id = id;
		this.type = type;
	}
}