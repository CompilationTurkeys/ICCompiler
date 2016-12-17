package AST;

public class AST_METHOD_TYPE_FORMALS extends AST_METHOD
{
	public AST_FORMALS formals;
	public String id;
	public AST_TYPE type;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_TYPE_FORMALS(AST_TYPE type,String id, AST_FORMALS formals)
	{
		this.type = type;
		this.id = id;
		this.formals = formals;
	}
}