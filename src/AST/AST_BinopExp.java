package AST;

public class AST_BinopExp extends AST_Exp
{
	public AST_Binop OP;
	public AST_Exp leftExp;
	public AST_Exp rightExp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_BinopExp(AST_Exp left,AST_Exp right,AST_Binop OP)
	{
		this.leftExp = left;
		this.rightExp = right;
		this.OP = OP;
	}
}