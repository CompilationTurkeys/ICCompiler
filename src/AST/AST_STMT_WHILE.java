package AST;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_Exp cond;
	public AST_STMT stmt;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_Exp cond,AST_STMT stmt)
	{
		this.cond = cond;
		this.stmt = stmt;
	}
}