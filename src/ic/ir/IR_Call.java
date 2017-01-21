package ic.ir;
import java.util.Arrays;
import java.util.List;

import ic.compiler.PrinterVisitor;
import ic.compiler.Visitor;
import ir.mipsgen.IRVisitor;

public class IR_Call extends IR_Exp{
	
	public TempLabel label;
	public List<IR_Exp> args;
	
	public IR_Call(TempLabel label, List<IR_Exp> args)
	{
		this.label = label;
		// the arguments are already reversed
		this.args = args;
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