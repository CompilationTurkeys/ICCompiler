package ic.ast;

import ic.compiler.IRVisitor;
import ic.compiler.PrinterVisitor;

public class IR_Prologue extends IR_Exp{
	
	public int frameSize;
	
	public IR_Prologue(int frameSize)
	{
		this.frameSize = frameSize;
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