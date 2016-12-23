package ic.ast;

public class AST_ExpNewClass extends AST_Exp
{
	public String className;
	
	public AST_ExpNewClass(String name)
	{
		this.className = name;
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