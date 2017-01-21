package ic.ir;
import ic.ir.IR_Exp;
import ir.mipsgen.IRVisitor;
import ic.ast.BinaryOpTypes;
import ic.compiler.PrinterVisitor;

public class IR_NewObject extends IR_Exp{
	
	public String className;

	
	public IR_NewObject(String className)
	{
		this.className = className;
	}

	@Override
	public void accept(PrinterVisitor visitor) {
		// TODO Auto-generated method stub		
	}

	@Override
	public <ResultType> ResultType accept(IRVisitor<ResultType> visitor) {
		return visitor.visit(this);
	}

}