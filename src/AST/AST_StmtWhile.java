package AST;

public class AST_StmtWhile extends AST_Stmt
{
	public AST_Exp cond;
	public AST_Stmt stmt;

	public AST_StmtWhile(AST_Exp cond,AST_Stmt stmt)
	{
		this.cond = cond;
		this.stmt = stmt;
	}
}