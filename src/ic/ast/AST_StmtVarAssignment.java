package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtVarAssignment extends AST_Stmt
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((assignExp == null) ? 0 : assignExp.hashCode());
		result = prime * result + ((var == null) ? 0 : var.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AST_StmtVarAssignment other = (AST_StmtVarAssignment) obj;
		if (assignExp == null) {
			if (other.assignExp != null)
				return false;
		} else if (!assignExp.equals(other.assignExp))
			return false;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
			return false;
		return true;
	}

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

	@Override
	public String toString() {
		return "AST_StmtVarAssignment [assignExp=" + assignExp + ", var=" + var + "]";
	}
	
	
}