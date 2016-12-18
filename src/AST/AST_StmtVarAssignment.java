package AST;

public class AST_StmtVarAssignment extends AST_Stmt
{
	public AST_Exp assignExp;
	public AST_Variable var;

	public AST_StmtVarAssignment(AST_Variable var,AST_Exp exp)
	{
		this.var = var;
		this.assignExp = exp;
	}
}