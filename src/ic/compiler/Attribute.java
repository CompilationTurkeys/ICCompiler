package ic.compiler;
import ic.ast.*;

public class Attribute {

	private AST_Type type;
	private boolean isNull;
	private boolean isMethod;

	public Attribute(AST_Type type)
	{
		this.type = type;
		this.isNull = false;
		this.isMethod = false;
	}
	
	public void setIsMethod(boolean isMethod) {
		this.isMethod = isMethod;
	}
	
	public boolean isMethod() {
		return isMethod;
	}

	public AST_Type getType(){
		return type;
	}
	
	public boolean isNull() {
		return isNull;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isNull ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Attribute other = (Attribute) obj;
		if (isNull != other.isNull)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}