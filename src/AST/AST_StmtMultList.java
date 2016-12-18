package AST;

import java.util.ArrayList;

public class AST_StmtMultList extends AST_Stmt
{
	public ArrayList<AST_Stmt> stmtList;

	public AST_StmtMultList(ArrayList<AST_Stmt> body){
		this.stmtList = body;
	}
}