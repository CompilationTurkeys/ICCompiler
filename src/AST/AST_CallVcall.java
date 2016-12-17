package AST;

public class AST_CallVcall extends AST_Call
{
	public AST_VCALL call;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CallVcall(AST_VCALL call)
	{
		this.call = call;
	}
}