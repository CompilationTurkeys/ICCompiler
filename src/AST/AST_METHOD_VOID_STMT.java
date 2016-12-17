package AST;

public class AST_METHOD_VOID_STMT extends AST_METHOD
{
	public AST_STMT_LIST stmtList;
	public String id;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_VOID_STMT(String id, AST_STMT_LIST stmtList)
	{
		this.id = id;
		this.stmtList = stmtList;
	}
}