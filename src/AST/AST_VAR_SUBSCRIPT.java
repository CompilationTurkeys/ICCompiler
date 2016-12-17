package AST;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_Exp e1;
	public AST_Exp e2;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_Exp e1,AST_Exp e2)
	{
		this.e1 = e1;
		this.e2 = e2;
	}
}