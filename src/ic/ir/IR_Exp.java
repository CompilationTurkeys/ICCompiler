package ic.ir;
import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;
import ir.mipsgen.IRVisitor;

public abstract class IR_Exp{
	
	public abstract void accept(PrinterVisitor visitor);

	public abstract <ResultType> ResultType accept(IRVisitor<ResultType> 
											visitor);


}