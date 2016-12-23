package ic.ast;

public class AST_StmtVariableDeclaration extends AST_Stmt
{
	public AST_Exp assignedExp;
	public AST_Type varType;
	public String varName;

	public AST_StmtVariableDeclaration(AST_Type type,String name, AST_Exp exp)
	{
		this.varType = type;
		this.varName = name;
		this.assignedExp = exp;
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