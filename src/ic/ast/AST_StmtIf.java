package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtIf extends AST_Stmt
{
	public AST_Exp cond;
	public AST_Stmt body;

	public AST_StmtIf(AST_Exp cond,AST_Stmt body)
	{
		this.cond = cond;
		this.body = body;
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