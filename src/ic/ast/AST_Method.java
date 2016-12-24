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
		this.methodArgs = methodArgs;
		setMethodStmtList(methodStmtList);
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
}