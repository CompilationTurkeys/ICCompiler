package AST;

public class AST_ExpLiteral extends AST_Exp
{
	public AST_LITERAL l;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ExpLiteral(AST_LITERAL l)
	{
		this.l = l;
	}
}