package AST;

public class AST_VariableExpArray extends AST_Variable
{
	public AST_Exp arrayExp;
	public AST_Exp arraySize;
	
	public AST_VariableExpArray(AST_Exp e1,AST_Exp e2)
	{
		this.arrayExp = e1;
		this.arraySize = e2;
	}
}