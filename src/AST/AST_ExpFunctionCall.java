package AST;

public class AST_ExpFunctionCall extends AST_Exp
{
	public AST_Call call;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ExpFunctionCall(AST_Call call)
	{
		this.call = call;
	}
}