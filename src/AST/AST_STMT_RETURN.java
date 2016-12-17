package AST;

public class AST_STMT_RETURN extends AST_STMT
{
	public AST_Exp exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_Exp exp){
		this.exp = exp;
	}
}