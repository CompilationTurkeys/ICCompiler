package AST;

public class AST_StmtIf extends AST_Stmt
{
	public AST_Exp cond;
	public AST_Stmt stmt;

	public AST_StmtIf(AST_Exp cond,AST_Stmt stmt)
	{
		this.cond = cond;
		this.stmt = stmt;
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