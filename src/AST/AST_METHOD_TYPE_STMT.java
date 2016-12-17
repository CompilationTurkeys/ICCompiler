package AST;

public class AST_METHOD_TYPE_STMT extends AST_METHOD
{
	public AST_TYPE type;
	public String id;
	public AST_STMT_LIST stmtList;
	

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_TYPE_STMT(AST_TYPE type,String id, AST_STMT_LIST stmtList)
	{
		this.type = type;
		this.id = id;
		this.stmtList = stmtList;
	}
}