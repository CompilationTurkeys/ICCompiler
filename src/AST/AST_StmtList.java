package AST;

import java.util.ArrayList;

public class AST_StmtList extends AST_Stmt
{
	public ArrayList<AST_Stmt> stmtList;

	public AST_StmtList(ArrayList<AST_Stmt> body){
		this.stmtList = body;
	}
}