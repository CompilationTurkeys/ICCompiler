package AST;

public class AST_ExpFunctionCall extends AST_Exp
{
	public AST_VirtualCall call;

	public AST_ExpFunctionCall(AST_VirtualCall call)
	{
		this.call = call;
	}
	
	@Override
	public <ContextType, ResultType> ResultType accept(
			Visitor<ContextType, ResultType>  visitor, ContextType context) {
		return visitor.visit(this, context);
	}
	
	@Override
	public void accept(PrinterVisitor visitor) {
		visitor.visit(this);
	}
}