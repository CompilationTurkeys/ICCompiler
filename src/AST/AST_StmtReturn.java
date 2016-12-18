package AST;

public class AST_StmtReturn extends AST_Stmt
{
	public AST_Exp returnExp;

	public AST_StmtReturn(AST_Exp exp){
		this.returnExp = exp;
	}
}