package AST;

public class AST_STMT_TYPE_EXP extends AST_STMT
{
	public AST_Exp exp;
	public AST_TYPE type;
	public String name;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_TYPE_EXP(AST_TYPE type,String name, AST_Exp exp)
	{
		this.type = type;
		this.name = name;
		this.exp = exp;
	}
}