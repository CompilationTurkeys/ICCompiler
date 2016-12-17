package AST;

public class AST_STMT_MULTSTMTS extends AST_STMT
{
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_MULTSTMTS(AST_STMT_LIST body){
		this.body = body;
	}
}