package AST;

public class AST_STMT_ASSIGN extends AST_STMT
{
	public AST_Exp exp;
	public AST_VAR var;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_Exp exp)
	{
		this.var = var;
		this.exp = exp;
	}
}