package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_StmtCall extends AST_Stmt
{
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