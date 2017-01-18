package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public class AST_ExpBinop extends AST_Exp
{
	public BinaryOpTypes OP;
	public AST_Exp leftExp;
	public AST_Exp rightExp;
	
	public AST_ExpBinop(AST_Exp left,AST_Exp right,BinaryOpTypes OP)
	{
		this.leftExp = left;
		this.rightExp = right;
		this.OP = OP;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((OP == null) ? 0 : OP.hashCode());
		result = prime * result + ((leftExp == null) ? 0 : leftExp.hashCode());
		result = prime * result + ((rightExp == null) ? 0 : rightExp.hashCode());
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
		AST_ExpBinop other = (AST_ExpBinop) obj;
		if (OP != other.OP)
			return false;
		if (leftExp == null) {
			if (other.leftExp != null)
				return false;
		} else if (!leftExp.equals(other.leftExp))
			return false;
		if (rightExp == null) {
			if (other.rightExp != null)
				return false;
		} else if (!rightExp.equals(other.rightExp))
			return false;
		return true;
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
		return "AST_ExpBinop [OP=" + OP + ", leftExp=" + leftExp + ", rightExp=" + rightExp + "]";
	}
	
	
}