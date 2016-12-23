package ic.compiler;
import java.util.ArrayList;
import ic.ast.*; 

public class MethodAttribute extends Attribute {
	
	final private ArrayList<AST_FuncArgument> funcArgs;
	
	public MethodAttribute(AST_Type type, ArrayList<AST_FuncArgument> funcArgs) {
		super(type);
		this.funcArgs = funcArgs;
	}

	public ArrayList<AST_FuncArgument> getParameters() {
		return funcArgs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((funcArgs == null) ? 0 : funcArgs.hashCode());
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
		MethodAttribute other = (MethodAttribute) obj;
		if (funcArgs == null) {
			if (other.funcArgs != null)
				return false;
		} else if (!funcArgs.equals(other.funcArgs))
			return false;
		return true;
	}
	
}
