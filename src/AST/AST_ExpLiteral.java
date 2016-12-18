package AST;

public class AST_ExpLiteral extends AST_Exp
{
	public AST_Literal l;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ExpLiteral(AST_Literal l)
	{
		this.l = l;
	}
}