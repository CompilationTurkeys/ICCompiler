package AST;

public class AST_VariableExpField extends AST_Variable
{
	public AST_Exp exp;
	public String fieldName;
	
	public AST_VariableExpField(AST_Exp exp,String fieldName)
	{
		this.exp = exp;
		this.fieldName = fieldName;
	}
}