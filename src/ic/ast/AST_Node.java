package ic.ast;

public abstract class AST_Node
{
	public abstract void accept(PrinterVisitor visitor);

	public abstract <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> visitor, ContextType context);
}