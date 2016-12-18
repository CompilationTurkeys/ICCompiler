package AST;

public class AST_ExpFunctionCall extends AST_Exp
{
	public AST_VirtualCall call;

	public AST_ExpFunctionCall(AST_VirtualCall call)
	{
		this.call = call;
	}
}