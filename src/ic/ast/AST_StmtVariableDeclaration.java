package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtVariableDeclaration extends AST_Stmt
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((assignedExp == null) ? 0 : assignedExp.hashCode());
		result = prime * result + ((varName == null) ? 0 : varName.hashCode());
		result = prime * result + ((varType == null) ? 0 : varType.hashCode());
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
		AST_StmtVariableDeclaration other = (AST_StmtVariableDeclaration) obj;
		if (assignedExp == null) {
			if (other.assignedExp != null)
				return false;
		} else if (!assignedExp.equals(other.assignedExp))
			return false;
		if (varName == null) {
			if (other.varName != null)
				return false;
		} else if (!varName.equals(other.varName))
			return false;
		if (varType == null) {
			if (other.varType != null)
				return false;
		} else if (!varType.equals(other.varType))
			return false;
		return true;
	}

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

	@Override
	public String toString() {
		return "AST_StmtVariableDeclaration [assignedExp=" + assignedExp + ", varType=" + varType + ", varName="
				+ varName + "]";
	}
	
	
}