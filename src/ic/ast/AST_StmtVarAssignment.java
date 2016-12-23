package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtVarAssignment extends AST_Stmt
{
	public AST_Exp assignExp;
	public AST_Variable var;

	public AST_StmtVarAssignment(AST_Variable var,AST_Exp exp)
	{
		this.var = var;
		this.assignExp = exp;
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