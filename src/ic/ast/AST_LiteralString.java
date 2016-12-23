package ic.ast;

public class AST_LiteralString extends AST_Literal
{
	public String str;

	public AST_LiteralString(String str)
	{
		this.str = str;
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