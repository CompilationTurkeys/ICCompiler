package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public abstract class AST_Node{
	int moish;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + moish;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AST_Node other = (AST_Node) obj;
		if (moish != other.moish)
			return false;
		return true;
	}

	public abstract void accept(PrinterVisitor visitor);

	public abstract <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> 
											visitor, ContextType context);


}