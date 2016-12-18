package AST;

public class AST_ExpVariable extends AST_Exp
{
	public AST_Variable var;
	
	public AST_ExpVariable(AST_Variable var)
	{
		this.var = var;
	}
}