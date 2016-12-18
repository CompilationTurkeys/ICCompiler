package AST;

public class AST_ExpLiteral extends AST_Exp
{
	public AST_Literal literal;
	
	public AST_ExpLiteral(AST_Literal l)
	{
		this.literal = l;
	}
}