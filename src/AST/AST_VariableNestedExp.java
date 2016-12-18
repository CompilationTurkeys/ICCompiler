package AST;

public class AST_VariableNestedExp extends AST_Variable
{
	public AST_Exp leftExp;
	public AST_Exp rightExp;
	
	public AST_VariableNestedExp(AST_Exp e1,AST_Exp e2)
	{
		this.leftExp = e1;
		this.rightExp = e2;
	}
}