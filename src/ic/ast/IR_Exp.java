package ic.ast;
import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;

public abstract class IR_Exp{
	
	public abstract void accept(PrinterVisitor visitor);

	public abstract <ResultType> ResultType accept(IRVisitor<ResultType> 
											visitor);


}