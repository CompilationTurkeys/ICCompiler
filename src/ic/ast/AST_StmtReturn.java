package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtReturn extends AST_Stmt
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((returnExp == null) ? 0 : returnExp.hashCode());
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
		AST_StmtReturn other = (AST_StmtReturn) obj;
		if (returnExp == null) {
			if (other.returnExp != null)
				return false;
		} else if (!returnExp.equals(other.returnExp))
			return false;
		return true;
	}

	public AST_Exp returnExp;

	public AST_StmtReturn(AST_Exp exp){
		this.returnExp = exp;
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
		return "AST_StmtReturn [returnExp=" + returnExp + "]";
	}
	
	
}