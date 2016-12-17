package AST;

public class AST_METHOD_TYPE_FORMALS_STMT extends AST_METHOD
{
	public AST_STMT_LIST stmtList;
	public AST_TYPE type;
	public AST_FORMALS formals;
	public String id;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_METHOD_TYPE_FORMALS_STMT(AST_TYPE type,String id, AST_FORMALS formals, AST_STMT_LIST stmtList)
	{
		this.type = type;
		this.id = id;
		this.formals = formals;
		this.stmtList = stmtList;
	}
}