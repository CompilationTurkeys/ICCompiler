package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_VariableExpArray extends AST_Variable
{
	public AST_Exp arrayExp;
	public AST_Exp arraySize;
	
	public boolean isDeclarationExp;
	
	public AST_VariableExpArray(AST_Exp e1,AST_Exp e2)
	{
		this.arrayExp = e1;
		this.arraySize = e2;
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

	@Override
	public String toString() {
		return "AST_VariableExpArray [arrayExp=" + arrayExp + ", arraySize=" + arraySize + "]";
	}
	
	
}