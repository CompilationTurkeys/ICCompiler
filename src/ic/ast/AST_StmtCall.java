package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtCall extends AST_Stmt
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((funcCall == null) ? 0 : funcCall.hashCode());
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
		AST_StmtCall other = (AST_StmtCall) obj;
		if (funcCall == null) {
			if (other.funcCall != null)
				return false;
		} else if (!funcCall.equals(other.funcCall))
			return false;
		return true;
	}

	public AST_VirtualCall funcCall;

	public AST_StmtCall(AST_VirtualCall call)
	{
		this.funcCall = call;
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
		return "AST_StmtCall [funcCall=" + funcCall + "]";
	}
	
	
}