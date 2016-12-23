package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public abstract class AST_Node{
	
	
	
	private String className;

	public abstract void accept(PrinterVisitor visitor);

	public abstract <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> visitor, ContextType context);

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}