package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

import java.util.ArrayList;

public class AST_Method extends AST_FieldOrMethod
{
	public AST_Type methodType;
	public String methodName;
	public ArrayList<AST_FuncArgument> methodArgs;
	public AST_StmtList methodStmtList;
	
	public AST_Method(AST_Type methodType, String methodName, ArrayList<AST_FuncArgument> methodArgs, ArrayList<AST_Stmt> methodStmtList)
	{
		this.methodType = methodType;
		this.methodName = methodName;
		this.methodArgs = methodArgs == null ? new ArrayList<>() : methodArgs;
		setMethodStmtList(methodStmtList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((methodArgs == null) ? 0 : methodArgs.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((methodStmtList == null) ? 0 : methodStmtList.hashCode());
		result = prime * result + ((methodType == null) ? 0 : methodType.hashCode());
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
		AST_Method other = (AST_Method) obj;
		if (methodArgs == null) {
			if (other.methodArgs != null)
				return false;
		} else if (!methodArgs.equals(other.methodArgs))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (methodStmtList == null) {
			if (other.methodStmtList != null)
				return false;
		} else if (!methodStmtList.equals(other.methodStmtList))
			return false;
		if (methodType == null) {
			if (other.methodType != null)
				return false;
		} else if (!methodType.equals(other.methodType))
			return false;
		return true;
	}

	public void setMethodStmtList(ArrayList<AST_Stmt> methodStmtList) {
		this.methodStmtList = new AST_StmtList(methodStmtList);
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
	
	public String getName(){
		return methodName;
	}

	public AST_Type getType() {
		return methodType;
	}

	public ArrayList<AST_FuncArgument> getArguments() {
		return methodArgs;
	}
	
	public AST_StmtList getMethodStmtList() {
		return methodStmtList;
	}

	@Override
	public String toString() {
		return "AST_Method [methodType=" + methodType + ", methodName=" + methodName + ", methodArgs=" + methodArgs
				+ ", methodStmtList=" + methodStmtList + "]";
	}
	
	
}