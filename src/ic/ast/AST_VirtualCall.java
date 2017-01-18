package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

import java.util.ArrayList;
import java.util.List;

public class AST_VirtualCall extends AST_Exp
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((argList == null) ? 0 : argList.hashCode());
		result = prime * result + ((callingExp == null) ? 0 : callingExp.hashCode());
		result = prime * result + ((funcName == null) ? 0 : funcName.hashCode());
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
		AST_VirtualCall other = (AST_VirtualCall) obj;
		if (argList == null) {
			if (other.argList != null)
				return false;
		} else if (!argList.equals(other.argList))
			return false;
		if (callingExp == null) {
			if (other.callingExp != null)
				return false;
		} else if (!callingExp.equals(other.callingExp))
			return false;
		if (funcName == null) {
			if (other.funcName != null)
				return false;
		} else if (!funcName.equals(other.funcName))
			return false;
		return true;
	}

	public AST_Exp 	callingExp;
	public ArrayList<AST_Exp> argList;
	public String funcName;

	public AST_VirtualCall(AST_Exp exp, String name, ArrayList<AST_Exp> expList)
	{
		this.argList = expList == null ? new ArrayList<>() : expList;
		this.callingExp = exp;
		this.funcName = name;
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
	
	public AST_Exp getCallingExpression(){
		return callingExp;
	}
	
	public void setCallingExpression(AST_Exp callingExp) {
		this.callingExp = callingExp;
	}
	
	public String getFuncName(){
		return funcName;
	}

	public List<AST_Exp> getArguments() {
		return argList;
	}

	@Override
	public String toString() {
		return "AST_VirtualCall [callingExp=" + callingExp + ", argList=" + argList + ", funcName=" + funcName + "]";
	}
	
	
}