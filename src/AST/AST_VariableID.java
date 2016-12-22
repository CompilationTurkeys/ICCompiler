package AST;

public class AST_VariableID extends AST_Variable
{
	public String fieldName;
	
	public AST_VariableID(String name)
	{
		this.fieldName = name;
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