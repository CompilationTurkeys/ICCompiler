package AST;

public class AST_StmtVariableExp extends AST_Stmt
{
	public AST_Exp assignedExp;
	public AST_Type varType;
	public String varName;

	public AST_StmtVariableExp(AST_Type type,String name, AST_Exp exp)
	{
		this.varType = type;
		this.varName = name;
		this.assignedExp = exp;
	}
}