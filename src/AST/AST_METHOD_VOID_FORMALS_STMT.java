package AST;

public class AST_METHOD_VOID_FORMALS_STMT extends AST_METHOD
{
	public AST_STMT_LIST stmtList;
	public AST_FORMALS formals;
	public String id;


	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_VOID_FORMALS_STMT(String id, AST_FORMALS formals, AST_STMT_LIST stmtList)
	{
		this.id = id;
		this.formals = formals;
		this.stmtList = stmtList;
	}
}