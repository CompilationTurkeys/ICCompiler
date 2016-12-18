package AST;

public class AST_StmtCall extends AST_Stmt
{
	public AST_VirtualCall funcCall;

	public AST_StmtCall(AST_VirtualCall call)
	{
		this.funcCall = call;
	}
}