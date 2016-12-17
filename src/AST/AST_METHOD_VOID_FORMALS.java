package AST;

public class AST_METHOD_VOID_FORMALS extends AST_METHOD
{
	public AST_FORMALS formals;
	public String id;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_VOID_FORMALS(String id, AST_FORMALS formals)
	{
		this.id = id;
		this.formals = formals;
	}
}