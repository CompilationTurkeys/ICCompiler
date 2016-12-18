package AST;

public class AST_ExpNewTypeArray extends AST_Exp
{
	public AST_Type arrayType;
	public AST_Exp arraySize;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ExpNewTypeArray(AST_Type type, AST_Exp exp)
	{
		this.arrayType = type;
		this.arraySize = exp;
	}
}