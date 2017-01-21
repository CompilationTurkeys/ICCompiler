package ic.ir;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;
import ir.mipsgen.IRVisitor;

public class IR_Const extends IR_Exp{
	
	int value;
	
	public IR_Const(int val)
	{
		value = val;
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