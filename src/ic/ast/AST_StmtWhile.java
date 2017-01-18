package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtWhile extends AST_Stmt
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((cond == null) ? 0 : cond.hashCode());
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
		AST_StmtWhile other = (AST_StmtWhile) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (cond == null) {
			if (other.cond != null)
				return false;
		} else if (!cond.equals(other.cond))
			return false;
		return true;
	}

	public AST_Exp cond;
	public AST_Stmt body;

	public AST_StmtWhile(AST_Exp cond,AST_Stmt body)
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

	@Override
	public String toString() {
		return "AST_StmtWhile [cond=" + cond + ", body=" + body + "]";
	}
	
	
	
}