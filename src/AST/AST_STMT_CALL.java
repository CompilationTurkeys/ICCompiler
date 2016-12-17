package AST;

public class AST_STMT_CALL extends AST_STMT
{
	public AST_Call call;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_CALL(AST_Call call)
	{
		this.call = call;
	}
}