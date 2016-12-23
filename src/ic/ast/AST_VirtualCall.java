package ic.ast;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

import java.util.ArrayList;

public class AST_VirtualCall extends AST_Node
{
	public AST_Exp 	callingExp;
	public ArrayList<AST_Exp> argList;
	public String funcName;

	public AST_VirtualCall(AST_Exp exp, String name, ArrayList<AST_Exp> expList)
	{
		this.argList = expList;
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
}