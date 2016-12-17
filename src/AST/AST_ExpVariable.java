package AST;

public class AST_ExpVariable extends AST_Exp
{
	public AST_VAR var;
	
	public AST_ExpVariable(AST_VAR var)
	{
		this.var = var;
	}
}