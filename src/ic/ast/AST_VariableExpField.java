package ic.ast;

public class AST_VariableExpField extends AST_Variable
{
	public AST_Exp exp;
	public String fieldName;
	
	public AST_VariableExpField(AST_Exp exp,String fieldName)
	{
		this.exp = exp;
		this.fieldName = fieldName;
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