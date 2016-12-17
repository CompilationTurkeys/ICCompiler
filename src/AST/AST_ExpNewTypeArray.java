package AST;

public class AST_ExpNewTypeArray extends AST_Exp
{
	public AST_TYPE arrayType;
	public AST_Exp arraySize;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ExpNewTypeArray(AST_TYPE type, AST_Exp exp)
	{
		this.arrayType = type;
		this.arraySize = exp;
	}
}