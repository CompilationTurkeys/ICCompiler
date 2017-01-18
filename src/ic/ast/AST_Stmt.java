package ic.ast;

public abstract class AST_Stmt extends AST_Node
{
	int moish;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + moish;
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
		AST_Stmt other = (AST_Stmt) obj;
		if (moish != other.moish)
			return false;
		return true;
	}
}