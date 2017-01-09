package ic.ir;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public abstract class IR_Node{
	
	public abstract void accept(PrinterVisitor visitor);

	public abstract <ContextType, ResultType> ResultType accept(Visitor<ContextType, ResultType> 
											visitor, ContextType context);


}