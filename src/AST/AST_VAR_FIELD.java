package AST;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_Exp exp;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_Exp exp,String fieldName)
	{
		this.exp = exp;
		this.fieldName = fieldName;
	}
}