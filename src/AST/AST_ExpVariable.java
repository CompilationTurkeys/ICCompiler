package AST;

public class AST_ExpVariable extends AST_Exp
{
	public AST_Variable var;
	
	public AST_ExpVariable(AST_Variable var)
	{
		this.var = var;
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