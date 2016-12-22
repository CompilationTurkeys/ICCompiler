package AST;

public class AST_ExpNewTypeArray extends AST_Exp
{
	public AST_Type arrayType;
	public AST_Exp	sizeExpression;

	public AST_ExpNewTypeArray(AST_Type type, AST_Exp exp)
	{
		this.arrayType = type;
		this.sizeExpression = exp;
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