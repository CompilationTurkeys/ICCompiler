package AST;

public class AST_LiteralNumber extends AST_Literal
{
	public int num;
	
	public AST_LiteralNumber(int num)
	{
		this.num = num;
	
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