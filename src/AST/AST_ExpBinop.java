package AST;

public class AST_ExpBinop extends AST_Exp
{
	public BinaryOpTypes OP;
	public AST_Exp leftExp;
	public AST_Exp rightExp;
	
	public AST_ExpBinop(AST_Exp left,AST_Exp right,BinaryOpTypes OP)
	{
		this.leftExp = left;
		this.rightExp = right;
		this.OP = OP;
	}
}