package ic.ast;

public abstract class AST_FieldOrMethod extends AST_Node
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + fieldMethod;
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
		AST_FieldOrMethod other = (AST_FieldOrMethod) obj;
		if (fieldMethod != other.fieldMethod)
			return false;
		return true;
	}

	public int fieldMethod;
}