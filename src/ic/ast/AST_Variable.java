package ic.ast;


public abstract class AST_Variable extends AST_Exp
{
	public boolean isAssigned = true;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (isAssigned ? 1231 : 1237);
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
		AST_Variable other = (AST_Variable) obj;
		if (isAssigned != other.isAssigned)
			return false;
		return true;
	}

}