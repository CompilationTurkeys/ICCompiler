package AST;

public class AST_TypeArray extends AST_Type
{
	public AST_Type arrayType;
	
	public AST_TypeArray(AST_Type type)
	{
		this.arrayType = type;
	}
}