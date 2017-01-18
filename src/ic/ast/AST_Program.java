package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

import java.util.ArrayList;
import java.util.List;

public class AST_Program extends AST_Node
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((classList == null) ? 0 : classList.hashCode());
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
		AST_Program other = (AST_Program) obj;
		if (classList == null) {
			if (other.classList != null)
				return false;
		} else if (!classList.equals(other.classList))
			return false;
		return true;
	}

	public ArrayList<AST_ClassDecl> classList;

	public AST_Program(ArrayList<AST_ClassDecl> lst)
	{
		this.classList = lst;
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

	public List<AST_ClassDecl> getClasses() {
		return classList;
	}

	@Override
	public String toString() {
		return "AST_Program [classList=" + classList + "]";
	}
	
	

}