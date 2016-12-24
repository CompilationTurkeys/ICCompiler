package ic.ast;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public abstract class AST_Variable extends AST_Exp
{

	@Override
	public abstract <ContextType, ResultType> ResultType accept(
			Visitor<ContextType, ResultType>  visitor, ContextType context);
	
	@Override
	public abstract void accept(PrinterVisitor visitor);
}