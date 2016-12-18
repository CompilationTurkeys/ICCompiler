package AST;

public class AST_ExpInsideParens extends AST_Exp
{
	public AST_Exp exp;
	
	public AST_ExpInsideParens(AST_Exp exp)
	{
		this.exp = exp;
	}
}